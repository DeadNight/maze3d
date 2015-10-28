package common;

public class ServerStats {
	private int connected;
	private int pending;
	private int solving;
	private int solved;
	private int noSolution;
	private int cached;
	
	public int getConnected() {
		return connected;
	}
	public void setConnected(int connected) {
		this.connected = connected;
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
	}
	
	public int getCached() {
		return cached;
	}
	public void setCached(int cached) {
		this.cached = cached;
	}
}
