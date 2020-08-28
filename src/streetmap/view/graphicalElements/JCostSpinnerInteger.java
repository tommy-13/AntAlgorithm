package streetmap.view.graphicalElements;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import streetmap.view.general.ChoicePanel;

/**
 * A JSpinner with lower and upper bounds.
 * @author tommy
 *
 */
@SuppressWarnings("serial")
public class JCostSpinnerInteger extends JSpinner implements ChangeListener {

	private int 		lowerBound;
	private int 		upperBound;
	private ChoicePanel choicePanel;
	
	public JCostSpinnerInteger(ChoicePanel choicePanel, int lowerBound, int upperBound) {
		super();
		this.choicePanel = choicePanel;
		this.lowerBound  = lowerBound;
		this.upperBound  = upperBound;
		this.setValue(lowerBound);
		this.addChangeListener(this);
	}

	public int getInt() {
		return (int) getValue();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == this) {
			/* volume > upperBound or volume < lowerBound is not allowed */
			if((int) getValue() > upperBound) {
				setValue(upperBound);
			}
			else if((int) getValue() < lowerBound) {
				setValue(lowerBound);
			}
			choicePanel.fireChosenCostChange(getInt());
		}
	}
	
	
	
}
