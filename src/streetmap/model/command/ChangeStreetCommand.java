package streetmap.model.command;

import streetmap.model.DataBase;
import streetmap.model.Street;


public class ChangeStreetCommand implements Command {
	
	private int		x;
	private int		y;
	private Street	oldStreet;
	private Street	newStreet;
	
	public ChangeStreetCommand(Street street) {
		this.x = street.getXPos();
		this.y = street.getYPos();
		this.oldStreet = DataBase.getInstance().getStreet(x, y);
		this.newStreet = street;
	}

	
	@Override
	public void execute() {
		DataBase.getInstance().changeStreet(x, y, newStreet.copy());
	}

	@Override
	public void undo() {
		DataBase.getInstance().changeStreet(x, y, oldStreet.copy());
	}

	@Override
	public CommandType getType() {
		return CommandType.EDIT_STREET;
	}

}
