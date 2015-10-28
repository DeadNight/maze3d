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
	private static final long serialVersionUID = 5636583976010404614L;
	private Maze3d maze;
	private State<Position> initialState;

	/**
	 * Wrap a maze to be used as a Searchable, and use the maze start position as the initial state
	 * @param maze The maze to be used as a Searchable
	 */
	public Maze3dSearchable(Maze3d maze) {
		this(maze, maze == null ? null : maze.getStartPosition());
	}
	
	/**
	 * Wrap a maze to be used as a Searchable, and use the given initial state
	 * @param maze The maze to be used as a Searchable
	 * @param initialState The initial state
	 */
	public Maze3dSearchable(Maze3d maze, Position initialPosition) {
		this.maze = maze;
		initialState = new State<Position>(initialPosition);
	}
	
	/**
	 * Get internal state maze (used for serialization)
	 * @return Maze3d maze
	 */
	public Maze3d getMaze() {
		return maze;
	}
	
	/**
	 * Set internal state maze (used for deserialization)
	 * @param maze maze
	 */
	public void setMaze(Maze3d maze) {
		this.maze = maze;
	}

	@Override
	public State<Position> getInitialState() {
		if(maze == null)
			return null;
		return initialState;
	}
	
	/**
	 * Set initial state (used for deserialization)
	 * @param initialState Initial state
	 */
	public void setInitialState(State<Position> initialState) {
		this.initialState = initialState;
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
		String str = ""+maze.hashCode() + getInitialState().hashCode() + getGoalState().hashCode();
		return str.hashCode();
	}
}
