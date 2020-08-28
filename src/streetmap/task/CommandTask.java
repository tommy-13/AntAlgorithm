package streetmap.task;

import java.awt.Component;

import language.Messages;
import streetmap.model.command.Command;
import streetmap.model.command.CommandList;

public class CommandTask implements Task {
	
	private Component	parent;
	private Command		command;
	private String		title;
	private String		text;
	
	public CommandTask(Component parent, Command command) {
		this.parent = parent;
		this.command = command;
		title = Messages.getString("Task.CommandTitle");
		text  = Messages.getString("Task.CommandText") + ": " + command.getType().toString();
	}
	
	
	
	@Override
	public boolean isDone() {
		return true;
	}
	
	@Override
	public void execute() {
		command.execute();
		CommandList.getInstance().put(command);
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
