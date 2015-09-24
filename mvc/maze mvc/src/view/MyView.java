package view;

import io.MyDecompressorInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import algorithms.search.State;
import controller.Command;
import controller.Controller;

/**
 * @author nirleibo
 * <h1>My implementation of the View Façade</h1>
 * Using a CLI as a user interface
 */
public class MyView extends CommonView {
	CLI cli;
	HashMap<String, Command> commands;
	
	BufferedReader in;
	PrintWriter out;
	
	/**
	 * Initialize the View Façade instance
	 * @param controller Controller Façade instance
	 */
	public MyView(Controller controller) {
		super(controller);
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(System.out);
		cli = new CLI(controller, in, out);
	}
	
	@Override
	public void setCommands(HashSet<String> commands) {
		super.setCommands(commands);
		cli.setCommands(commands);
	}

	@Override
	public void start() {
		// start the CLI and exit immediately
		cli.start();
	}

	@Override
	public void displayError(String error) {
		cli.displayLine(error);
	}

	@Override
	public void displayFiles(String[] list) {
		for(String file : list)
			cli.displayLine(file);
	}

	@Override
	public void display3dMaze(byte[] mazeData) {
		BufferedInputStream mazeIn = new BufferedInputStream(new MyDecompressorInputStream(new ByteArrayInputStream(mazeData)));
		ByteArrayOutputStream mazeOut = new ByteArrayOutputStream();
		BufferedOutputStream bufMazeOut = new BufferedOutputStream(mazeOut);
		
		try {
			int b;
			while((b = mazeIn.read()) != -1)
				bufMazeOut.write(b);
			bufMazeOut.flush();
			
			Maze3d maze = new Maze3d(mazeOut.toByteArray());
			
			for(int y = maze.getHeight() - 1; y >= 0 ; --y) {
				cli.displayLine("Floor " + y + ":");
				for(int z = maze.getDepth() - 1; z >= 0; --z) {
					for(int x = 0; x < maze.getWidth(); ++x) {
						if(maze.isWall(x, y, z))
							cli.display((char)0x2593);
						else if(y > 0 && y < maze.getHeight() - 1 &&
								maze.isPath(x, y - 1, z) && maze.isPath(x, y + 1, z))
							cli.display('x');
						else if(y > 0 && maze.isPath(x, y - 1, z))
							cli.display('\\');
						else if(y < maze.getHeight() - 1 && maze.isPath(x, y + 1, z))
							cli.display('/');
						else
							cli.display(' ');
					}
					cli.displayLine();
				}
				cli.displayLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void displayCrossSection(int[][] crossSection) {
		for(int[] row : crossSection) {
			for(int cell : row)
				if(cell == 1)
					cli.display((char)0x2593);
				else
					cli.display(' ');
			cli.displayLine();
		}
	}

	@Override
	public void displayMazeSize(int size) {
		cli.displayLine(size);
	}

	@Override
	public void displayFileSize(int size) {
		cli.displayLine(size);
	}

	@Override
	public void displaySolution(Solution<Position> solution) {
		for(State<Position> state : solution.getSequence()) {
			cli.displayLine(state.getState());
		}
	}

	@Override
	public void displayMazeReady(String name) {
		cli.displayLine("maze " + name + " is ready");
	}

	@Override
	public void display3dMazeSaved(String name) {
		cli.displayLine("maze " + name + " saved");
	}

	@Override
	public void display3dMazeLoaded(String name) {
		cli.displayLine("maze " + name + " loaded");
	}

	@Override
	public void displaySolutionReady(String name) {
		cli.displayLine("solution for " + name + " is ready");
	}

	@Override
	public void displayShuttingDown() {
		cli.displayLine("shutting down...");
	}

	@Override
	public void displayShutdown() {
		cli.displayLine("done");
	}
}
