package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import common.Client;
import common.Properties;

public abstract class CommonModel extends Observable implements Model {
	Properties properties;
	ExecutorService threadPool;
	boolean running;
	MyServer server;
	int nextClientId;
	HashMap<Integer, Client> clients;
	HashMap<String, Function<Client, Void>> clientCommands;
	Searcher<Position> mazeSearchAlgorithm;
	HashMap<Maze3dSearchable, Solution<Position>> solutionCache;
	
	public CommonModel() {
		solutionCache = new HashMap<Maze3dSearchable, Solution<Position>>();
		clients = new HashMap<Integer, Client>();
		clientCommands = new HashMap<String, Function<Client, Void>>();
		initClientCommands();
	}
	
	protected abstract void initClientCommands();
	
	@Override
	public boolean start(int port, int numOfClients, int socketTimeout, int poolSize) {
		threadPool = Executors.newFixedThreadPool(poolSize);
		nextClientId = 1;
		if(running)
			return true;
		server = new MyServer(port, new ClientHandler() {
			@Override
			public void handleClient(InputStream inFromClient, OutputStream outToClient) {
				Client client = new Client(nextClientId++, inFromClient, outToClient);
				clients.put(client.getId(), client);
				setChanged();
				notifyObservers(new Object[] { "client connected", client.getId() });
				
				BufferedReader clientReader = new BufferedReader(new InputStreamReader(inFromClient)); 
				PrintWriter clientWriter = new PrintWriter(outToClient);
				
				clientWriter.println("hello");
				clientWriter.flush();
				
				String line;
				try {
					while(!(line = clientReader.readLine()).equals("exit")) {
						if(!(running && client.getRunning())) {
							clientWriter.println("disconnect");
							clientWriter.flush();
						} else if(clientCommands.get(line) == null) {
							clientWriter.println("unsupported command");
							clientWriter.flush();
						} else {
							clientCommands.get(line).apply(client);
						}
					}
					clientWriter.println("good bye");
					clientWriter.flush();
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
		
		threadPool.shutdownNow();
		boolean terminated = false;
		while(!terminated)
			try {
				terminated = threadPool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// OK, keep waiting
			}
	}
	
	@Override
	public Client getClient(int id) {
		return clients.get(id);
	}

	@Override
	public void setMazeSearchAlgorithm(Searcher<Position> mazeSearchAlgorithm) {
		this.mazeSearchAlgorithm = mazeSearchAlgorithm;
	}
	
	/**
	 * Utility to run tasks in the background, handle cancelation by cancel or interrupt &amp;
	 * handle exceptions
	 * @param task Task to run
	 * @see Task
	 */
	<T> void runTaskInBackground(Task<T> task) {
		Future<T> future = threadPool.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return task.doTask();
			}
		});
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				boolean waiting = true;
				while(waiting)
					try {
						T result = future.get(10, TimeUnit.SECONDS);
						waiting = false;
						task.handleResult(result);
					} catch (ExecutionException e) {
						waiting = false;
						task.handleExecutionException(e);
					} catch (InterruptedException e) {
						waiting = false;
						future.cancel(true);
					} catch (TimeoutException e) {
						// OK, keep waiting
					}
			}
		});
	}
}
