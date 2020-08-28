package language;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	
	private static String BUNDLE_NAME = "language.messages_de"; //$NON-NLS-1$
	private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
	
	public static void setLanguage(Language newLanguage) {
		switch(newLanguage) {
		case GERMAN:	BUNDLE_NAME = "language.messages_de"; break;
		case ENGLISH:
		default:		BUNDLE_NAME = "language.messages_en";
		}
		RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);
	}
	
}
