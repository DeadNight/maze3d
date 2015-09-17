package model;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public interface Model {
	void stop();
	
	String[] getFileList(String[] args);

	void generate3dMaze(String[] args);

	byte[] getMaze3d(String[] args);

	int[][] getCrossSection(String[] args);

	void sazeMaze(String[] args);

	void loadMaze(String[] args);

	int mazeSize(String[] args);

	int fileSize(String[] args);

	void solveMaze(String[] args);

	Solution<Position> getSolution(String[] args);
}
