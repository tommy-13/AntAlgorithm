package streetmap.model.command;

import streetmap.model.DataBase;
import streetmap.model.Street;


public class AddStreetCommand implements Command {
	
	private Street street;
	
	public AddStreetCommand(Street street) {
		this.street = street;
	}
	
	
	@Override
	public void execute() {
		DataBase.getInstance().addStreet(street.copy());
	}

	@Override
	public void undo() {
		DataBase.getInstance().removeStreet(street);
	}

	@Override
	public CommandType getType() {
		return CommandType.NEW_STREET;
	}

}
