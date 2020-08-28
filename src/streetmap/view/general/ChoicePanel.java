package streetmap.view.general;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import language.Messages;
import streetmap.globals.Constant;
import streetmap.globals.Function;
import streetmap.model.StreetType;
import streetmap.observer.StreetTileSelectionObservable;
import streetmap.observer.StreetTileSelectionObserver;
import streetmap.observer.TourStateObserver;
import streetmap.view.graphicalElements.JCostSpinnerInteger;
import streetmap.view.graphicalElements.JStreetButton;


@SuppressWarnings("serial")
public class ChoicePanel extends JPanel implements ActionListener, StreetTileSelectionObservable, TourStateObserver {
	
	private Map<StreetType, JStreetButton>	streetButtons;
	private StreetType						currentType = Constant.INIT_STREET_TYPE;
	private JCostSpinnerInteger				siCost;
	
	
	public ChoicePanel() {
		
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(8, 0, 8, 0),
				BorderFactory.createLoweredSoftBevelBorder()));
		
		/* buttons */
		streetButtons = new HashMap<StreetType, JStreetButton>();
		StreetType[] types = StreetType.values();
		for(int i=0; i<types.length; i++) {
			JStreetButton sb = new JStreetButton(types[i]);
			streetButtons.put(types[i], sb);
			sb.addActionListener(this);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = i % 4;
			gbc.gridy = i / 4;
			gbc.insets = new Insets(4, 4, 4, 4);
			add(sb, gbc);
		}
		streetButtons.get(currentType).setChosen(true);
		
		Container cCost = new Container();
		cCost.setLayout(new GridBagLayout());
		GridBagConstraints gbc_cCost = new GridBagConstraints();
		gbc_cCost.gridx = 0;
		gbc_cCost.gridy = (types.length - 1) / 4 + 1;
		gbc_cCost.gridwidth = 5;
		gbc_cCost.anchor = GridBagConstraints.CENTER;
		gbc_cCost.insets = new Insets(10, 5, 5, 5);
		add(cCost, gbc_cCost);
		
		JLabel lCost = new JLabel(Messages.getString("Global.WayCost") + ":");
		GridBagConstraints gbc_lCost = new GridBagConstraints();
		gbc_lCost.gridx = 0;
		gbc_lCost.gridy = 0;
		gbc_lCost.anchor = GridBagConstraints.EAST;
		cCost.add(lCost, gbc_lCost);
		
		siCost = new JCostSpinnerInteger(this, 1, Constant.MAX_COST);
		siCost.setPreferredSize(Function.getCostDimension());
		siCost.setValue(Constant.INIT_COST);
		GridBagConstraints gbc_siCost = new GridBagConstraints();
		gbc_siCost.gridx = 1;
		gbc_siCost.gridy = 0;
		gbc_siCost.anchor = GridBagConstraints.EAST;
		gbc_siCost.insets = new Insets(0, 10, 0, 0);
		cCost.add(siCost, gbc_siCost);
	}
	

	public void setChosenStreetType(JStreetButton sb) {
		// unchoose old
		streetButtons.get(currentType).setChosen(false);
		// choose new
		sb.setChosen(true);
		currentType = sb.getStreetType();
		notifySelectionChange(currentType, siCost.getInt());
	}
	public void fireChosenCostChange(int cost) {
		notifySelectionChange(currentType, cost);
	}

	public void enableInput(boolean enabled) {
		for(JStreetButton sb : streetButtons.values()) {
			sb.setEnabled(enabled);
		}
		siCost.setEnabled(enabled);
	}
	
	
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		Object component = ae.getSource();
		for(JStreetButton sb : streetButtons.values()) {
			if(component == sb) {
				setChosenStreetType(sb);
				return;
			}
		}
	}
	
	
	private List<StreetTileSelectionObserver> observers = new LinkedList<StreetTileSelectionObserver>();
	@Override
	public void registerStreetTileSelectionObserver(StreetTileSelectionObserver observer) {
		observers.add(observer);
	}
	@Override
	public void removeStreetTileSelectionObserver(StreetTileSelectionObserver observer) {
		observers.remove(observer);
	}
	@Override
	public void notifySelectionChange(StreetType streetType, int cost) {
		for(StreetTileSelectionObserver o : observers) {
			o.fireSelectionChange(streetType, cost);
		}
	}

	@Override
	public void firePartialPathChanged(int id) {}
	@Override
	public void fireTourClosed() {
		enableInput(true);
	}
}
