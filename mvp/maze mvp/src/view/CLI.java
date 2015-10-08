package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;

public class CLI extends CommonView {
	boolean running;
	BufferedReader in;
	PrintWriter out;
	
	public CLI(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	@Override
	public void start() {
		if(running)
			return;
		running = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(running) {
					out.print("> ");
					out.flush();
					
					try {
						userCommand = in.readLine();
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					
					userCommand = userCommand.trim().replaceAll("\\s\\s+", " ");
					if(!userCommand.isEmpty()) {
						setChanged();
						notifyObservers();
					}
				}
				
				out.println("good bye!");
				out.flush();
			}
		}).start();
	}
	
	@Override
	public void stop() {
		running = false;
	}

	@Override
	public void displayMazeNotFound() {
		displayError("maze not found");
	}

	@Override
	public void displayUnknownCommand() {
		displayError("unknown command");
	}
	
	private void displayError(String message) {
		System.err.println(message);
	}

	@Override
	public void displayMaze(byte[] compressedMazeData) {
		byte[] mazeData;
		try {
			mazeData = decompressData(compressedMazeData);
		} catch (IOException e) {
			e.printStackTrace();
			displayError("error decompressing maze data");
			return;
		}
		
		Maze3d maze;
		try {
			maze = createMaze(mazeData);
		} catch (IOException e) {
			e.printStackTrace();
			displayError("error creating maze from data");
			return;
		}
		
		printMaze(maze);
	}

	@Override
	public void displayCommandTemplate(String commandName, String template) {
		displayError("wrong params." + System.lineSeparator() + commandName + " " + template);
	}

	@Override
	public void displayFilesList(String[] filesList) {
		for(String file : filesList)
			out.println(file);
		out.flush();
	}

	@Override
	public void displayDirectoryNotFound() {
		displayError("directory not found");
	}

	@Override
	public void displayNotDirectory() {
		displayError("not a directory");
	}

	@Override
	public void displayGetMazeError() {
		out.println("error getting maze data");
		out.flush();
	}

	@Override
	public void displayMazeGenerated(String name) {
		out.println("maze " + name + " generated");
		out.flush();
	}

	@Override
	public void displayCrossSection(int[][] crossSection) {
		for(int[] row : crossSection) {
			for(int cell : row)
				if(cell == 1)
					System.out.print((char)0x2593);
				else
					System.out.print(' ');
			System.out.println();
		}
	}

	@Override
	public void displayIndexOutOfRange() {
		displayError("index out of range");
	}
	
	@Override
	public void displaySaveMazeError() {
		displayError("error occured while saving maze");
	}

	@Override
	public void displayMazeSaved() {
		out.println("maze saved successfully");
		out.flush();
	}

	@Override
	public void displayMazeLoaded(String name) {
		out.println("maze " + name + " loaded successfully");
		out.flush();
	}

	@Override
	public void displayFileNotFound() {
		displayError("file not found");
	}

	@Override
	public void displayLoadMazeError() {
		out.println("error occurred while loading maze");
		out.flush();
	}

	@Override
	public void displayMazeSize(int mazeSize) {
		out.println(mazeSize);
		out.flush();
	}

	@Override
	public void displayDecompressionError() {
		displayError("error occured while decompressing data");
	}

	@Override
	public void displayFileSize(int fileSize) {
		out.println(fileSize);
		out.flush();
	}

	@Override
	public void displayInvalidAxis() {
		out.println("invlaid axis");
		out.flush();
	}

	@Override
	public void displayMazeSolved(String name) {
		out.println("solution for " + name + " is ready");
		out.flush();
	}

	@Override
	public void displaySolveMazeError(String name) {
		out.println("maze " + name + " could not be solved");
		out.flush();
	}

	@Override
	public void displayMazeSolutionNotFound() {
		out.println("maze wasn't solved");
		out.flush();
	}
	
	private void printMaze(Maze3d maze) {
		for(int y = maze.getHeight() - 1; y >= 0 ; --y) {
			System.out.println("Floor " + y + ":");
			for(int z = maze.getDepth() - 1; z >= 0; --z) {
				for(int x = 0; x < maze.getWidth(); ++x) {
					if(maze.isWall(x, y, z))
						System.out.print((char)0x2593);
					else if(y > 0 && y < maze.getHeight() - 1 &&
						maze.isPath(x, y - 1, z) && maze.isPath(x, y + 1, z))
						System.out.print('x');
					else if(y > 0 && maze.isPath(x, y - 1, z))
						System.out.print('\\');
					else if(y < maze.getHeight() - 1 && maze.isPath(x, y + 1, z))
						System.out.print('/');
					else
						System.out.print(' ');
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	@Override
	public void displayMazeSolution(Solution<Position> solution) {
		for(State<Position> state : solution.getSequence())
			out.println("{" + state.getState().getX() + ", "
					+ state.getState().getY() + ", "
					+ state.getState().getZ() + "}");
		out.flush();
	}

	@Override
	public void displayFileNameError() {
		displayError("Error occurred while parsing file name");
	}

	@Override
	public void displayProperties(Properties properties) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void displayPropertiesSaved() {
		out.println("properties saved successfully");
	}
}
