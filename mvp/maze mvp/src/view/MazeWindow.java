package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

/**
 * @author Nir Leibovitch
 * <h1>Maze game window</h1>
 * Enables to start a new game, save &amp; load maze, edit, import &amp; export game properties,
 * and solve the current maze
 */
public class MazeWindow extends BasicWindow {
	Button newGameButton;
	MazeDisplayer mazeDisplayer;
	Combo viewPlaneCombo;
	Text positionText;
	Button solveButton;
	Button exitButton;
	MenuItem newGameMenuItem;
	MenuItem loadMazeMenuItem;
	MenuItem saveMazeMenuItem;
	MenuItem editPropertiesMenuItem;
	MenuItem importPropertiesMenuItem;
	MenuItem exportPropertiesMenuItem;
	MenuItem exitMenuItem;
	
	/**
	 * Initialize the maze window
	 * @param title Window title
	 * @param width Window width
	 * @param height Window height
	 */
	public MazeWindow(String title, int width, int height) {
		super(title, width, height);
	}

	@Override
	void initWidgets() {
		initMenu();
		
		shell.setLayout(new GridLayout(3, false));
		
		newGameButton = new Button(shell, SWT.PUSH);
		newGameButton.setText("Start new game");
		newGameButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1));
		
		mazeDisplayer = new Maze2dDisplayer(shell, SWT.BORDER);
		mazeDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5));
		
		Label viewPlaneLabel = new Label(shell, SWT.NONE);
		viewPlaneLabel.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
		viewPlaneLabel.setText("View Plane");
		
		viewPlaneCombo = new Combo(shell, SWT.READ_ONLY);
		viewPlaneCombo.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		viewPlaneCombo.setItems(new String[] { "XZ", "XY", "ZY" });
		viewPlaneCombo.select(0);
		
		Label positionLabel = new Label(shell, SWT.NONE);
		viewPlaneLabel.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
		positionLabel.setText("Position:");
		
		positionText = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		positionText.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		
		solveButton = new Button(shell, SWT.PUSH);
		solveButton.setText("Solve");
		solveButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1));
		solveButton.setEnabled(false);
		
		exitButton = new Button(shell, SWT.PUSH);
		exitButton.setText("Exit");
		exitButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1));
	}
	
	// initialize the window menu bar
	private void initMenu() {
		Menu menu = new Menu(shell, SWT.BAR);
		
		MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
		fileMenuItem.setText("File");
		
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuItem.setMenu(fileMenu);
		
		newGameMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		newGameMenuItem.setText("New game");
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		loadMazeMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		loadMazeMenuItem.setText("Load maze");
		
		saveMazeMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		saveMazeMenuItem.setText("Save maze");
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		editPropertiesMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		editPropertiesMenuItem.setText("Properties");
		
		importPropertiesMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		importPropertiesMenuItem.setText("Import properties");
		
		exportPropertiesMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exportPropertiesMenuItem.setText("Export properties");
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		exitMenuItem = new MenuItem(fileMenu, SWT.PUSH);
		exitMenuItem.setText("Exit");
		
		shell.setMenuBar(menu);
	}

	/**
	 * Add a selection to handle user requests to satrt a new game
	 * @param listener Listener
	 */
	public void addNewGameSelectionListener(SelectionListener listener) {
		newGameButton.addSelectionListener(listener);
		newGameMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to change the view plane
	 * @param listener Listener
	 */
	public void addViewPlaneSelectionListener(SelectionListener listener) {
		viewPlaneCombo.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to solve the current maze from the current
	 * position
	 * @param listener Listener
	 */
	public void addSolveListener(SelectionListener listener) {
		solveButton.addSelectionListener(listener);
	}

	/**
	 * Add a listener to handle user requests to exit the game
	 * @param listener Listener
	 */
	public void addExitListener(Listener listener) {
		exitButton.addListener(SWT.Selection, listener);
		exitMenuItem.addListener(SWT.Selection, listener);
		shell.addListener(SWT.Close, listener);
	}
	
	/**
	 * Add a listener to handle user requests to load a maze
	 * @param listener Listener
	 */
	public void addLoadMazeListener(SelectionListener listener) {
		loadMazeMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to save the current maze
	 * @param listener Listener
	 */
	public void addSaveMazeListener(SelectionListener listener) {
		saveMazeMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to edit the game properties
	 * @param listener Listener
	 */
	public void addEditPropertiesListener(SelectionListener listener) {
		editPropertiesMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to import game properties
	 * @param listener Listener
	 */
	public void addImportPropertiesListener(SelectionListener listener) {
		importPropertiesMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle user requests to export the current game properties
	 * @param listener Listener
	 */
	public void addExportPropertiesListener(SelectionListener listener) {
		exportPropertiesMenuItem.addSelectionListener(listener);
	}
	
	/**
	 * Add a listener to handle keyboard input
	 * @param listener Listener
	 */
	public void addKeyListener(KeyListener listener) {
		mazeDisplayer.addKeyListener(listener);
	}

	/**
	 * Set the current maze loaded by the game
	 * @param maze Maze
	 * @param characterPosition Game character initial position
	 */
	public void setMaze(Maze3d maze, Position characterPosition) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				solveButton.setEnabled(maze != null);
				mazeDisplayer.setMaze(maze, characterPosition);
				positionText.setText(characterPosition.toString());
			}
		});
	}

	/**
	 * Set the position of the game character
	 * @param position Position
	 */
	public void setCharacterPosition(Position position) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				mazeDisplayer.setCharacterPosition(position);
				positionText.setText(position.toString());
			}
		});
	}

	/**
	 * Set the view plane displayed by the game
	 * @param viewPlane View plane
	 */
	public void setViewPlane(String viewPlane) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				viewPlaneCombo.select(viewPlaneCombo.indexOf(viewPlane));
				mazeDisplayer.setViewPlane(viewPlane);
			}
		});
	}

	/**
	 * Get the view played displayed by the game
	 * @return String View plane
	 */
	public String getSelectedViewPlane() {
		return viewPlaneCombo.getText();
	}

	/**
	 * Set whether the maze was solved
	 * @param solved Whether the maze was solved
	 */
	public void setSolved(boolean solved) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				solveButton.setEnabled(!solved);
				mazeDisplayer.setSolved(solved);
			}
		});
	}
}
