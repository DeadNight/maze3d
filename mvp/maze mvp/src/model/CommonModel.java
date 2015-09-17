package model;

import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;

public abstract class CommonModel extends Observable implements Model {
	Properties properties;
	ExecutorService threadPool;
	Maze3dGenerator mazeGenerator;
	Searcher<Position> mazeSearchAlgorithm;
	HashMap<String, Maze3d> mazeCache;
	
	public CommonModel(Properties properties) {
		this.properties = properties;
		threadPool = Executors.newFixedThreadPool(properties.getPoolSize());
		mazeCache = new HashMap<String, Maze3d>();
		
		initMazeGenerator();
		initMazeSearchAlgorithm();
	}

	abstract void initMazeGenerator();
	abstract void initMazeSearchAlgorithm();
}
