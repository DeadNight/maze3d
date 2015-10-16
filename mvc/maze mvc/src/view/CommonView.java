package view;

import java.util.HashSet;

import controller.Controller;

/**
 * @author Nir Leibovitch
 * <h1>Common implementation of the View Façade</h1>
 */
public abstract class CommonView implements View {
	Controller controller;
	HashSet<String> commands;
	
	/**
	 * Initialize the View Façade instance
	 * @param controller Controller Façade instance
	 */
	public CommonView(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void setCommands(HashSet<String> commands) {
		this.commands = commands;
	}
}
