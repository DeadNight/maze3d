package algorithms.search;

import algorithms.mazeGenerators.Position;

/**
 * @author Nir Leibovitch
 * <h1>3d air distance heuristic</h1>
 * Estimates the cost to get from an origin position to a position using air distance
 */
public class MazeAirDistance implements Heuristic<Position> {

	@Override
	public double estimateCost(State<Position> from, State<Position> to) {
		return Math.sqrt(
				Math.pow(to.getState().getX() - from.getState().getX(), 2)
				+ Math.pow(to.getState().getY() - from.getState().getY(), 2)
				+ Math.pow(to.getState().getZ() - from.getState().getZ(), 2)
				);
	}

}
