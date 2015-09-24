package algorithms.search;

import java.util.HashMap;

/**
 * @author Nir Leibovitch
 * <h1>General BFS algorithm</h1>
 * Solves {@linkplain Searchable} problems using BFS
 * @param <T> The type of the internal representation of a state of the searchable
 */
public abstract class CommonBFSearcher<T> extends CommonSearcher<T> {
	
	HashMap<T, State<T>> visitedMap;
	
	/**
	 * Initialize a new BFS Searcher
	 */
	public CommonBFSearcher() {
		super();
		visitedMap = new HashMap<T, State<T>>();
	}

	@Override
	public Solution<T> search(Searchable<T> s) {
		State<T> goal = s.getGoalState();
		State<T> initialState = s.getInitialState();
		
		addToOpenList(initialState);
		
		while(openList.size() > 0) {
			if(Thread.interrupted())
				// enable thread to be interrupted
				return null;
			
			State<T> n = popOpenList();
			
			if(n.equals(goal))
				return backTrace(n, s.getInitialState());
			
			for(State<T> state : s.getAllPossibleStates(n)) {
				double stateCost = calculateCost(s, n, state);
				if(!visitedMap.containsKey(state.getState())) {
					updateState(state, n, stateCost);
					addToOpenList(state);
				} else {
					State<T> visitedState = visitedMap.get(state.getState());
					if(stateCost < visitedState.getCost()) {
						if(openList.contains(state)) {
							openList.remove(visitedState);
							updateState(visitedState, n, stateCost);
							addToOpenList(visitedState);
						} else {
							updateState(state, n, stateCost);
							addToOpenList(state);
						}
					}
				}
			}
		}
		return null;
	}
	
	@Override
	protected void addToOpenList(State<T> s) {
		super.addToOpenList(s);
		visitedMap.put(s.getState(), s);
	}
	
	/**
	 * Get the cost to get from #{linkplain Searchable#getStartPosition()} to a given
	 * state (to) through a given neighbor state (from) in a giver searchable
	 * @param s The searchable
	 * @param from The neighbor state
	 * @param to The target state
	 * @return double The cost
	 */
	protected double getCost(Searchable<T> s, State<T> from, State<T> to) {
		/*
		 *  `to` was generated by `s.getPossibleMoves(from)`, so `to.getCost()` will
		 *  return the cost to move from `from` to `to`
		 */
		return from.getCost() + to.getCost();
	}
	
	/**
	 * Calculate the cost of a given state (to) through a given neighbor state (from)
	 * in a given searchable
	 * Override this function to provide different BFS implementations.
	 * @param s The searchable
	 * @param from The initial state
	 * @param to The target state
	 * @return double The cost
	 */
	protected abstract double calculateCost(Searchable<T> s, State<T> from, State<T> to);
	
	private void updateState(State<T> state, State<T> from, double cost) {
		state.setCameFrom(from);
		state.setCost(cost);
	}
	
	private Solution<T> backTrace(State<T> goal, State<T> initial) {
		return new Solution<T>(goal, initial);
	}

}
