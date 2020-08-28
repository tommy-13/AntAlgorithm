package streetmap.solver;

public interface TravellingSalesmanSolver {
	
	public void 	runOptimization();
	public int[] 	getResultingPath();
	public int 		getResultingPathLength();
	public boolean 	isFeasiblePath();
	
}
