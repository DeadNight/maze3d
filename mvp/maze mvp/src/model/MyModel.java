package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.AStarSearcher;
import algorithms.search.BFSearcher;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;

public class MyModel extends CommonModel {
	public MyModel() throws ArrayIndexOutOfBoundsException, FileNotFoundException {
		super();
	}

	@Override
	void initMazeGenerator() {
		switch(properties.getMazeGenerator()) {
		case SIMPLE:
			mazeGenerator = new SimpleMaze3dGenerator();
			break;
		case MY:
			mazeGenerator = new MyMaze3dGenerator();
			break;
		}
	}

	@Override
	void initMazeSearchAlgorithm() {
		switch(properties.getMazeSearchAlgorithm()) {
		case BFS:
			mazeSearchAlgorithm = new BFSearcher<Position>();
		case AStar_Air:
			mazeSearchAlgorithm = new AStarSearcher<Position>(new MazeAirDistance());
			break;
		case AStar_Manhattan:
			mazeSearchAlgorithm = new AStarSearcher<Position>(new MazeManhattanDistance());
			break;
		}
	}
	
	@Override
	public void stop() {
		threadPool.shutdown();
		boolean terminated = false;
		while(!terminated)
			try {
				terminated = threadPool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// OK, keep waiting
			}
	}

	@Override
	public void generateMaze(String name, int width, int height, int depth) {
		runTaskInBackground(new Task<Maze3d>() {
			@Override
			public Maze3d doTask() {
				return mazeGenerator.generate(width, height, depth);
			}

			@Override
			public void handleResult(Maze3d result) {
				mazeCache.remove(name);
				mazeCache.put(name, result);
				setChanged();
				notifyObservers(new String[] { "generated", name });
			}

			@Override
			public void handleException(Exception e) {
				notifyObservers(new String[] { "error", "generate", name });
			}
		});
	}

	@Override
	public byte[] getMazeData(String name) throws IOException {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			return null;
		}
		return compressData(maze.toByteArray());
	}
}
