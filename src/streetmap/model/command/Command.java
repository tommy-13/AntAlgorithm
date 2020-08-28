package streetmap.model.command;

public interface Command {

	public void execute();
	public void undo();
	public CommandType getType();
	
}
