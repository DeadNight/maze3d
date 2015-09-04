package controller;

import java.util.HashMap;

import model.Model;
import view.View;

public interface Controller {
	public void setModel(Model model);
	public void setView(View view);
	public HashMap<String, Command> getCommands();
}
