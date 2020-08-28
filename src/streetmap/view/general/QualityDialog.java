package streetmap.view.general;


import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRootPane;

import language.Messages;
import streetmap.globals.Constant;
import streetmap.globals.Function;
import streetmap.globals.Variable;
import streetmap.view.graphicalElements.JSpinnerInteger;


@SuppressWarnings("serial")
public class QualityDialog extends JDialog implements ActionListener {
	
	private JSpinnerInteger siThreads;
	private JButton			bOk;
	private JButton			bCancel;
	
	public QualityDialog(Component parent) {
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setIconImages(Function.getIconImages());
		setTitle(Messages.getString("QualityDialog.Title"));
		JRootPane rootPane = getRootPane();
		rootPane.setLayout(new GridBagLayout());
		rootPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		JLabel lNote = new JLabel(Messages.getString("QualityDialog.Note"));
		GridBagConstraints gbc_lNote = new GridBagConstraints();
		gbc_lNote.gridx = 0;
		gbc_lNote.gridy = 0;
		gbc_lNote.gridwidth = 2;
		gbc_lNote.anchor = GridBagConstraints.WEST;
		gbc_lNote.insets = new Insets(0, 0, 10, 0);
		rootPane.add(lNote, gbc_lNote);
		
		JLabel lThreads = new JLabel(Messages.getString("QualityDialog.Threads") + ":");
		GridBagConstraints gbc_lThreads = new GridBagConstraints();
		gbc_lThreads.gridx = 0;
		gbc_lThreads.gridy = 1;
		gbc_lThreads.anchor = GridBagConstraints.WEST;
		rootPane.add(lThreads, gbc_lThreads);
		
		siThreads = new JSpinnerInteger(1, Constant.MAX_THREADS);
		siThreads.setPreferredSize(Function.getPositionDimension());
		siThreads.setValue(Variable.solverThreads);
		GridBagConstraints gbc_siThreads = new GridBagConstraints();
		gbc_siThreads.gridx = 1;
		gbc_siThreads.gridy = 1;
		gbc_siThreads.anchor = GridBagConstraints.WEST;
		gbc_siThreads.insets = new Insets(0, 10, 0, 0);
		rootPane.add(siThreads, gbc_siThreads);
		
		Container cButtons = new Container();
		cButtons.setLayout(new GridBagLayout());
		GridBagConstraints gbc_cButtons = new GridBagConstraints();
		gbc_cButtons.gridx = 0;
		gbc_cButtons.gridy = 2;
		gbc_cButtons.gridwidth = 2;
		gbc_cButtons.insets = new Insets(30, 10, 0, 10);
		rootPane.add(cButtons, gbc_cButtons);
		
		bOk = new JButton(Messages.getString("Global.ButtonOk"));
		bOk.addActionListener(this);
		getRootPane().setDefaultButton(bOk);
		GridBagConstraints gbc_bOk = new GridBagConstraints();
		gbc_bOk.gridx = 0;
		gbc_bOk.gridy = 0;
		gbc_bOk.insets = new Insets(0, 0, 0, 10);
		cButtons.add(bOk, gbc_bOk);
		
		bCancel = new JButton(Messages.getString("Global.ButtonCancel"));
		bCancel.addActionListener(this);
		GridBagConstraints gbc_bCancel = new GridBagConstraints();
		gbc_bCancel.gridx = 1;
		gbc_bCancel.gridy = 0;
		cButtons.add(bCancel, gbc_bCancel);
		
		pack();
		setResizable(false);
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if(o == bOk) {
			Variable.solverThreads = siThreads.getInt();
			dispose();
			return;
		}
		if(o == bCancel) {
			dispose();
			return;
		}
	}
}
