package view;

import java.util.Observable;

public abstract class CommonView extends Observable implements View {
	String userCommand;

	protected void notifyCommand(String command) {
		userCommand = command;
		setChanged();
		notifyObservers();
	}
}
