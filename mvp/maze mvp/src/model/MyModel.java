package model;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.AStarSearcher;
import algorithms.search.BFSearcher;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;

public class MyModel extends CommonModel {
	public MyModel(Properties properties) {
		super(properties);
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
	public void generate3dMaze(String name, int width, int height, int depth) {
		threadPool.submit(new FutureTask<Maze3d>(
			new Callable<Maze3d>() {
				@Override
				public Maze3d call() throws Exception {
					return mazeGenerator.generate(width, height, depth);
				}
			}
		) {
			@Override
			protected void done() {
				if(isDone()) {
					Maze3d maze;
					try {
						maze = this.get();
						mazeCache.put(name, maze);
						notifyObservers(new String[] { "maze", name });
					} catch(CancellationException | InterruptedException e) {
						System.err.println("this shouldn't happen when the task isDone");
						e.printStackTrace();
					} catch(ExecutionException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
	}
}
