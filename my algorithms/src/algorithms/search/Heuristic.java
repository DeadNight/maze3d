package algorithms.search;

/**
 * @author Nir Leibovitch
 * <h1>General heuristic</h1>
 * Represents a heuristic for the cost to get from an origin state to a target state
 * @param <T> The type of the internal representation of state
 */
public interface Heuristic<T> {
	/**
	 * Get the estimated cost from a given origin state to a given target state
	 * @param from The origin state
	 * @param to The target state
	 * @return double The estimated cost
	 */
	public double estimateCost(State<T> from, State<T> to);
}
