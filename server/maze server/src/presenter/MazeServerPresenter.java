package presenter;

import java.util.function.Function;

import model.CommonModel;
import view.CommonView;

public class MazeServerPresenter extends CommonPresenter {
	public MazeServerPresenter(CommonModel model, CommonView view) {
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
		
		modelCommands.put("solve request", noOp);
		modelCommands.put("read searchable error", noOp);
		modelCommands.put("solving", updateServerStats);
		modelCommands.put("no solution", updateServerStats);
		modelCommands.put("solved", updateServerStats);
	}
}
