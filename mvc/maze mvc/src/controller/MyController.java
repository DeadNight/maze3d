package controller;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public class MyController extends CommonController {
	@Override
	protected void initCommands() {
		commands.put("dir", new Command() {
			@Override
			public void doCommand(String[] args) {
				String[] list = model.getFileList(args);
				if(list != null)
					view.displayFiles(list);
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
				byte[] mazeData = model.getMaze3d(args);
				if(mazeData != null)
					view.displayMaze3d(mazeData);
			}
		});
		
		commands.put("display cross section by", new Command() {
			@Override
			public void doCommand(String[] args) {
				int[][] crossSection = model.getCrossSection(args);
				if(crossSection != null)
					view.displayCrossSection(crossSection);
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
				int size = model.mazeSize(args);
				if(size > -1)
					view.displayMazeSize(size);
			}
		});
		
		commands.put("file size", new Command() {
			@Override
			public void doCommand(String[] args) {
				int size = model.fileSize(args);
				if(size > -1)
					view.displayFileSize(size);
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
				Solution<Position> solution = model.getSolution(args);
				if(solution != null)
					view.displaySolution(solution);
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
	public void displayMessage(String message) {
		view.displayMessage(message);
	}
}
