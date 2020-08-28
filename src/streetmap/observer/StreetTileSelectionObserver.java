package streetmap.observer;

import streetmap.model.StreetType;

public interface StreetTileSelectionObserver {

	public void fireSelectionChange(StreetType streetType, int cost);
	
}
