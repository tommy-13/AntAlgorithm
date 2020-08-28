package streetmap.task;

import java.awt.Component;

import language.Messages;
import streetmap.model.command.CommandList;

public class UndoTask implements Task {
	
	private Component	parent;
	private String		title;
	private String		text;
	
	public UndoTask(Component parent) {
		this.parent = parent;
		title = Messages.getString("Task.UndoTitle");
		text  = Messages.getString("Task.UndoText") + ": " +
				CommandList.getInstance().getTopUndoCommand().toString();
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
	@Override
	public void execute() {
		CommandList.getInstance().undo();
	}
	@Override
	public Component getParent() {
		return parent;
	}
	@Override
	public String getTaskTitle() {
		return title;
	}
	@Override
	public String getTaskText() {
		return text;
	}

}
