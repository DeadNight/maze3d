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

/**
 * @author nirleibo
 * <h1>My implementation of the Model Façade</h1>
 */
public class MyModel extends CommonModel {
	MyMaze3dGenerator mazeGen;
	HashMap<String, Maze3d> mazeCache;
	HashMap<Maze3d, Solution<Position>> solutionCache;
	
	/**
	 * Initiate the my Model Façade instance
	 * @param controller The Controller Façade instance
	 * @param poolSize Size of the underlying thread pool
	 */
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
	public void calculateFileList(String path) {
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
	public void generate3dMaze(String name, int width, int height, int depth) {
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
	public void get3dMaze(String name) {
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		byte[] compressMazeData;
		try {
			compressMazeData = compressData(maze.toByteArray());
		} catch (IOException e) {
			// appropriate displayError was called by compressData
			return;
		}
		
		controller.display3dMaze(compressMazeData);
	}

	@Override
	public void getCrossSection(String name, String axis, int index) {
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
	public void sazeMaze(String name, String fileName) {
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
	public void loadMaze(String fileName, String name) {
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
	public void calculateMazeSize(String name) {
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		controller.displayMazeSize(maze.toByteArray().length);
	}

	@Override
	public void calculateFileSize(String name) {
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		byte[] compressMazeData;
		try {
			compressMazeData = compressData(maze.toByteArray());
		} catch (IOException e) {
			// appropriate displayError was called by compressData
			return;
		}
		
		controller.displayFileSize(compressMazeData.length);
	}

	@Override
	public void solveMaze(String name, String algorithmName) {
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		Searcher<Position> searcher;
		switch(algorithmName.toLowerCase()) {
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
	public void getSolution(String name) {
		Maze3d maze;
		if((maze = mazeCache.get(name)) == null) {
			controller.displayError("maze '" + name + "' not found");
			return;
		}
		
		if(!solutionCache.containsKey(maze)) {
			controller.displayError("solution for '" + name + "' not found");
			return;
		}
		
		Solution<Position> solution = solutionCache.get(maze);
		if(solution == null) {
			controller.displayError("maze could not be solved");
			return;
		}
		
		controller.displaySolution(solution);
	}
}
