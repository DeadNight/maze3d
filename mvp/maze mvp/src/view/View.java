package view;

import presenter.Properties;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * @author Nir Leibovitch
 * <h1>View layer Façade</h1>
 */
public interface View {
	/**
	 * Start the view &amp; user interaction with the application
	 */
	void start();
	/**
	 * Stop the view, dispose any resources, cancel any pending threads &amp; abort any running
	 * threads
	 */
	void stop();
	
	/**
	 * Get the last user command
	 * @return String User command
	 */
	String getUserCommand();

	/**
	 * Display the given compressed data as a maze to the user
	 * @param compressedMazeData Compressed maze data
	 */
	void displayMaze(byte[] compressedMazeData);

	/**
	 * Inform the user that a maze could not be found
	 */
	void displayMazeNotFound();

	/**
	 * Inform the user that a command could not be recognized
	 */
	void displayUnknownCommand();

	/**
	 * Display the expected template for a command requested with a wrong format
	 * @param commandName Name of the requested command
	 * @param template Expected template
	 */
	void displayCommandTemplate(String commandName, String template);

	/**
	 * Display a list of files to the user
	 * @param filesList List of file paths
	 */
	void displayFilesList(String[] filesList);

	/**
	 * Inform the user that a directory path could not be found
	 */
	void displayDirectoryNotFound();

	/**
	 * Inform the user that a path isn't a directory
	 */
	void displayNotDirectory();

	/**
	 * Inform the user that generating a maze has failed
	 */
	void displayGetMazeError();

	/**
	 * Inform the user that the named  maze was generated successfully
	 * @param name Name of the generated maze
	 */
	void displayMazeGenerated(String name);

	/**
	 * Display a cross section to the user
	 * @param crossSection Cross section data
	 */
	void displayCrossSection(int[][] crossSection);

	/**
	 * Inform the user that an index is out of range
	 */
	void displayIndexOutOfRange();

	/**
	 * Inform the user that saving a maze has failed
	 */
	void displaySaveMazeError();

	/**
	 * Inform the user that a maze was saved successfully
	 */
	void displayMazeSaved();

	/**
	 * Inform the user that the named maze was loaded successfully
	 * @param name Name of the loaded maze
	 */
	void displayMazeLoaded(String name);

	/**
	 * Inform the user that loading a maze has failed
	 */
	void displayLoadMazeError();

	/**
	 * Inform the user that a file could not be found
	 */
	void displayFileNotFound();

	/**
	 * Display a maze size to the user
	 * @param mazeSize Maze size
	 */
	void displayMazeSize(int mazeSize);

	/**
	 * Inform the user that data compression has failed
	 */
	void displayDecompressionError();

	/**
	 * Display a file size to the user
	 * @param fileSize File size
	 */
	void displayFileSize(int fileSize);

	/**
	 * Inform the user that an axis is invalid
	 */
	void displayInvalidAxis();

	/**
	 * Inform the user that the named maze was solved successfully
	 * @param name Name of the solved maze
	 */
	void displayMazeSolved(String name);

	/**
	 * Inform the user that solving the named maze has failed
	 * @param name Name of the maze
	 */
	void displaySolveMazeError(String name);

	/**
	 * Inform the user that a solution for a maze wasn't found
	 */
	void displayMazeSolutionNotFound();

	/**
	 * Display the given solution to the user
	 * @param solution Solution
	 */
	void displayMazeSolution(Solution<Position> solution);

	/**
	 * Inform the user that a file name could not be parsed
	 */
	void displayFileNameError();

	/**
	 * Display the given properties to the user
	 * @param properties Properties
	 */
	void displayProperties(Properties properties);

	/**
	 * Inform the user that properties were saved successfully
	 */
	void displayPropertiesSaved();
}
