package model;

import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import common.Client;

public interface Model {
	boolean start(int port, int numOfClients, int socketTimeout, int poolSize);
	void stop();
	Client getClient(int id);
	void setMazeSearchAlgorithm(Searcher<Position> mazeSearchAlgorithm);
}
