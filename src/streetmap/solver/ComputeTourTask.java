package streetmap.solver;

import java.awt.Point;
import java.util.List;

import streetmap.model.DataBase;
import streetmap.model.TourSolution;
import streetmap.streetGraph.StreetDijkstra;
import streetmap.streetGraph.StreetGraph;

public class ComputeTourTask {
	
	private DataBase					dataBase;
	private StreetGraph					graph;
	private StreetDijkstra				dijkstra;
	private TravellingSalesmanSolver	solver;
	private TourSolution				solution;
	private int							threads;
	
	
	public ComputeTourTask(int nrOfThreads) {
		dataBase = DataBase.getInstance();
		graph	 = new StreetGraph();
		solution = TourSolution.getInstance();
		threads	 = nrOfThreads;
	}
	
	public boolean buildGraph() {
		graph.build(dataBase.getStreetMap());
		return graph.getNumberOfMarked() > 1;
	}
	public boolean isGraphComplete() {
		return graph.isMapComplete();
	}
	
	public boolean computeDistances() {
		dijkstra = new StreetDijkstra(graph.getMarkedMap());
		dijkstra.run();
		return dijkstra.isMapCorrect();
	}
	
	public void computeTour() {
		solver = new MultiACSAlgorithm(dijkstra.getDistanceMatrix(), threads);
		solver.runOptimization();
	}
	
	public void processSolution() {
		int[] permutation = solver.getResultingPath();
		solution.reset();
		for(int i=0; i<permutation.length-1; i++) {
			int 		from	= permutation[i];
			int 		to	 	= permutation[i+1];
			List<Point> path 	= dijkstra.getPath(from, to);
			int 		cost	= dijkstra.getDistance(from, to);
			solution.addPath(i, path, cost);
		}
		
		
//		// TODO: delete printing
//		int	  pathLength  = solver.getResultingPathLength();
//		System.out.println("Tour length: " + pathLength + ", compare: " + solution.getAllCost());
//		for(int i=0; i<permutation.length-1; i++) {
//			System.out.println("id: " + i + ", length: " + solution.getCost(i));
//			for(Point p : solution.getPath(i)) {
//				System.out.print("(" + p.x + ", " + p.y + ")\t");
//			}
//			System.out.println();
//		}
	}

}
