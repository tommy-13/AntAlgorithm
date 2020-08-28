package streetmap.solver;

import java.util.Random;

public class ACSAlgorithm implements TravellingSalesmanSolver {
	
	private final double 	BETA	= 2.0;
	private final double 	RHO		= 0.1;
	private final int 		NR_ANTS = 10;
	private final double	XI		= 0.1;
	private final double	Q_0		= 0.98;
	private double			TAU_0;
	
	private int			nrOfNodes;
	private final int	DIST_MIN = 1;
	private int[][] 	distance;
	private double[][] 	heuristic;
	private double[][]	pheromone;
	private double[][]	choiceInfo;
	private Ant[]		ants;
	
	private Ant 		bestSoFarAnt;
	private Ant			nearestNeighborAnt;
	
	
	// statistics
	private int roundsWithoutChange;
	
	
	private Random random = new Random();
	
	
	
	public ACSAlgorithm(final int[][] distance) {
		
		// distance
		nrOfNodes = distance.length;
		if(nrOfNodes < 2 || nrOfNodes != distance[0].length) {
			throw new IllegalArgumentException("Expected quadratic matrix of dimension at least 3");
		}
		
		this.distance = distance;
		heuristic = new double[nrOfNodes][nrOfNodes];
		
		for(int row=0; row<nrOfNodes; row++) {
			for(int col=0; col<nrOfNodes; col++) {
				if(row == col) {
					heuristic[row][col] = Double.NaN;
				}
				else {
					if(this.distance[row][col] < DIST_MIN) {
						this.distance[row][col] = DIST_MIN;
					}
					heuristic[row][col] = Math.pow(1.0 / this.distance[row][col], BETA);
				}
			}
		}
		
		// nearest neighbours
		nearestNeighborAnt	= new Ant(nrOfNodes);
		constructNearestNeighbourAnt();

		// ants
		bestSoFarAnt		= new Ant(nrOfNodes);
		ants = new Ant[NR_ANTS];
		for(int i=0; i<NR_ANTS; i++) {
			ants[i] = new Ant(nrOfNodes);
		}
		
		// remainder
		TAU_0 = 1.0 / (nrOfNodes * nearestNeighborAnt.getPathLength()); 
		pheromone  = new double[nrOfNodes][nrOfNodes];
		choiceInfo = new double[nrOfNodes][nrOfNodes];
	}
	
	private void constructNearestNeighbourAnt() {
		int start = random.nextInt(nrOfNodes);
		nearestNeighborAnt.setPath(0, start);
		nearestNeighborAnt.setPath(nrOfNodes, start);
		
		int current = start;
		for(int i=1; i<nrOfNodes; i++) {
			int next = 0;
			double minDist = Double.MAX_VALUE;
			for(int j=0; j<nrOfNodes; j++) {
				if(!nearestNeighborAnt.hasVisited(j) && distance[current][j] < minDist) {
					minDist = distance[current][j];
					next = j;
				}
			}
			nearestNeighborAnt.setPath(i, next);
			current = next;
		}
		
		computePathLength(nearestNeighborAnt);
	}
	
	
	@Override
	public void runOptimization() {
		initializeData();
		initializeStatistics();
		if(nrOfNodes == 2) {
			return;
		}
		while(!terminate()) {
			constructSolutions();
			localSearch();
			updateStatistics();
			updatePheromones();
		}
	}
	
	
	private void initializeStatistics() {
		roundsWithoutChange = 0;
	}
	
	private void initializeData() {
		// pheromones
		for(int i=0; i<nrOfNodes; i++) {
			for(int j=0; j<nrOfNodes; j++) {
				if(i == j) {
					pheromone[i][j]  = Double.NaN;
					choiceInfo[i][j] = Double.NaN;
				}
				else {
					pheromone[i][j]  = TAU_0;
					choiceInfo[i][j] = TAU_0 * heuristic[i][j];
				}
			}
		}
		
		// ants
		bestSoFarAnt.reset();
		bestSoFarAnt = nearestNeighborAnt.copy();
		
		for(Ant a : ants) {
			a.reset();
		}
	}
	
	private boolean terminate() {
		return roundsWithoutChange > 1000;
	}
	
	private void localSearch() {
		for(Ant a : ants) {
			searchLocalImprovment(a);
		}
	}
	private void searchLocalImprovment(Ant a) {
		Ant copy = a.copy();
		for(int i=1; i<nrOfNodes; i++) {
			int pos1 = copy.getPath(i);
			for(int j=i+1; j<nrOfNodes; j++) {
				int pos2 = copy.getPath(j);
				copy.setPath(i, pos2);
				copy.setPath(j, pos1);
				computePathLength(copy);
				if(copy.getPathLength() < a.getPathLength()) {
					// better path found
					a = copy;
					return;
				}
				else {
					// redo change
					copy.setPath(i, pos1);
					copy.setPath(j, pos2);
				}
			}
		}
		
	}
	
	private void updateStatistics() {
		roundsWithoutChange++;
		
		for(Ant a : ants) {
			if(a.getPathLength() < bestSoFarAnt.getPathLength()) {
				bestSoFarAnt = a.copy();
				roundsWithoutChange = 0;
			}
		}
	}
	
