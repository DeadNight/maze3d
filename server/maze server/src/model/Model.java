package model;

import common.Client;

public interface Model {
	boolean start(int port, int numOfClients, int socketTimeout);
	void stop();
	Client getClient(int id);
}
