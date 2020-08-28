package streetmap.view.general;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import language.Messages;
import streetmap.globals.Constant;
import streetmap.globals.Variable;
import streetmap.model.DataBase;
import streetmap.model.command.CommandList;
import streetmap.observer.CommandListObserver;
import streetmap.observer.TourStateObserver;
import streetmap.solver.ComputTourDialog;
import streetmap.solver.ComputeTourTask;
import streetmap.task.LoadDataBaseTask;
import streetmap.task.RedoTask;
import streetmap.task.SaveAsDataBaseTask;
import streetmap.task.SaveDataBaseTask;
import streetmap.task.Task;
import streetmap.task.UndoTask;
import streetmap.task.WaitingDialog;
import streetmap.view.graphicalElements.JMenuHover;
import streetmap.view.graphicalElements.JMenuItemLingual;



@SuppressWarnings("serial")
public class MTMenuBar extends JMenuBar implements ActionListener, CommandListObserver, TourStateObserver {
	
	private MainView mainView;
	
	/* menu */
	private JMenuHover menuFile;
	private JMenuItemLingual menuItemNew;
	private JMenuItemLingual menuItemLoad;
	private JMenuItemLingual menuItemSave;
	private JMenuItemLingual menuItemSaveAs;
	private JMenuItemLingual menuItemEnd;
	
	private JMenuHover menuEdit;
	private JMenuItemLingual menuItemUndo;
	private JMenuItemLingual menuItemRedo;
	private JMenuItemLingual menuItemComputeTour;
	private JMenuItemLingual menuItemTourQuality;
	

	public MTMenuBar(MainView mainView) {
		this.mainView = mainView;
		CommandList.getInstance().registerCommandListObserver(this);

		createMenuFile();
		createMenuEdit();
		
		add(Box.createHorizontalGlue());
		add(new JLabel(Constant.COPYRIGHT + " tommy-13  "));
		
		
		// register menu items as for this action listener
		for(Component c : getComponents()) {
			if(c instanceof JMenuHover) {
				JMenuHover menu = (JMenuHover) c;
				for(Component cmi : menu.getMenuComponents()) {
					if(cmi instanceof JMenuItem) {
						JMenuItem menuItem = (JMenuItem) cmi;
						menuItem.addActionListener(this);
					}
				}
			}
		}
	}
	
	private void createMenuFile() {
		menuFile = new JMenuHover("MenuBar.MenuFile");
		menuFile.setMnemonic(Messages.getString("Mnemonic.File").charAt(0));
		
		menuItemNew  = new JMenuItemLingual("MenuBar.MenuItemNew");
		menuItemNew.setAccelerator(KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK));
		menuFile.add(menuItemNew);
		
		menuFile.addSeparator();
		
		menuItemLoad = new JMenuItemLingual("MenuBar.MenuItemLoad");
		menuItemLoad.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
		menuFile.add(menuItemLoad);
		
		menuItemSave = new JMenuItemLingual("MenuBar.MenuItemSave");
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
		menuFile.add(menuItemSave);
		
		menuItemSaveAs = new JMenuItemLingual("MenuBar.MenuItemSaveAs");
		menuItemSaveAs.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK));
		menuFile.add(menuItemSaveAs);

		menuFile.addSeparator();
		
		menuItemEnd  = new JMenuItemLingual("MenuBar.MenuItemEnd");
		menuItemEnd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
		menuFile.add(menuItemEnd);
		
		add(menuFile);
	}
	
	private void createMenuEdit() {
		menuEdit = new JMenuHover("MenuBar.MenuEdit");
		menuEdit.setMnemonic(Messages.getString("Mnemonic.Edit").charAt(0));
		
		menuItemUndo = new JMenuItemLingual("MenuBar.MenuItemUndo");
		menuItemUndo.setEnabled(false);
		menuItemUndo.setAccelerator(KeyStroke.getKeyStroke('Z', InputEvent.CTRL_DOWN_MASK));
		menuEdit.add(menuItemUndo);
		
		menuItemRedo = new JMenuItemLingual("MenuBar.MenuItemRedo");
		menuItemRedo.setEnabled(false);
		menuItemRedo.setAccelerator(KeyStroke.getKeyStroke('Y', InputEvent.CTRL_DOWN_MASK));
		menuEdit.add(menuItemRedo);
		
		menuEdit.addSeparator();
		
		menuItemComputeTour = new JMenuItemLingual("MenuBar.MenuItemComputeTour");
		menuItemComputeTour.setAccelerator(KeyStroke.getKeyStroke('T', InputEvent.CTRL_DOWN_MASK));
		menuEdit.add(menuItemComputeTour);
		
		menuItemTourQuality = new JMenuItemLingual("MenuBar.MenuItemTourQuality");
		menuItemTourQuality.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
		menuEdit.add(menuItemTourQuality);
		
		add(menuEdit);
	}

	
	public void createNewFile() {
		DataBase dataBase = DataBase.getInstance();
		int answer = JOptionPane.OK_OPTION;
		if(dataBase.isSavingNecessary()) {
			answer = JOptionPane.showConfirmDialog(mainView,
					Messages.getString("DataLoseWarning.ConfirmText"),
					Messages.getString("DataLoseWarning.ConfirmTitle"),
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
		if(answer == JOptionPane.OK_OPTION) {
			CommandList.getInstance().reset();
			mainView.reset();
			dataBase.reset();
		}
	}

	public void loadFile() {
		int answer = JOptionPane.OK_OPTION;
		if(DataBase.getInstance().isSavingNecessary()) {
			answer = JOptionPane.showConfirmDialog(mainView,
					Messages.getString("DataLoseWarning.ConfirmText"),
					Messages.getString("DataLoseWarning.ConfirmTitle"),
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
		if(answer == JOptionPane.OK_OPTION) {
			Task task = new LoadDataBaseTask(mainView);
			new WaitingDialog(task);
			if(task.isDone()) {
				CommandList.getInstance().reset();
				mainView.reset();
			}
		}
	}
	
	public void saveFile(boolean saveAs) {
		Task task = saveAs ? new SaveAsDataBaseTask(mainView) : new SaveDataBaseTask(mainView);
		new WaitingDialog(task);
	}


	public void enableInput(boolean enabled) {
		menuEdit.setEnabled(enabled);
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if(actionCommand.equals("MenuBar.MenuItemNew")) {
			createNewFile();
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemSave")) {
			saveFile(false);
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemSaveAs")) {
			saveFile(true);
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemLoad")) {
			loadFile();
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemEnd")) {
			mainView.endProgram();
			return;
		}
		
		if(actionCommand.equals("MenuBar.MenuItemUndo")) {
			Task task = new UndoTask(mainView);
			task.execute();
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemRedo")) {
			Task task = new RedoTask(mainView);
			task.execute();
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemComputeTour")) {
			ComputeTourTask task = new ComputeTourTask(Variable.solverThreads);
			new ComputTourDialog(mainView, task);
			return;
		}
		if(actionCommand.equals("MenuBar.MenuItemTourQuality")) {
			new QualityDialog(mainView);
			return;
		}
		
	}
	
	@Override
	public void fireUndoStackState(boolean isEmpty) {
		menuItemUndo.setEnabled(!isEmpty);
	}
	@Override
	public void fireRedoStackState(boolean isEmpty) {
		menuItemRedo.setEnabled(!isEmpty);
	}

	@Override
	public void firePartialPathChanged(int id) {}
	@Override
	public void fireTourClosed() {
		enableInput(true);
	}
}
