package presenter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Observable;
import java.util.function.Function;

import model.CommonModel;
import model.Model;
import view.CommonView;
import view.View;

public abstract class CommonPresenter implements Presenter {
	Model model;
	View view;
	
	HashMap<String, Function<Object[], Void>> modelCommands;
	HashMap<String, Function<Object[], Void>> viewCommands;
	
	public CommonPresenter(CommonModel model, CommonView view) {
		this.model = model;
		this.view = view;
		
		modelCommands = new HashMap<String, Function<Object[],Void>>();
		initModelCommands();
		
		viewCommands = new HashMap<String, Function<Object[],Void>>();
		initViewCommands();
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
		viewCommands.put("exit", new Function<Object[], Void>() {
			@Override
			public Void apply(Object[] args) {
				view.displayShuttingDown();
				model.stop();
				view.stop();
				return null;
			}
		});
	}
	
	@Override
	public void start() {
		//TODO: take from properties
		model.start(5400, 10, 20*1000, 10);
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
