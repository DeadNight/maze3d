package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;

import controller.Controller;

/**
 * @author Nir Leibovitch
 * <h1>Command Line Interface</h1>
 * Get commands from the user and display results using the command line
 * The command "exit" will stop the application
 */
public class CLI extends Thread {
	Controller controller;
	private BufferedReader in;
	private PrintWriter out;
	HashSet<String> commands;
	
	/**
	 * Initialize the CLI
	 * @param controller Controller Façade instance
	 * @param in Input reader
	 * @param out Output writer
	 */
	public CLI(Controller controller, BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
		this.controller = controller;
	}
	
	/**
	/**
	 * Set the list of commands supported by the Controller Façade instance
	 * @param commands List of commands
	 */
	public void setCommands(HashSet<String> commands) {
		this.commands = commands;
	}

	@Override
	public void run() {
		String line = "";
		try {
			out.print("> ");
			out.flush();
			// The command "exit" will stop the application
			while(!(line = in.readLine()).equals("exit")) {
				String commandName = line.trim().replaceAll("\\s\\s+", " ");
				// if the command is empty, ignore it
				if(commandName.isEmpty())
					continue;
				
				/*
				 * slice the command backwards and try to find a matching command
				 * supported by the Controller Façade instance
				 */
				while(!commandName.isEmpty()) {	
					if(commands.contains(commandName)) {
						String[] args;
						if(commandName.equals(line))
							args = new String[0];
						else
							args = line.replace(commandName + " ", "").split(" ");
						// invoke the command
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
		
		// stop the application
		controller.stop();
		out.print("good bye!");
		out.flush();
	}

	/**
	 * Display a message to the user
	 * @param message The message
	 */
	public void display(Object message) {
		out.print(message);
		out.flush();
	}

	/**
	 * Display a line break to the user
	 */
	public void displayLine() {
		out.println("");
		out.flush();
	}

	/**
	 * Display a message to the user, followed by a line break
	 * @param message The message
	 */
	public void displayLine(Object message) {
		out.println(message);
		out.flush();
	}
}
