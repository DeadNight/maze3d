package presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

import model.Model;
import view.View;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

/**
 * @author Nir Leibovitch
 * <h1>My implementation of the Presenter Façade</h1>
 */
public class MyPresenter extends CommonPresenter {
	/**
	 * Initiate the Presenter Façade instance, load properties, create the View Façade instance to be used by the
	 * application, set the Model Façade instance properties &amp; initialize command maps
	 * @param model Model Façade instance
	 * @param createView Function to create the view to be used by the application according to
	 * properties file
	 * @throws URISyntaxException When the properties file path can't be parsed
	 * @throws FileNotFoundException When the properties file can't be opened for reading
	 * @throws IOException When an error occurs while reading the properties file
	 */
	public MyPresenter(Model model, Function<ViewTypes, View> createView) throws URISyntaxException, FileNotFoundException, IOException {
		super(model, createView);
	}
	
	@Override
	void initViewCommands() {
		super.initViewCommands();
		
		viewCommands.put("dir", new Command() {
			@Override
			public String getTemplate() {
				return "<path>";
			};
			
			@Override
			public void doCommand(String[] args) {
				String path = args[0];
				model.listFiles(path);
			}
		});
		
		viewCommands.put("generate 3d maze", new Command() {
			@Override
			public String getTemplate() {
				return "<name> <width> <height> <depth>";
			};
			
			@Override
			public boolean verifyParams(String[] args) {
				if(!super.verifyParams(args)) return false;
				try {
					Integer.parseInt(args[1]);
					Integer.parseInt(args[2]);
					Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					return false;
				}
				return true;
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				int width = Integer.parseInt(args[1]);
				int height = Integer.parseInt(args[2]);
				int depth = Integer.parseInt(args[3]);
				model.generateMaze(name, width, height, depth);
			}
		});
		
		viewCommands.put("display cross section by", new Command() {
			@Override
			public String getTemplate() {
				return "{X,Y,Z} <index> for <name>";
			}
			
			@Override
			public boolean verifyParams(String[] args) {
				if(!super.verifyParams(args)) return false;
				
				if(!(args[0].equals("X") || args[0].equals("Y") || args[0].equals("Z")))
					return false;
				
				try {
					Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					return false;
				}
				
				return args[2].equals("for");
			}
			
			@Override
			public void doCommand(String[] args) {
				String axis = args[0];
				Integer index = Integer.parseInt(args[1]);
				String name = args[3];
				model.calculateCrossSection(name, axis, index);
			}
		});
		
		viewCommands.put("display solution from", new Command() {
			@Override
			public String getTemplate() {
				return "<name> <x> <y> <z>";
			}
			
			@Override
			public boolean verifyParams(String[] args) {
				if(!super.verifyParams(args)) return false;
				try {
					Integer.parseInt(args[1]);
					Integer.parseInt(args[2]);
					Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					return false;
				}
				return true;
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int z = Integer.parseInt(args[3]);
				Solution<Position> solution = model.getMazeSolution(name, x, y, z);
				if(solution != null)
					view.displayMazeSolution(solution);
			}
		});
		
		viewCommands.put("display solution", new Command() {
			@Override
			public String getTemplate() {
				return "<name>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				Solution<Position> solution = model.getMazeSolution(name);
				if(solution != null)
					view.displayMazeSolution(solution);
			}
		});
		
		viewCommands.put("display", new Command() {
			@Override
			public String getTemplate() {
				return "<name>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				byte[] data;
				try {
					data = model.getMazeData(name);
				} catch (IOException e) {
					e.printStackTrace();
					view.displayGetMazeError();
					return;
				}
				if(data == null)
					view.displayMazeNotFound();
				else
					view.displayMaze(data);
			}
		});
		
		viewCommands.put("save maze", new Command() {
			@Override
			public String getTemplate() {
				return "<name> <filename>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				String fileName = args[1];
				model.saveMaze(name, fileName);
			}
		});
		
		viewCommands.put("load maze", new Command() {
			@Override
			public String getTemplate() {
				return "<filename> <name>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String fileName = args[0];
				String name = args[1];
				model.loadMaze(fileName, name);
			}
		});
		
		viewCommands.put("maze size", new Command() {
			@Override
			public String getTemplate() {
				return "<name>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				model.calculateMazeSize(name);
			}
		});
		
		viewCommands.put("file size", new Command() {
			@Override
			public String getTemplate() {
				return "<name>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				model.calculateFileSize(name);
			}
		});
		
		viewCommands.put("solve from", new Command() {
			@Override
			public String getTemplate() {
				return "<name> <x> <y> <z>";
			}
			
			@Override
			public boolean verifyParams(String[] args) {
				if(!super.verifyParams(args)) return false;
				try {
					Integer.parseInt(args[1]);
					Integer.parseInt(args[2]);
					Integer.parseInt(args[3]);
				} catch (NumberFormatException e) {
					return false;
				}
				return true;
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				int x = Integer.parseInt(args[1]);
				int y = Integer.parseInt(args[2]);
				int z = Integer.parseInt(args[3]);
				model.solveMaze(name, x, y, z);
			}
		});
		
		viewCommands.put("solve", new Command() {
			@Override
			public String getTemplate() {
				return "<name>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				model.solveMaze(name);
			}
		});
		
		viewCommands.put("get properties", new Command() {
			@Override
			public void doCommand(String[] args) {
				try {
					model.loadProperties(PROPERTIES_FILE_NAME);
				} catch (IOException | URISyntaxException e) {
					// ignore - will be handled by modelCommands
				}
			}
		});
		
		viewCommands.put("load properties", new Command() {
			@Override
			public String getTemplate() {
				return "<fileName>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String fileName = args[0];
				try {
					model.loadProperties(fileName);
				} catch (IOException | URISyntaxException e) {
					// ignore - will be handled by modelCommands
				}
			}
		});
		
		viewCommands.put("edit properties", new Command() {
			@Override
			public String getTemplate() {
				return "<poolSize> <mazeGenerator> <mazeSearcher> <viewType>";
			}
			
			@Override
			public boolean verifyParams(String[] args) {
				if(!super.verifyParams(args)) return false;
				try {
					Integer.parseInt(args[0]);
					MazeGeneratorTypes.valueOf(args[1]);
					MazeSearcherTypes.valueOf(args[2]);
					ViewTypes.valueOf(args[3]);
				} catch (IllegalArgumentException e) {
					return false;
				}
				return true;
			}
			
			@Override
			public void doCommand(String[] args) {
				int poolSize = Integer.parseInt(args[0]);
				MazeGeneratorTypes generator = MazeGeneratorTypes.valueOf(args[1]);
				MazeSearcherTypes searcher = MazeSearcherTypes.valueOf(args[2]);
				ViewTypes viewType = ViewTypes.valueOf(args[3]);
				model.saveProperties(PROPERTIES_FILE_NAME, poolSize, generator, searcher, viewType);
			}
		});
		
		viewCommands.put("save properties", new Command() {
			@Override
			public String getTemplate() {
				return "<fileName>";
			}
			
			@Override
			public void doCommand(String[] args) {
				String fileName = args[0];
				Properties properties = model.getProperties();
				model.saveProperties(fileName, properties.getPoolSize()
						, properties.getMazeGeneratorType(), properties.getMazeSearcherType()
						, properties.getViewType());
			}
		});
	}

