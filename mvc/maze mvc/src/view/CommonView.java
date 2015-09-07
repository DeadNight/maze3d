package view;

import java.util.HashSet;

import controller.Controller;

public abstract class CommonView implements View {
	Controller controller;
	HashSet<String> commands;
	
	public CommonView(Controller controller) {
		this.controller = controller;
	}
	
	@Override
	public void setCommands(HashSet<String> commands) {
		this.commands = commands;
	}
}
