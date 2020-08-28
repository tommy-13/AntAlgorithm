package streetmap.view.graphicalElements;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;

import language.Messages;
import streetmap.globals.Variable;

@SuppressWarnings("serial")
public class JMenuHover extends JMenu implements MouseListener {
	
	private final Color HOVER_COLOR  = Color.blue;
	private final Color NORMAL_COLOR = Color.black;
	
	private String languageKey;
	
	
	public JMenuHover(String languageKey) {
		super();
		super.setActionCommand(languageKey);
		this.languageKey = languageKey;
		this.setText(Messages.getString(languageKey));
		if(!Variable.nimbusLookAndFeel) {
			this.addMouseListener(this);
		}
	}
	
	public String getLanguageKey() {
		return languageKey;
	}


	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource() == this) {
			setForeground(HOVER_COLOR);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource() == this) {
			resetView();
		}
	}
	
	public void resetView() {
		setForeground(NORMAL_COLOR);
	}

}
