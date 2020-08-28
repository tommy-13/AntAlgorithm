package streetmap.view.graphicalElements;

import javax.swing.Icon;
import javax.swing.JButton;

import streetmap.globals.Function;
import streetmap.model.StreetType;

@SuppressWarnings("serial")
public class JStreetButton extends JButton {
	
	private StreetType	streetType;
	private Icon		iconNormal;
	private Icon		iconRollover;
	private Icon		iconDisabled;
	private Icon		iconChosen;
	private Icon		iconChosenRollover;
	private Icon		iconChosenDisabled;
	private boolean		chosen;

	
	public JStreetButton(StreetType streetType) {
		super();
		this.streetType		= streetType;
		iconNormal			= Function.getImage(streetType, 0, true);
		iconRollover		= Function.getImage(streetType, 1, true);
		iconChosen			= Function.getImage(streetType, 2, true);
		iconChosenRollover	= Function.getImage(streetType, 3, true);
		iconDisabled		= Function.getImage(streetType, 4, true);
		iconChosenDisabled	= Function.getImage(streetType, 5, true);
		
		setIcon(iconNormal);
		setPressedIcon(iconRollover);
		setRolloverEnabled(true);
		setRolloverIcon(iconRollover);
		setDisabledIcon(iconDisabled);
		setBorder(null);
		setBackground(null);
		setOpaque(true);
		setFocusable(false);
		
		chosen = false;
	}
	
	public StreetType getStreetType() {
		return streetType;
	}
	
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
		if(chosen) {
			setIcon(iconChosen);
			setRolloverIcon(iconChosenRollover);
			setPressedIcon(iconChosenRollover);
			setDisabledIcon(iconChosenDisabled);
		}
		else {
			setIcon(iconNormal);
			setRolloverIcon(iconRollover);
			setPressedIcon(iconRollover);
			setDisabledIcon(iconDisabled);
		}
	}
	public boolean isChosen() {
		return chosen;
	}
}
