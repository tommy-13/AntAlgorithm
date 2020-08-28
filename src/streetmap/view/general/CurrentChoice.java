package streetmap.view.general;

import java.awt.Point;

public class CurrentChoice {

	private static CurrentChoice instance = new CurrentChoice();
	private CurrentChoice() {
		topLeftCorner = new Point(0, 0);
		previousTopLeftCorner = new Point(0, 0);
	}
	
	public static CurrentChoice getInstance() {
		return instance;
	}
	
	private Point		topLeftCorner;
	private Point		previousTopLeftCorner;
	
	
	public void setTopLeftCorner(int x, int y) {
		previousTopLeftCorner.x = topLeftCorner.x;
		previousTopLeftCorner.y = topLeftCorner.y;
		topLeftCorner.x = x;
		topLeftCorner.y = y;
	}
	
	public Point getTopLeftCorner() {
		return topLeftCorner;
	}
	public Point getPreviousTopLeftCorner() {
		return previousTopLeftCorner;
	}
	
}
