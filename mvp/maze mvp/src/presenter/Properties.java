package presenter;

import java.io.Serializable;

public class Properties implements Serializable {
	private static final long serialVersionUID = -3069362332373746033L;
	int threadPoolSize;
	MazeGeneratorTypes mazeGeneratorType;
	MazeSearcherTypes mazeSearcherType;
	ViewTypes viewType;

	public int getPoolSize() {
		return threadPoolSize;
	}
	
	public void setPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
	public MazeSearcherTypes getMazeSearcherType() {
		return mazeSearcherType;
	}
	
	public void setMazeSearcherType(MazeSearcherTypes mazeSearcherType) {
		this.mazeSearcherType = mazeSearcherType;
	}
	
	public MazeGeneratorTypes getMazeGeneratorType() {
		return mazeGeneratorType;
	}
	
	public void setMazeGeneratorType(MazeGeneratorTypes mazeGeneratorType) {
		this.mazeGeneratorType = mazeGeneratorType;
	}
	
	public ViewTypes getViewType() {
		return viewType;
	}

	public void setViewType(ViewTypes viewType) {
		this.viewType = viewType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Properties)) return false;
		Properties other = (Properties) obj;
		if(other.threadPoolSize != threadPoolSize) return false;
		if(other.mazeGeneratorType != mazeGeneratorType) return false;
		if(other.mazeSearcherType != mazeSearcherType) return false;
		if(other.viewType != viewType) return false;
		return true;
	};
}
