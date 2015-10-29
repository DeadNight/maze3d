package boot;

import java.io.IOException;

import algorithms.mazeGenerators.CommonMetric;

/**
 * @author Nir Leibovitch
 * <h1>A 2-dimensional position</h1>
 * Represents a 2d position in x,y coordinates
 */
public class Position extends CommonMetric {
	private static final long serialVersionUID = 6843649468736667199L;

	/**
	 * Initialize a new Position with 0 x &amp; y coordinates
	 */
	public Position() {
		super(2);
	}
	
	/**
	 * Initialize a new Position with specific x,y coordinates
	 * @param x The x coordinate
	 * @param y The y coordinate
	 */
	public Position(int x, int y) {
		super(new int[]{x, y});
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
		getMetrics()[0] = x;
	}
	
	/**
	 * Set the y coordinate
	 * @param y The new y coordinate
	 */
	public void setY(int y) {
		getMetrics()[1] = y;
	}
	
	/**
	 * Get the x coordinate
	 * @return int The x coordinate
	 */
	public int getX() {
		return getMetrics()[0];
	}	

	/**
	 * Get the y coordinate
	 * @return int The y coordinate
	 */
	public int getY() {
		return getMetrics()[1];
	}

	/**
	 * Move the position right by 1 (towards positive x axis)
	 * @return Position This instance after moving it
	 */
	public Position moveRight() {
		++getMetrics()[0];
		return this;
	}

	/**
	 * Move the position left by 1 (towards negative x axis)
	 * @return Position This instance after moving it
	 */
	public Position moveLeft() {
		--getMetrics()[0];
		return this;
	}

	/**
	 * Move the position up by 1 (towards positive y axis)
	 * @return Position This instance after moving it
	 */
	public Position moveUp() {
		--getMetrics()[1];
		return this;
	}

	/**
	 * Move the position down by 1 (towards negative y axis)
	 * @return Position This instance after moving it
	 */
	public Position moveDown() {
		++getMetrics()[1];
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Position)) return false;
		return super.equals(obj);
	}
}
