package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyServer {
	int port;
	ClientHandler clientHandler;
	int numOfClients;
	int socketTimeout;
	
	ExecutorService threadPool;
	ServerSocket server;
	volatile boolean stop;
	
	public MyServer(int port, ClientHandler clientHandler, int numOfClients, int socketTimeout) {
		this.port = port;
		this.clientHandler = clientHandler;
		this.numOfClients = numOfClients;
		this.socketTimeout = socketTimeout;
	}
	
	public boolean start() {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch(IllegalArgumentException e) {
			System.err.println("port out of range");
			return false;
		}
		
		try {
			// set socket timeout we don't hang if no client connects after a call to stop()
			server.setSoTimeout(socketTimeout);
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		}
		
		threadPool = Executors.newFixedThreadPool(numOfClients);
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(!stop) {
					try {
						System.out.println("waiting for client...");
						Socket client = server.accept();
						if(client != null) {
							System.out.println("client connected");
							threadPool.execute(new Runnable() {
								@Override
								public void run() {
									try {
										InputStream clientIn = client.getInputStream();
										OutputStream clientOut = client.getOutputStream();
										clientHandler.handleClient(clientIn, clientOut);
										
										clientIn.close();
										clientOut.close();
										client.close();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});
						}
					} catch (SocketTimeoutException e) {
						// it's ok
					} catch(SocketException e) {
						System.out.println("Server socket closed");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Good bye!");
			}
		}).start();
		return true;
	}
	
	public void close() {
		System.out.println("Shutting down...");
		stop = true;
		threadPool.shutdown();
		
		System.out.println("Waiting for pending requests to complete...");
		while(!threadPool.isTerminated())
			try {
				threadPool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// continue
			}
		
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
