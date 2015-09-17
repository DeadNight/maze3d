package model;

import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AStarSearcher;
import algorithms.search.BFSearcher;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;
import algorithms.search.Solution;
import controller.Controller;

public class MyModel extends CommonModel {
	MyMaze3dGenerator mazeGen;
	HashMap<String, Maze3d> mazeCache;
	HashMap<Maze3d, Solution<Position>> solutionCache;
	
	public MyModel(Controller controller, int poolSize) {
		super(controller, poolSize);
		mazeGen = new MyMaze3dGenerator();
		mazeCache = new HashMap<String, Maze3d>();
		solutionCache = new HashMap<Maze3d, Solution<Position>>();
	}
	
	@Override
	public void stop() {
		threadPool.shutdown();
		boolean terminated = false;
		while(!terminated)
			try {
				terminated = threadPool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// OK, keep waiting for termination
			}
	}

	@Override
	public String[] getFileList(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("dir <path>");
			return null;
		}
		
		File f = new File(args[0]);
		
		if(!f.exists()) {
			controller.displayError("directory not found");
			return null;
		}
		
		if(!f.isDirectory()) {
			controller.displayError("path is not a directory");
			return null;
		}
		
		return f.list();
	}

	@Override
	public void generate3dMaze(String[] args) {
		if(args.length != 4) {
			controller.displayWrongArguments("generate 3d maze <name> <width> <height> <depth>");
			return;
		}
		
		threadPool.submit(new Runnable() {
			
			@Override
			public void run() {
				Maze3d maze = mazeGen.generate(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				mazeCache.remove(args[0]);
				mazeCache.put(args[0], maze);
				controller.displayMessage("maze " + args[0] + " is ready");
			}
		});
	}

	@Override
	public byte[] getMaze3d(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("display <name>");
			return null;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(args[0])) == null) {
			controller.displayError("maze '" + args[0] + "' not found");
			return null;
		}
			
		ByteArrayOutputStream mazeOut = new ByteArrayOutputStream();
		BufferedOutputStream bufferedMazeCompressor = new BufferedOutputStream(new MyCompressorOutputStream(mazeOut));
		try {
			bufferedMazeCompressor.write(maze.toByteArray());
			bufferedMazeCompressor.flush();
		} catch (IOException e) {
			controller.displayError("error occurred while compressing maze data");
			return null;
		} finally {
			try {
				bufferedMazeCompressor.close();
			} catch (IOException e) {
				controller.displayError("error occurred while closing the stream");
			}
		}
		
		return mazeOut.toByteArray();
	}

	@Override
	public int[][] getCrossSection(String[] args) {
		if(args.length != 4 || !args[2].equals("for")) {
			controller.displayWrongArguments("display cross section by <axis> <index> for <name>");
			return null;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(args[3])) == null) {
			controller.displayError("maze '" + args[3] + "' not found");
			return null;
		}
		
		int index = Integer.parseInt(args[1]);
		if(index < 0) {
			controller.displayError("invalid index '" + args[1] + "'");
			return null;
		}
		
		switch(args[0].toLowerCase()) {
		case "x":
			if(index >= maze.getWidth()) {
				controller.displayError("invalid index '" + args[1] + "'");
				return null;
			}
			return maze.getCrossSectionByX(index);
		case "y":
			if(index >= maze.getHeight()) {
				controller.displayError("invalid index '" + args[1] + "'");
				return null;
			}
			return maze.getCrossSectionByY(index);
		case "z":
			if(index >= maze.getDepth()) {
				controller.displayError("invalid index '" + args[1] + "'");
				return null;
			}
			return maze.getCrossSectionByZ(index);
		default:
			controller.displayError("invalid axis '" + args[0] + "'");
			return null;
		}
	}

	@Override
	public void sazeMaze(String[] args) {
		if(args.length != 2) {
			controller.displayWrongArguments("load maze <name> <file name>");
			return;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(args[0])) == null) {
			controller.displayError("maze '" + args[0] + "' not found");
			return;
		}
		
		BufferedOutputStream bufferedMazeDecompressor = null;
		try {
			bufferedMazeDecompressor = new BufferedOutputStream(new MyCompressorOutputStream(new FileOutputStream(args[1])));
			bufferedMazeDecompressor.write(maze.toByteArray());
			bufferedMazeDecompressor.flush();
		} catch (FileNotFoundException e1) {
			controller.displayError("could not open file '" + args[1] + "' for writing");
		} catch (IOException e) {
			controller.displayError("error occurred while compressing maze data");
		} finally {
			if(bufferedMazeDecompressor != null)
				try {
					bufferedMazeDecompressor.close();
				} catch (IOException e) {
					controller.displayError("error occurred while closing the file");
				}
		}
	}

	@Override
	public void loadMaze(String[] args) {
		if(args.length != 2) {
			controller.displayWrongArguments("load maze <file name> <name>");
			return;
		}
		
		ByteArrayOutputStream mazeDataOut = new ByteArrayOutputStream();
		BufferedOutputStream bufferedMazeDataOut = new BufferedOutputStream(mazeDataOut);
		BufferedInputStream bufferedMazeDecompressor = null;
		try {
			bufferedMazeDecompressor = new BufferedInputStream(new MyDecompressorInputStream(new FileInputStream(args[0])));
			int b;
			while((b = bufferedMazeDecompressor.read()) != -1)
				bufferedMazeDataOut.write(b);
			bufferedMazeDataOut.flush();
			
			try {
				Maze3d maze = new Maze3d(mazeDataOut.toByteArray());
				mazeCache.remove(args[1]);
				mazeCache.put(args[1], maze);
			} catch (IOException e) {
				controller.displayError("error occurred while creating maze");
			}
		} catch (FileNotFoundException e) {
			controller.displayError("could not open file '" + args[0] + "' for reading");
		} catch (IOException e) {
			controller.displayError("error occurred while decompressing maze data");
		} finally {
			if(bufferedMazeDecompressor != null)
				try {
					bufferedMazeDecompressor.close();
				} catch (IOException e) {
					controller.displayError("error occurred while closing the file");
				}
		}
	}

	@Override
	public int mazeSize(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("maze size <name>");
			return -1;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(args[0])) == null) {
			controller.displayError("maze '" + args[0] + "' not found");
			return -1;
		}
		
		return maze.toByteArray().length;
	}

	@Override
	public int fileSize(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("file size <name>");
			return -1;
		}
		
		byte[] mazeData = getMaze3d(args);
		if(mazeData == null)
			return -1;
		
		return mazeData.length;
	}

	@Override
	public void solveMaze(String[] args) {
		if(args.length != 2) {
			controller.displayWrongArguments("solve <name> <algorithm>");
			return;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(args[0])) == null) {
			controller.displayError("maze '" + args[0] + "' not found");
			return;
		}
		
		Searcher<Position> algorithm;
		switch(args[1].toLowerCase()) {
		case "bfs":
			algorithm = new BFSearcher<Position>();
			break;
		case "a*":
		case "astar":
			algorithm = new AStarSearcher<Position>(new MazeManhattanDistance());
			break;
		default:
			controller.displayMessage("invalid algorithm");
			return;
		}
		
		if(solutionCache.containsKey(maze)) {
			if(solutionCache.get(maze) == null)
				controller.displayMessage("maze cannot be solved");
			else
				controller.displayMessage("solution for " + args[0] + " is ready");
			return;
		}
		
		threadPool.submit(new Runnable() {
			
			@Override
			public void run() {
				Solution<Position> solution = algorithm.search(new Maze3dSearchable(maze));
				solutionCache.put(maze, solution);
				
				if(solution == null) {
					controller.displayMessage("maze cannot be solved");
					return;
				}
				
				controller.displayMessage("solution for " + args[0] + " is ready");
			}
		});
	}

	@Override
	public Solution<Position> getSolution(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("display solution <name>");
			return null;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(args[0])) == null) {
			controller.displayError("maze '" + args[0] + "' not found");
			return null;
		}
		
		if(!solutionCache.containsKey(maze)) {
			controller.displayError("solution for '" + args[0] + "' not found");
			return null;
		}
		
		Solution<Position> solution = solutionCache.get(maze);
		if(solution == null) {
			controller.displayMessage("maze cannot be solved");
			return null;
		}
		
		return solution;
	}
}
