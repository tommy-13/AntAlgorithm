package streetmap.model;

public enum Compass {

	EAST,
	NORTH,
	WEST,
	SOUTH;
	
	public Compass getOpposite() {
		switch(this) {
		case EAST: return WEST;
		case WEST: return EAST;
		case NORTH: return SOUTH;
		case SOUTH: return NORTH;
		default: return null;
		}
	}
}
