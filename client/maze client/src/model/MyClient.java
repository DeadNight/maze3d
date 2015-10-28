package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MyClient {
	String host;
	int port;
	ServerHandler sessionHandler;
	
	public MyClient(String host, int port, ServerHandler sessionHandler) {
		this.host = host;
		this.port = port;
		this.sessionHandler = sessionHandler;
	}
	
	public void start() { // use try/catch here instead!
		System.out.print("connecting to server... ");
		Socket server;
		try {
			server = new Socket(host, port); // blocking
			System.out.println("connected to server");
			
			InputStream serverIn = server.getInputStream();
			OutputStream serverOut = server.getOutputStream();
			
			sessionHandler.handleServer(serverIn, serverOut);
			
			// release resources
			serverIn.close();
			serverOut.close();
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
