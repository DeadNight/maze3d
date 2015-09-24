package view;

import java.util.HashSet;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;


public interface View {
	void start();
	void setCommands(HashSet<String> commands);
	void displayError(String error);
	void displayFiles(String[] list);
	void displayMessage(String message);
	void display3dMaze(byte[] mazeData);
	void displayCrossSection(int[][] crossSection);
	void displayMazeSize(int size);
	void displayFileSize(int size);
	void displaySolution(Solution<Position> solution);
	void displayMazeReady(String name);
	void display3dMazeSaved(String name);
	void display3dMazeLoaded(String name);
	void displaySolutionReady(String name);
}
