package streetmap.view.general;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import language.Messages;
import streetmap.globals.Constant;
import streetmap.globals.Function;
import streetmap.model.DataBase;
import streetmap.observer.SaveStateObserver;


@SuppressWarnings("serial")
public class MainView extends JFrame implements SaveStateObserver {
	
	private JPanel			mainPanel;
	private MTMenuBar 		menuBar;
	private NavigationPanel navigationPanel;
	private ChoicePanel		choicePanel;
	private TourPanel		tourPanel;
	private StreetPanel 	streetPanel;

	/* dimensions for window size */
	private Dimension basicWindowSize;
	private Dimension minimalWindowSize;


	public MainView() {
		/* save the basic window size */
		basicWindowSize = calculateBasicWindowDimension(0.97);
		minimalWindowSize = calculateBasicWindowDimension(0.5);

		/* initialize main frame */
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowListener() {
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosed(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {
				endProgram();
			}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
		});
		
		/* set icon */
		setIconImages(Function.getIconImages());
		
		/* main Panel containing all except the menu */
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(20,20,20,20));
		mainPanel.setLayout(new BorderLayout());
		setContentPane(mainPanel);
		
		Container cNorth = new Container();
		cNorth.setLayout(new BorderLayout());
		mainPanel.add(cNorth, BorderLayout.NORTH);
		
		Container cWest = new Container();
		cWest.setLayout(new GridBagLayout());
		cNorth.add(cWest, BorderLayout.WEST);
		
		/* menu bar */
		menuBar = new MTMenuBar(this);
		setJMenuBar(menuBar);
		
		/* panel with choice of street */
		JPanel sidePanel = new JPanel();
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
		GridBagConstraints gbc_sidePanel = new GridBagConstraints();
		gbc_sidePanel.gridx = 0;
		gbc_sidePanel.gridy = 0;
		gbc_sidePanel.anchor = GridBagConstraints.NORTHWEST;
		cWest.add(sidePanel, gbc_sidePanel);
		
		// move map
		navigationPanel = new NavigationPanel();
		sidePanel.add(navigationPanel, BorderLayout.NORTH);
		// buttons
		choicePanel = new ChoicePanel();
		sidePanel.add(choicePanel, BorderLayout.CENTER);
		// tour panel
		tourPanel = new TourPanel();
		sidePanel.add(tourPanel, BorderLayout.SOUTH);
		
		/* panel with streets */
		streetPanel = new StreetPanel(this);
		GridBagConstraints gbc_streetPanel = new GridBagConstraints();
		gbc_streetPanel.gridx = 1;
		gbc_streetPanel.gridy = 0;
		gbc_streetPanel.anchor = GridBagConstraints.NORTHWEST;
		cWest.add(streetPanel, gbc_streetPanel);
		
		// register observers
		navigationPanel.registerMapMovementObserver(streetPanel);
		choicePanel.registerStreetTileSelectionObserver(streetPanel);
		tourPanel.registerMapMovementObserver(streetPanel);
		tourPanel.registerTourStateObserver(streetPanel);
		tourPanel.registerTourStateObserver(menuBar);
		tourPanel.registerTourStateObserver(choicePanel);
		
		/* title */
		setProgramTitle(null, false);
		DataBase.getInstance().registerSaveStateObserver(this);
		
		/* set size of window */
		setMinimumSize(minimalWindowSize);
		setSize(basicWindowSize);
		//mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		/* center window on screen */
		setLocationRelativeTo(null);
		setBackground(Constant.COL_BACKGROUND);
		setVisible(true);
	}

	/**
	 * Calculate the dimension of the visible screen and set the window
	 * size to a percentage of this value. The visible screen is the screen
	 * without the task bar(s).
	 * @param screenPercentage percentage of the visible screen
	 * @return dimension of the visible screen
	 */
	public Dimension calculateBasicWindowDimension(double screenPercentage) {
		/* get screen size */
		Dimension _screenDimension = Toolkit.getDefaultToolkit().getScreenSize();

		/* visible rectangle of screen */
		Rectangle _maxBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		/* calculate borders */
		Insets _screenInsets = new Insets(
				(int)_maxBounds.getY(),
				(int)_maxBounds.getX(),
				(int)(_screenDimension.getHeight() - _maxBounds.getY() - _maxBounds.getHeight()),
				(int)(_screenDimension.getWidth() - _maxBounds.getWidth() - _maxBounds.getX()));

		/* dimension of visible screen */
		int width = (int)(_screenDimension.getWidth() - _screenInsets.right - _screenInsets.left);
		int height = (int)(_screenDimension.getHeight() - _screenInsets.top - _screenInsets.bottom);

		Dimension _screenDimensionView = new Dimension((int) (width * screenPercentage),
				(int) (height * screenPercentage));

		return _screenDimensionView;
	}
	
	

	public void endProgram() {
		int answer = JOptionPane.OK_OPTION;
		if(DataBase.getInstance().isSavingNecessary()) {
			answer = JOptionPane.showConfirmDialog(this,
					Messages.getString("EndProgram.ConfirmText"),
					Messages.getString("EndProgram.ConfirmTitle"),
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
		}
		if(answer == JOptionPane.OK_OPTION) {
			System.exit(0);
		}
	}
	

	public void fireTourFound() {
		menuBar.enableInput(false);
		choicePanel.enableInput(false);
		streetPanel.enableTourModus(true);
		tourPanel.enableInput(true);
	}
	
	
	
	@Override
	public void fireChangedSaveState(String newPath, boolean savingNecessary) {
		setProgramTitle(newPath, savingNecessary);
	}
	private void setProgramTitle(String savePath, boolean savingNecessary) {
		String title = Messages.getString("Program.Title");
		if(savePath == null) {
			if(savingNecessary) {
				title += "*";
			}
		}
		else { 
			int index1 = savePath.lastIndexOf(Constant.FILE_SEPARATOR) + 1;
			int index2 = savePath.length() - 4;
			String file = savePath.substring(index1, index2);
			title += " (" + file + (savingNecessary ? "*" : "") + ")";
		}
		setTitle(title);
	}

	
	public void reset() {
		tourPanel.notifyTourClosed();
	}



}
