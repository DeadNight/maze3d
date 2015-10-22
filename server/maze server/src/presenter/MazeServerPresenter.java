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
		modelCommands.put("client connected", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				int clientId = (int)args[0];
				view.displayClientConnected(model.getClient(clientId));
				return null;
			}
		});
		
		modelCommands.put("client disconnected", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				int clientId = (int)args[0];
				view.displayClientDisconnected(clientId);
				return null;
			}
		});

		modelCommands.put("client command", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				int clientId = (int)args[0];
				view.displayClientUpdated(model.getClient(clientId));
				return null;
			}
		});
	}
}
