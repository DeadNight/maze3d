package presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observable;
import java.util.function.Function;

import model.Model;
import view.View;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;
import algorithms.search.AStarSearcher;
import algorithms.search.BFSearcher;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;

/**
 * @author Nir Leibovitch
 * <h1>Common implementation of the Presenter Façade</h1>
 */
public abstract class CommonPresenter implements Presenter {
	final public static String PROPERTIES_FILE_NAME = "properties.xml";
	
	Model model;
	View view;
	HashMap<String, Command> modelCommands;
	LinkedHashMap<String, Command> viewCommands;
	
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
	public CommonPresenter(Model model, Function<ViewTypes, View> createView) throws URISyntaxException, FileNotFoundException, IOException {
		this.model = model;
		
		try {
			model.loadProperties(PROPERTIES_FILE_NAME);
		} catch(URISyntaxException | FileNotFoundException e) {
			System.err.println(PROPERTIES_FILE_NAME + " not found");
			throw e;
		} catch (IOException e) {
			System.err.println("error loading " + PROPERTIES_FILE_NAME);
			throw e;
		}
		
		Properties properties = model.getProperties();
		view = createView.apply(properties.getViewType());
		
		model.setMazeGenerator(getMazeGenerator(properties.getMazeGeneratorType()));
		model.setMazeSearchAlgorithm(getMazeSearcher(properties.getMazeSearcherType()));
		
		viewCommands = new LinkedHashMap<String, Command>();
		modelCommands = new HashMap<String, Command>();
		
		initViewCommands();
		initModelCommands();
	}
	
	@Override
	public void setView(View view) {
		this.view = view;
	}

	@Override
	public void start() {
		model.start(model.getProperties().threadPoolSize);
		view.start();
	}

	/**
	 * Utility method to create an appropriate maze generator of the given type
	 * @param mazeGenerator Type of maze generator
	 * @return Maze3dGenerator Maze generator instance
	 * @see MazeGeneratorTypes
	 */
	Maze3dGenerator getMazeGenerator(MazeGeneratorTypes mazeGenerator) {
		switch(mazeGenerator) {
		case MY:
			return new MyMaze3dGenerator();
		case SIMPLE:
			return new SimpleMaze3dGenerator();
		}
		return null;
	}
	
	/**
	 * Utility method to create an appropriate maze Searcher of the given type
	 * @param mazeSearcher Type of maze searcher
	 * @return Searcher<Position> Maze searcher instance
	 * @see MazeSearcherTypes
	 */
	Searcher<Position> getMazeSearcher(MazeSearcherTypes mazeSearcher) {
		switch(mazeSearcher) {
		case A_STAR_AIR:
			return new AStarSearcher<Position>(new MazeAirDistance());
		case A_STAR_MANHATTER:
			return new AStarSearcher<Position>(new MazeManhattanDistance());
		case BFS:
			return new BFSearcher<Position>();
		}
		return null;
	}

	/**
	 * Populate view commands map. Define the "exit" command by default.
	 * <br>Override to populate the view commands map.
	 * @see Command
	 */
	void initViewCommands() {
		viewCommands.put("exit", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.stop();
				view.stop();
			}
		});
	}
	
	/**
	 * Populate model commands map.
	 * @see Command
	 */
	abstract void initModelCommands();
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == model) {
			String[] args = (String[]) arg;
			String cmd = args[0];
			String[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
			modelCommands.get(cmd).doCommand(cmdArgs);
		} else if(o == view) {
			String userCommand = view.getUserCommand();
			for(String commandName : viewCommands.keySet()) {
				if(userCommand.startsWith(commandName)) {
					Command command = viewCommands.get(commandName);
					String rest = userCommand.substring(commandName.length()).trim();
					String[] args;
					if(rest.equals(""))
						args = new String[0];
					else
						args = rest.split(" ");
					if(command.verifyParams(args))
						command.doCommand(args);
					else
						view.displayCommandTemplate(commandName, command.getTemplate());
					return;
				}
			}
			view.displayUnknownCommand();
		}
	}
}
