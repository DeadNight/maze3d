package algorithms.demo;

import java.util.ArrayList;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dMove;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searchable;
import algorithms.search.State;

/**
 * @author Nir Leibovitch
 * <h1>{@linkplain algorithms.search.Searchable} adapter for
 * {@linkplain algorithms.mazeGenerators.Maze3d}</h1>
 * Wraps a Maze3d instance to be used as a Searchable
 */
public class Maze3dSearchable implements Searchable<Position> {
	
	private Maze3d maze;
	
	/**
	 * Wrap a maze to be used as a Searchable
	 * @param maze The maze to be used as a Searchable.
	 */
	public Maze3dSearchable(Maze3d maze) {
		this.maze = maze;
	}

	@Override
	public State<Position> getInitialState() {
		if(maze == null)
			return null;
		Position startPosition = maze.getStartPosition();
		if(startPosition == null)
			return null;
		return new State<Position>(startPosition);
	}

	@Override
	public State<Position> getGoalState() {
		if(maze == null)
			return null;
		Position goalPosition = maze.getGoalPosition();
		if(goalPosition == null)
			return null;
		return new State<Position>(goalPosition);
	}

	@Override
	public ArrayList<State<Position>> getAllPossibleStates(State<Position> from) {
		ArrayList<State<Position>> states = new ArrayList<State<Position>>();
		for(Maze3dMove move : maze.getPossibleMoves(from.getState())) {
			State<Position> to = new State<Position>(move.getDestination());
			to.setCameFrom(from);
			to.setCost(move.getCost());
			states.add(to);
		}
		return states;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Maze3dSearchable)) return false;
		Maze3dSearchable other = (Maze3dSearchable) obj;
		if(!other.maze.equals(maze)) return false;
		if(!other.getInitialState().equals(getInitialState())) return false;
		if(!other.getGoalState().equals(getGoalState())) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		String str = ""+maze.hashCode() + getInitialState().hashCode()+getGoalState().hashCode();
		return str.hashCode();
	}
}
