package algorithms.search;

/**
 * @author Nir Leibovitch
 * <h1>A* search algorithm</h1>
 * Solves {@linkplain Searchable} problems using A* search
 * @param <T> The type of the internal representation of a state of the searchable
 */
public class AStarSearcher<T> extends CommonBFSearcher<T> {
	
	Heuristic<T> heuristic;
	
	/**
	 * Initialize a new A* searcher with a given heuristic
	 * @param heuristic The heuristic
	 */
	public AStarSearcher(Heuristic<T> heuristic) {
		this.heuristic = heuristic;
	}

	@Override
	protected double calculateCost(Searchable<T> s, State<T> from, State<T> to) {
		double cost = getCost(s, from, to) + heuristic.estimateCost(to, s.getGoalState());
		if(!from.equals(s.getInitialState()))
			cost -= heuristic.estimateCost(from, s.getGoalState());
		return cost;
	}

}
