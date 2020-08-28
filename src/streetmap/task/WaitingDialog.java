package streetmap.task;


import java.awt.Cursor;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import streetmap.globals.Function;


@SuppressWarnings("serial")
public class WaitingDialog extends JDialog {
	
	public WaitingDialog(final Task task) {
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
		/* set icon */
		setIconImages(Function.getIconImages());

		setTitle(task.getTaskTitle());
		
		JLabel label = new JLabel(task.getTaskText());
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(label);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(task.getParent());
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				task.execute();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						dispose();
					}
				});
			}
		}).start();
		
		setVisible(true);
	}
}
