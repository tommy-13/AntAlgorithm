package streetmap.observer;


public interface SaveStateObserver {

	public void fireChangedSaveState(String newPath, boolean savingNecessary);

}
