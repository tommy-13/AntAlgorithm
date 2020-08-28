package streetmap.observer;

public interface CommandListObservable {
	
	public void registerCommandListObserver(CommandListObserver o);
	public void removeCommandListObserver(CommandListObserver o);
	public void notifyObserversUndoStackState(boolean isEmpty);
	public void notifyObserversRedoStackState(boolean isEmpty);

}
