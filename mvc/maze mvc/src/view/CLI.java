package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import controller.Command;

public class CLI extends Thread {
	private BufferedReader in;
	private PrintWriter out;
	HashMap<String, Command> commands;
	
	public CLI(BufferedReader in, PrintWriter out, HashMap<String, Command> commands) {
		this.in = in;
		this.out = out;
		this.commands = commands;
	}
	
	@Override
	public void run() {
		String line = "";
		Command command;
		try {
			out.print("> ");
			out.flush();
			while(!(line = in.readLine()).equals("exit")) {
				String commandName = line;
				while(!commandName.isEmpty()) {
					if((command = commands.get(commandName)) != null) {
						String[] args;
						if(commandName.equals(line))
							args = new String[0];
						else
							args = line.replace(commandName + " ", "").split(" ");
						command.doCommand(args);
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
		out.println(string);
		out.flush();
	}
}
