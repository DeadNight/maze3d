package common;

/**
 * @author Nir Leibovitch
 * <h1>Representation of the server statistics</h1>
 */
public class ServerStats {
	private int connected;
	private int pending;
	private int solving;
	private int solved;
	private int noSolution;
	private int cached;
	
	/**
	 * Get the amount of connected clients
	 * @return int Amount of connected clients
	 */
	public int getConnected() {
		return connected;
	}
	/**
	 * Set the amount of connected clients
	 * @param connected Amount of connected clients
	 */
	public void setConnected(int connected) {
		this.connected = connected;
	}

	/**
	 * Get the amount of pending requests
	 * @return int Amount of pending requests
	 */
	public int getPending() {
		return pending;
	}
	/**
	 * Increment the amount of pending requests
	 */
	public void incrementPending() {
		++pending;
	}
	
	/**
	 * Get the amount of requests being solved right now
	 * @return int Amount of requests being solved
	 */
	public int getSolving() {
		return solving;
	}
	/**
	 * Increment the amount of requests being solved.
	 * <br>Will also decrease the amount of pending requests.
	 */
	public void incrementSolving() {
		--pending;
		++solving;
	}
	
	/**
	 * Get the amount of requests that were solved
	 * @return int Amount of requests that were solved
	 */
	public int getSolved() {
		return solved;
	}
	/**
	 * Increment the amount of requests that were solved.
	 * <br>Will also decrease the amount of requests being solved.
	 */
	public void incrementSolved() {
		--solving;
		++solved;
	}
	
	/**
	 * Get the amount of requests that could not be solved
	 * @return int Amount of requests that could not be solved
	 */
	public int getNoSolution() {
		return noSolution;
	}
	/**
	 * Increment the amount of that could not be being solved.
	 * <br>Will also decrease the amount of requests being solved.
	 */
	public void incrementNoSolution() {
		--solving;
		++noSolution;
	}
	
	/**
	 * Get the amount of cached solutions
	 * @return int Amount of cached solutions
	 */
	public int getCached() {
		return cached;
	}
	/**
	 * Set the amount of cached solutions
	 * @param cached Amount of cached solutions
	 */
	public void setCached(int cached) {
		this.cached = cached;
	}
}
