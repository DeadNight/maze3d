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

/**
 * @author Nir Leibovitch
 * <h1>Model layer Façade</h1>
 */
public interface Model {
	boolean start(int port, int numOfClients, int socketTimeout, int poolSize);
	void stop();
	Client getClient(int id);
	/**
	 * Set the maze searcher algorithm to be used by the model Façade instance
	 * @param mazeSearcher Maze searcher
	 */
	void setMazeSearchAlgorithm(Searcher<Position> mazeSearcher);
	/**
	 * Get the statistics of the server
	 * @return ServerStats Statistics of the server
	 */
	ServerStats getServerStats();
	
	/**
	 * Load properties file &amp; instantiate a {@link Properties object}
	 * @param fileName Path to the properties file
	 * @throws URISyntaxException When the path can't be parsed
	 * @throws FileNotFoundException When the file can't be opened for reading
	 * @throws IOException When the cannot be read into properties
	 * @see Model#getProperties()
	 */
	void loadProperties(String fileName) throws URISyntaxException, FileNotFoundException, IOException;
	/**
	 * Get the properties object instantiated by {@link Model#loadProperties(String)}
	 * @return Properties Properties object
	 * @see Model#loadProperties(String)
	 */
	Properties getProperties();
	/**
	 * Instantiate a Properties object with the given data &amp; save it to the given file path
	 * @param fileName File path
	 * @param port Port the server should listen to
	 * @param numOfClients Number of concurrent clients
	 * @param socketTimeout Time in milliseconds before socket timeout
	 * @param poolSize Size of the thread pool
	 * @param searcher Type of maze searcher to use
	 */
	void saveProperties(String fileName, int port, int numOfClients, int socketTimeout, int poolSize, MazeSearcherTypes searcher);
}
