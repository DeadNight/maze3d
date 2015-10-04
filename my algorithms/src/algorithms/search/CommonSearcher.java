package algorithms.search;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Nir Leibovitch
 * <h1>General search algorithm</h1>
 * Solves {@linkplain Searchable} problems
 * @param <T> The type of the internal representation of a state of the Searchable
 */
public abstract class CommonSearcher<T> implements Searcher<T> {
	protected PriorityQueue<State<T>> openList;
	private int evaluatedNodes = 0;
	
	 /**
	 * Initialize a new Searcher
	 */
	public CommonSearcher() {
		openList = new PriorityQueue<State<T>>(new Comparator<State<T>>() {

			@Override
			public int compare(State<T> s1, State<T> s2) {
				return (int)(s1.getCost() - s2.getCost());
			}
			
		});
		evaluatedNodes = 0;
	}
	
	@Override
	public Solution<T> search(Searchable<T> s) {
		openList.clear();
		evaluatedNodes = 0;
		return doSearch(s);
	}
	
	protected abstract Solution<T> doSearch(Searchable<T> s);

	/**
	 * Add a state to the openList for later evaluation
	 * @param s The state
	 */
	protected void addToOpenList(State<T> s) {
		openList.add(s);
	}
	 
	/**
	 * Get the best state from the openList for evaluation.
	 * removes it from the openList, and increments the number of evaluated nodes.
	 * @return State&lt;T&gt; The state
	 */
	protected State<T> popOpenList() {
		++evaluatedNodes;
		return openList.poll();
	}

	@Override
	public int getNumberOfNodesEvaluated() {
		return evaluatedNodes;
	}
}
