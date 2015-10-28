package common;

import java.io.InputStream;
import java.io.OutputStream;

public class Client {
	private int id;
	private boolean running;
	private InputStream in;
	private OutputStream out;
	private int solved;
	private int pending;
	private int solving;
	private int noSolution;
	
	public Client(int id, InputStream in, OutputStream out) {
		this.id = id;
		this.in = in;
		this.out = out;
		running = true;
	}
	
	public int getId() {
		return id;
	}

	public InputStream getIn() {
		return in;
	}

	public OutputStream getOut() {
		return out;
	}

	public boolean getRunning() {
		return running;
	}
	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getPending() {
		return pending;
	}
	public void incrementPending() {
		++pending;
	}

	public int getSolving() {
		return solving;
	}
	public void incrementSolving() {
		--pending;
		++solving;
	}

	public int getSolved() {
		return solved;
	}
	public void incrementSolved() {
		--solving;
		++solved;
	}

	public int getNoSolution() {
		return noSolution;
	}
	public void incrementNoSolution() {
		--solving;
		++noSolution;
		// TODO Auto-generated method stub
		
	}
}
