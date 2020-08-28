package streetmap.model;

public enum StreetType {

	SOUTH_TO_NORTH,			// one way streets
	NORTH_TO_SOUTH,
	EAST_TO_WEST,
	WEST_TO_EAST,

	SOUTH_TO_EAST,
	SOUTH_TO_WEST,
	NORTH_TO_EAST,
	NORTH_TO_WEST,

	EAST_TO_SOUTH,
	WEST_TO_SOUTH,
	EAST_TO_NORTH,
	WEST_TO_NORTH,
	
	
	SOUTH,					// connection to the south
	NORTH,
	EAST,
	WEST,

	EAST_SOUTH,
	WEST_SOUTH,
	EAST_NORTH,
	NORTH_WEST,

	EAST_NORTH_SOUTH,
	NORTH_WEST_SOUTH,
	EAST_WEST_SOUTH,
	EAST_NORTH_WEST,

	NORTH_SOUTH,
	EAST_WEST,
	EAST_NORTH_WEST_SOUTH,
	GRASS;
	
	public boolean isOneWayStreet() {
		return this.name().contains("TO");
	}
	public Compass getOneWayToDirection() {
		switch(this) {
		case SOUTH_TO_NORTH: case EAST_TO_NORTH: case WEST_TO_NORTH:
			return Compass.NORTH;
		case NORTH_TO_SOUTH: case EAST_TO_SOUTH: case WEST_TO_SOUTH:
			return Compass.SOUTH;
		case EAST_TO_WEST: case SOUTH_TO_WEST: case NORTH_TO_WEST:
			return Compass.WEST;
		case WEST_TO_EAST: case SOUTH_TO_EAST: case	NORTH_TO_EAST:
			return Compass.EAST;
		default: return null;
		}
	}
	public Compass getOneWayFromDirection() {
		switch(this) {
		case NORTH_TO_SOUTH: case NORTH_TO_WEST: case NORTH_TO_EAST:
			return Compass.NORTH;
		case SOUTH_TO_WEST: case SOUTH_TO_EAST: case SOUTH_TO_NORTH:
			return Compass.SOUTH;
		case WEST_TO_SOUTH: case WEST_TO_EAST: case WEST_TO_NORTH:
			return Compass.WEST;
		case EAST_TO_WEST: case EAST_TO_SOUTH: case	EAST_TO_NORTH:
			return Compass.EAST;
		default: return null;
		}
	}
	
	public boolean isOneWayStreetToEast() {
		return this.name().endsWith("TO_EAST");
	}
	public boolean isOneWayStreetToNorth() {
		return this.name().endsWith("TO_NORTH");
	}
	public boolean isOneWayStreetToWest() {
		return this.name().endsWith("TO_WEST");
	}
	public boolean isOneWayStreetToSouth() {
		return this.name().endsWith("TO_SOUTH");
	}
	public boolean isOneWayStreetFromEast() {
		return this.name().startsWith("EAST_TO");
	}
	public boolean isOneWayStreetFromNorth() {
		return this.name().startsWith("NORTH_TO");
	}
	public boolean isOneWayStreetFromWest() {
		return this.name().startsWith("WEST_TO");
	}
	public boolean isOneWayStreetFromSouth() {
		return this.name().startsWith("SOUTH_TO");
	}
	
	public boolean hasConnection(Compass direction) {
		switch(direction) {
		case EAST:	return hasEastConnection();
		case NORTH:	return hasNorthConnection();
		case WEST:	return hasWestConnection();
		case SOUTH:	return hasSouthConnection();
		default:	return false;
		}
	}
	public boolean hasEastConnection() {
		return this.name().contains("EAST");
	}
	public boolean hasNorthConnection() {
		return this.name().contains("NORTH");
	}
	public boolean hasWestConnection() {
		return this.name().contains("WEST");
	}
	public boolean hasSouthConnection() {
		return this.name().contains("SOUTH");
	}
	
}
