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
	}

	@Override
	public void displayError(String[] strings) {
		view.displayError(strings);
	}

	@Override
	public void displayFiles(String[] list) {
		view.displayFiles(list);
	}
}
