package model;

import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import common.Client;
import common.ServerStats;

public interface Model {
	boolean start(int port, int numOfClients, int socketTimeout, int poolSize);
	void stop();
	Client getClient(int id);
	/**
	 * Set the maze searcher algorithm to be used by the model Façade instance
	 * @param mazeSearcher Maze searcher
	 */
	void setMazeSearchAlgorithm(Searcher<Position> mazeSearcher);
	ServerStats getServerStats();
}
