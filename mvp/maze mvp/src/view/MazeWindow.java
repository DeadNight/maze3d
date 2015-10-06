package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;

public class MazeWindow extends BasicWindow {
	Button newGameButton;
	MazeDisplayer mazeDisplayer;
	Button solveButton;
	Button exitButton;
	MenuItem newGameMenuItem;
	MenuItem loadMazeMenuItem;
	MenuItem saveMazeMenuItem;
	MenuItem editPropertiesMenuItem;
	MenuItem importPropertiesMenuItem;
	MenuItem exportPropertiesMenuItem;
	MenuItem exitMenuItem;
	Thread solutionDisplayer;
	
	public MazeWindow(String title, int width, int height) {
		super(title, width, height);
	}

	@Override
	void initWidgets() {
		initMenu();
		
		shell.setLayout(new GridLayout(2, false));
		
		newGameButton = new Button(shell, SWT.PUSH);
		newGameButton.setText("Start new game");
		newGameButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		
		mazeDisplayer = new Maze2dDisplayer(shell, SWT.BORDER);
		mazeDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		
		solveButton = new Button(shell, SWT.PUSH);
		solveButton.setText("Solve");
		solveButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		
		exitButton = new Button(shell, SWT.PUSH);
		exitButton.setText("Exit");
		exitButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
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
	
	public void addImportPropertiesListener(SelectionListener listener) {
		importPropertiesMenuItem.addSelectionListener(listener);
	}
	
	public void addExportPropertiesListener(SelectionListener listener) {
		exportPropertiesMenuItem.addSelectionListener(listener);
	}
	
	public void addEditPropertiesListener(SelectionListener listener) {
		exportPropertiesMenuItem.addSelectionListener(listener);
	}
	
	public void addKeyListener(KeyListener listener) {
		mazeDisplayer.addKeyListener(listener);
	}

	public void displayMaze(Maze3d maze) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				mazeDisplayer.setMaze(maze);
			}
		});
	}

	public void moveForward() {
		if(solutionDisplayer != null)
			solutionDisplayer.interrupt();
		mazeDisplayer.moveForward();
	}

	public void moveBack() {
		if(solutionDisplayer != null)
			solutionDisplayer.interrupt();
		mazeDisplayer.moveBack();
	}
	
	public void moveLeft() {
		if(solutionDisplayer != null)
			solutionDisplayer.interrupt();
		mazeDisplayer.moveLeft();
	}
	
	public void moveRight() {
		if(solutionDisplayer != null)
			solutionDisplayer.interrupt();
		mazeDisplayer.moveRight();
	}

	public void moveUp() {
		if(solutionDisplayer != null)
			solutionDisplayer.interrupt();
		mazeDisplayer.moveUp();
	}

	public void moveDown() {
		if(solutionDisplayer != null)
			solutionDisplayer.interrupt();
		mazeDisplayer.moveDown();
	}
	
	public Position getCharacterPosition() {
		return mazeDisplayer.getCaracterPosition();
	}

	public void displaySolution(Solution<Position> solution) {
		if(solutionDisplayer != null)
			return;
		solutionDisplayer = new Thread(new Runnable() {
			@Override
			public void run() {
				for(State<Position> s : solution.getSequence()) {
					// enable thread interruption
					if(Thread.interrupted())
						break;
					display.syncExec(new Runnable() {
						@Override
						public void run() {
							mazeDisplayer.setCharacterPosition(new Position(s.getState()));
						}
					});
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// enable thread interruption
						break;
					}
				}
				solutionDisplayer = null;
			}
		});
		solutionDisplayer.start();
	}
}
