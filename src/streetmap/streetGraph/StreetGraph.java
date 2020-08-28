package streetmap.streetGraph;

import java.util.HashMap;
import java.util.Map;

import streetmap.model.Compass;
import streetmap.model.Street;
import streetmap.model.StreetType;

public class StreetGraph {

	private Map<Integer, Map<Integer, StreetNode>>	nodes;
	private Map<Integer, StreetNode>			 	markedNodes;
	private int										nextMarkedId = 0;
	private boolean									isMapComplete = true;
	
	
	public StreetGraph() {
		nodes		= new HashMap<Integer, Map<Integer,StreetNode>>();
		markedNodes = new HashMap<Integer, StreetNode>();
	}
	
	public Map<Integer, StreetNode> getMarkedMap() {
		return markedNodes;
	}
	public int getNumberOfMarked() {
		return nextMarkedId;
	}
	
	
	private boolean hasNode(int x, int y) {
		return nodes.containsKey(x) && nodes.get(x).containsKey(y);
	}
	private StreetNode getNode(int x, int y) {
		return nodes.get(x).get(y);
	}
	
	
	private void addNode(Street street) {
		int x = street.getXPos();
		if(!nodes.containsKey(x)) {
			nodes.put(x, new HashMap<Integer, StreetNode>());
		}
		StreetNode sn = new StreetNode(street);
		nodes.get(x).put(street.getYPos(), sn);
		if(street.isMarked()) {
			markedNodes.put(nextMarkedId, sn);
			nextMarkedId++;
		}
	}
	
	
	public void build(Map<Integer, Map<Integer, Street>> streets) {
		// build nodes
		for(Map<Integer, Street> yMap : streets.values()) {
			for(Street s : yMap.values()) {
				addNode(s);
			}
		}
		
		// build connections
		for(Map<Integer, StreetNode> yMap : nodes.values()) {
			for(StreetNode fromNode : yMap.values()) {
				StreetType type = fromNode.getStreetType();
				if(type.isOneWayStreet()) {
					tryConnecting(fromNode, type.getOneWayToDirection());
				}
				else { // no one way street
					if(type.hasEastConnection()) {
						tryConnecting(fromNode, Compass.EAST);
					}
					if(type.hasNorthConnection()) {
						tryConnecting(fromNode, Compass.NORTH);
					}
					if(type.hasWestConnection()) {
						tryConnecting(fromNode, Compass.WEST);
					}
					if(type.hasSouthConnection()) {
						tryConnecting(fromNode, Compass.SOUTH);
					}
				}
			}
		}	
	}
	
	private void tryConnecting(StreetNode fromNode, Compass fromDir) {
		int fromX 		= fromNode.getX();
		int fromY 		= fromNode.getY();
		int toX			= fromX;
		int toY 		= fromY;
		Compass toDir = fromDir.getOpposite();
		
		switch(fromDir) {
		case EAST:	toX += 1; break;
		case NORTH: toY -= 1; break;
		case WEST:	toX -= 1; break;
		case SOUTH: toY += 1; break;
		}
		
		if(hasNode(toX, toY)) {
			StreetNode toNode = getNode(toX, toY);
			StreetType toType = toNode.getStreetType();
			if(toType.isOneWayStreet()) {
				if(toType.getOneWayFromDirection() == toDir) {
					fromNode.addFollower(toNode);
				}
			}
			else { // no one way street
				if(toType.hasConnection(toDir)) {
					fromNode.addFollower(toNode);
				}
				else {
					System.out.println("1 - fromX: " + fromX + ", fromY: " + fromY + ", toX: " + toX + ", toY: " + toY);
					isMapComplete = false;
				}
			}
		}
		else {
			System.out.println("2 - fromX: " + fromX + ", fromY: " + fromY + ", toX: " + toX + ", toY: " + toY);
			isMapComplete = false;
		}
	}
	
	public boolean isMapComplete() {
		return isMapComplete;
	}
	
}
