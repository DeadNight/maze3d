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
		characterPosition = new Position(maze.getStartPosition());
		redraw();
	}

	public void setCharacterPosition(Position position) {
		characterPosition = position;
		redraw();
	}

	public Position getCaracterPosition() {
		return characterPosition;
	}

	public void moveForward() {
		Position next = new Position(characterPosition).moveForward();
		try {
			if(maze.isPath(next)) {
				characterPosition.moveForward();
				redraw();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// don't move
		}
	}

	public void moveBack() {
		Position next = new Position(characterPosition).moveBack();
		try {
			if(maze.isPath(next)) {
				characterPosition.moveBack();
				redraw();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// don't move
		}
	}

	public void moveLeft() {
		Position next = new Position(characterPosition).moveLeft();
		try {
			if(maze.isPath(next)) {
				characterPosition.moveLeft();
				redraw();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// don't move
		}
	}
	
	public void moveRight() {
		Position next = new Position(characterPosition).moveRight();
		try {
			if(maze.isPath(next)) {
				characterPosition.moveRight();
				redraw();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// don't move
		}
	}
	
	public void moveUp() {
		Position next = new Position(characterPosition).moveUp();
		try {
			if(maze.isPath(next)) {
				characterPosition.moveUp();
				redraw();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// don't move
		}
	}
	
	public void moveDown() {
		Position next = new Position(characterPosition).moveDown();
		try {
			if(maze.isPath(next)) {
				characterPosition.moveDown();
				redraw();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// don't move
		}
	}
}
