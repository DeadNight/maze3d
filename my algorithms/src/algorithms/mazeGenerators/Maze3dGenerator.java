package algorithms.mazeGenerators;

/**
 * @author Nir Leibovitch
 * <h1>General 3-dimensional maze generator</h1>
 * Generates 3d mazes of any volume
 */
public interface Maze3dGenerator {
	
	/**
	 * Generate a 3d maze with specific width, height &amp; depth
	 * @param width The width
	 * @param height The height
	 * @param depth The depth
	 * @return Maze3d The generated maze
	 */
	Maze3d generate(int width, int height, int depth);
	/**
	 * Generate a 3d maze with a specific volume
	 * @param volume The volume
	 * @return Maze3d The generated maze
	 */
	Maze3d generate(Volume volume);
	
	/**
	 * Measure the time taken to create a 3d maze with specific width, height &amp; depth
	 * @param width The width
	 * @param height The height
	 * @param depth The depth
	 * @return String Formatted time taken
	 */
	String measureAlgorithmTime(int width, int height, int depth);
	/**
	 * Measure the time taken to create a 3d maze with a specific volume
	 * @param volume The volume
	 * @return String Formatted time taken
	 */
	String measureAlgorithmTime(Volume volume);
}
