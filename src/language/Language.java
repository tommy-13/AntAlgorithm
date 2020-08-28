package language;

import java.util.ArrayList;
import java.util.List;

public enum Language {
	
	GERMAN,
	ENGLISH;
	
	
	public String toString() {
		switch(this) {
		case GERMAN: return Messages.getString("Language.German");
		case ENGLISH:
		default: return Messages.getString("Language.English");
		}
	}
	
	public static Language parseLanguage(String str) {
		for(Language l : values()) {
			if(str.equals(l.toString())) {
				return l;
			}
		}
		return ENGLISH;
	}
	
	public static String[] getArray() {
		Language[] cs = Language.values();
		String[] strs = new String[cs.length];
		for(int i=0; i<cs.length; i++) {
			strs[i] = cs[i].toString();
		}
		return strs;
	}

	public static List<Language> getAll() {
		List<Language> cs = new ArrayList<Language>();
		for(Language c : values()) {
			cs.add(c);
		}
		return cs;
	}

}
