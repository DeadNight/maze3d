package view;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;

public abstract class MazeDisplayer extends Canvas {
	Maze3d maze;
	Position characterPosition;
	char crossSectionAxis;
	
	public MazeDisplayer(Composite parent, int style) {
		super(parent, style);
		crossSectionAxis = 'Y';
	}

	public void setMaze(Maze3d maze, Position characterPosition) {
		this.maze = maze;
		setCharacterPosition(characterPosition);
	}

	public void setCharacterPosition(Position position) {
		characterPosition = position;
		redraw();
	}

	public void setCrossSectionAxis(char c) throws IllegalArgumentException {
		if(!(c == 'X' || c == 'Y' || c == 'Z'))
			throw new IllegalArgumentException();
		crossSectionAxis = c;
		redraw();
	}
}
