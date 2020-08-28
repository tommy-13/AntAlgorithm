package streetmap.model;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourSolution {
	
	private static TourSolution instance = new TourSolution();
	private TourSolution() {
		paths = new HashMap<Integer, List<Point>>();
		costs = new HashMap<Integer, Integer>();
	}
	public static TourSolution getInstance() {
		return instance;
	}

	
	private Map<Integer, List<Point>> 	paths;
	private Map<Integer, Integer>		costs;
	private int							allCosts;
	
	public void reset() {
		paths.clear();
		costs.clear();
		allCosts = 0;
	}
	public void addPath(int id, List<Point> path, int cost) {
		if(costs.containsKey(id)) {
			allCosts -= costs.get(id);
		}
		paths.put(id, path);
		costs.put(id, cost);
		allCosts += cost;
	}
	
	public int getSize() {
		return paths.size();
	}
	public List<Point> getPath(int id) {
		return paths.get(id);
	}
	public int getCost(int id) {
		return costs.get(id);
	}
	public int getAllCost() {
		return allCosts;
	}

	public Point getStartPoint(int id) {
		return paths.get(id).get(0);
	}
	public String getStart(int id) {
		Point p = getStartPoint(id);
		return "(" + p.x + ", " + p.y + ")";
	}
	
	public Point getTargetPoint(int id) {
		List<Point> path = getPath(id);
		return path.get(path.size()-1);
	}
	public String getTarget(int id) {
		Point p = getTargetPoint(id);
		return "(" + p.x + ", " + p.y + ")";
	}
	
	public Point getTopLeft(int id) {
		int 		x		= Integer.MAX_VALUE;
		int 		y 		= Integer.MAX_VALUE;
		List<Point> path	= getPath(id);
		
		for(Point p : path) {
			x = Math.min(x, p.x);
			y = Math.min(y, p.y);
		}
		
		return new Point(x, y);
	}
	
}
