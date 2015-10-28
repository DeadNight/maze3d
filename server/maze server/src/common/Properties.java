package common;

import java.io.Serializable;

/**
 * @author Nir Leibovitch
 * <h1>Application properties</h1>
 * Serializable properties for saving &amp; loading 
 */
public class Properties implements Serializable {
	private static final long serialVersionUID = -3274497332274620414L;
	int threadPoolSize;
	MazeSearcherTypes mazeSearcherType;

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
	 * Get the configured maze searcher type
	 * @return MazeSearcherTypes Maze searcher type
	 */
	public MazeSearcherTypes getMazeSearcherType() {
		return mazeSearcherType;
	}
	
	/**
	 * Set the configured maze searcher type
	 * @param mazeSearcherType Maze searcher type
	 */
	public void setMazeSearcherType(MazeSearcherTypes mazeSearcherType) {
		this.mazeSearcherType = mazeSearcherType;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Properties)) return false;
		Properties other = (Properties) obj;
		if(other.threadPoolSize != threadPoolSize) return false;
		if(other.mazeSearcherType != mazeSearcherType) return false;
		return true;
	};
}
