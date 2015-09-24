package model;

public interface Model {
	void stop();
	
	void calculateFileList(String[] args);

	void generate3dMaze(String[] args);

	void get3dMaze(String[] args);

	void getCrossSection(String[] args);

	void sazeMaze(String[] args);

	void loadMaze(String[] args);

	void mazeSize(String[] args);

	void fileSize(String[] args);

	void solveMaze(String[] args);

	void getSolution(String[] args);
}
