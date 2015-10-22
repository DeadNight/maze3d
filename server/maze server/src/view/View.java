package view;

import common.Client;

public interface View {
	void start();
	void stop();
	void displayShuttingDown();
	
	String getUserCommand();
	void displayUnknownCommand();
	
	void displayClientConnected(Client client);
	void displayClientDisconnected(int clientId);
	void displayClientUpdated(Client client);
}
