package algorithms.mazeGenerators;

/**
 * @author Nir Leibovitch
 * <h1>3-dimensional volume</h1>
 * Represents 3d volume by width, height &amp; depth
 */
public class Volume extends CommonMetric {
	
	/**
	 * Initialize a new Volume with 0 width, height &amp; depth
	 */
	public Volume() {
		super(3);
	}
	
	/**
	 * Initialize a new Volume with specific width, height &amp; depth
	 * @param width The width
	 * @param height The height
	 * @param depth The depth
	 */
	public Volume(int width, int height, int depth) {
		super(new int[] {width, height, depth});
	}

	/**
	 * Copy another Volume
	 * @param other The instance to copy
	 */
	public Volume(Volume other) {
		super(other);
	}
	
	/**
	 * Set the width
	 * @param width The width
	 */
	public void setWidth(int width) {
		metrics[0] = width;
	}
	
	/**
	 * Get the width
	 * @return int The width
	 */
	public int getWidth() {
		return metrics[0];
	}
	
	/**
	 * Set the height
	 * @param height The height
	 */
	public void setHeight(int height) {
		metrics[1] = height;
	}
	
	/**
	 * Get the height
	 * @return int The height
	 */
	public int getHeight() {
		return metrics[1];
	}
	
	/**
	 * Set the depth
	 * @param depth The depth
	 */
	public void setDepth(int depth) {
		metrics[2] = depth;
	}
	
	/**
	 * Get the depth
	 * @return int The depth
	 */
	public int getDepth() {
		return metrics[2];
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Volume)) return false;
		return super.equals(obj);
	}
}
