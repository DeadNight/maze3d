package view;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

/**
 * @author Nir Leibovitch
 * <h1>Widget for displaying mazes</h1>
 */
public abstract class MazeDisplayer extends Canvas {
	Maze3d maze;
	Position characterPosition;
	String viewPlane;
	boolean solved;
	
	/**
	 * Instantiate the maze displayer with the given parent &amp; style
	 * @param parent Parent of the widget
	 * @param style Style for the widget
	 */
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
		viewPlane = "XZ";
	}

	/**
	 * Set the maze displayed by this widget
	 * @param maze The maze to display
	 * @param characterPosition The character starting position
	 */
	public void setMaze(Maze3d maze, Position characterPosition) {
		this.maze = maze;
		setCharacterPosition(characterPosition);
	}

	/**
	 * Set the character position
	 * @param position Character position
	 */
	public void setCharacterPosition(Position position) {
		characterPosition = position;
		redraw();
	}

	/**
	 * Set the view plane displayed by this widget
	 * @param plane View plane
	 */
	public void setViewPlane(String plane) {
		viewPlane = plane;
		redraw();
	}

	/**
	 * Display that the maze was solved successfully
	 * @param solved Whether or not the maze was solved
	 */
	public void setSolved(boolean solved) {
		this.solved = solved;
		redraw();
	}
}
