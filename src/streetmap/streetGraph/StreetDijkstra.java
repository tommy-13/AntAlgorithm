package streetmap.streetGraph;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StreetDijkstra {
	
	private Map<Integer, StreetNode> 			markedNodes;
	private boolean								isMapCorrect;
	private Map<Integer, SingleStreetDijkstra> 	singleStreetDijkstra;
	private int[][]								dist;
	
	
	public StreetDijkstra(Map<Integer, StreetNode> markedNodes) {
		this.markedNodes	 = markedNodes;
		isMapCorrect		 = true;
		singleStreetDijkstra = new HashMap<Integer, SingleStreetDijkstra>();
		for(Entry<Integer, StreetNode> entry : markedNodes.entrySet()) {
			singleStreetDijkstra.put(entry.getKey(), new SingleStreetDijkstra(entry.getValue()));
		}
		dist = new int[markedNodes.size()][markedNodes.size()];
	}
	
	
	public void run() {
		for(SingleStreetDijkstra ssd : singleStreetDijkstra.values()) {
			ssd.run();
		}
		
		// get distances
		for(Entry<Integer, SingleStreetDijkstra> entry : singleStreetDijkstra.entrySet()) {
			int from = entry.getKey();
			SingleStreetDijkstra ssd = entry.getValue();
			for(Entry<Integer, StreetNode> eNode : markedNodes.entrySet()) {
				if(ssd.hasNode(eNode.getValue())) {
					int to = eNode.getKey();
					dist[from][to] = ssd.getCost(eNode.getValue());
				}
				else {
					isMapCorrect = false;
					return;
				}
			}
		}
	}
	
	public int[][] getDistanceMatrix() {
		return dist;
	}
	
	public int getDistance(int from, int to) {
		return dist[from][to];
	}
	
	public List<Point> getPath(int from, int to) {
		StreetNode target = markedNodes.get(to);
		return singleStreetDijkstra.get(from).getPath(target);
	}
	
	public boolean isMapCorrect() {
		return isMapCorrect;
	}
	
}
