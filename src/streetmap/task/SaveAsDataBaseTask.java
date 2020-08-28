package streetmap.task;

import io.safeLoad.DataBaseSaver;

import java.awt.Component;

import language.Messages;

public class SaveAsDataBaseTask implements Task {
	
	private Component	parent;
	private boolean		isSaved = false;
	
	public SaveAsDataBaseTask(Component parent) {
		this.parent = parent;
	}
	
	
	
	@Override
	public boolean isDone() {
		return isSaved;
	}
	
	@Override
	public void execute() {
		isSaved = DataBaseSaver.safe(true, parent);
	}

	@Override
	public Component getParent() {
		return parent;
	}

	@Override
	public String getTaskTitle() {
		return Messages.getString("Task.SaveAsTitle");
	}

	@Override
	public String getTaskText() {
		return Messages.getString("Task.SaveAsText");
	}

}
