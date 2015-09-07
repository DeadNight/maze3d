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
						commandName = commandName.replaceFirst("^\\S*$| \\S*$", "");
					}
				}
				
				if(commandName.isEmpty()) {
					out.println("unknown command");
					out.flush();
				}
				
				//controller.close();
				out.print("> ");
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("bye!");
		out.flush();
	}

	public void display(String string) {
		out.print(string);
		out.flush();
	}

	public void displayLine(String string) {
		out.println(string);
		out.flush();
	}
}