	@Override
	void initModelCommands() {
		modelCommands.put("properties not found", new Command() {
			@Override
			public void doCommand(String[] args) {
				if(view != null)
					view.displayFileNotFound();
			}
		});
		
		modelCommands.put("maze generated", new Command() {
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				view.displayMazeGenerated(name);
			}
		});
		
		modelCommands.put("files listed", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayFilesList(model.getFilesList());
			}
		});
		
		modelCommands.put("cross section generated", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayCrossSection(model.getCrossSection());
			}
		});
		
		modelCommands.put("maze saved", new Command() {
			@Override
			public String getTemplate() {
				return "<fileName>";
			}
			
			@Override
			public void doCommand(String[] args) {
				view.displayMazeSaved();
			}
		});
		
		modelCommands.put("maze loaded", new Command() {
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				view.displayMazeLoaded(name);
			}
		});
		
		modelCommands.put("maze size calculated", new Command() {
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				view.displayMazeSize(model.getMazeSize(name));
			}
		});
		
		modelCommands.put("file size calculated", new Command() {
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				view.displayFileSize(model.getFileSize(name));
			}
		});
		
		modelCommands.put("maze solved", new Command() {
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				view.displayMazeSolved(name);
			}
		});
		
		modelCommands.put("maze not found", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayMazeNotFound();
			}
		});
		
		modelCommands.put("directory not found", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayDirectoryNotFound();
			}
		});
		
		modelCommands.put("not a directory", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayNotDirectory();
			}
		});
		
		modelCommands.put("index out of range", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayIndexOutOfRange();
			}
		});
		
		modelCommands.put("invalid axis", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayInvalidAxis();
			}
		});
		
		modelCommands.put("error saving maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displaySaveMazeError();
			}
		});
		
		modelCommands.put("file not found", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayFileNotFound();
			}
		});
		
		modelCommands.put("file name error", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayFileNameError();
			}
		});
		
		modelCommands.put("error loading maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayLoadMazeError();
			}
		});
		
		modelCommands.put("decompression error", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayDecompressionError();
			}
		});
		
		modelCommands.put("solve maze error", new Command() {
			@Override
			public void doCommand(String[] args) {
				String name = args[0];
				view.displaySolveMazeError(name);
			}
		});
		
		modelCommands.put("solution not found", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayMazeSolutionNotFound();
			}
		});
		
		modelCommands.put("properties loaded", new Command() {
			@Override
			public void doCommand(String[] args) {
				view.displayProperties(model.getProperties());
			}
		});
		
		modelCommands.put("properties saved", new Command() {
			@Override
			public void doCommand(String[] args) {
				Properties properties = model.getProperties();
				model.setMazeGenerator(getMazeGenerator(properties.getMazeGeneratorType()));
				view.displayPropertiesSaved();
			}
		});
	}
}
