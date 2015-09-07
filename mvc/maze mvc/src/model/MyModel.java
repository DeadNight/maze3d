package model;

import java.io.File;
import java.util.HashMap;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import controller.Controller;

public class MyModel extends CommonModel {
	MyMaze3dGenerator mazeGen;
	HashMap<String, Maze3d> mazeCache;
	
	public MyModel(Controller controller) {
		super(controller);
		mazeGen = new MyMaze3dGenerator();
		mazeCache = new HashMap<String, Maze3d>();
	}

	@Override
	public void list(String[] args) {
		if(args.length != 1) {
			controller.displayWrongArguments("dir <path>");
			return;
		}
		
		File f = new File(args[0]);
		
		if(!f.exists()) {
			controller.displayError(new String[] { "directory not found" });
			return;
		}
		
		if(!f.isDirectory()) {
			controller.displayError(new String[] { "path is not a directory" });
			return;
		}
		
		controller.displayFiles(f.list());
	}

	@Override
	public void generate3dMaze(String[] args) {
		if(args.length != 4) {
			controller.displayWrongArguments("generate 3d maze <name> <width> <height> <depth>");
			return;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Maze3d maze = mazeGen.generate(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
				mazeCache.remove(args[0]);
				mazeCache.put(args[0], maze);
				controller.displayAsyncMessage(new String[] { "maze " + args[0] + " is ready" });
			}
		}).start();
	}
}
