package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.function.Function;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import common.Client;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

public class MazeServerModel extends CommonModel {
	@Override
	protected void initClientCommands() {
		clientCommands.put("solve", new Function<Client, Void>() {
			@Override
			public Void apply(Client client) {
				PrintWriter clientWriter = new PrintWriter(client.getOut());
				Maze3dSearchable mazeSearchable;
				try {
					@SuppressWarnings("resource") // do not close the client stream
					ObjectInputStream clientObjectIn = new ObjectInputStream(new MyDecompressorInputStream(new BufferedInputStream(client.getIn())));
					
					clientWriter.println("ok, send searchable");
					clientWriter.flush();
					
					setChanged();
					notifyObservers(new String[] { "solve request", ""+client.getId() });
					
					mazeSearchable = (Maze3dSearchable)clientObjectIn.readObject();
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
					
					clientWriter.println("exception occured");
					clientWriter.flush();
					
					setChanged();
					notifyObservers(new String[] { "read searchable exception", ""+client.getId() });
					return null;
				}
				clientWriter.println("solving");
				clientWriter.flush();
				
				setChanged();
				notifyObservers(new String[] { "solving", ""+client.getId() });
				
				//TODO: submit callable to threadpool
				new Thread(new Runnable() {
					@Override
					public void run() {
						Solution<Position> solution = mazeSearchAlgorithm.search(mazeSearchable);
						
						if(solution == null) {
							clientWriter.println("no solution");
							clientWriter.flush();
							
							
							setChanged();
							notifyObservers(new String[] { "no solution", ""+client.getId() });
							return;
						}
						
						try {
							@SuppressWarnings("resource") // do not close the client stream
							ObjectOutputStream clientObjectOut = new ObjectOutputStream(new MyCompressorOutputStream(new BufferedOutputStream(client.getOut())));
							clientObjectOut.writeObject(solution);
							clientObjectOut.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
						setChanged();
						notifyObservers(new String[] { "solved", ""+client.getId() });
					}
				}).start();
				return null;
			}
		});
	}
}
