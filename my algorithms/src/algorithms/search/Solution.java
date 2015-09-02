package algorithms.search;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Nir Leibovitch
 * <h1>Search problem solution</h1>
 * Represents the sequence of states that make a solution for a search problem
 * @param <T> The type of the internal representation of state
 */
public class Solution<T> {
	ArrayList<State<T>> sequence;
	
	/**
	 * Initialize a Solution with the given linked list of states
	 * @param to The goal state
	 * @param from The initial state
	 */
	public Solution(State<T> to, State<T> from) {
		sequence = new ArrayList<State<T>>();
		State<T> state = to;
		sequence.add(state);
		while(!state.getCameFrom().equals(from)) {
			state = state.getCameFrom();
			sequence.add(state);
		}
		Collections.reverse(sequence);
	}
	
	/**
	 * Get the sequence of states
	 * @return ArrayList&lt;State&lt;T&gt;&gt; The sequence
	 */
	public ArrayList<State<T>> getSequence() {
		return sequence;
	}

	/**
	 * Helper method to print the states in the sequence in order
	 */
	public void print() {
		for(State<?> state : sequence)
			System.out.print(state.getState() + ", ");
		System.out.println("done!");
	}
}
