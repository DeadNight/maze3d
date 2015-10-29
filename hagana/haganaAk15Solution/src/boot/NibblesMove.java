package boot;

/**
 * @author Nit Leibovitch
 * <h1>Possible move in a Nibbles game</h1>
 * Represent a possible move in a Nibbles game
 */
public class NibblesMove {
	private Position origin;
	private String direction;
	private Position destination;
	private double cost;
	
	/**
	 * Initialize the possible move from given values
	 * @param origin The origin of the move
	 * @param direction The direction of the move
	 * @param destination The destination of the move
	 * @param cost The cost of the move
	 */
	public NibblesMove(Position origin, String direction, Position destination, double cost) {
		this.origin = origin;
		this.direction = direction;
		this.destination = destination;
		this.cost = cost;
	}

	/**
	 * Get the origin of the move
	 * @return Position The origin
	 */
	public Position getOrigin() {
		return origin;
	}

	/**
	 * Get the direction of the move
	 * @return String The direction
	 */
	public String getDirection() {
		return direction;
	}

	/**
	 * Get the destination of the move
	 * @return Position The destination
	 */
	public Position getDestination() {
		return destination;
	}

	/**
	 * Get the cost of the move
	 * @return double The cost
	 */
	public double getCost() {
		return cost;
	}
}
