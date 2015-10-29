package model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import common.Client;
import common.MazeSearcherTypes;
import common.Properties;
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
	public void loadProperties(String fileName) throws URISyntaxException, FileNotFoundException, IOException {
		try {
			fileName = new URI(fileName).getPath();
		} catch (URISyntaxException e) {
			notifyObservers(new Object[] { "properties not found" });
			throw e;
		}
		XMLDecoder xmlDecoder;
		try {
			xmlDecoder = new XMLDecoder(new FileInputStream(fileName));
		} catch(FileNotFoundException e) {
			setChanged();
			notifyObservers(new Object[] { "properties not found" });
			throw e;
		}
		
		try {
			properties = (Properties) xmlDecoder.readObject();
			setChanged();
			notifyObservers(new Object[] { "properties loaded" });
		} catch(ArrayIndexOutOfBoundsException e) {
			setChanged();
			notifyObservers(new Object[] { "bad file format" });
			throw e;
		} finally {
			xmlDecoder.close();
		}
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}
	
	@Override
	public void saveProperties(String fileName, int port, int numOfClients, int socketTimeout
			, int poolSize, MazeSearcherTypes searcher) {
		try {
			fileName = new URI(fileName).getPath();
		} catch (URISyntaxException e) {
			notifyObservers(new Object[] { "properties not found" });
		}
		
		properties = new Properties();
		properties.setPort(port);
		properties.setNumOfClients(numOfClients);
		properties.setSocketTimeout(socketTimeout);
		properties.setPoolSize(poolSize);
		properties.setMazeSearcherType(searcher);
		
		XMLEncoder xmlEncoder;
		try {
			xmlEncoder = new XMLEncoder(new FileOutputStream(fileName));
		} catch(FileNotFoundException e) {
			setChanged();
			notifyObservers(new Object[] { "properties not found" });
			return;
		}
		
		xmlEncoder.writeObject(properties);
		xmlEncoder.close();
		
		setChanged();
		notifyObservers(new Object[] { "properties saved" });
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
					notifyObservers(new Object[] { "recieve searchable error", client.getId() });
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
				notifyObservers(new Object[] { "solving", client.getId() });
				
				client.incrementSolving();
				stats.incrementSolving();
				
				Solution<Position> solution = mazeSearchAlgorithm.search(mazeSearchable);
				
				if(solution == null) {
					client.incrementNoSolution();
					stats.incrementNoSolution();
					clientWriter.println("no solution");
					clientWriter.flush();
					setChanged();
					notifyObservers(new Object[] { "no solution", client.getId() } );
					return null;
				}
					
				solutionCache.remove(mazeSearchable);
				solutionCache.put(mazeSearchable, solution);

				client.incrementSolved();
				stats.incrementSolved();
				clientWriter.println("solved");
				clientWriter.flush();
				setChanged();
				notifyObservers(new Object[] { "solved", client.getId() } );
				
				try {
					sendSolution(client, solution);
					clientWriter.println("done");
					clientWriter.flush();
				} catch (IOException e) {
					e.printStackTrace();
					setChanged();
					notifyObservers(new Object[] { "send solution error", client.getId() });
				}
				
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
