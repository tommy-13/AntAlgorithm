package streetmap.model.command;

import java.util.ArrayList;
import java.util.List;

import streetmap.observer.CommandListObservable;
import streetmap.observer.CommandListObserver;

public class CommandList implements CommandListObservable {

	private static final int STACK_SIZE = 100;
	private static CommandList uniqueList = new CommandList();
	public static CommandList getInstance() {
		return uniqueList;
	}
	
	
	private List<CommandListObserver> observers;
	
	private Command[] undoStack;
	private int undoPointer; 
	private Command[] redoStack;
	private int redoPointer;
	
	private CommandList() {
		observers = new ArrayList<CommandListObserver>();
		
		undoStack = new Command[STACK_SIZE];
		redoStack = new Command[STACK_SIZE];
		clearUndoStack();
		clearRedoStack();
	}
	
	private void clearUndoStack() {
		for(int i=0; i<STACK_SIZE; i++) {
			undoStack[i] = new NoCommand();
		}
		undoPointer = 0;
		notifyObserversUndoStackState(true);
	}
	private void clearRedoStack() {
		for(int i=0; i<STACK_SIZE; i++) {
			redoStack[i] = new NoCommand();
		}
		redoPointer = 0;
		notifyObserversRedoStackState(true);
	}
	
	
	public void put(Command command) {
		if(undoPointer >= STACK_SIZE) {
			for(int i=0; i<STACK_SIZE-1; i++) {
				undoStack[i] = undoStack[i+1];
			}
			undoStack[STACK_SIZE-1] = command;
		}
		else {
			undoStack[undoPointer] = command;
			undoPointer++;
		}
		notifyObserversUndoStackState(false);
		clearRedoStack();
	}
	
	
	public void undo() {
		if(undoPointer == 0) {
			return;
		}
		undoPointer--;
		Command command = undoStack[undoPointer];
		undoStack[undoPointer] = new NoCommand();
		redoStack[redoPointer] = command;
		redoPointer++;
		command.undo();
		notifyObserversRedoStackState(false);
		if(undoPointer == 0) {
			notifyObserversUndoStackState(true);
		}
	}
	
	public void redo() {
		if(redoPointer == 0) {
			return;
		}
		redoPointer--;
		Command command = redoStack[redoPointer];
		redoStack[redoPointer] = new NoCommand();
		undoStack[undoPointer] = command;
		undoPointer++;
		command.execute();
		notifyObserversUndoStackState(false);
		if(redoPointer == 0) {
			notifyObserversRedoStackState(true);
		}
	}

	public void reset() {
		clearUndoStack();
		clearRedoStack();
	}
	
	public boolean isUndoEmpty() {
		return undoPointer == 0;
	}
	public boolean isRedoEmpty() {
		return redoPointer == 0;
	}
	
	public CommandType getTopUndoCommand() {
		return undoStack[undoPointer-1].getType();
	}
	public CommandType getTopRedoCommand() {
		return redoStack[redoPointer-1].getType();
	}

	


	@Override
	public void registerCommandListObserver(CommandListObserver o) {
		observers.add(o);
	}

	@Override
	public void removeCommandListObserver(CommandListObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObserversUndoStackState(boolean isEmpty) {
		for(CommandListObserver clo : observers) {
			clo.fireUndoStackState(isEmpty);
		}
	}

	@Override
	public void notifyObserversRedoStackState(boolean isEmpty) {
		for(CommandListObserver clo : observers) {
			clo.fireRedoStackState(isEmpty);
		}
	}
	
	
}
