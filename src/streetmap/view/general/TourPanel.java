package streetmap.view.general;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import language.Messages;
import streetmap.globals.Function;
import streetmap.model.TourSolution;
import streetmap.observer.MapMovementObservable;
import streetmap.observer.MapMovementObserver;
import streetmap.observer.TourStateObservable;
import streetmap.observer.TourStateObserver;
import streetmap.view.graphicalElements.JTourSpinnerInteger;


@SuppressWarnings("serial")
public class TourPanel extends JPanel implements ActionListener, TourStateObservable, MouseListener,
													MapMovementObservable {
	
	private TourSolution		tourSolution  = TourSolution.getInstance();
	private JLabel				allCost;
	private JLabel				partCost;
	private JLabel				start;
	private JLabel				target;
	private JTourSpinnerInteger	siPart;
	private JButton				bShowPart;
	private JButton				bCloseTour;
	
	
	public TourPanel() {
	
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createLoweredSoftBevelBorder());
		
		Container west = new Container();
		west.setLayout(new GridBagLayout());
		add(west, BorderLayout.WEST);
		
		
		JLabel lAllCost = new JLabel(Messages.getString("TourFrame.AllCost") + ":");
		GridBagConstraints gbc_lAllCost = new GridBagConstraints();
		gbc_lAllCost.gridx = 0;
		gbc_lAllCost.gridy = 0;
		gbc_lAllCost.anchor = GridBagConstraints.WEST;
		gbc_lAllCost.insets = new Insets(5, 5, 0, 10);
		west.add(lAllCost, gbc_lAllCost);
		
		allCost = new JLabel("---");
		GridBagConstraints gbc_allCost = new GridBagConstraints();
		gbc_allCost.gridx = 1;
		gbc_allCost.gridy = 0;
		gbc_allCost.anchor = GridBagConstraints.EAST;
		gbc_allCost.insets = new Insets(5, 0, 0, 5);
		west.add(allCost, gbc_allCost);

		JLabel lPartCost = new JLabel(Messages.getString("TourFrame.PartCost") + ":");
		GridBagConstraints gbc_lPartCost = new GridBagConstraints();
		gbc_lPartCost.gridx = 0;
		gbc_lPartCost.gridy = 1;
		gbc_lPartCost.anchor = GridBagConstraints.WEST;
		gbc_lPartCost.insets = new Insets(0, 5, 0, 10);
		west.add(lPartCost, gbc_lPartCost);
		
		partCost = new JLabel("---");
		GridBagConstraints gbc_partCost = new GridBagConstraints();
		gbc_partCost.gridx = 1;
		gbc_partCost.gridy = 1;
		gbc_partCost.anchor = GridBagConstraints.EAST;
		gbc_partCost.insets = new Insets(0, 0, 0, 5);
		west.add(partCost, gbc_partCost);

		JLabel lStart = new JLabel(Messages.getString("TourFrame.Source") + ":");
		GridBagConstraints gbc_lStart = new GridBagConstraints();
		gbc_lStart.gridx = 0;
		gbc_lStart.gridy = 2;
		gbc_lStart.anchor = GridBagConstraints.WEST;
		gbc_lStart.insets = new Insets(0, 5, 0, 10);
		west.add(lStart, gbc_lStart);
		
		start = new JLabel("---");
		GridBagConstraints gbc_start = new GridBagConstraints();
		gbc_start.gridx = 1;
		gbc_start.gridy = 2;
		gbc_start.anchor = GridBagConstraints.EAST;
		gbc_start.insets = new Insets(0, 0, 0, 5);
		west.add(start, gbc_start);

		JLabel ltarget = new JLabel(Messages.getString("TourFrame.Target") + ":");
		GridBagConstraints gbc_ltarget = new GridBagConstraints();
		gbc_ltarget.gridx = 0;
		gbc_ltarget.gridy = 3;
		gbc_ltarget.anchor = GridBagConstraints.WEST;
		gbc_ltarget.insets = new Insets(0, 5, 5, 10);
		west.add(ltarget, gbc_ltarget);
		
		target = new JLabel("---");
		GridBagConstraints gbc_target = new GridBagConstraints();
		gbc_target.gridx = 1;
		gbc_target.gridy = 3;
		gbc_target.anchor = GridBagConstraints.EAST;
		gbc_target.insets = new Insets(0, 0, 5, 5);
		west.add(target, gbc_target);
		
		
		JLabel lChoice = new JLabel(Messages.getString("TourFrame.Choice") + ":");
		GridBagConstraints gbc_lChoice = new GridBagConstraints();
		gbc_lChoice.gridx = 0;
		gbc_lChoice.gridy = 4;
		gbc_lChoice.anchor = GridBagConstraints.WEST;
		gbc_lChoice.insets = new Insets(0, 5, 0, 10);
		west.add(lChoice, gbc_lChoice);
		
		siPart = new JTourSpinnerInteger(1, 1, this);
		siPart.setPreferredSize(Function.getPositionDimension());
		siPart.setEnabled(false);
		GridBagConstraints gbc_siPart = new GridBagConstraints();
		gbc_siPart.gridx = 1;
		gbc_siPart.gridy = 4;
		gbc_siPart.anchor = GridBagConstraints.EAST;
		west.add(siPart, gbc_siPart);
		
		Container cButtons = new Container();
		cButtons.setLayout(new GridBagLayout());
		GridBagConstraints gbc_cButtons = new GridBagConstraints();
		gbc_cButtons.gridx = 0;
		gbc_cButtons.gridy = 5;
		gbc_cButtons.gridwidth = 2;
		gbc_cButtons.anchor = GridBagConstraints.CENTER;
		gbc_cButtons.insets = new Insets(10, 10, 5, 10);
		west.add(cButtons, gbc_cButtons);
		
		bCloseTour = new JButton(Messages.getString("TourFrame.Close"));
		bCloseTour.setEnabled(false);
		bCloseTour.addActionListener(this);
		GridBagConstraints gbc_bClose = new GridBagConstraints();
		gbc_bClose.gridx = 1;
		gbc_bClose.gridy = 0;
		gbc_bClose.insets = new Insets(0, 10, 0, 10);
		cButtons.add(bCloseTour, gbc_bClose);
		
		bShowPart = new JButton(Messages.getString("TourFrame.ShowPart"));
		bShowPart.setEnabled(false);
		bShowPart.addActionListener(this);
		GridBagConstraints gbc_bShowPart = new GridBagConstraints();
		gbc_bShowPart.gridx = 0;
		gbc_bClose.gridy = 0;
		cButtons.add(bShowPart, gbc_bShowPart);
	}


	public void enableInput(boolean enabled) {
		if(enabled) {
			allCost.setText("" + tourSolution.getAllCost());
			partCost.setText("" + tourSolution.getCost(0));
			start.setText("" + tourSolution.getStart(0));
			target.setText("" + tourSolution.getTarget(0));
			siPart.setUpperBound(tourSolution.getSize());
			
			start.addMouseListener(this);
			target.addMouseListener(this);
			
			notifyPartialPathChanged(0);
		}
		else {
			siPart.setUpperBound(1);
			allCost.setText("---");
			partCost.setText("---");
			start.setText("---");
			target.setText("---");
			
			start.removeMouseListener(this);
			target.removeMouseListener(this);
		}
		
		siPart.setEnabled(enabled);
		bCloseTour.setEnabled(enabled);
		bShowPart.setEnabled(enabled);
	}

	
	public void fireSpinnerChange(int val) {
		partCost.setText("" + tourSolution.getCost(val-1));
		start.setText("" + tourSolution.getStart(val-1));
		target.setText("" + tourSolution.getTarget(val-1));
		notifyPartialPathChanged(val-1);
	}
	


	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object component = ae.getSource();
		if(component == bCloseTour) {
			notifyTourClosed();
			return;
		}
		if(component == bShowPart) {
			Point p = tourSolution.getTopLeft(siPart.getInt()-1);
			notifyJumpRequest(p.x-1, p.y-1);
			return;
		}
	}



	private List<TourStateObserver> observers = new LinkedList<TourStateObserver>();
	@Override
	public void registerTourStateObserver(TourStateObserver observer) {
		observers.add(observer);
	}
	@Override
	public void removeTourStateObserver(TourStateObserver observer) {
		observers.remove(observer);
	}
	@Override
	public void notifyPartialPathChanged(int id) {
		for(TourStateObserver o : observers) {
			o.firePartialPathChanged(id);
		}
	}
	@Override
	public void notifyTourClosed() {
		enableInput(false);
		for(TourStateObserver o : observers) {
			o.fireTourClosed();
		}
	}


	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(!SwingUtilities.isLeftMouseButton(e)) {
			return;
		}
		Object 	o	= e.getSource();
		int 	id	= siPart.getInt()-1;
		if(o == start) {
			Point p = tourSolution.getStartPoint(id);
			notifyJumpRequest(p.x, p.y);
			return;
		}
		if(o == target) {
			Point p = tourSolution.getTargetPoint(id);
			notifyJumpRequest(p.x, p.y);
			return;
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		Object o = e.getSource();
		if(o == start) {
			start.setForeground(Color.blue);
			return;
		}
		if(o == target) {
			target.setForeground(Color.blue);
			return;
		}
	}
	@Override
	public void mouseExited(MouseEvent e) {
		Object o = e.getSource();
		if(o == start) {
			start.setForeground(Color.black);
			return;
		}
		if(o == target) {
			target.setForeground(Color.black);
			return;
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}

	

	private List<MapMovementObserver> mapObservers = new LinkedList<MapMovementObserver>();
	@Override
	public void registerMapMovementObserver(MapMovementObserver observer) {
		mapObservers.add(observer);
	}
	@Override
	public void removeMapMovementObserver(MapMovementObserver observer) {
		mapObservers.remove(observer);
	}
	@Override
	public void notifyMoveRequest(int dirX, int dirY) {}
	@Override
	public void notifyJumpRequest(int x, int y) {
		for(MapMovementObserver o : mapObservers) {
			o.fireJumpRequest(x, y);
		}
	}
	@Override
	public void notifyZoomRequest() {}
}
