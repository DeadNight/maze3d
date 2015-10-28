package common;

public class ServerStats {
	private int pending;
	private int solving;
	private int solved;
	private int noSolution;

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
}
