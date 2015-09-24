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
	public void calculateFileList(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("dir <path>");
			return;
		}
		String path = args[0];
		
		File f = new File(path);
		
		if(!f.exists()) {
			controller.displayError("directory not found");
			return;
		}
		
		if(!f.isDirectory()) {
			controller.displayError("path is not a directory");
			return;
		}
		
		controller.displayFilesList(f.list());
	}

	@Override
	public void generate3dMaze(String[] args) {
		String format = "generate 3d maze <name> <width> <height> <depth>";
		if(args.length != 4) {
			controller.displayWrongArguments(format);
			return;
		}
		String name = args[0];
		int width;
		int height;
		int depth;
		try {
			width = Integer.parseInt(args[1]);
			height = Integer.parseInt(args[2]);
			depth = Integer.parseInt(args[3]);
		} catch (NumberFormatException e) {
			controller.displayWrongArguments(format);
			return;
		}
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Maze3d maze = mazeGen.generate(width, height, depth);
				mazeCache.remove(name);
				mazeCache.put(name, maze);
				controller.display3dMazeReady(name);
			}
		});
	}

	@Override
	public void get3dMaze(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("display <name>");
			return;
		}
		String name = args[0];
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
			
		ByteArrayOutputStream mazeOut = new ByteArrayOutputStream();
		BufferedOutputStream bufferedMazeCompressor = new BufferedOutputStream(new MyCompressorOutputStream(mazeOut));
		try {
			bufferedMazeCompressor.write(maze.toByteArray());
			bufferedMazeCompressor.flush();
		} catch (IOException e) {
			controller.displayError("error occurred while compressing maze data");
			return;
		} finally {
			try {
				bufferedMazeCompressor.close();
			} catch (IOException e) {
				controller.displayError("error occurred while closing the stream");
			}
		}
		
		controller.display3dMaze(mazeOut.toByteArray());
	}

	@Override
	public void getCrossSection(String[] args) {
		String format = "display cross section by <axis> <index> for <name>";
		if(args.length != 4 || !args[2].equals("for")) {
			controller.displayWrongArguments(format);
			return;
		}
		String axis = args[0];
		int index;
		String name = args[3];
		
		try {
			index = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			controller.displayWrongArguments(format);
			return;
		}
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		String invalidIndexMessage = "invalid index '" + index + "'";
		
		if(index < 0) {
			controller.displayError(invalidIndexMessage);
			return;
		}
		
		int[][] crossSection;
		switch(axis.toLowerCase()) {
		case "x":
			if(index >= maze.getWidth()) {
				controller.displayError(invalidIndexMessage);
				return;
			}
			crossSection = maze.getCrossSectionByX(index);
			break;
		case "y":
			if(index >= maze.getHeight()) {
				controller.displayError(invalidIndexMessage);
				return;
			}
			crossSection = maze.getCrossSectionByY(index);
			break;
		case "z":
			if(index >= maze.getDepth()) {
				controller.displayError(invalidIndexMessage);
				return;
			}
			crossSection = maze.getCrossSectionByZ(index);
			break;
		default:
			controller.displayError("invalid axis '" + axis + "'");
			return;
		}
		
		controller.displayCrossSection(crossSection);
	}

	@Override
	public void sazeMaze(String[] args) {
		if(args.length != 2) {
			controller.displayWrongArguments("load maze <name> <file name>");
			return;
		}
		String name = args[0];
		String fileName = args[1];
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		BufferedOutputStream bufferedMazeDecompressor;
		try {
			bufferedMazeDecompressor = new BufferedOutputStream(new MyCompressorOutputStream(new FileOutputStream(fileName)));
		} catch (FileNotFoundException e) {
			controller.displayError("could not open file '" + fileName + "' for writing");
			return;
		}
		
		try {
			bufferedMazeDecompressor.write(maze.toByteArray());
			bufferedMazeDecompressor.flush();
		} catch (IOException e) {
			controller.displayError("error occurred while compressing maze data");
			return;
		} finally {
			try {
				bufferedMazeDecompressor.close();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		
		controller.display3dMazeSaved(name);
	}

	@Override
	public void loadMaze(String[] args) {
		if(args.length != 2) {
			controller.displayWrongArguments("load maze <file name> <name>");
			return;
		}
		String fileName = args[0];
		String name = args[1];
		
		ByteArrayOutputStream mazeDataOut = new ByteArrayOutputStream();
		BufferedOutputStream bufferedMazeDataOut = new BufferedOutputStream(mazeDataOut);
		BufferedInputStream bufferedMazeDecompressor = null;
		try {
			bufferedMazeDecompressor = new BufferedInputStream(new MyDecompressorInputStream(new FileInputStream(fileName)));
		} catch (FileNotFoundException e) {
			controller.displayError("could not open file '" + fileName + "' for reading");
			return;
		}
		
		try {
			int b;
			while((b = bufferedMazeDecompressor.read()) != -1)
				bufferedMazeDataOut.write(b);
			bufferedMazeDataOut.flush();
		} catch (IOException e) {
			controller.displayError("error occurred while decompressing maze data");
			return;
		} finally {
			try {
				bufferedMazeDecompressor.close();
			} catch (IOException e) {
				controller.displayError("error occurred while closing the file");
				return;
			}
		}
		
		Maze3d maze;
		try {
			maze = new Maze3d(mazeDataOut.toByteArray());
		} catch (IOException e) {
			controller.displayError("error occurred while creating maze");
			return;
		}
		
		mazeCache.remove(name);
		mazeCache.put(name, maze);
		
		controller.display3dMazeLoaded(name);
	}

	@Override
	public void mazeSize(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("maze size <name>");
			return;
		}
		String name = args[0];
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		controller.displayMazeSize(maze.toByteArray().length);
	}

	@Override
	public void fileSize(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("maze size <name>");
			return;
		}
		String name = args[0];
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		byte[] compressMazeData;
		try {
			compressMazeData = compressData(maze.toByteArray());
		} catch (IOException e) {
			return;
		}
		
		controller.displayFileSize(compressMazeData.length);
	}

	@Override
	public void solveMaze(String[] args) {
		if(args.length != 2) {
			controller.displayWrongArguments("solve <name> <algorithm>");
			return;
		}
		String name = args[0];
		String algorithm = args[1];
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		Searcher<Position> searcher;
		switch(algorithm.toLowerCase()) {
		case "bfs":
			searcher = new BFSearcher<Position>();
			break;
		case "a*":
		case "astar":
			searcher = new AStarSearcher<Position>(new MazeManhattanDistance());
			break;
		default:
			controller.displayError("invalid algorithm");
			return;
		}
		
		if(solutionCache.containsKey(maze)) {
			if(solutionCache.get(maze) == null)
				controller.displayError("maze could not be solved");
			else
				controller.displaySolutionReady(name);
			return;
		}
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				Solution<Position> solution = searcher.search(new Maze3dSearchable(maze));
				solutionCache.put(maze, solution);
				
				if(solution == null) {
					controller.displayError("maze cannot be solved");
					return;
				}
				
				controller.displaySolutionReady(name);
			}
		});
	}

	@Override
	public void getSolution(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("display solution <name>");
			return;
		}
		String name = args[0];
		
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		if(!solutionCache.containsKey(maze)) {
			controller.displayError("solution for '" + args[0] + "' not found");
			return;
		}
		
		Solution<Position> solution = solutionCache.get(maze);
		if(solution == null) {
			controller.displayError("maze could not be solved");
			return;
		}
		
		controller.displaySolution(solution);
	}
	
	private byte[] compressData(byte[] data) throws IOException {
		ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
		MyCompressorOutputStream dataCompressor = new MyCompressorOutputStream(new BufferedOutputStream(dataOut));
		try {
			dataCompressor.write(data);
			dataCompressor.flush();
		} catch (IOException e) {
			controller.displayError("error occurred while compressing maze data");
			throw e;
		} finally {
			try {
				dataCompressor.close();
			} catch (IOException e) {
				controller.displayError("error occurred while closing the stream");
				throw e;
			}
		}
		
		return dataOut.toByteArray();
	}
}
