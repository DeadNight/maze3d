package controller;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import model.Model;
import view.View;

/**
 * @author nirleibo
 * <h1>Controller layer Façade</h1>
 */
public interface Controller {
	/**
	 * Stop the application
	 */
	void stop();
	/**
	 * Set the Model Façade instance
	 * @param model The Model Façade instance
	 */
	void setModel(Model model);
	/**
	 * Set the View Façade instance
	 * @param view The View Façade instance
	 */
	void setView(View view);
	/**
	 * Invoke the named command using the specified arguments
	 * @param command The name of the command to invoke
	 * @param args Arguments for the command
	 */
	void doCommand(String command, String[] args);
	/**
	 * Display an error message
	 * @param error The error message
	 */
	void displayError(String error);
	/**
	 * Display a list of files
	 * @param list The file list
	 */
	void displayFilesList(String[] list);
	/**
	 * Display that a 3d maze was generated
	 * @param name The name of the ready maze
	 */
	void display3dMazeReady(String name);
	/**
	 * Display a 3d maze
	 * @param mazeData The compressed maze data
	 */
	void display3dMaze(byte[] mazeData);
	/**
	 * Display a maze cross section
	 * @param crossSection The cross section data
	 */
	void displayCrossSection(int[][] crossSection);
	/**
	 * Display that a maze was saved
	 * @param name The name of the saved maze
	 */
	void display3dMazeSaved(String name);
	/**
	 * Display that a maze was loaded
	 * @param name The name of the loaded maze
	 */
	void display3dMazeLoaded(String name);
	/**
	 * Display the size of a maze
	 * @param size The size of the maze
	 */
	void displayMazeSize(int size);
	/**
	 * Display the compressed size of a maze
	 * @param length The compressed size of the maze
	 */
	void displayFileSize(int length);
	/**
	 * Display that a maze was solved
	 * @param name The name of the maze for which the solution is ready
	 */
	void displaySolutionReady(String name);
	/**
	 * Display the solution for a maze
	 * @param solution The solution
	 */
	void displaySolution(Solution<Position> solution);
}
