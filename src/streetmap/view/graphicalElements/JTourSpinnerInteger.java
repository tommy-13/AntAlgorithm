package streetmap.view.graphicalElements;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import streetmap.view.general.TourPanel;

/**
 * A JSpinner with lower and upper bounds.
 * @author tommy
 *
 */
@SuppressWarnings("serial")
public class JTourSpinnerInteger extends JSpinner implements ChangeListener {

	private int 		lowerBound;
	private int 		upperBound;
	private TourPanel	tourPanel;
	
	public JTourSpinnerInteger(int lowerBound, int upperBound, TourPanel tourPanel) {
		super();
		this.lowerBound  = lowerBound;
		this.upperBound  = upperBound;
		this.tourPanel = tourPanel;
		this.setValue(lowerBound);
		this.addChangeListener(this);
	}

	public int getInt() {
		return (int) getValue();
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
		setValue(lowerBound);
	}
	
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == this) {
			/* volume > upperBound or volume < lowerBound is not allowed */
			int val = getInt();
			if(val > upperBound) {
				setValue(upperBound);
			}
			else if(val < lowerBound) {
				setValue(lowerBound);
			}
			tourPanel.fireSpinnerChange(getInt());
		}
	}
	
}
