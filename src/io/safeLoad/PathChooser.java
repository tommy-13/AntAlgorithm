package io.safeLoad;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import language.Messages;

public class PathChooser {

	/* current directory */
	public static String DIRECTORY = null;
	
	
	//public static final String PDF_FILE_STRING = EnumFileType.getFileExtension(EnumFileType.PDF);
	public static final String CSV_FILE_STRING = EnumFileType.getFileExtension(EnumFileType.CSV);
	public static final String DATA_FILE_STRING = EnumFileType.getFileExtension(EnumFileType.DATA_BASE);
	
	//public static final String[] EXPORT_TYPES = {PDF_FILE_STRING, TXT_FILE_STRING};
	
	private static final FileFilter CSV_FILTER = new FileNameExtensionFilter(CSV_FILE_STRING, CSV_FILE_STRING);
	//private static final FileFilter PDF_FILTER = new FileNameExtensionFilter(PDF_FILE_STRING, PDF_FILE_STRING);
	private static final FileFilter DATA_FILTER = new FileNameExtensionFilter(DATA_FILE_STRING, DATA_FILE_STRING);
	
	
	public static String displayExportFileChooser(Component parent, EnumFileType fileType) {
		
		/* choose file destination */
		@SuppressWarnings("serial")
		JFileChooser fileChooser = new JFileChooser(DIRECTORY){
			@Override
			public void approveSelection(){
				File f = getSelectedFile();
				String fName = f.getAbsolutePath();
				String extension = getFileFilter().getDescription();
				
				if(!fName.toLowerCase().endsWith("." + extension)) {
					fName += "." + extension;
				}
				
				
				if(new File(fName).exists()) {
					int result = JOptionPane.showConfirmDialog(
							this,
							Messages.getString("PathChooser.ConfirmFileOverwriteText"),
							Messages.getString("PathChooser.ConfirmFileOverwriteTitle"),
							JOptionPane.OK_CANCEL_OPTION);
					switch(result){
					case JOptionPane.OK_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
			super.approveSelection();
			}
		};
		
		
		String chooserTitle = "";
		switch(fileType) {
		case DATA_BASE: default:
			fileChooser.setFileFilter(DATA_FILTER);
			chooserTitle = Messages.getString("PathChooser.TitleSaveDataBase");
			break;
		case CSV:
			fileChooser.setFileFilter(CSV_FILTER);
			//fileChooser.addChoosableFileFilter(PDF_FILTER);
			chooserTitle = Messages.getString("PathChooser.TitleExport");
			break;
		}
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		
		int returnValue = fileChooser.showDialog(parent, chooserTitle);
		DIRECTORY = fileChooser.getCurrentDirectory().getAbsolutePath();
		
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String fileName = selectedFile.getAbsolutePath();
			String extension = fileChooser.getFileFilter().getDescription();
			
			if(!fileName.toLowerCase().endsWith("." + extension)) {
				fileName += "." + extension;
			}
			
			return fileName;
		}
		return null;
	}
	
	
	
	public static File displayImportFileChooser(Component parent, EnumFileType fileType) {
		
		FileFilter fileFilter = null;
		String chooserTitle = "";
		switch(fileType) {
		case DATA_BASE: default:
			fileFilter = DATA_FILTER;
			chooserTitle = Messages.getString("PathChooser.TitleLoadDataBase");
			break;
		}
		
		
		/* choose file */
		JFileChooser fileChooser = new JFileChooser(DIRECTORY);
		fileChooser.setFileFilter(fileFilter);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		fileChooser.setAcceptAllFileFilterUsed(false);

		int returnValue = fileChooser.showDialog(parent, chooserTitle);
		DIRECTORY = fileChooser.getCurrentDirectory().getAbsolutePath();
		
		
		if(returnValue == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

}
