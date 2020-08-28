package streetmap.globals;

import java.awt.Dimension;
import java.awt.Image;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import streetmap.model.StreetType;

public class Function {
	
	public static List<Image> getIconImages() {
		List<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(Constant.ICON_PATH_64).getImage());
		icons.add(new ImageIcon(Constant.ICON_PATH_32).getImage());
		icons.add(new ImageIcon(Constant.ICON_PATH_16).getImage());
		return icons;
	}
	
	private static Map<StreetType, List<ImageIcon>> streetImages32;
	private static Map<StreetType, List<ImageIcon>> streetImages16;
	public static void loadStreetImages() {
		StreetType[] types = StreetType.values();
		int len = types.length;
		streetImages32 = new HashMap<StreetType, List<ImageIcon>>();
		streetImages16 = new HashMap<StreetType, List<ImageIcon>>();
		for(int i=0; i<len; i++) {
			List<ImageIcon> lImg32 = new ArrayList<ImageIcon>();
			List<ImageIcon> lImg16 = new ArrayList<ImageIcon>();
			for(int j=0; j<6; j++) {
				lImg32.add(new ImageIcon("res/img/street32/" + types[i].name() + j + ".png"));
				lImg16.add(new ImageIcon("res/img/street16/" + types[i].name() + j + ".png"));
			}
			streetImages32.put(types[i], lImg32);
			streetImages16.put(types[i], lImg16);
		}
	}
	public static ImageIcon getImage(StreetType streetType, int imageType, boolean big) {
		if(big) {
			return streetImages32.get(streetType).get(imageType);
		}
		else {
			return streetImages16.get(streetType).get(imageType);
		}
	}

	public static ImageIcon getButtonImage(int type, int i) {
		return new ImageIcon("res/img/arrow32/ARROWBUTTON_" + type + "_" + i + ".png");
	}
	
	
	public static Dimension getCostDimension() {
		if(Variable.nimbusLookAndFeel) {
			return new Dimension(70, 26);
		}
		else {
			return new Dimension(55, 20);
		}
	}
	public static Dimension getPositionDimension() {
		if(Variable.nimbusLookAndFeel) {
			return new Dimension(70, 26);
		}
		else {
			return new Dimension(55, 20);
		}
	}
	

	public static String[] stringListToArray(List<String> list) {
		String[] array = new String[list.size()];
		for(int i=0; i<list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	public static List<String> StringArrayToList(String[] array) {
		List<String> list = new ArrayList<String>();
		for(String s : array) {
			list.add(s);
		}
		return list;
	}
	
	
	
	
	private static final DecimalFormat fDoublePercent = new DecimalFormat("0.00");
	public static String formatDoublePercent(double percent) {
		return fDoublePercent.format(percent);
	}
	
	private static final DecimalFormat fAmount = new DecimalFormat("#,##0.00");
	public static String formatDoubleAmount(double amount) {
		return fAmount.format(amount);
	}
	
	
	public static double round(double value, int positions) {
		int roundingTemp = 1;
		for(int i=0; i<positions; i++) {
			roundingTemp *= 10;
		}
		return Math.round(roundingTemp * value) / roundingTemp;
	}
	
	public static double round(double value) {
		return Math.round(100 * value) / 100;
	}

}
