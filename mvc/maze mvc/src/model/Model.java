package model;

/**
 * @author nirleibo
 * <h1>Model layer Façade</h1>
 */
public interface Model {
	/**
	 * Abort any threads waiting for execution, and block until all currently
	 * executing threads finish their execution
	 */
	void stop();
	
	/**
	 * Calculate the list of files under the given directory path
	 * @param path Path of a directory
	 */
	void calculateFileList(String path);

	/**
	 * Generate a new 3d maze
	 * @param name Name for the new maze
	 * @param width Width of the new maze
	 * @param height Height of the new maze
	 * @param depth Depth of the new maze
	 */
	void generate3dMaze(String name, int width, int height, int depth);

	/**
	 * Get a named 3d maze
	 * @param name Name of the 3d maze 
	 */
	void get3dMaze(String name);

	/**
	 * Get a 2d cross section of a 3d maze
	 * @param name Name of the maze
	 * @param axis Axis of the cross section
	 * @param index 0-based index of the cross section
	 */
	void getCrossSection(String name, String axis, int index);

	/**
	 * Save a named maze in the given file
	 * @param name Name of the maze
	 * @param fileName Path + name of the file
	 */
	void sazeMaze(String name, String fileName);

	/**
	 * Load a maze from a given file and give it a name
	 * @param fileName Path + name of the file
	 * @param name Name for the loaded maze
	 */
	void loadMaze(String fileName, String name);

	/**
	 * Calculate the size of a named maze
	 * @param name Name of the maze
	 */
	void calculateMazeSize(String name);

	/**
	 * Calculate the compressed size of a named maze
	 * @param name Name of the maze
	 */
	void calculateFileSize(String name);

	/**
	 * Solve a named maze using the named algorithm
	 * @param name Name of the maze
	 * @param algorithmName Name of the algorithm
	 */
	void solveMaze(String name, String algorithmName);

	/**
	 * Get the solution for a named maze 
	 * @param name Name of the maze
	 */
	void getSolution(String name);
}
