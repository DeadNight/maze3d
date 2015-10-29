package boot;

import algorithms.search.Heuristic;
import algorithms.search.State;

public class NibblesManhattanDistance implements Heuristic<Position> {
	@Override
	public double estimateCost(State<Position> from, State<Position> to) {
		return Math.abs(to.getState().getX() - from.getState().getX())
				+ Math.abs(to.getState().getY() - from.getState().getY());
	}
}
