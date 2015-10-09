package view;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

public abstract class MazeDisplayer extends Canvas {
	Maze3d maze;
	Position characterPosition;
	String viewPlane;
	boolean solved;
	
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
		viewPlane = "XZ";
	}

	public void setMaze(Maze3d maze, Position characterPosition) {
		this.maze = maze;
		setCharacterPosition(characterPosition);
	}

	public void setCharacterPosition(Position position) {
		characterPosition = position;
		redraw();
	}

	public void setViewPlane(String plane) {
		viewPlane = plane;
		redraw();
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
		redraw();
	}
}
