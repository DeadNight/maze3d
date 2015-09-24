package controller;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import model.Model;
import view.View;

public interface Controller {
	void stop();
	void setModel(Model model);
	void setView(View view);
	void doCommand(String command, String[] args);
	void displayError(String error);
	void displayWrongArguments(String format);
	void displayFilesList(String[] list);
	void display3dMazeReady(String name);
	void display3dMaze(byte[] mazeData);
	void displayCrossSection(int[][] crossSection);
	void display3dMazeSaved(String name);
	void display3dMazeLoaded(String name);
	void displayMazeSize(int size);
	void displayFileSize(int length);
	void displaySolutionReady(String name);
	void displaySolution(Solution<Position> solution);
}
