package streetmap.globals;

import java.awt.Color;
import java.awt.Point;

import streetmap.model.StreetType;


public class Constant {
	
	public static final String		FILE_SEPARATOR 		= System.getProperty("file.separator");
	public static final String		LINE_SEPARATOR 		= System.getProperty("line.separator");
	public static final String		COPYRIGHT			= "\u00a9";
	
	public static final String		ICON_PATH_64		= "res/icon/icon64.png";
	public static final String		ICON_PATH_32 		= "res/icon/icon32.png";
	public static final String		ICON_PATH_16 		= "res/icon/icon16.png";
	
	public static final int			MAX_COST			= 1000;
	public static final int 		MAX_THREADS 		= 100;
	
	// colors
	public static final Color 		COL_BACKGROUND		= Color.LIGHT_GRAY;
	
	// initial values
	public static final StreetType	INIT_STREET_TYPE	= StreetType.values()[0];
	public static final int			INIT_COST			= 1;
	public static final Point		INIT_TOPLEFT_CORNER = new Point(0, 0);
}
