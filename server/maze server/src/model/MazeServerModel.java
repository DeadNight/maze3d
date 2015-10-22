package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

import common.Client;

public class MazeServerModel extends CommonModel {
	boolean running;
	MyServer server;
	int nextClientId;
	LinkedHashMap<Integer, Client> clients;
	
	public MazeServerModel() {
		clients = new LinkedHashMap<Integer, Client>();
	}

	@Override
	public boolean start(int port, int numOfClients, int socketTimeout) {
		nextClientId = 1;
		if(running)
			return true;
		server = new MyServer(port, new ClientHandler() {
			@Override
			public void handleClient(InputStream inFromClient, OutputStream outToClient) {
				Client client = new Client(nextClientId++);
				clients.put(client.getId(), client);
				setChanged();
				notifyObservers(new Object[] { "client connected", client.getId() });
				
				BufferedReader clientReder = new BufferedReader(new InputStreamReader(inFromClient));
				PrintWriter clientPrinter = new PrintWriter(outToClient);
				
				String line;
				try {
					while(!(line = clientReder.readLine()).equals("exit")) {
						client.setLastCommand(line);
						setChanged();
						notifyObservers(new Object[] { "client command", client.getId() });
						
						StringBuilder sb = new StringBuilder(line);
						clientPrinter.println(sb.reverse());
						clientPrinter.flush();
					}
					clientPrinter.println("good bye");
					clientPrinter.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				clients.remove(client.getId());
				setChanged();
				notifyObservers(new Object[] { "client disconnected", client.getId() });
			}
		}, numOfClients, socketTimeout);
		return (running = server.start());
	}

	@Override
	public void stop() {
		running = false;
		server.close();
	}
	
	public Client getClient(int id) {
		return clients.get(id);
	}
}
