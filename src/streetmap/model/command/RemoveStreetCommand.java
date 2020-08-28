package streetmap.model.command;

import streetmap.model.DataBase;
import streetmap.model.Street;


public class RemoveStreetCommand implements Command {
	
	private Street street;
	
	public RemoveStreetCommand(int x, int y) {
		street = DataBase.getInstance().getStreet(x, y);
	}

		
	@Override
	public void execute() {
		DataBase.getInstance().removeStreet(street);
	}

	@Override
	public void undo() {
		DataBase.getInstance().addStreet(street.copy());
	}

	@Override
	public CommandType getType() {
		return CommandType.REMOVE_STREET;
	}

}
