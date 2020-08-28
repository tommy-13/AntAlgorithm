package streetmap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import streetmap.observer.DataObservable;
import streetmap.observer.DataObserver;
import streetmap.observer.SaveStateObservable;
import streetmap.observer.SaveStateObserver;
import streetmap.view.general.CurrentChoice;

public class DataBase implements SaveStateObservable, DataObservable {
	
	private static DataBase uniqueDataBase = new DataBase();
	public static DataBase getInstance() {
		return uniqueDataBase;
	}
	
	private List<DataObserver>		dataObserver		= new ArrayList<DataObserver>();
	private List<SaveStateObserver> savePathObserver	= new ArrayList<SaveStateObserver>();

	private boolean savingNecessary = false;
	private String	savePath		= null;
	
	private Map<Integer, Map<Integer, Street>>	streets;
	
	
	private DataBase() {
		streets = new HashMap<Integer, Map<Integer,Street>>();
	}
	
	public void reset() {
		streets.clear();
		savingNecessary = false;
		setSavePath(null);
		CurrentChoice.getInstance().setTopLeftCorner(0, 0);
		notifyObserversDataReseted();
		notifyObserversSaveStateChange();
	}
	
	public void setSavePath(String savePath) {
		this.savePath = savePath;
		notifyObserversSaveStateChange();
	}
	public String getSafePath() {
		return savePath;
	}
	public boolean isSavingNecessary() {
		return savingNecessary;
	}
	public void setSavingNecessary(boolean b) {
		savingNecessary = b;
		notifyObserversSaveStateChange();
	}
	
	@Override
	public void registerSaveStateObserver(SaveStateObserver o) {
		savePathObserver.add(o);
	}
	@Override
	public void removeSaveStateObserver(SaveStateObserver o) {
		savePathObserver.remove(o);
	}
	@Override
	public void notifyObserversSaveStateChange() {
		for(SaveStateObserver o : savePathObserver) {
			o.fireChangedSaveState(savePath, savingNecessary);
		}
	}
	
	
	
	public boolean hasStreet(int x, int y) {
		return streets.containsKey(x) && streets.get(x).containsKey(y);
	}
	public Street getStreet(int x, int y) {
		Map<Integer, Street> yMap = streets.get(x);
		if(yMap != null && yMap.containsKey(y)) {
			return yMap.get(y).copy();
		}
		else {
			return null;
		}
	}
	public List<Street> getAllStreets() {
		List<Street> sl = new LinkedList<Street>();
		for(Map<Integer, Street> yMap : streets.values()) {
			for(Street s : yMap.values()) {
				sl.add(s);
			}
		}
		return sl;
	}
	public Map<Integer, Map<Integer, Street>> getStreetMap() {
		return streets;
	}
	
	
	public void addStreet(Street street) {
		addStreetIntern(street.getXPos(), street.getYPos(), street);
		fireDataChanged();
	}
	private void addStreetIntern(int x, int y, Street street) {
		if(!streets.containsKey(x)) {
			streets.put(x, new HashMap<Integer, Street>());
		}
		Map<Integer, Street> xMap = streets.get(x);
		xMap.put(y, street);
	}
	
	public void removeStreet(Street street) {
		removeStreet(street.getXPos(), street.getYPos());
	}
	public void removeStreet(int x, int y) {
		if(removeStreetIntern(x, y)) {
			fireDataChanged();
		}
	}
	private boolean removeStreetIntern(int x, int y) {
		if(streets.containsKey(x) && streets.get(x).containsKey(y)) {
			Map<Integer, Street> yMap = streets.get(x);
			yMap.remove(y);
			if(yMap.isEmpty()) {
				streets.remove(yMap);
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public void changeStreet(int x, int y, Street street) {
		addStreetIntern(x, y, street);
		fireDataChanged();
	}

	

	
	public void load(List<Street> sl) {
		streets.clear();
		
		for(Street s : sl) {
			addStreetIntern(s.getXPos(), s.getYPos(), s);
		}
		
		savingNecessary = false;
		CurrentChoice.getInstance().setTopLeftCorner(0, 0);
		
		notifyObserversDataReseted();
		notifyObserversSaveStateChange();
	}
	
	public void fireDataChanged() {
		setSavingNecessary(true);
		notifyObserversDataChange();
	}
	
	
	
	@Override
	public void registerDataObserver(DataObserver o) {
		if(!dataObserver.contains(o)) {
			dataObserver.add(o);
		}
	}
	@Override
	public void removeDataObserver(DataObserver o) {
		if(dataObserver.contains(o)) {
			dataObserver.remove(o);
		}
	}
	@Override
	public void notifyObserversDataReseted() {
		for(DataObserver o : dataObserver) {
			o.fireDataReseted();
		}
	}
	@Override
	public void notifyObserversDataChange() {
		for(DataObserver o : dataObserver) {
			o.fireDataChanged();
		}
	}
	
}
