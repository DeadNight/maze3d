package boot;

import java.util.ArrayList;

import algorithms.search.CommonSearchable;
import algorithms.search.State;

public class NibblesSearchable extends CommonSearchable<Position> {
	private static final long serialVersionUID = 7726699294074863685L;
	
	Nibbles nibbles;
	State<Position> initialState;
	
	public NibblesSearchable(Nibbles nibbles) {
		this.nibbles = nibbles;
		initialState = new State<Position>(nibbles.getHeadPosition());
	}

	@Override
	public State<Position> getInitialState() {
		return initialState;
	}

	@Override
	public State<Position> getGoalState() {
		return new State<Position>(nibbles.getApplePosition());
	}

	@Override
	public ArrayList<State<Position>> getAllPossibleStates(State<Position> from) {
		ArrayList<State<Position>> states = new ArrayList<State<Position>>();
		for(NibblesMove move : nibbles.getPossibleMoves(from.getState())) {
			State<Position> to = new State<Position>(move.getDestination());
			to.setCameFrom(from);
			to.setCost(move.getCost());
			states.add(to);
		}
		return states;
	}
}
