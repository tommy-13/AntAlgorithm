package streetmap.task;

import java.awt.Component;

import language.Messages;
import streetmap.model.command.CommandList;

public class RedoTask implements Task {
	
	private Component	parent;
	private String		title;
	private String		text;
	
	public RedoTask(Component parent) {
		this.parent = parent;
		title = Messages.getString("Task.RedoTitle");
		text  = Messages.getString("Task.RedoText") + ": " +
				CommandList.getInstance().getTopRedoCommand().toString();
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
	@Override
	public void execute() {
		CommandList.getInstance().redo();
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
