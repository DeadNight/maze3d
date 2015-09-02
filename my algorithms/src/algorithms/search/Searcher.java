package algorithms.search;

/**
 * @author Nir Leibovitch
 * <h1>General search algorithm</h1>
 * Solves {@linkplain Searchable} problems
 * @param <T> The type of the internal representation of a state of the Searchable
 */
public interface Searcher<T> {
	/**
	 * Search for a solution to a given Searchable
	 * @param s The searchable
	 * @return Solution&lt;T&gt; The solution
	 */
	Solution<T> search(Searchable<T> s);
	
	/**
	 * Get the number of nodes that the algorithm evaluated in order to find the
	 * solution returned by {@linkplain #search(Searchable)}
	 * @return int The number of nodes evaluated
	 */
	int getNumberOfNodesEvaluated();
}
