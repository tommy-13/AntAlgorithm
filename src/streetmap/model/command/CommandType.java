package streetmap.model.command;

import language.Messages;


public enum CommandType {

	NEW_STREET,
	REMOVE_STREET,
	EDIT_STREET,
	NO;
	
	public String toString() {
		return Messages.getString("CommandType." + name());
	}
	
}
