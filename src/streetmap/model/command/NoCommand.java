package streetmap.model.command;

public class NoCommand implements Command {
	
	public NoCommand() {}

	@Override
	public void execute() {}

	@Override
	public void undo() {}

	@Override
	public CommandType getType() {
		return CommandType.NO;
	}
}
