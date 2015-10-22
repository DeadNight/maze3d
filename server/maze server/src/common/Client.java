package common;

public class Client {
	private int id;
	private String lastCommand;
	
	public Client(int id) {
		this.id = id;
		lastCommand = "";
	}
	
	public int getId() {
		return id;
	}
	
	public String getLastCommand() {
		return lastCommand;
	}
	public void setLastCommand(String lastCommand) {
		this.lastCommand = lastCommand;
	}
}
