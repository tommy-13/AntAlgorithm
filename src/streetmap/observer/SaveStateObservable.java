package streetmap.observer;

public interface SaveStateObservable {
	
	public void registerSaveStateObserver(SaveStateObserver o);
	public void removeSaveStateObserver(SaveStateObserver o);
	public void notifyObserversSaveStateChange();

}
