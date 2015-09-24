package controller;

/**
 * @author nirleibo
 * <h1>General Command</h1>
 * Represents a command to be invoked when appropriate
 */
public interface Command {
	/**
	 * Override this method to define what the command should do
	 * @param args Arguments for the command
	 */
	void doCommand(String[] args);
}
