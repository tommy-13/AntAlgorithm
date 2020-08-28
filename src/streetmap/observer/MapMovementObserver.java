package streetmap.observer;

public interface MapMovementObserver {

	public void fireMoveRequest(int dirX, int dirY);
	public void fireJumpRequest(int x, int y);
	public void fireZoomRequest();
	
}
