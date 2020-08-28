package streetmap.observer;


public interface DataObservable {
	
	public void registerDataObserver(DataObserver o);
	public void removeDataObserver(DataObserver o);
	public void notifyObserversDataReseted();
	public void notifyObserversDataChange();

}
