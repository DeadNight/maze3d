package presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.function.Function;

import algorithms.mazeGenerators.Position;
import algorithms.search.AStarSearcher;
import algorithms.search.BFSearcher;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;
import common.MazeSearcherTypes;
import common.Properties;
import model.CommonModel;
import model.Model;
import view.CommonView;
import view.View;

public abstract class CommonPresenter implements Presenter {
	final static String PROPERTIES_FILE_NAME = "properties.xml";
	Model model;
	View view;
	
	HashMap<String, Function<Object[], Void>> modelCommands;
	HashMap<String, Function<String[], Void>> viewCommands;
	
	public CommonPresenter(CommonModel model, CommonView view) throws URISyntaxException, FileNotFoundException, IOException {
		this.model = model;
		this.view = view;
		
		try {
			model.loadProperties(PROPERTIES_FILE_NAME);
		} catch(URISyntaxException | FileNotFoundException e) {
			System.err.println("properties.xml not found");
			throw e;
		} catch (IOException e) {
			System.err.println("error loading properties.xml");
			throw e;
		}
		
		Properties properties = model.getProperties();
		model.setMazeSearchAlgorithm(getMazeSearcher(properties.getMazeSearcherType()));
		
		modelCommands = new HashMap<String, Function<Object[],Void>>();
		initModelCommands();
		
		viewCommands = new HashMap<String, Function<String[],Void>>();
		initViewCommands();
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
	 * Populate model commands map.
	 * @see Command
	 */
	abstract void initModelCommands();
	
	/**
	 * Populate view commands map. Define the "exit" command by default.
	 * <br>Override to populate the view commands map.
	 * @see Command
	 */
	void initViewCommands() {
		viewCommands.put("exit", new Function<String[], Void>() {
			@Override
			public Void apply(String[] args) {
				view.displayShuttingDown();
				model.stop();
				view.stop();
				return null;
			}
		});
	}
	
	@Override
	public void start() {
		Properties properties = model.getProperties();
		model.start(properties.getPort(), properties.getNumOfClients(), properties.getSocketTimeout()
				, properties.getPoolSize());
		view.start();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == model) {
			Object[] args = (Object[]) arg;
			String cmd = (String)args[0];
			Object[] cmdArgs = Arrays.copyOfRange(args, 1, args.length);
			modelCommands.get(cmd).apply(cmdArgs);
		} else if(o == view) {
			String userCommand = view.getUserCommand();
			for(String commandName : viewCommands.keySet()) {
				if(userCommand.startsWith(commandName)) {
					String rest = userCommand.substring(commandName.length()).trim();
					String[] args;
					if(rest.equals(""))
						args = new String[0];
					else
						args = rest.split(" ");
					viewCommands.get(commandName).apply(args);
					return;
				}
			}
			view.displayUnknownCommand();
		}
	}
}
