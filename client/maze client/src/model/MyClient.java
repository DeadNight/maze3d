package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Nir Leibovitch
 * <h1>General purpose tcp/ip client</h1>
 * Handles connection to a tcp/ip server
 */
public class MyClient {
	String host;
	int port;
	ServerHandler sessionHandler;
	
	/**
	 * Create a new tcp/ip client to connect to the given server (host:port) using the
	 * given session protocol
	 * @param host Host of the tcp/ip server
	 * @param port Port of the tcp/ip server
	 * @param sessionHandler  Defines the session protocol
	 */
	public MyClient(String host, int port, ServerHandler sessionHandler) {
		this.host = host;
		this.port = port;
		this.sessionHandler = sessionHandler;
	}
	
	/**
	 * Connect to the tcp/ip server &amp; handle the session asynchronously
	 * @return boolean Whether connected successfully
	 */
	public boolean start() {
		System.out.print("connecting to server... ");
		Socket server;
		try {
			server = new Socket(host, port); // blocking
			System.out.println("connected to server");
			
			InputStream serverIn = server.getInputStream();
			OutputStream serverOut = server.getOutputStream();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					sessionHandler.handleServer(serverIn, serverOut);
					
					// release resources
					try {
						serverIn.close();
						serverOut.close();
						server.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
