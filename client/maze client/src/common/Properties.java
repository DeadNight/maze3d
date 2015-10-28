package common;

import java.io.Serializable;

/**
 * @author Nir Leibovitch
 * <h1>Application properties</h1>
 * Serializable properties for saving &amp; loading 
 */
public class Properties implements Serializable {
	private static final long serialVersionUID = -3069362332373746033L;
	int threadPoolSize;
	MazeGeneratorTypes mazeGeneratorType;
	ViewTypes viewType;

	/**
	 * Get the configured thread pool size
	 * @return int Thread pool size
	 */
	public int getPoolSize() {
		return threadPoolSize;
	}
	
	/**
	 * Set the configured thread pool size
	 * @param threadPoolSize Thread pool size
	 */
	public void setPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
	
	/**
	 * Get the configured maze generator type
	 * @return MazeSearcherTypes Maze generator type
	 */
	public MazeGeneratorTypes getMazeGeneratorType() {
		return mazeGeneratorType;
	}
	
	/**
	 * Set the configured maze generator type
	 * @param mazeGeneratorType Maze generator type
	 */
	public void setMazeGeneratorType(MazeGeneratorTypes mazeGeneratorType) {
		this.mazeGeneratorType = mazeGeneratorType;
	}
	
	/**
	 * Get the configured view type
	 * @return ViewTypes View type
	 */
	public ViewTypes getViewType() {
		return viewType;
	}
	
	/**
	 * Set the configured view type
	 * @param viewType View type
	 */
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
		if(other.viewType != viewType) return false;
		return true;
	};
}
