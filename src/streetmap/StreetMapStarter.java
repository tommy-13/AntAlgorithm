package streetmap;


import java.awt.Color;
import java.util.Locale;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import language.Messages;
import streetmap.globals.Function;
import streetmap.globals.Variable;
import streetmap.view.general.MainView;


public class StreetMapStarter {

	public static void main(String[] args) {

		// language settings
		Messages.setLanguage(Variable.language);
		switch(Variable.language) {
		case GERMAN:	Locale.setDefault(Locale.GERMAN); break;
		case ENGLISH:
		default:		Locale.setDefault(Locale.ENGLISH);
		}
		
		// load images
		Function.loadStreetImages();
		
		// set look and and feel
		try {
			UIManager.setLookAndFeel(NimbusLookAndFeel.class.getCanonicalName());
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {

			Variable.nimbusLookAndFeel = false;

			/* set layout */
			UIManager.put("OptionPane.buttonPadding", 30);
			UIManager.put("OptionPane.sameSizeButtons", true);
			UIManager.put("Menu.selectionBackground", Color.black);
			UIManager.put("Menu.selectionForeground", Color.white);
			UIManager.put("MenuItem.selectionBackground", Color.black);
			UIManager.put("MenuItem.selectionForeground", Color.white);
			UIManager.put("MenuItem.acceleratorSelectionForeground", Color.white);
			UIManager.put("MenuItem.acceleratorForeground", Color.white);

			UIDefaults ui = UIManager.getLookAndFeelDefaults();
			ui.put("Menu.selectionBackground", Color.black);
			ui.put("PopupMenu.background", Color.LIGHT_GRAY);
			ui.put("Menu.background", Color.LIGHT_GRAY);
			ui.put("Menu.opaque", true);
			ui.put("MenuItem.background", Color.LIGHT_GRAY);
			ui.put("MenuItem.opaque", true);
			ui.put("PopupMenu.contentMargins", null);
		}
		
		new MainView();
	}

}
