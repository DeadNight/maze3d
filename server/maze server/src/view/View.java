package view;

import common.Client;
import common.Properties;
import common.ServerStats;

/**
 * @author Nir Leibovitch
 * <h1>View layer Façade</h1>
 */
public interface View {
	/**
	 * Start the view &amp; user interaction with the application
	 */
	void start();
	/**
	 * Stop the view, dispose any resources, cancel any pending threads &amp; abort any running
	 * threads
	 */
	void stop();
	/**
	 * Notify the user that the server is shutting down
	 */
	void displayShuttingDown();
	
	/**
	 * Get the last user command
	 * @return String User command
	 */
	String getUserCommand();
	/**
	 * Inform the user that a command could not be recognized
	 */
	void displayUnknownCommand();
	
	/**
	 * Notify the user that a client has connected to the server
	 * @param client Client data
	 */
	void displayClientConnected(Client client);
	/**
	 * Notify the user that a client has disconnected from the server
	 * @param clientId Client id
	 */
	void displayClientDisconnected(int clientId);
	/**
	 * Notify the user that a client's statistics has been updated
	 * @param client Updated client data
	 */
	void updateClient(Client client);
	/**
	 * Notify the user that the server statistics has been updated
	 * @param serverStats Updated server statistics
	 */
	void updateServerStats(ServerStats serverStats);
	
	/**
	 * Display the given properties to the user
	 * @param properties Properties
	 */
	void displayProperties(Properties properties);
	/**
	 * Inform the user that properties were saved successfully
	 */
	void displayPropertiesSaved();
	/**
	 * Inform the user that a file could not be found
	 */
	void displayFileNotFound();
}
