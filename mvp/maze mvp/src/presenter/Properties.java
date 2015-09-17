package presenter;

import java.io.Serializable;

public class Properties implements Serializable {
	public enum MazeGenerators {
		SIMPLE,
		MY
	}
	
	public enum MazeSearchAlgorithms {
		BFS,
		AStar_Air,
		AStar_Manhattan
	}
	
	int threadPoolSize;
	MazeGenerators mazeGenerator;
	MazeSearchAlgorithms mazeSearchAlgorithm;
	
	public int getPoolSize() {
		return threadPoolSize;
	}
	
	public void setPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
	public MazeSearchAlgorithms getMazeSearchAlgorithm() {
		return mazeSearchAlgorithm;
	}
	
	public void setMazeSearchAlgorithm(MazeSearchAlgorithms mazeSearchAlgorithm) {
		this.mazeSearchAlgorithm = mazeSearchAlgorithm;
	}
	
	public MazeGenerators getMazeGenerator() {
		return mazeGenerator;
	}
	
	public void setMazeGenerator(MazeGenerators mazeGenerator) {
		this.mazeGenerator = mazeGenerator;
	}
}
