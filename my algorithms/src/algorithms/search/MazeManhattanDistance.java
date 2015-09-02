package algorithms.search;

import algorithms.mazeGenerators.Position;

/**
 * @author Nir Leibovitch
 * <h1>3d Manhattan distance heuristic</h1>
 * Estimates the cost to get from an origin position to a position using Manhattan distance
 */
public class MazeManhattanDistance implements Heuristic<Position> {

	@Override
	public double estimateCost(State<Position> from, State<Position> to) {
		return Math.abs(to.getState().getX() - from.getState().getX())
				+ Math.abs(to.getState().getY() - from.getState().getY())
				+ Math.abs(to.getState().getZ() - from.getState().getZ());
	}
	
}