	private void updatePheromones() {
		for(int i=0; i<nrOfNodes; i++) {
			int from = bestSoFarAnt.getPath(i);
			int to 	 = bestSoFarAnt.getPath(i+1);
			pheromone[from][to] *= (1 - RHO); // evaporate
			pheromone[from][to] += RHO / bestSoFarAnt.getPathLength();
			choiceInfo[from][to] = pheromone[from][to] * heuristic[from][to];
		}
	}
	
	
	private void constructSolutions() {
		// choose random start node
		for(Ant a : ants) {
			a.reset();
			int firstNode = random.nextInt(nrOfNodes);
			a.setPath(0, firstNode);
			a.setPath(nrOfNodes, firstNode);
		}
		
		// construct ant paths
		for(int step=1; step<nrOfNodes; step++) {
			for(Ant a : ants) {
				if(random.nextDouble() <= Q_0) {
					chooseBestNext(a, step);
				}
				else {
					asDecisionRule(a, step);
				}
				localPheromoneUpdate(a, step);
			}
		}
		
		// operations at end
		for(Ant a : ants) {
			localPheromoneUpdate(a, nrOfNodes);
			computePathLength(a);
		}
	}
	
	private void computePathLength(Ant ant) {
		int len = 0;
		for(int i=0; i<nrOfNodes; i++) {
			len += distance[ant.getPath(i)][ant.getPath(i+1)];
		}
		ant.setPathLength(len);
	}
	
	
	private void asDecisionRule(Ant ant, int step) {
		int lastNode = ant.getPath(step-1);
		double sumProbabilities = 0.0;
		double[] selectionProbabiliy = new double[nrOfNodes];
		
		for(int i=0; i<nrOfNodes; i++) {
			if(ant.hasVisited(i)) {
				selectionProbabiliy[i] = 0.0;
			}
			else {
				selectionProbabiliy[i] = choiceInfo[lastNode][i];
				sumProbabilities += selectionProbabiliy[i];
			}
		}
		
		double rand = random.nextDouble() * sumProbabilities;
		int nr = 0;
		while(rand > selectionProbabiliy[nr]) {
			rand -= selectionProbabiliy[nr];
			nr++;
		}
		ant.setPath(step, nr);
	}
	
	
	private void chooseBestNext(Ant ant, int step) {
		int bestNode 	= 0;
		double bestVal	= 0.0;
		int lastNode  	= ant.getPath(step-1);
		for(int i=0; i<nrOfNodes; i++) {
			if(!ant.hasVisited(i) && choiceInfo[lastNode][i] > bestVal) {
				bestNode = i;
				bestVal = choiceInfo[lastNode][i];
			}
		}
		ant.setPath(step, bestNode);
	}
	
	private void localPheromoneUpdate(Ant ant, int step) {
		int lastNode 	= ant.getPath(step-1);
		int currentNode = ant.getPath(step);
		pheromone[lastNode][currentNode] = pheromone[lastNode][currentNode] * (1 - XI) + XI * TAU_0;
		choiceInfo[lastNode][currentNode] = pheromone[lastNode][currentNode] * heuristic[lastNode][currentNode];
	}
	
	
	
	@Override
	public int[] getResultingPath() {
		int[] res = new int[nrOfNodes+1];
		for(int i=0; i<=nrOfNodes; i++) {
			res[i] = bestSoFarAnt.getPath(i);
		}
		return res;
	}
	
	@Override
	public int getResultingPathLength() {
		return bestSoFarAnt.getPathLength();
	}

	@Override
	public boolean isFeasiblePath() {
		int[]		res 		= getResultingPath();
		if(res[0] != res[res.length-1]) {
			return false;
		}
		
		boolean[]	contained	= new boolean[res.length-1];
		for(int i=0; i<contained.length; i++) {
			if(res[i] < 0 || res[i] > nrOfNodes-1) {
				return false;
			}
			contained[i] = false;
		}
		
		for(int i=0; i<res.length-1; i++) {
			if(contained[res[i]]) {
				// already visited
				return false;
			}
			contained[res[i]] = true;
		}
		
		return true;
	}
	
	
	
//	private void print(Ant ant) {
//		System.out.println("Ant Path:");
//		for(int i=0; i<=nrOfNodes; i++) {
//			System.out.print(ant.getPath(i) + "\t");
//		}
//		System.out.println("\nLength:\n" + ant.getPathLength() + "\n");
//	}
	
	
	
	
	private class Ant {
		private int			nodes;
		private int			pathLength;
		private int[]		path;
		private boolean[]	visited;
		
		public Ant(int nodes) {
			this.nodes 	= nodes;
			path		= new int[nodes+1];
			visited		= new boolean[nodes];
		}
		public Ant copy() {
			Ant copy = new Ant(nodes);
			for(int step=0; step<=nodes; step++) {
				copy.setPath(step, path[step]);
			}
			copy.setPathLength(pathLength);
			return copy;
		}
		
		public void reset() {
			pathLength	= 0;
			for(int i=0; i<nodes; i++) {
				visited[i] = false;
			}
		}

		public boolean hasVisited(int node) {
			return visited[node];
		}

		public int getPath(int i) {
			return path[i];
		}
		public void setPath(int step, int node) {
			path[step] = node;
			visited[node] = true;
		}

		public int getPathLength() {
			return pathLength;
		}
		public void setPathLength(int len) {
			pathLength = len;
		}
	}
	
}
