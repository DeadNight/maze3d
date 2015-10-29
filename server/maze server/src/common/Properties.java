package common;

import java.io.Serializable;

/**
 * @author Nir Leibovitch
 * <h1>Application properties</h1>
 * Serializable properties for saving &amp; loading 
 */
public class Properties implements Serializable {
	private static final long serialVersionUID = -3274497332274620414L;
	int port;
	private int numOfClients;
	private int socketTimeout;
	int threadPoolSize;
	MazeSearcherTypes mazeSearcherType;
	
	public int getPort() {
		return port;
	};
	public void setPort(int port) {
		this.port = port;
	}
	
	public int getNumOfClients() {
		return numOfClients;
	}
	public void setNumOfClients(int numOfClients) {
		this.numOfClients = numOfClients;
	}
	
	public int getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

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
		if(other.port != port) return false;
		if(other.numOfClients != numOfClients) return false;
		if(other.socketTimeout != socketTimeout) return false;
		if(other.threadPoolSize != threadPoolSize) return false;
		if(other.mazeSearcherType != mazeSearcherType) return false;
		return true;
	}
}
