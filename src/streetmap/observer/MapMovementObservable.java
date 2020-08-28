package streetmap.observer;

public interface MapMovementObservable {

	public void registerMapMovementObserver(MapMovementObserver observer);
	public void removeMapMovementObserver(MapMovementObserver observer);
	
	public void notifyMoveRequest(int dirX, int dirY);
	public void notifyJumpRequest(int x, int y);
	public void notifyZoomRequest();
	
}
