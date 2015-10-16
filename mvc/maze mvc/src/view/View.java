package view;

import java.util.HashSet;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * @author Nir Leibovitch
 * <h1>View layer Façade</h1>
 */
public interface View {
	/**
	 * Start the user interface
	 */
	void start();
	/**
	 * Set the list of commands supported by the Controller Façade instance
	 * @param commands List of commands
	 */
	void setCommands(HashSet<String> commands);
	/**
	 * Display an error
	 * @param error Error message
	 */
	void displayError(String error);
	/**
	 * Display a list of files
	 * @param list The list of files
	 */
	void displayFiles(String[] list);
	/**
	 * Display a 3d maze
	 * @param mazeData Maze compressed data
	 */
	void display3dMaze(byte[] mazeData);
	/**
	 * Display a 2d cross section of a 3d maze
	 * @param crossSection Cross section data
	 */
	void displayCrossSection(int[][] crossSection);
	/**
	 * Display the size of a maze
	 * @param size The size
	 */
	void displayMazeSize(int size);
	/**
	 * Display the compressed size of a maze
	 * @param size Compressed size
	 */
	void displayFileSize(int size);
	/**
	 * Display the solution of a maze
	 * @param solution The solution
	 */
	void displaySolution(Solution<Position> solution);
	/**
	 * Display that a maze was generated
	 * @param name Name of the generated maze
	 */
	void displayMazeReady(String name);
	/**
	 * Display that a maze was saved successfully
	 * @param name Name of the saved maze
	 */
	void display3dMazeSaved(String name);
	/**
	 * Display that a maze was loaded successfully
	 * @param name Name of the loaded maze
	 */
	void display3dMazeLoaded(String name);
	/**
	 * Display that a solution is ready for a maze
	 * @param name Name of the maze
	 */
	void displaySolutionReady(String name);
	/**
	 * Display the the application is shutting down
	 */
	void displayShuttingDown();
	/**
	 * Display that the application has shut down
	 */
	void displayShutdown();
}
