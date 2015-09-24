package controller;

import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * @author nirleibo
 * <h1>My implementation of the Controller Façade</h1>
 */
public class MyController extends CommonController {
	@Override
	protected void initCommands() {
		commands.put("dir", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 1) {
					displayWrongArguments("dir <path>");
					return;
				}
				String path = args[0];
				
				model.calculateFileList(path);
			}
		});
		
		commands.put("generate 3d maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				String format = "generate 3d maze <name> <width> <height> <depth>";
				if(args.length != 4) {
					displayWrongArguments(format);
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
					displayWrongArguments(format);
					return;
				}
				
				model.generate3dMaze(name, width, height, depth);
			}
		});
		
		commands.put("display", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 1) {
					displayWrongArguments("display <name>");
					return;
				}
				String name = args[0];
				
				model.get3dMaze(name);
			}
		});
		
		commands.put("display cross section by", new Command() {
			@Override
			public void doCommand(String[] args) {
				String format = "display cross section by <axis> <index> for <name>";
				if(args.length != 4 || !args[2].equals("for")) {
					displayWrongArguments(format);
					return;
				}
				
				String axis = args[0];
				int index;
				String name = args[3];
				
				try {
					index = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					displayWrongArguments(format);
					return;
				}
				
				model.getCrossSection(name, axis, index);
			}
		});
		
		commands.put("save maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 2) {
					displayWrongArguments("load maze <name> <file name>");
					return;
				}
				
				String name = args[0];
				String fileName = args[1];
				
				model.sazeMaze(name, fileName);
			}
		});
		
		commands.put("load maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 2) {
					displayWrongArguments("load maze <file name> <name>");
					return;
				}
				
				String fileName = args[0];
				String name = args[1];
				
				model.loadMaze(fileName, name);
			}
		});
		
		commands.put("maze size", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 1) {
					displayWrongArguments("maze size <name>");
					return;
				}
				
				String name = args[0];
				
				model.calculateMazeSize(name);
			}
		});
		
		commands.put("file size", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 1) {
					displayWrongArguments("maze size <name>");
					return;
				}
				
				String name = args[0];
				
				model.calculateFileSize(name);
			}
		});
		
		commands.put("solve", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 2) {
					displayWrongArguments("solve <name> <algorithm>");
					return;
				}
				
				String name = args[0];
				String algorithmName = args[1];
				
				model.solveMaze(name, algorithmName);
			}
		});
		
		commands.put("display solution", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(args.length != 1) {
					displayWrongArguments("display solution <name>");
					return;
				}
				
				String name = args[0];
				
				model.getSolution(name);
			}
		});
	}
	
	@Override
	public void stop() {
		view.displayShuttingDown();
		model.stop();
		view.displayShutdown();
	}

	@Override
	public void displayError(String error) {
		view.displayError(error);
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
	
	/**
	 * Display wrong format error
	 * @param format The expected format
	 */
	private void displayWrongArguments(String format) {
		view.displayError("unrecognized arguments" + System.lineSeparator() + format);
	}
}
