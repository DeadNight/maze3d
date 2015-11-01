package view;

import java.io.File;
import java.util.StringJoiner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import common.Client;
import common.Properties;
import common.ServerStats;

/**
 * @author Nir Leibovitch
 * <h1>Graphical User Interface implementation of the View Façade</h1>
 */
public class GUI extends CommonView {
	ObjectInitializer objectInitializer;
	MazeServerWindow window;
	
	/**
	 * Initiate the GUI View Façade instance
	 */
	public GUI() {
		objectInitializer = new ObjectInitializer();
		window = new MazeServerWindow();
		
		window.addExitListener(new Listener() {
			@Override
			public void handleEvent(Event e) {
				e.doit = false;
				if(window.displayQuestion("Confirm exit", "Do you want to exit?") == SWT.YES)
					notifyCommand("exit");
			}
		});
		
		window.addEditPropertiesListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				userCommand = "get properties";
				setChanged();
				notifyObservers();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		window.addImportPropertiesListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = window.displayFileDialog(SWT.OPEN, "Open properties"
						, new String[] { "*.xml" });
				if(fileName != null) {
					StringJoiner stringJoiner = new StringJoiner(" ");
					stringJoiner
						.add("load properties")
						.add(new File(fileName).toURI().toString());
					userCommand = stringJoiner.toString();
					setChanged();
					notifyObservers();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		window.addExportPropertiesListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = window.displayFileDialog(SWT.SAVE, "Save properties"
						, new String[] { "*.xml" });
				if(fileName != null) {
					StringJoiner stringJoiner = new StringJoiner(" ");
					stringJoiner
						.add("save properties")
						.add(new File(fileName).toURI().toString());
					userCommand = stringJoiner.toString();
					setChanged();
					notifyObservers();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
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
		window.displayShuttingDown();
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
	public void updateServerStats(ServerStats serverStats) {
		window.updateServerStats(serverStats);
	}

	@Override
	public void updateClient(Client client) {
		window.updateClient(client);
	}
	
	@Override
	public void displayProperties(Properties properties) {
		Properties newProperties = (Properties) objectInitializer.initialize(Properties.class, properties);
		if(newProperties != null) {
			if(!properties.equals(newProperties))
				window.displayInfo("Changes will take effect later"
						, "View type and pool size change will take only effect after restarting the game");
			StringJoiner stringJoiner = new StringJoiner(" ");
			stringJoiner
				.add("edit properties")
				.add(""+newProperties.getPort())
				.add(""+newProperties.getNumOfClients())
				.add(""+newProperties.getSocketTimeout())
				.add(""+newProperties.getPoolSize())
				.add(""+newProperties.getMazeSearcherType());
			userCommand = stringJoiner.toString();
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void displayPropertiesSaved() {
		window.displayInfo("Properties saved", "Properties saved successfully");
	}
	
	@Override
	public void displayFileNotFound() {
		window.displayError("IO error", "File not found");
	}
}
