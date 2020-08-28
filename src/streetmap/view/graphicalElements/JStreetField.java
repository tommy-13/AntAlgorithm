package streetmap.view.graphicalElements;

import java.awt.Point;

import javax.swing.JButton;

import language.Messages;
import streetmap.globals.Constant;
import streetmap.globals.Function;
import streetmap.model.StreetType;

@SuppressWarnings("serial")
public class JStreetField extends JButton {

	public static final int fieldLength = 32;
	
	private StreetType	streetType;
	private int			cost;
	private Point		offset;
	private boolean		marked 		= false;
	private boolean		highlighted = false;
	private boolean		bigPics 	= true;
	
	
	public JStreetField(int offsetX, int offsetY) {
		super();
		
		this.offset = new Point(offsetX, offsetY);
		setStreetType(StreetType.GRASS, 0, false, Constant.INIT_TOPLEFT_CORNER);
		setBorder(null);
		setMarked(false);
		setRolloverEnabled(true);
		setBackground(null);
		setOpaque(true);
		setFocusable(false);
	}
	
	public StreetType getStreetType() {
		return streetType;
	}
	public int getCost() {
		return cost;
	}
	
	public void setStreetType(StreetType streetType, int cost, boolean marked, Point topLeftCorner) {
		this.streetType = streetType;
		this.cost		= cost;
		this.marked		= marked;
		
		int x = topLeftCorner.x + offset.x;
		int y = topLeftCorner.y + offset.y;
		String toolTip = Messages.getString("Global.Position") + ": (" + x + ", " + y + ")";
		if(streetType == StreetType.GRASS) {
			setToolTipText(toolTip);
		}
		else {
			setToolTipText("<html>" + Messages.getString("Global.WayCost") + ": " + cost +
					"<br>" + toolTip + "</html>");
		}
		
		updateIcons();
	}
	
	
	private void updateIcons() {
		if(marked) {
			setIcon(Function.getImage(streetType, 2, bigPics));
			setPressedIcon(Function.getImage(streetType, 3, bigPics));
			setRolloverIcon(Function.getImage(streetType, 3, bigPics));
			if(highlighted) {
				setDisabledIcon(Function.getImage(streetType, 2, bigPics));
			}
			else {
				setDisabledIcon(Function.getImage(streetType, 5, bigPics));
			}
		}
		else {
			setIcon(Function.getImage(streetType, 0, bigPics));
			setPressedIcon(Function.getImage(streetType, 1, bigPics));
			setRolloverIcon(Function.getImage(streetType, 1, bigPics));
			if(highlighted) {
				setDisabledIcon(Function.getImage(streetType, 0, bigPics));
			}
			else {
				setDisabledIcon(Function.getImage(streetType, 4, bigPics));
			}
		}
	}
	
	public void setMarked(boolean marked) {
		if(this.marked == marked) {
			return;
		}
		this.marked = marked;
		updateIcons();
	}
	
	public void setHighlighted(boolean highlighted) {
		if(this.highlighted == highlighted) {
			return;
		}
		this.highlighted = highlighted;
		updateIcons();
	}
	
	
	public void setBigPictures(boolean big) {
		if(bigPics == big) {
			return;
		}
		bigPics = big;
		updateIcons();
	}
	
}
