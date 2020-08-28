package streetmap.streetGraph;

import java.util.LinkedList;
import java.util.List;

import streetmap.model.Street;
import streetmap.model.StreetType;

public class StreetNode {

	private Street		street;
	private List<StreetNode>	followers;
	
	
	public StreetNode(Street street) {
		this.street		= street;
		this.followers	= new LinkedList<StreetNode>();
	}
	
	public StreetType getStreetType() {
		return street.getStreetType();
	}
	public int getX() {
		return street.getXPos();
	}
	public int getY() {
		return street.getYPos();
	}
	public int getCost() {
		return street.getCost();
	}
	public boolean isMarked() {
		return street.isMarked();
	}
	public List<StreetNode> getFollowers() {
		return followers;
	}
	
	public void addFollower(StreetNode follower) {
		followers.add(follower);
	}
	
}
