package streetmap.task;

import java.awt.Component;

public interface Task {

	public void execute();
	public boolean isDone();
	public Component getParent();
	public String getTaskTitle();
	public String getTaskText();
	
}
