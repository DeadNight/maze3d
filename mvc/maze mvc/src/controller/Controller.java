package controller;

import model.Model;
import view.View;

public interface Controller {
	void stop();
	void setModel(Model model);
	void setView(View view);
	void doCommand(String command, String[] args);
	void displayError(String error);
	void displayWrongArguments(String format);
	void displayMessage(String message);
}
