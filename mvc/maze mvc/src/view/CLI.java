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
			while((line = in.readLine()).intern() != "exit") {
				if((command = commands.get(line)) != null) {
					runCommandInThread(command);
				} else {
					out.println("unknown command");
					out.flush();
				}
				out.print("> ");
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println("bye!");
		out.flush();
	}
	
	private void runCommandInThread(Command command){
		new Thread(new Runnable() {
			public void run() {
				command.doCommand();
			}
		}).start();
	}
}
