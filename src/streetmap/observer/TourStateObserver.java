package streetmap.observer;

public interface TourStateObserver {

	public void firePartialPathChanged(int id);
	public void fireTourClosed();
	
}
