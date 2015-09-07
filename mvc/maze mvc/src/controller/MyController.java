package controller;

public class MyController extends CommonController {
	@Override
	protected void initCommands() {
		commands.put("dir", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.list(args);
			}
		});
		
		commands.put("generate 3d maze", new Command() {
			@Override
			public void doCommand(String[] args) {
				model.generate3dMaze(args);
			}
		});
	}

	@Override
	public void displayError(String[] strings) {
		view.displayError(strings);
	}

	@Override
	public void displayFiles(String[] list) {
		view.displayFiles(list);
	}

	@Override
	public void displayWrongArguments(String format) {
		view.displayError(new String[] { "unrecognized arguments", format });
	}

	@Override
	public void displayAsyncMessage(String[] strings) {
		view.displayAsyncMessage(strings);
	}
}
