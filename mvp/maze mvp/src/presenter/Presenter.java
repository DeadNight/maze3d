package presenter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.View;

public class Presenter implements Observer {
	Model model;
	View view;
	
	public Presenter(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(o == model) {
			String[] args = (String[]) arg;
			switch(args[0]) {
			case "error":
				handleModelError(Arrays.copyOfRange(args, 1, args.length));
				break;
			case "generated":
				handleGenerated(args[1]);
				break;
			}
		} else if(o == view) {
			
		}
	}

	private void handleModelError(String[] args) {
		switch(args[0]) {
		case "generate":
			view.displayError("error generating maze " + args[1]);
			break;
		}
	}

	private void handleGenerated(String name) {
		byte[] data;
		try {
			data = model.getMazeData(name);
		} catch (IOException e) {
			e.printStackTrace();
			view.displayError("error getting maze data");
			return;
		}
		if(data == null)
			view.displayError("maze not found");
		else
			view.displayMaze(data);
	}
}
