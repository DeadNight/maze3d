package view;

import java.util.HashMap;

import controller.Command;

public interface View {
	void start();
	void setCommands(HashMap<String, Command> commands);
	void displayError(String[] strings);
	void displayFiles(String[] list);
}
