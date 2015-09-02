package algorithms.search;

/**
 * @author Nir Leibovitch
 * <h1>BFS algorithm</h1>
 * Solves {@linkplain Searchable} problems using BFS
 * @param <T> The type of the internal representation of a state of the searchable
 */
public class BFSearcher<T> extends CommonBFSearcher<T> {

	@Override
	protected double calculateCost(Searchable<T> s, State<T> from, State<T> to) {
		return getCost(s, from, to);
	}
	
}
