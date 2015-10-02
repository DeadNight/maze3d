package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;

import algorithms.mazeGenerators.Maze3d;

public class MazeWindow extends BasicWindow {
	Button startButton;
	MazeDisplayer mazeDisplayer;
	Button exitButton;
	
	public MazeWindow(String title, int width, int height) {
		super(title, width, height);
	}

	@Override
	void initWidgets() {
		shell.setLayout(new GridLayout(2, false));
		
		startButton = new Button(shell, SWT.PUSH);
		startButton.setText("Start new game");
		startButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
		
		mazeDisplayer = new Maze2dDisplayer(shell, SWT.BORDER);
		mazeDisplayer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
		
		exitButton = new Button(shell, SWT.PUSH);
		exitButton.setText("Exit");
		exitButton.setLayoutData(new GridData(SWT.FILL, SWT.NONE, false, false));
	}
	
	public void addStartSelectionListener(SelectionListener listener) {
		startButton.addSelectionListener(listener);
	}

	public void addExitListener(Listener listener) {
		exitButton.addListener(SWT.Selection, listener);
		shell.addListener(SWT.Close, listener);
	}

	public void displayMaze(Maze3d maze) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				mazeDisplayer.setMaze(maze);
			}
		});
	}
}
