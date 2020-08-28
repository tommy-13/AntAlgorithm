package streetmap.model;

public class Street {

	protected StreetType streetType;
	
	protected int		xPos;
	protected int		yPos;
	protected int		cost;
	protected boolean	marked;
	
	
	public Street(StreetType streetType, int x, int y, int cost, boolean marked) {
		this.streetType = streetType;
		this.xPos		= x;
		this.yPos		= y;
		this.cost		= cost;
		this.marked		= marked;
	}
	
	public Street copy() {
		return new Street(streetType, xPos, yPos, cost, marked);
	}
	
	
	public StreetType getStreetType() {
		return streetType;
	}
	public int getXPos() {
		return xPos;
	}
	public int getYPos() {
		return yPos;
	}
	public int getCost() {
		return cost;
	}
	public boolean isMarked() {
		return marked;
	}
	
}
