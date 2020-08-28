package streetmap.solver;


import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import language.Messages;
import streetmap.globals.Function;
import streetmap.view.general.MainView;


@SuppressWarnings("serial")
public class ComputTourDialog extends JDialog {
	
	private MainView mainView;
	
	
	public ComputTourDialog(final MainView mainView, final ComputeTourTask task) {
		
		this.mainView = mainView;
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		/* set icon */
		setIconImages(Function.getIconImages());

		setTitle(Messages.getString("ComputeTourDialog.Title"));
		
		
		/* label */
		final JLabel label = new JLabel(Messages.getString("ComputeTour.BuildGraph"));
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(label);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(mainView);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// step 1
				if(!task.buildGraph()) {
//					System.out.println("fehler 1");
					tellOneNode();
					return;
				}
				if(!task.isGraphComplete()) {
//					System.out.println("fehler 2");
					tellError();
					return;
				}
				
				// step2
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						label.setText(Messages.getString("ComputeTour.ComputeDistances"));
					}
				});
				if(!task.computeDistances()) {
//					System.out.println("fehler 3");
					tellError();
					return;
				}
				
				// step 3
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						label.setText(Messages.getString("ComputeTour.SearchTour"));
					}
				});
				task.computeTour();
				
				// step 4
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						label.setText(Messages.getString("ComputeTour.ProcessSolution"));
					}
				});
				task.processSolution();
				tellSuccess();
			}
		}).start();
		
		setVisible(true);
	}
	
	
	private void tellError() {
		dispose();
		JOptionPane.showMessageDialog(mainView,
				Messages.getString("ComputeTour.ErrorText"),
				Messages.getString("ComputeTour.ErrorTitle"),
				JOptionPane.ERROR_MESSAGE);
	}
	private void tellOneNode() {
		dispose();
		JOptionPane.showMessageDialog(mainView,
				Messages.getString("ComputeTour.OnlyOneNodeText"),
				Messages.getString("ComputeTour.OnlyOneNodeTitle"),
				JOptionPane.ERROR_MESSAGE);
	}
	private void tellSuccess() {
		dispose();
		mainView.fireTourFound();
	}
	
}
