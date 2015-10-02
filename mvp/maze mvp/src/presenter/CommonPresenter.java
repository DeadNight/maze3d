package presenter;

import java.beans.XMLDecoder;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observable;

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

public abstract class CommonPresenter implements Presenter {
	Model model;
	View view;
	Properties properties;
	HashMap<String, Command> modelCommands;
	LinkedHashMap<String, Command> viewCommands;
	
	public CommonPresenter(Model model) throws FileNotFoundException, IOException {
		this.model = model;
		
		byte[] propertiesData = model.getPropertiesData();
		XMLDecoder xmlDecoder = new XMLDecoder(new ByteArrayInputStream(propertiesData));
		
		try {
			properties = (Properties) xmlDecoder.readObject();
		} finally {
			xmlDecoder.close();
		}
		
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
		model.start(properties.threadPoolSize);
		view.start();
	}
	
	@Override
	public ViewTypes getViewType() {
		return properties.getViewType();
	}

	private Maze3dGenerator getMazeGenerator(MazeGeneratorTypes mazeGenerator) {
		switch(mazeGenerator) {
		case MY:
			return new MyMaze3dGenerator();
		case SIMPLE:
			return new SimpleMaze3dGenerator();
		}
		return null;
	}
	
	private Searcher<Position> getMazeSearcher(MazeSearcherTypes mazeSearcher) {
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

	void initViewCommands() {
		viewCommands.put("exit", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.stop();
				view.stop();
			}
		});
	}
	
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
