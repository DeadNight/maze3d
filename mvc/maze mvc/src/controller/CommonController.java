package controller;

import java.util.HashMap;
import java.util.HashSet;

import model.Model;
import view.View;

/**
 * @author nirleibo
 * <h1>Common implementation of the Controller Façade</h1>
 */
public abstract class CommonController implements Controller {
	Model model;
	View view;
	HashMap<String, Command> commands;
	
	/**
	 * Initialize the Controller Façade instance
	 */
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
	
	/**
	 * Override this to initialize the commands supported by this implementation
	 * of the Controller Façade
	 */
	protected abstract void initCommands();

	@Override
	public void doCommand(String command, String[] args) {
		commands.get(command).doCommand(args);
	}
}
