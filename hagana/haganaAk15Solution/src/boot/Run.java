package boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.rmi.UnexpectedException;
import java.util.ArrayList;

import algorithms.search.BFSearcher;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import algorithms.search.State;
import model.MyClient;
import model.ServerHandler;

public class Run {
	private static boolean running;
	
	public static void main(String[] args) {
		System.out.println("Nibbles Client");
		System.out.println("press Enter to begin");
		
		BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(System.in));
		Searcher<Position> searcher = new BFSearcher<Position>();
		running = true;
		
		try {
			bufferedIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		new MyClient("127.0.0.1", 5400, new ServerHandler() {
			@Override
			public void handleServer(InputStream inFromServer, OutputStream outToServer) {
				BufferedReader serverIn = new BufferedReader(new InputStreamReader(inFromServer));
				PrintWriter serverOut = new PrintWriter(outToServer);
				
				Position headPosition;
				Position applePosition = null;
				
				while(running) {
					try {
						Position newApplePosition;
						while((newApplePosition = getApplePosition(serverIn, serverOut)).equals(applePosition)) {
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								//OK, keep waiting
							}
						}
						applePosition = newApplePosition;
						
						headPosition = getHeadPosition(serverIn, serverOut);
						
						serverOut.println("get body");
						serverOut.flush();
						String body = serverIn.readLine();
						String[] bodyParts = body.split("\t");
						ArrayList<Position> bodyPartPositions = new ArrayList<Position>();
						for(String bodyPart : bodyParts) {
							String[] bodyPartCoordinates = bodyPart.split(",");
							bodyPartPositions.add(new Position(Integer.parseInt(bodyPartCoordinates[0])
									, Integer.parseInt(bodyPartCoordinates[1])));
						}
						
						Nibbles nibbles = new Nibbles(headPosition, applePosition, bodyPartPositions);
						Solution<Position> solution = searcher.search(new NibblesSearchable(nibbles));
						
						for(State<Position> state : solution.getSequence()) {
							if(new Position(state.getCameFrom().getState()).moveUp().equals(state.getState()))
								serverOut.println("up");
							else if(new Position(state.getCameFrom().getState()).moveDown().equals(state.getState()))
								serverOut.println("down");
							else if(new Position(state.getCameFrom().getState()).moveLeft().equals(state.getState()))
								serverOut.println("left");
							else if(new Position(state.getCameFrom().getState()).moveRight().equals(state.getState()))
								serverOut.println("right");
							else
								throw new UnexpectedException("only 4 directions");
							
							serverOut.flush();
							serverIn.readLine(); // done
							
							Position newHeadPosition;
							while((newHeadPosition = getHeadPosition(serverIn, serverOut)).equals(headPosition)) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									//OK, keep waiting
								}
							}
							headPosition = newHeadPosition;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				System.out.println("Game over");
				
				
				serverOut.println("exit"); 
				serverOut.flush();
			}

			private Position getApplePosition(BufferedReader serverIn, PrintWriter serverOut) throws IOException {
				Position applePosition;
				serverOut.println("get apple");
				serverOut.flush();
				String apple = serverIn.readLine();
				String[] appleCoordinates = apple.split(",");
				applePosition = new Position(Integer.parseInt(appleCoordinates[0])
						, Integer.parseInt(appleCoordinates[1]));
				return applePosition;
			}
		}).start();
		
		System.out.println("press Enter to exit");
		try {
			bufferedIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			running = false;
		}
	}

	private static Position getHeadPosition(BufferedReader serverIn, PrintWriter serverOut) throws IOException {
		serverOut.println("get head");
		serverOut.flush();
		String head = serverIn.readLine();
		String[] headCoordinates = head.split(",");
		Position headPosition = new Position(Integer.parseInt(headCoordinates[0])
				, Integer.parseInt(headCoordinates[1]));
		return headPosition;
	}
}
