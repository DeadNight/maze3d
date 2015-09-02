package algorithms.search;

import java.util.ArrayList;

/**
 * @author Nir Leibovitch
 * <h1>General searchable problem</h1>
 * Represents a problem that can be solved using {@linkplain Searcher}
 * @param <T> The type of the internal representation of a state of the searchable
 */
public interface Searchable<T> {
	/**
	 * Get the initial state of the problem
	 * @return State&lt;T&gt; The initial state
	 */
	State<T> getInitialState();
	
	/**
	 * Get the goal state of the problem
	 * @return State&lt;T&gt; The goal state
	 */
	State<T> getGoalState();
	
	/**
	 * Get all the possible states from a given origin state
	 * @param s The origin state
	 * @return ArrayList&lt;State&lt;T&gt;&gt; The possible states
	 */
	ArrayList<State<T>> getAllPossibleStates(State<T> s);
}
