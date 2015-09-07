package controller;

import model.Model;
import view.View;

public interface Controller {
	public void setModel(Model model);
	public void setView(View view);
	public void doCommand(String command, String[] args);
	public void displayError(String[] strings);
	public void displayFiles(String[] list);
	public void displayWrongArguments(String format);
	public void displayAsyncMessage(String[] strings);
}
