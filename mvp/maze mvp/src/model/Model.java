package model;

import java.io.IOException;

import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public interface Model {
	void loadProperties() throws ArrayIndexOutOfBoundsException, IOException;
	byte[] getPropertiesData();
	
	void setMazeGenerator(Maze3dGenerator mazeGenerator);
	void setMazeSearchAlgorithm(Searcher<Position> mazeSearchAlgorithm);
	
	void start(int poolSize);
	void stop();
	
	void listFiles(String path);
	String[] getFilesList();
	
	void generateMaze(String name, int width, int height, int depth);
	byte[] getMazeData(String name) throws IOException;

	void generateCrossSection(String name, String axis, int index);
	int[][] getCrossSection();

	void saveMaze(String name, String fileName);

	void loadMaze(String fileName, String name);

	void calculateMazeSize(String name);
	int getMazeSize(String name);

	void calculateFileSize(String name);
	int getFileSize(String name);

	void solveMaze(String name);
	Solution<Position> getMazeSolution(String name);
}
