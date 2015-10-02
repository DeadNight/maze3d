package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import algorithms.mazeGenerators.Maze3d;

public class MazeWindow extends BasicWindow {
	Button startButton;
	Text mazeDisplayer;
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
		
		mazeDisplayer = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
		mazeDisplayer.setFont(new Font(display, new FontData("Courier New", 10, SWT.NONE)));
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
		StringBuilder stringBuilder = new StringBuilder();
		
		for(int y = maze.getHeight() - 1; y >= 0 ; --y) {
			stringBuilder.append("Floor " + y + ":");
			stringBuilder.append(System.lineSeparator());
			for(int z = maze.getDepth() - 1; z >= 0; --z) {
				for(int x = 0; x < maze.getWidth(); ++x) {
					if(maze.isWall(x, y, z))
						stringBuilder.append((char)0x2593);
					else if(y > 0 && y < maze.getHeight() - 1 &&
						maze.isPath(x, y - 1, z) && maze.isPath(x, y + 1, z))
						stringBuilder.append('x');
					else if(y > 0 && maze.isPath(x, y - 1, z))
						stringBuilder.append('\\');
					else if(y < maze.getHeight() - 1 && maze.isPath(x, y + 1, z))
						stringBuilder.append('/');
					else
						stringBuilder.append(' ');
				}
				stringBuilder.append(System.lineSeparator());
			}
			stringBuilder.append(System.lineSeparator());
		}
		
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				mazeDisplayer.setText(stringBuilder.toString());
			}
		});
	}
}
