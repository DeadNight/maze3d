package view;

import common.Client;
import common.ServerStats;

public interface View {
	void start();
	void stop();
	void displayShuttingDown();
	
	String getUserCommand();
	void displayUnknownCommand();
	
	void displayClientConnected(Client client);
	void displayClientDisconnected(int clientId);
	void displayClientUpdated(Client client);
	void updateServerStats(ServerStats serverStats);
	void updateClient(Client client);
}
