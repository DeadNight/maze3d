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

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

public class MazeWindow extends BasicWindow {
	Button newGameButton;
	MazeDisplayer mazeDisplayer;
	Combo viewPlaneCombo;
	Button solveButton;
	Button exitButton;
	MenuItem newGameMenuItem;
	MenuItem loadMazeMenuItem;
	MenuItem saveMazeMenuItem;
	MenuItem editPropertiesMenuItem;
	MenuItem importPropertiesMenuItem;
	MenuItem exportPropertiesMenuItem;
	MenuItem exitMenuItem;
	
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
		mazeDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
		
		Label viewPlaneLabel = new Label(shell, SWT.NONE);
		viewPlaneLabel.setLayoutData(new GridData(SWT.NONE, SWT.CENTER, false, false));
		viewPlaneLabel.setText("View Plane");
		
		viewPlaneCombo = new Combo(shell, SWT.READ_ONLY);
		viewPlaneCombo.setLayoutData(new GridData(SWT.NONE, SWT.NONE, false, false));
		viewPlaneCombo.setItems(new String[] { "XZ", "XY", "ZY" });
		viewPlaneCombo.select(0);
		
		solveButton = new Button(shell, SWT.PUSH);
		solveButton.setText("Solve");
		solveButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1));
		solveButton.setEnabled(false);
		
		exitButton = new Button(shell, SWT.PUSH);
		exitButton.setText("Exit");
		exitButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1));
	}
	
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

	public void addNewGameSelectionListener(SelectionListener listener) {
		newGameButton.addSelectionListener(listener);
		newGameMenuItem.addSelectionListener(listener);
	}
	
	public void addViewPlaneSelectionListener(SelectionListener listener) {
		viewPlaneCombo.addSelectionListener(listener);
	}
	
	public void addSolveListener(SelectionListener listener) {
		solveButton.addSelectionListener(listener);
	}

	public void addExitListener(Listener listener) {
		exitButton.addListener(SWT.Selection, listener);
		exitMenuItem.addListener(SWT.Selection, listener);
		shell.addListener(SWT.Close, listener);
	}
	
	public void addLoadMazeListener(SelectionListener listener) {
		loadMazeMenuItem.addSelectionListener(listener);
	}
	
	public void addSaveMazeListener(SelectionListener listener) {
		saveMazeMenuItem.addSelectionListener(listener);
	}
	
	public void addEditPropertiesListener(SelectionListener listener) {
		editPropertiesMenuItem.addSelectionListener(listener);
	}
	
	public void addImportPropertiesListener(SelectionListener listener) {
		importPropertiesMenuItem.addSelectionListener(listener);
	}
	
	public void addExportPropertiesListener(SelectionListener listener) {
		exportPropertiesMenuItem.addSelectionListener(listener);
	}
	
	public void addKeyListener(KeyListener listener) {
		mazeDisplayer.addKeyListener(listener);
	}

	public void setMaze(Maze3d maze, Position characterPosition) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				solveButton.setEnabled(maze != null);
				mazeDisplayer.setMaze(maze, characterPosition);
			}
		});
	}

	public void setCharacterPosition(Position position) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				mazeDisplayer.setCharacterPosition(position);
			}
		});
	}

	public void setCrossSectionAxis(char c) {
		mazeDisplayer.setCrossSectionAxis(c);
	}

	public String getSelectedViewPlane() {
		return viewPlaneCombo.getText();
	}
}
