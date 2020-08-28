package streetmap.view.general;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import streetmap.globals.Constant;
import streetmap.model.DataBase;
import streetmap.model.Street;
import streetmap.model.StreetType;
import streetmap.model.TourSolution;
import streetmap.model.command.AddStreetCommand;
import streetmap.model.command.ChangeStreetCommand;
import streetmap.model.command.RemoveStreetCommand;
import streetmap.observer.DataObserver;
import streetmap.observer.MapMovementObserver;
import streetmap.observer.StreetTileSelectionObserver;
import streetmap.observer.TourStateObserver;
import streetmap.task.CommandTask;
import streetmap.task.Task;
import streetmap.task.WaitingDialog;
import streetmap.task.ZoomTask;
import streetmap.view.graphicalElements.JStreetField;

@SuppressWarnings("serial")
public class StreetPanel
			extends JPanel
			implements ActionListener, DataObserver, MouseListener, StreetTileSelectionObserver,
						MapMovementObserver, TourStateObserver, MouseWheelListener {
	

	private boolean				bigFields	= true;
	private final Point			BIG_DIM		= new Point(34, 19);
	private final Point			SMALL_DIM	= new Point(68, 38);
	private Point				dimension	= BIG_DIM; 
	
	private MainView 			mainView;
	private DataBase 			dataBase;
	private Point				topLeftCorner		= Constant.INIT_TOPLEFT_CORNER;
	private StreetType			selectedStreetType	= Constant.INIT_STREET_TYPE;
	private int					selectedCost		= Constant.INIT_COST;
	
	private JStreetField[][] 	fields;
	private GridBagConstraints[][] gbcs;
	private boolean				tourModus = false;
	private List<Point>			highlightedFields;
	
	
	
	public StreetPanel(MainView mainView) {
		this.mainView = mainView;
		this.dataBase = DataBase.getInstance();
		dataBase.registerDataObserver(this);
		
		
		setLayout(new GridBagLayout());
		fields  = new JStreetField[SMALL_DIM.x][SMALL_DIM.y];
		gbcs	= new GridBagConstraints[SMALL_DIM.x][SMALL_DIM.y]; 
		for(int i=0; i<SMALL_DIM.x; i++) {
			for(int j=0; j<SMALL_DIM.y; j++) {
				fields[i][j] = new JStreetField(i, j);
				fields[i][j].addActionListener(this);
				fields[i][j].addMouseListener(this);
				fields[i][j].addMouseWheelListener(this);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = i;
				gbc.gridy = j;
				gbcs[i][j] = gbc;
				if(i<dimension.x && j<dimension.y) {
					add(fields[i][j], gbc);
				}
			}
		}
		
		highlightedFields = new LinkedList<Point>();
	}
	
	/**
	 * Update the street fields. If the fields are little, more fields will be shown.
	 */
	private void updateFieldComponents() {
		Task task = new ZoomTask(this, dimension, fields, gbcs, bigFields);
		new WaitingDialog(task);
	}



	@Override
	public void actionPerformed(ActionEvent ae) {
		Object component = ae.getSource();
		for(int i=0; i<dimension.x; i++) {
			for(int j=0; j<dimension.y; j++) {
				if(component == fields[i][j]) {
					int x = i + topLeftCorner.x;
					int y = j + topLeftCorner.y;
					
					if(selectedStreetType == fields[i][j].getStreetType()) {
						// street type of selected and old type are the same
						if(selectedStreetType != StreetType.GRASS && selectedCost != fields[i][j].getCost()) {
							changeStreet(x, y);
						}
					}
					else { // street type of selected and old type are different
						if(selectedStreetType == StreetType.GRASS) {
							deleteStreet(x, y);
						}
						else if(fields[i][j].getStreetType() == StreetType.GRASS) {
							addNewStreet(x, y);
						}
						else {
							changeStreet(x, y);
						}
					}
					return;
				}
			}
		}
	}

	/**
	 * @param x
	 * @param y
	 */
	private void addNewStreet(int x, int y) {
		Street street = new Street(selectedStreetType, x, y, selectedCost, false);
		Task task = new CommandTask(mainView, new AddStreetCommand(street));
		task.execute();
	}

	/**
	 * @param x
	 * @param y
	 */
	private void deleteStreet(int x, int y) {
		Task task = new CommandTask(mainView, new RemoveStreetCommand(x, y));
		task.execute();
	}

	/**
	 * @param x
	 * @param y
	 * @param switchMarked
	 */
	private void changeStreet(int x, int y) {
		Street street = dataBase.getStreet(x, y);
		street = new Street(selectedStreetType, x, y, selectedCost, street.isMarked());
		Task task = new CommandTask(mainView, new ChangeStreetCommand(street));
		task.execute();
	}



	@Override
	public void fireDataReseted() {
		updateView();
	}
	@Override
	public void fireDataChanged() {
		updateView();
	}
	
	
	private void updateView() {
		for(int i=0; i<SMALL_DIM.x; i++) {
			for(int j=0; j<SMALL_DIM.y; j++) {
				if(dataBase.hasStreet(topLeftCorner.x + i, topLeftCorner.y + j)) {
					Street street = dataBase.getStreet(topLeftCorner.x + i, topLeftCorner.y + j);
					fields[i][j].setStreetType(street.getStreetType(), street.getCost(), street.isMarked(), topLeftCorner);
					fields[i][j].setMarked(street.isMarked());
				}
				else {
					fields[i][j].setStreetType(StreetType.GRASS, 0, false, topLeftCorner);
				}
			}
		}
	}



	@Override
	public void mouseClicked(MouseEvent me) {
		if(tourModus || !SwingUtilities.isRightMouseButton(me)) {
			return;
		}
		Object component = me.getSource();
		for(int i=0; i<dimension.x; i++) {
			for(int j=0; j<dimension.y; j++) {
				if(component == fields[i][j] && fields[i][j].getStreetType() != StreetType.GRASS) {
					int x = topLeftCorner.x + i;
					int y = topLeftCorner.y + j;					
					if(me.isControlDown()) {
						deleteStreet(x, y);
					}
					else {
						Street street = dataBase.getStreet(x, y);
						street = new Street(street.getStreetType(), x, y, selectedCost, !street.isMarked());
						Task task = new CommandTask(mainView, new ChangeStreetCommand(street));
						task.execute();
					}
					return;
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent me) {
	}
	@Override
	public void mouseExited(MouseEvent me) {
	}
	@Override
	public void mousePressed(MouseEvent me) {
	}
	@Override
	public void mouseReleased(MouseEvent me) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {
		if(tourModus) {
			return;
		}
		Object component = me.getSource();
		for(int i=0; i<dimension.x; i++) {
			for(int j=0; j<dimension.y; j++) {
				if(component == fields[i][j] && fields[i][j].getStreetType() != StreetType.GRASS) {
					int x = topLeftCorner.x + i;
					int y = topLeftCorner.y + j;
					Street street = dataBase.getStreet(x, y);
					
					int cost = street.getCost();
					cost -= me.getWheelRotation();
					cost = Math.min(Math.max(1, cost), Constant.MAX_COST);
					if(cost == street.getCost()) {
						return;
					}
							
					street = new Street(street.getStreetType(), x, y, cost, street.isMarked());
					Task task = new CommandTask(mainView, new ChangeStreetCommand(street));
					task.execute();
					return;
				}
			}
		}
	}
	


	public void enableTourModus(boolean tourModus) {
		this.tourModus = tourModus;
		if(!tourModus) {
			highlightPath(false);
		}
		for(int i=0; i<SMALL_DIM.x; i++) {
			for(int j=0; j<SMALL_DIM.y; j++) {
				fields[i][j].setEnabled(!tourModus);
			}
		}
	}
	
	
	private void highlightPath(boolean highlighted) {
		for(Point p : highlightedFields) {
			int x = p.x - topLeftCorner.x;
			int y = p.y - topLeftCorner.y;
			if(x >= 0 && x < SMALL_DIM.x && y >= 0 && y < SMALL_DIM.y) {
				fields[x][y].setHighlighted(highlighted);
			}
		}
	}



	@Override
	public void fireSelectionChange(StreetType streetType, int cost) {
		selectedStreetType	= streetType;
		selectedCost		= cost;
	}

	@Override
	public void fireMoveRequest(int dirX, int dirY) {
		if(tourModus) {
			highlightPath(false);
		}
		
		topLeftCorner.x += dirX;
		topLeftCorner.y += dirY;
		updateView();
		
		if(tourModus) {
			highlightPath(true);
		}
	}
	@Override
	public void fireJumpRequest(int x, int y) {
		if(tourModus) {
			highlightPath(false);
		}
		
		topLeftCorner.x = x;
		topLeftCorner.y = y;
		updateView();
		
		if(tourModus) {
			highlightPath(true);
		}
	}
	@Override
	public void fireZoomRequest() {
		bigFields = !bigFields;
		dimension = bigFields ? BIG_DIM : SMALL_DIM;
		updateFieldComponents();
	}

	@Override
	public void firePartialPathChanged(int id) {
		// un-highlight old path
		highlightPath(false);
		// handle new path
		highlightedFields = TourSolution.getInstance().getPath(id);
		// highlight new path
		highlightPath(true);
	}
	@Override
	public void fireTourClosed() {
		enableTourModus(false);
	}

}
