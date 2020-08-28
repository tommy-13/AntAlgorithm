package streetmap.view.graphicalElements;

import javax.swing.Icon;
import javax.swing.JButton;

import streetmap.globals.Function;

@SuppressWarnings("serial")
public class JArrowButton extends JButton {
	
	public static final int LEFT	= 180;
	public static final int UP		= 90;
	public static final int DOWN	= 270;
	public static final int RIGHT	= 0;
	public static final int ZOOM	= 777;
	public static final int ZERO	= 888;
	
	private Icon		iconNormal;
	private Icon		iconRollover;

	
	public JArrowButton(int type) {
		super();
		iconNormal		= Function.getButtonImage(type, 0);
		iconRollover	= Function.getButtonImage(type, 1);
		
		setIcon(iconNormal);
		setPressedIcon(iconRollover);
		setRolloverEnabled(true);
		setRolloverIcon(iconRollover);
		setBorder(null);
		setBackground(null);
		setOpaque(true);
		setFocusable(false);
	}
}
