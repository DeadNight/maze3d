package presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Function;

import model.CommonModel;
import view.CommonView;

public class MazeServerPresenter extends CommonPresenter {
	public MazeServerPresenter(CommonModel model, CommonView view) throws URISyntaxException, FileNotFoundException, IOException {
		super(model, view);
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
	}
	
	@Override
	public void start() {
		view.updateServerStats(model.getServerStats());
		super.start();
	}
}
