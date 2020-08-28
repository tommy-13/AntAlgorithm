package streetmap.solver;

public class MultiACSAlgorithm implements TravellingSalesmanSolver {

	private int							number;
	private TravellingSalesmanSolver[]	solvers;
	private int[]						path;
	private int							bestLength;
	
	
	public MultiACSAlgorithm(final int[][] distances, int numberOfComputations) {
		number = Math.max(1, numberOfComputations);
		solvers = new TravellingSalesmanSolver[number];
		for(int i=0; i<number; i++) {
			solvers[i] = new ACSAlgorithmLocalInsert(distances);
		}
	}
	
	
	@Override
	public void runOptimization() {
		Thread[] threads = new Thread[number];
		for(int i=0; i<number; i++) {
			final int nr = i;
			threads[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					solvers[nr].runOptimization();
				}
			});
			threads[i].start();
		}
		
		for(int i=0; i<number; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int bestNr = 0;
		bestLength = Integer.MAX_VALUE;
		for(int i=0; i<number; i++) {
			if(solvers[i].isFeasiblePath()) {
				isFeasible = true;
				int pathLen = solvers[i].getResultingPathLength();
				if(pathLen < bestLength) {
					bestLength	= pathLen;
					bestNr		= i;
				}
			}
		}
		path = solvers[bestNr].getResultingPath();
	}
	
	@Override
	public int[] getResultingPath() {
		return path;
	}
	
	@Override
	public int getResultingPathLength() {
		return bestLength;
	}


	private boolean isFeasible = false;
	@Override
	public boolean isFeasiblePath() {
		return isFeasible;
	}
}
