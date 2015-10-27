package common;

import java.io.InputStream;
import java.io.OutputStream;

public class Client {
	private int id;
	private String lastCommand;
	
	InputStream in;
	OutputStream out;
	
	public Client(int id, InputStream in, OutputStream out) {
		this.id = id;
		lastCommand = "";
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
	
	public String getLastCommand() {
		return lastCommand;
	}
	public void setLastCommand(String lastCommand) {
		this.lastCommand = lastCommand;
	}
}
