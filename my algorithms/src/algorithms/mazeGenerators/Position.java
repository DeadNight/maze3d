package algorithms.mazeGenerators;

import java.io.IOException;

/**
 * @author Nir Leibovitch
 * <h1>A 3-dimensional position</h1>
 * Represents a 3d position in x,y,z coordinates
 */
public class Position extends CommonMetric {
	/**
	 * Initialize a new Position with specific x,y,z coordinates
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 */
	public Position(int x, int y, int z) {
		super(new int[]{x, y, z});
	}

	/**
	 * Copy another position
	 * @param other The instance to copy
	 */
	public Position(Position other) {
		super(other);
	}

	public Position(byte[] data) throws IOException {
		super(data);
	}

	/**
	 * Set the x coordinate
	 * @param x The new x coordinate
	 */
	public void setX(int x) {
		metrics[0] = x;
	}
	
	/**
	 * Set the y coordinate
	 * @param y The new y coordinate
	 */
	public void setY(int y) {
		metrics[1] = y;
	}
	
	/**
	 * Set the z coordinate
	 * @param z The new z coordinate
	 */
	public void setZ(int z) {
		metrics[2] = z;
	}
	
	/**
	 * Get the x coordinate
	 * @return int The x coordinate
	 */
	public int getX() {
		return metrics[0];
	}	

	/**
	 * Get the y coordinate
	 * @return int The y coordinate
	 */
	public int getY() {
		return metrics[1];
	}

	/**
	 * Get the z coordinate
	 * @return int The z coordinate
	 */
	public int getZ() {
		return metrics[2];
	}
	
	/**
	 * Move the position by 1 to a specific direction.
	 * <p>
	 * Delegated to the appropriate MoveD() method
	 * @param direction The direction to move
	 * <p>
	 * right | left | up | down | forward | back
	 * @return Position This instance after moving it
	 */
	public Position move(String direction) {
		switch(direction) {
		case "right":
			moveRight();
			break;
		case "left":
			moveLeft();
			break;
		case "up":
			moveUp();
			break;
		case "down":
			moveDown();
			break;
		case "forward":
			moveForward();
			break;
		case "back":
			moveBack();
			break;
		}
		return this;
	}

	/**
	 * Move the position right by 1 (towards positive x axis)
	 * @return Position This instance after moving it
	 */
	public Position moveRight() {
		++metrics[0];
		return this;
	}

	/**
	 * Move the position left by 1 (towards negative x axis)
	 * @return Position This instance after moving it
	 */
	public Position moveLeft() {
		--metrics[0];
		return this;
	}

	/**
	 * Move the position up by 1 (towards positive y axis)
	 * @return Position This instance after moving it
	 */
	public Position moveUp() {
		++metrics[1];
		return this;
	}

	/**
	 * Move the position down by 1 (towards negative y axis)
	 * @return Position This instance after moving it
	 */
	public Position moveDown() {
		--metrics[1];
		return this;
	}

	/**
	 * Move the position forward by 1 (towards negative z axis)
	 * @return Position This instance after moving it
	 */
	public Position moveForward() {
		--metrics[2];
		return this;
	}

	/**
	 * Move the position back by 1 (towards positive z axis)
	 * @return Position This instance after moving it
	 */
	public Position moveBack() {
		++metrics[2];
		return this;
	}
}
