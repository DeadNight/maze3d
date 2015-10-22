package algorithms.mazeGenerators;

import java.io.Serializable;

/**
 * @author Nir Leibovitch
 * <h1>n-dimensional metric</h1>
 * Represents a n-dimensional metric
 */
public interface Metric extends Serializable {
	/**
	 * Convert the metric into a byte array
	 * @return The byte array 
	 */
	byte[] toByteArray();
}
