package streetmap.view.graphicalElements;

import javax.swing.JMenuItem;

import language.Messages;

@SuppressWarnings("serial")
public class JMenuItemLingual extends JMenuItem {
	
	private String languageKey;
	

	public JMenuItemLingual(String languageKey) {
		super();
		super.setActionCommand(languageKey);
		this.languageKey = languageKey;
		this.setText(Messages.getString(languageKey));
	}
	
	
	public String getLanguageKey() {
		return languageKey;
	}
}
