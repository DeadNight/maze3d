package algorithms.search;

/**
 * @author Nir Leibovitch
 * <h1>Generic state of a search problem</h1>
 * Represents a single state of a search problem
 * @param <T> The type of the internal representation of state
 */
public class State<T> {
	private T state;
	private double cost;
	private State<T> cameFrom;
	
	/**
	 * Create a new State
	 * @param state The internal representation of state
	 */
	public State(T state) {
		this.state = state;
	}
	
	/**
	 * Get the internal representation of state
	 * @return T The internal representation of state
	 */
	public T getState() {
		return state;
	}
	
	/**
	 * Get the state that this state came from
	 * @return T The state that this state came from
	 */
	public State<T> getCameFrom() {
		return cameFrom;
	}

	/**
	 * Set the state that this state came from
	 * @param cameFrom The new cameFrom state
	 */
	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}
	
	/**
	 * Set the cost of the state
	 * @param cost The new cost
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * Get the cost of the state
	 * @return double The cost
	 */
	public double getCost() {
		return cost;
	}
	
	@Override
	public boolean equals(Object other) {
		return state.equals(((State<?>)other).state);
	}
	
	@Override
	public String toString() {
		String prev;
		if(cameFrom==null)
			prev = "null";
		else
			prev = "" + cameFrom.state;
		return "{" + state + "," + cost + "," + prev + "}";
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
