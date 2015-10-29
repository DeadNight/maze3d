package boot;

import java.util.ArrayList;

public class Nibbles {
	public static final int BOARD_SIZE = 30;
	
	private Position headPosition;
	private Position applePosition;
	private ArrayList<Position> bodyPositions;
	
	public Nibbles(Position headPosition, Position applePosition, ArrayList<Position> bodyPositions) {
		this.headPosition = headPosition;
		this.applePosition = applePosition;
		this.bodyPositions = bodyPositions;
	}

	public Position getHeadPosition() {
		return headPosition;
	}
	public Position getApplePosition() {
		return applePosition;
	}

	public ArrayList<Position> getBodyPositions() {
		return bodyPositions;
	}
	
	/**
	 * Get the possible moves from a given x,y coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return ArrayList&lt;Maze3dMove&gt; The possible moves
	 */
	public ArrayList<NibblesMove> getPossibleMoves(int x, int y) {
		return getPossibleMoves(new Position(x, y));
	}
	
	/**
	 * Get the possible moves from a given position
	 * @param from The position
	 * @return ArrayList&lt;Maze3dMove&gt; The possible moves
	 */
	public ArrayList<NibblesMove> getPossibleMoves(Position from) {
		ArrayList<NibblesMove> moves = new ArrayList<NibblesMove>();
		if(from.getX() > 0) {
			Position to = new Position(from).moveLeft();
			if(!bodyPositions.contains(to)) {
				moves.add(new NibblesMove(from, "left", to, 1));
			}
		}
		if(from.getX() < BOARD_SIZE - 1) {
			Position to = new Position(from).moveRight();
			if(!bodyPositions.contains(to)) {
				moves.add(new NibblesMove(from, "right", to, 1));
			}
		}
		if(from.getY() > 0) {
			Position to = new Position(from).moveUp();
			if(!bodyPositions.contains(to)) {
				moves.add(new NibblesMove(from, "up", to, 1));
			}
		}
		if(from.getY() < BOARD_SIZE - 1) {
			Position to = new Position(from).moveDown();
			if(!bodyPositions.contains(to)) {
				moves.add(new NibblesMove(from, "down", to, 1));
			}
		}
		return moves;
	}
	
	@Override
	public String toString() {
		StringBuilder resultBuilder = new StringBuilder();
		for(int y = 0; y < BOARD_SIZE; ++y) {
			for(int x = 0; x < BOARD_SIZE; ++x) {
				Position position = new Position(x, y);
				if(headPosition.equals(position))
					resultBuilder.append("O");
				else if(applePosition.equals(position))
					resultBuilder.append("@");
				else if(bodyPositions.indexOf(position) > -1)
					resultBuilder.append("#");
				else
					resultBuilder.append(" ");
			}
			resultBuilder.append(System.lineSeparator());
		}
		return resultBuilder.toString();
	}
}
