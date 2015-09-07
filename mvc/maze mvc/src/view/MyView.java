package view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import controller.Command;
import controller.Controller;

public class MyView extends CommonView {
	CLI cli;
	HashMap<String, Command> commands;
	
	public MyView(Controller controller) {
		super(controller);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
		cli = new CLI(controller, in, out);
	}
	
	@Override
	public void setCommands(HashSet<String> commands) {
		super.setCommands(commands);
		cli.setCommands(commands);
	}

	@Override
	public void start() {
		cli.start();
	}

	@Override
	public void displayError(String[] strings) {
		displayLine(strings);
	}

	@Override
	public void displayFiles(String[] list) {
		displayLine(list);
	}

	@Override
	public void displayAsyncMessage(String[] strings) {
		cli.displayLine("");
		displayLine(strings);
		cli.display("> ");
	}
	
	private void displayLine(String[] strings) {
		cli.displayLine(String.join(System.lineSeparator(), strings));
	}
}
