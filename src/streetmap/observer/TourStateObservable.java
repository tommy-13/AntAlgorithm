package streetmap.observer;

public interface TourStateObservable {
	
	public void registerTourStateObserver(TourStateObserver observer);
	public void removeTourStateObserver(TourStateObserver observer);
	
	public void notifyPartialPathChanged(int id);
	public void notifyTourClosed();
	
}
