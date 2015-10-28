package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import common.Client;
import common.MazeSearcherTypes;
import common.Properties;
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
	
	void loadProperties(String fileName) throws URISyntaxException, FileNotFoundException, IOException;
	Properties getProperties();
	void saveProperties(String fileName, int poolSize, MazeSearcherTypes searcher);
}
