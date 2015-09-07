package view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import controller.Controller;

public class MyView extends CommonView {
	CLI cli;
	
	public MyView(Controller controller) {
		super(controller);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
		cli = new CLI(in, out, controller.getCommands());
	}

	@Override
	public void start() {
		cli.start();
	}

	@Override
	public void displayError(String[] strings) {
		display(strings);
	}

	@Override
	public void displayFiles(String[] list) {
		display(list);
	}
	
	private void display(String[] strings) {
		cli.display(String.join(System.lineSeparator(), strings));
	}
}
