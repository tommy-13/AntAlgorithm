package streetmap.observer;

import streetmap.model.StreetType;

public interface StreetTileSelectionObservable {

	public void registerStreetTileSelectionObserver(StreetTileSelectionObserver observer);
	public void removeStreetTileSelectionObserver(StreetTileSelectionObserver observer);
	
	public void notifySelectionChange(StreetType streetType, int cost);
	
}
