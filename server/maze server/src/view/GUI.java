package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import common.Client;

public class GUI extends CommonView {
	MazeServerWindow window;
	
	public GUI() {
		window = new MazeServerWindow();
		
		window.addExitListener(new Listener() {
			@Override
			public void handleEvent(Event e) {
				e.doit = false;
				if(window.displayQuestion("Confirm exit", "Do you want to exit?") == SWT.YES)
					notifyCommand("exit");
			}
		});
	}

	@Override
	public void start() {
		window.run();
	}

	@Override
	public void stop() {
		window.close();
	}

	@Override
	public String getUserCommand() {
		return userCommand;
	}

	@Override
	public void displayUnknownCommand() {
		window.displayError("Protocol error", "Unknown command");
	}

	@Override
	public void displayShuttingDown() {
		//TODO: disable window controls & let user know we're waiting
	}

	@Override
	public void displayClientConnected(Client client) {
		window.addClient(client);
	}

	@Override
	public void displayClientDisconnected(int clientId) {
		window.removeClient(clientId);
	}

	@Override
	public void displayClientUpdated(Client client) {
		window.updateClient(client);
	}
}
