package view;

import java.util.HashMap;

import controller.Command;
import controller.Controller;

public abstract class CommonView implements View {
	Controller controller;
	HashMap<String, Command> commands;
	
	public CommonView(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void setCommands(HashMap<String, Command> commands) {
		this.commands = commands;
	}
}
