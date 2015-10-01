package presenter;

import java.io.Serializable;

public class Properties implements Serializable {
	private static final long serialVersionUID = -3069362332373746033L;
	int threadPoolSize;
	MazeGenerators mazeGenerator;
	MazeSearchers mazeSearcher;
	
	public int getPoolSize() {
		return threadPoolSize;
	}
	
	public void setPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
	public MazeSearchers getMazeSearcher() {
		return mazeSearcher;
	}
	
	public void setMazeSearcher(MazeSearchers mazeSolver) {
		this.mazeSearcher = mazeSolver;
	}
	
	public MazeGenerators getMazeGenerator() {
		return mazeGenerator;
	}
	
	public void setMazeGenerator(MazeGenerators mazeGenerator) {
		this.mazeGenerator = mazeGenerator;
	}
}
