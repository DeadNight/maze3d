package view;

import java.util.Observable;

/**
 * @author Nir Leibovitch
 * <h1>Common implementation of the View Façade</h1>
 */
public abstract class CommonView extends Observable implements View {
	String userCommand;

	/**
	 * Utility method to notify the existence of new user commands to the presenter
	 * @param command User command
	 */
	protected void notifyCommand(String command) {
		userCommand = command;
		setChanged();
		notifyObservers();
	}
}
