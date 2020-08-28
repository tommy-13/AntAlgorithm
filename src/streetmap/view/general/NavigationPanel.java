package streetmap.view.general;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import language.Messages;
import streetmap.globals.Function;
import streetmap.observer.MapMovementObservable;
import streetmap.observer.MapMovementObserver;
import streetmap.view.graphicalElements.JArrowButton;
import streetmap.view.graphicalElements.JSpinnerInteger;


@SuppressWarnings("serial")
public class NavigationPanel extends JPanel implements ActionListener, MapMovementObservable {
	
	
	private JSpinnerInteger siPositions;
	private JArrowButton 	abUp;
	private JArrowButton 	abDown;
	private JArrowButton 	abLeft;
	private JArrowButton 	abRight;
	private JArrowButton	abZero;
	private JArrowButton	abZoom;
	
	
	public NavigationPanel() {
		
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		
		JLabel lMove = new JLabel(Messages.getString("ChoicePanel.LabelMove") + ":");
		GridBagConstraints gbc_lMove = new GridBagConstraints();
		gbc_lMove.gridx = 0;
		gbc_lMove.gridy = 0;
		gbc_lMove.anchor = GridBagConstraints.WEST;
		gbc_lMove.insets = new Insets(5, 5, 0, 5);
		add(lMove, gbc_lMove);
		
		siPositions = new JSpinnerInteger(1, 1000);
		siPositions.setPreferredSize(Function.getPositionDimension());
		GridBagConstraints gbc_siPositions = new GridBagConstraints();
		gbc_siPositions.gridx = 1;
		gbc_siPositions.gridy = 0;
		gbc_siPositions.anchor = GridBagConstraints.EAST;
		gbc_siPositions.insets = new Insets(5, 5, 0, 5);
		add(siPositions, gbc_siPositions);
		
		Container cArrows = new Container();
		cArrows.setLayout(new GridBagLayout());
		GridBagConstraints gbc_cArrows = new GridBagConstraints();
		gbc_cArrows.gridx = 0;
		gbc_cArrows.gridy = 1;
		gbc_cArrows.gridwidth = 2;
		gbc_cArrows.insets = new Insets(10, 5, 5, 5);
		add(cArrows, gbc_cArrows);
		
		abUp = new JArrowButton(JArrowButton.UP);
		abUp.addActionListener(this);
		cArrows.add(abUp, getArrowConstraints(1, 0));
		
		abDown = new JArrowButton(JArrowButton.DOWN);
		abDown.addActionListener(this);
		cArrows.add(abDown, getArrowConstraints(1, 1));
		
		abLeft = new JArrowButton(JArrowButton.LEFT);
		abLeft.addActionListener(this);
		cArrows.add(abLeft, getArrowConstraints(0, 1));
		
		abRight = new JArrowButton(JArrowButton.RIGHT);
		abRight.addActionListener(this);
		cArrows.add(abRight, getArrowConstraints(2, 1));
		
		abZero = new JArrowButton(JArrowButton.ZERO);
		abZero.setToolTipText(Messages.getString("ChoicePanel.Center"));
		abZero.addActionListener(this);
		cArrows.add(abZero, getArrowConstraints(0, 0));
		
		abZoom = new JArrowButton(JArrowButton.ZOOM);
		abZoom.setToolTipText(Messages.getString("ChoicePanel.Zoom"));
		abZoom.addActionListener(this);
		cArrows.add(abZoom, getArrowConstraints(2, 0));
	}
	
	private GridBagConstraints getArrowConstraints(int x, int y) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(2, 2, 2, 2);
		return gbc;
	}
	


	@Override
	public void actionPerformed(ActionEvent ae) {
		Object component = ae.getSource();

		int value = siPositions.getInt();
		if(component == abLeft) {
			notifyMoveRequest(-value, 0);
			return;
		}
		if(component == abRight) {
			notifyMoveRequest(value, 0);
			return;
		}
		if(component == abUp) {
			notifyMoveRequest(0, -value);
			return;
		}
		if(component == abDown) {
			notifyMoveRequest(0, value);
			return;
		}
		if(component == abZero) {
			notifyJumpRequest(0, 0);
			return;
		}
		if(component == abZoom) {
			notifyZoomRequest();
			return;
		}
	}

	
	
	private List<MapMovementObserver> observers = new LinkedList<MapMovementObserver>();
	@Override
	public void registerMapMovementObserver(MapMovementObserver observer) {
		observers.add(observer);
	}
	@Override
	public void removeMapMovementObserver(MapMovementObserver observer) {
		observers.remove(observer);
	}
	@Override
	public void notifyMoveRequest(int dirX, int dirY) {
		for(MapMovementObserver o : observers) {
			o.fireMoveRequest(dirX, dirY);
		}
	}
	@Override
	public void notifyJumpRequest(int x, int y) {
		for(MapMovementObserver o : observers) {
			o.fireJumpRequest(x, y);
		}
	}
	@Override
	public void notifyZoomRequest() {
		for(MapMovementObserver o : observers) {
			o.fireZoomRequest();
		}
	}
}
