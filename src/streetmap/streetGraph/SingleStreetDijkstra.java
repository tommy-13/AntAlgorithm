package streetmap.streetGraph;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import toolbox.Tuple;

public class SingleStreetDijkstra {

	private StreetNode 									startingNode;
	private Map<StreetNode, Tuple<Integer, StreetNode>>	results;
	
	
	public SingleStreetDijkstra(StreetNode startingNode) {
		this.startingNode = startingNode;
		this.results = new HashMap<StreetNode, Tuple<Integer, StreetNode>>();
	}
	
	
	public void run() {
		Map<StreetNode, Tuple<Integer, StreetNode>> openNodes = new HashMap<StreetNode, Tuple<Integer, StreetNode>>();
		openNodes.put(startingNode, new Tuple<Integer, StreetNode>(0, null));
		while(!openNodes.isEmpty()) {
			StreetNode curNode = getBestNode(openNodes);
			results.put(curNode, openNodes.get(curNode));
			int curCost = openNodes.get(curNode).getFirst();
			openNodes.remove(curNode);
			
			for(StreetNode n : curNode.getFollowers()) {
				if(results.containsKey(n)) {
					continue;
				}
				if(openNodes.containsKey(n)) {
					Tuple<Integer, StreetNode> value = openNodes.get(n);
					if(curCost + n.getCost() < value.getFirst()) {
						// better way found
						openNodes.put(n, new Tuple<Integer, StreetNode>(curCost + n.getCost(), curNode));
					}
				}
				else {
					openNodes.put(n, new Tuple<Integer, StreetNode>(curCost + n.getCost(), curNode));
				}
			}
		}
		
	}
	
	private StreetNode getBestNode(Map<StreetNode, Tuple<Integer, StreetNode>> openNodes) {
		StreetNode 	bestNode 	= null;
		int 		bestValue	= Integer.MAX_VALUE; 
		
		for(Entry<StreetNode, Tuple<Integer, StreetNode>> entry : openNodes.entrySet()) {
			if(entry.getValue().getFirst() < bestValue) {
				bestValue = entry.getValue().getFirst();
				bestNode  = entry.getKey();
			}
		}
		
		return bestNode;
	}
	
	
	public boolean hasNode(StreetNode node) {
		return results.containsKey(node);
	}
	public int getCost(StreetNode node) {
		return results.get(node).getFirst();
	}
	
	public List<Point> getPath(StreetNode target) {
		List<Point> path = new LinkedList<Point>();
		while(target != null) {
			path.add(0, new Point(target.getX(), target.getY()));
			target = results.get(target).getSecond();
		}
		return path;
	}
	
}
