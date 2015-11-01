package common;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Nir Leibovitch
 * <h1>Representation of a client</h1>
 */
public class Client {
	private int id;
	private boolean running;
	private InputStream in;
	private OutputStream out;
	private int solved;
	private int pending;
	private int solving;
	private int noSolution;
	
	/**
	 * Create a new client with the given id, input &amp; output streams
	 * @param id Client id
	 * @param in Client input stream
	 * @param out Client output stream
	 */
	public Client(int id, InputStream in, OutputStream out) {
		this.id = id;
		this.in = in;
		this.out = out;
		running = true;
	}
	
	/**
	 * Get the client id
	 * @return int Client id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Get the client input stream
	 * @return InputStream Client input stream
	 */
	public InputStream getIn() {
		return in;
	}

	/**
	 * Get the client output stream
	 * @return InputStream Client output stream
	 */
	public OutputStream getOut() {
		return out;
	}

	/**
	 * Get an indication if the client should still run
	 * @return boolean Whether the client should still run
	 */
	public boolean getRunning() {
		return running;
	}
	/**
	 * Set the indication if the client should still run
	 * @param running Whether the client should still run
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Get the amount of pending requests for this user
	 * @return int Amount of pending requests
	 */
	public int getPending() {
		return pending;
	}
	/**
	 * Increment the amount of pending requests for this user
	 */
	public void incrementPending() {
		++pending;
	}

	/**
	 * Get the amount of requests being solved right now for this user
	 * @return int Amount of requests being solved
	 */
	public int getSolving() {
		return solving;
	}
	/**
	 * Increment the amount of requests being solved for this user.
	 * <br>Will also decrease the amount of pending requests.
	 */
	public void incrementSolving() {
		--pending;
		++solving;
	}

	/**
	 * Get the amount of requests that were solved for this user
	 * @return int Amount of requests that were solved
	 */
	public int getSolved() {
		return solved;
	}
	/**
	 * Increment the amount of requests that were solved for this user.
	 * <br>Will also decrease the amount of requests being solved.
	 */
	public void incrementSolved() {
		--solving;
		++solved;
	}

	/**
	 * Get the amount of requests that could not be solved for this user
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
}
