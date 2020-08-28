package io.safeLoad;

public enum EnumFileType {

	DATA_BASE, CSV;
	
	
	public static String getFileExtension(EnumFileType type) {
		switch(type) {
		case DATA_BASE: 			return "smf"; // street map file
		//case PDF: 					return "pdf"; // portable document file
		case CSV: default: 			return "csv"; // comma separated values
		}
	}
}
