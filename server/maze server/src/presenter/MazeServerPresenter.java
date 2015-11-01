package presenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

import common.MazeSearcherTypes;
import common.Properties;
import model.CommonModel;
import view.CommonView;

/**
 * @author Nir Leibovitch
 * <h1>Maze server implementation of the Presenter Façade</h1>
 */
public class MazeServerPresenter extends CommonPresenter {
	/**
	 * Initiate the Presenter Façade instance, load properties, set the View &amp; Model Façades
	 * instances, set the Model Façade instance properties &amp; initialize the command maps
	 * @param model Model Façade instance
	 * @param view View Façade instance
	 * @throws URISyntaxException When the properties file path can't be parsed
	 * @throws FileNotFoundException When the properties file can't be opened for reading
	 * @throws IOException When an error occurs while reading the properties file
	 */
	public MazeServerPresenter(CommonModel model, CommonView view) throws URISyntaxException, FileNotFoundException, IOException {
		super(model, view);
	}
	
	@Override
	void initViewCommands() { 
		super.initViewCommands();
		
		viewCommands.put("get properties", new Function<String[], Void>() {
			@Override
			public Void apply(String[] args) {
				try {
					model.loadProperties(new File(PROPERTIES_FILE_NAME).toURI().toString());
				} catch (IOException | URISyntaxException e) {
					// ignore - will be handled by modelCommands
				}
				return null;
			}
		});
		
		viewCommands.put("load properties", new Function<String[], Void>() {
			@Override
			public Void apply(String[] args) {
				String fileName = args[0];
				try {
					model.loadProperties(fileName);
				} catch (IOException | URISyntaxException e) {
					// ignore - will be handled by modelCommands
				}
				return null;
			}
		});
		
		viewCommands.put("edit properties", new Function<String[], Void>() {
			@Override
			public Void apply(String[] args) {
				int port = Integer.parseInt(args[0]);
				int numOfClients = Integer.parseInt(args[1]);
				int socketTimeout = Integer.parseInt(args[2]);
				int poolSize = Integer.parseInt(args[3]);
				MazeSearcherTypes mazeSearcherType = MazeSearcherTypes.valueOf(args[4]);
				model.saveProperties(PROPERTIES_FILE_NAME, port, numOfClients, socketTimeout, poolSize
						, mazeSearcherType);
				return null;
			}
		});
		
		viewCommands.put("save properties", new Function<String[], Void>() {
			@Override
			public Void apply(String[] args) {
				String fileName = args[0];
				Properties properties = model.getProperties();
				model.saveProperties(fileName, properties.getPort(), properties.getNumOfClients()
						, properties.getSocketTimeout(), properties.getPoolSize()
						, properties.getMazeSearcherType());
				return null;
			}
		});
	}

	@Override
	void initModelCommands() {
		Function<Object[], Void> noOp = new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) { return null; }
		};
		
		Function<Object[], Void> updateServerStats = new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				view.updateServerStats(model.getServerStats());
				return null;
			}
		};
		
		Function<Object[], Void> updateClient = new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				int clientId = (int)args[0];
				view.updateClient(model.getClient(clientId));
				updateServerStats.apply(null);
				return null;
			}
		};
		
		modelCommands.put("client connected", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				int clientId = (int)args[0];
				view.displayClientConnected(model.getClient(clientId));
				updateServerStats.apply(null);
				return null;
			}
		});
		
		modelCommands.put("client disconnected", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				int clientId = (int)args[0];
				view.displayClientDisconnected(clientId);
				updateServerStats.apply(null);
				return null;
			}
		});
		
		modelCommands.put("solve request", updateClient);
		modelCommands.put("recieve searchable error", noOp);
		modelCommands.put("solving", updateClient);
		modelCommands.put("no solution", updateClient);
		modelCommands.put("solved", updateClient);
		modelCommands.put("send solution error", noOp);
		
		modelCommands.put("properties not found", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				if(view != null)
					view.displayFileNotFound();
				return null;
			}
		});
		
		modelCommands.put("properties loaded", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				view.displayProperties(model.getProperties());
				return null;
			}
		});
		
		modelCommands.put("properties saved", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				Properties properties = model.getProperties();
				model.setMazeSearchAlgorithm(getMazeSearcher(properties.getMazeSearcherType()));
				view.displayPropertiesSaved();
				return null;
			}
		});
	}
	
	@Override
	public void start() {
		view.updateServerStats(model.getServerStats());
		super.start();
	}
}
