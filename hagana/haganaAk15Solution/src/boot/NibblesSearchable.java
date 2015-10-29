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
			if(to.equals(getInitialState()))
				if(to.getState().getX() <= 3 && new Position(from.getState()).moveLeft().equals(to))
					to.setCost(move.getCost() + 10);
				else if(to.getState().getY() <= 3 && new Position(from.getState()).moveUp().equals(to))
					to.setCost(move.getCost() + 10);
				else if(to.getState().getX() >= Nibbles.BOARD_SIZE - 4 && new Position(from.getState()).moveRight().equals(to))
					to.setCost(move.getCost() + 10);
				else if(to.getState().getY() >= Nibbles.BOARD_SIZE - 4 && new Position(from.getState()).moveDown().equals(to))
					to.setCost(move.getCost() + 10);
				else
					to.setCost(move.getCost());
			else
				to.setCost(move.getCost());
			states.add(to);
		}
		return states;
	}
}
