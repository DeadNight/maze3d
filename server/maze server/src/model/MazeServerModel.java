package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import common.Client;
import common.ServerStats;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

public class MazeServerModel extends CommonModel {
	private final static String SOLUTIONS_FILE_NAME = "solutions.gzip";
	ServerStats stats;
	
	public MazeServerModel() {
		stats = new ServerStats();
		if(new File(SOLUTIONS_FILE_NAME).exists())
			try {
				ObjectInputStream solutionsIn = new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(SOLUTIONS_FILE_NAME))));
				try {
					@SuppressWarnings("unchecked")
					HashMap<Maze3dSearchable, Solution<Position>> solutions = (HashMap<Maze3dSearchable, Solution<Position>>) solutionsIn.readObject();
					solutionCache = solutions;
				} catch (ClassNotFoundException | ClassCastException e) {
					e.printStackTrace();
				} finally {
					try {
						solutionsIn.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				/*
				 * FileNotFoundException - File couldn't be opened for reading
				 * ZipException - Bad format
				 */
				e.printStackTrace();
			}
	}
	
	@Override
	protected void initClientCommands() {
		clientCommands.put("solve", new Function<Client, Void>() {
			@Override
			public Void apply(Client client) {
				PrintWriter clientWriter = new PrintWriter(client.getOut());
				
				clientWriter.println("ok, send searchable");
				clientWriter.flush();
				
				Maze3dSearchable mazeSearchable;
				try {
					@SuppressWarnings("resource") // do not close the client stream
					ObjectInputStream clientObjectIn = new ObjectInputStream(new MyDecompressorInputStream(new BufferedInputStream(client.getIn())));
					
					setChanged();
					notifyObservers(new Object[] { "solve request", client.getId() });
					
					mazeSearchable = (Maze3dSearchable)clientObjectIn.readObject();
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					
					clientWriter.println("solve error");
					clientWriter.flush();
					
					setChanged();
					notifyObservers(new Object[] { "read searchable error", client.getId() });
					return null;
				}
				
				if(solutionCache.containsKey(mazeSearchable)) {
					Solution<Position> solution = solutionCache.get(mazeSearchable);
					if(solution == null) {
						clientWriter.println("no solution");
						clientWriter.flush();
					} else {
						clientWriter.println("solved");
						clientWriter.flush();
						try {
							sendSolution(client, solution);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					return null;
				}
				
				clientWriter.println("solving");
				clientWriter.flush();
				
				client.incrementPending();
				stats.incrementPending();
				
				setChanged();
				notifyObservers(new Object[] { "solving", ""+client.getId() });
				
				runTaskInBackground(new Task<Solution<Position>>() {
					@Override
					public Solution<Position> doTask() throws Exception {
						client.incrementSolving();
						stats.incrementSolving();
						return mazeSearchAlgorithm.search(mazeSearchable);
					}

					@Override
					public void handleResult(Solution<Position> result) {
						if(result == null) {
							client.incrementNoSolution();
							stats.incrementNoSolution();
							clientWriter.println("no solution");
							clientWriter.flush();
							setChanged();
							notifyObservers(new String[] { "no solution", ""+client.getId() } );
						}
							
						solutionCache.remove(mazeSearchable);
						solutionCache.put(mazeSearchable, result);
						
						client.incrementSolved();
						setChanged();
						notifyObservers(new String[] { "solved", ""+client.getId() } );
						
						
					}

					@Override
					public void handleExecutionException(ExecutionException e) {
						client.incrementNoSolution();
						notifyObservers(new String[] { "no solution", ""+client.getId() } );
					}
				});
				return null;
			}
		});
	}
	
	private void sendSolution(Client client, Solution<Position> solution) throws IOException {
		PrintWriter clientWriter = new PrintWriter(client.getOut());
		@SuppressWarnings("resource") // do not close the client stream
		ObjectOutputStream clientObjectOut = new ObjectOutputStream(new MyCompressorOutputStream(new BufferedOutputStream(client.getOut())));
		clientObjectOut.writeObject(solution);
		clientObjectOut.flush();
		clientWriter.println("done");
		clientWriter.flush();
	}

	@Override
	public void stop() {
		super.stop();
		
		try {
			ObjectOutputStream solutionsOut = new ObjectOutputStream(new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(SOLUTIONS_FILE_NAME))));
			
			try {
				solutionsOut.writeObject(solutionCache);
				solutionsOut.flush();
			} finally {
				solutionsOut.close();
			}
		} catch (IOException e) {
			// FileNotFoundException - File couldn't be opened for writing
			e.printStackTrace();
		}
	};

	@Override
	public ServerStats getServerStats() {
		stats.setConnected(clients.size());
		stats.setCached(solutionCache.size());
		return stats;
	}
}
