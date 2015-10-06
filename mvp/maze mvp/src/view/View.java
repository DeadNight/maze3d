package view;

import presenter.Properties;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public interface View {
	String getUserCommand();
	
	void start();
	void stop();

	void displayMaze(byte[] compressedMazeData);

	void displayMazeNotFound();

	void displayUnknownCommand();

	void displayCommandTemplate(String commandName, String template);

	void displayFilesList(String[] filesList);

	void displayDirectoryNotFound();

	void displayNotDirectory();

	void displayGetMazeError();

	void displayMazeGenerated(String name);

	void displayCrossSection(int[][] crossSection);

	void displayIndexOutOfRange();

	void displaySaveMazeError();

	void displayMazeSaved();

	void displayMazeLoaded(String name);

	void displayLoadMazeError();

	void displayFileNotFound();

	void displayMazeSize(int mazeSize);

	void displayDecompressionError();

	void displayFileSize(int fileSize);

	void displayInvalidAxis();

	void displayMazeSolved(String name);

	void displaySolveMazeError(String name);

	void displayMazeSolutionNotFound();

	void displayMazeSolution(Solution<Position> solution);

	void displayFileNameError();

	void displayProperties(Properties properties);
}
