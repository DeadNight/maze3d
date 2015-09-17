package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import controller.Controller;

public class CLI extends Thread {
	Controller controller;
	private BufferedReader in;
	private PrintWriter out;
	HashSet<String> commands;
	
	public CLI(Controller controller, BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
		this.controller = controller;
	}
	
	public void setCommands(HashSet<String> commands) {
		this.commands = commands;
	}

	@Override
	public void run() {
		String line = "";
		try {
			out.print("> ");
			out.flush();
			while(!(line = in.readLine()).equals("exit")) {
				String commandName = line;
				while(!commandName.isEmpty()) {
					if(commands.contains(commandName)) {
						String[] args;
						if(commandName.equals(line))
							args = new String[0];
						else
							args = line.replace(commandName + " ", "").split(" ");
						controller.doCommand(commandName, args);
						break;
					} else {
						// remove last word
						commandName = commandName.replaceFirst("^\\S*$| \\S*$", "");
					}
				}
				
				if(commandName.isEmpty()) {
					out.println("unknown command");
					out.flush();
				}
				
				out.print("> ");
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		controller.stop();
		out.print("good bye!");
		out.flush();
	}

	public void display() {
		out.print("");
		out.flush();
	}

	public void display(Object obj) {
		out.print(obj);
		out.flush();
	}

	public void displayLine() {
		out.println("");
		out.flush();
	}

	public void displayLine(Object obj) {
		out.println(obj);
		out.flush();
	}
	
	public void displayLines(Object[] objs) {
		out.println(String.join(System.lineSeparator(), (String[])objs));
	}
}
