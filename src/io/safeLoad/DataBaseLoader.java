package io.safeLoad;

import io.parseTree.ParseTreeStructureException;
import io.parseTree.TreeElement;
import io.parseTreeWriterReader.XMLTreeReader;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import language.Messages;
import streetmap.model.DataBase;
import streetmap.parseTreeTranslator.ParseTreeXMLDataBaseTranslator;

public class DataBaseLoader {
	
	public static boolean load(Component parent) {
		File loadedFile = PathChooser.displayImportFileChooser(parent, EnumFileType.DATA_BASE);
		if(loadedFile == null) {
			return false;
		}
		String loadPath = loadedFile.getAbsolutePath();
		
		TreeElement treeRoot = null;
		
		if(loadPath.toLowerCase().endsWith(EnumFileType.getFileExtension(EnumFileType.DATA_BASE))) {
			XMLTreeReader treeReader = new XMLTreeReader(loadPath);
			try {
				treeRoot = treeReader.read();
			} catch (IOException e) {}
		}
		if(treeRoot == null) {
			JOptionPane.showMessageDialog(parent,
					Messages.getString("DataBaseLoader.ProblemWithLoadingText"),
					Messages.getString("DataBaseLoader.ProblemWithLoadingTitle"),
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		try {
			ParseTreeXMLDataBaseTranslator.createDataBase(treeRoot);
		} catch (ParseTreeStructureException e) {
			JOptionPane.showMessageDialog(parent,
					Messages.getString("DataBaseLoader.ProblemWithLoadingText"),
					Messages.getString("DataBaseLoader.ProblemWithLoadingTitle"),
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		DataBase.getInstance().setSavePath(loadPath);
		return true;
	}
	
}
