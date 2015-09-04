package algorithms.mazeGenerators;

import java.io.IOException;

/**
 * @author Nir Leibovitch
 * <h1>n-dimensional metric</h1>
 * Represents a n-dimensional metric
 */
public interface Metric {
	byte[] toByteArray() throws IOException;
}
