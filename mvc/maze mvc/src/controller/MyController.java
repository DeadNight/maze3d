package controller;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public class MyController extends CommonController {
	@Override
	protected void initCommands() {
		commands.put("dir", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.calculateFileList(args);
			}
		});
		
		commands.put("generate 3d maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.generate3dMaze(args);
			}
		});
		
		commands.put("display", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.get3dMaze(args);
			}
		});
		
		commands.put("display cross section by", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.getCrossSection(args);
			}
		});
		
		commands.put("save maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.sazeMaze(args);
			}
		});
		
		commands.put("load maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.loadMaze(args);
			}
		});
		
		commands.put("maze size", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.mazeSize(args);
			}
		});
		
		commands.put("file size", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.fileSize(args);
			}
		});
		
		commands.put("solve", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.solveMaze(args);
			}
		});
		
		commands.put("display solution", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.getSolution(args);
			}
		});
	}
	
	@Override
	public void stop() {
		view.displayMessage("shutting down...");
		model.stop();
		view.displayMessage("done");
	}

	@Override
	public void displayError(String error) {
		view.displayError(error);
	}
	
	@Override
	public void displayWrongArguments(String format) {
		view.displayError("unrecognized arguments" + System.lineSeparator() + format);
	}
	
	@Override
	public void displayFilesList(String[] list) {
		view.displayFiles(list);
	}

	@Override
	public void display3dMazeReady(String name) {
		view.displayMazeReady(name);
	}

	@Override
	public void display3dMaze(byte[] mazeData) {
		view.display3dMaze(mazeData);
	}

	@Override
	public void displayCrossSection(int[][] crossSection) {
		view.displayCrossSection(crossSection);
	}

	@Override
	public void display3dMazeSaved(String name) {
		view.display3dMazeSaved(name);
	}

	@Override
	public void display3dMazeLoaded(String name) {
		view.display3dMazeLoaded(name);
	}

	@Override
	public void displayMazeSize(int size) {
		view.displayMazeSize(size);
	}

	@Override
	public void displayFileSize(int size) {
		view.displayFileSize(size);
	}

	@Override
	public void displaySolutionReady(String name) {
		view.displaySolutionReady(name);
	}

	@Override
	public void displaySolution(Solution<Position> solution) {
		view.displaySolution(solution);
	}
}
