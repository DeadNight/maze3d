package view;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

public abstract class MazeDisplayer extends Canvas {
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
	}

	Maze3d maze;
	Position characterPosition;

	public void setMaze(Maze3d maze) {
		this.maze = maze;
		characterPosition = maze.getStartPosition();
		redraw();
	}
}
