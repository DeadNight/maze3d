package controller;

import java.util.HashMap;
import java.util.HashSet;

import model.Model;
import view.View;

public abstract class CommonController implements Controller {
	Model model;
	View view;
	HashMap<String, Command> commands;
	
	public CommonController() {
		commands = new HashMap<String, Command>();
		initCommands();
	}
	
	public void setModel(Model model) {
		this.model = model;
	}
	
	public void setView(View view) {
		this.view = view;
		view.setCommands(new HashSet<String>(commands.keySet()));
	}
	
	protected abstract void initCommands();
	
	public HashMap<String, Command> getCommands() {
		return commands;
	}

	@Override
	public void doCommand(String command, String[] args) {
		commands.get(command).doCommand(args);
	}
}
