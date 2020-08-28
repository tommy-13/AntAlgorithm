package streetmap.observer;


public interface CommandListObserver {

	public void fireUndoStackState(boolean isEmpty);
	public void fireRedoStackState(boolean isEmpty);

}
