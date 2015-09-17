package boot;

import java.io.FileNotFoundException;

import model.CommonModel;
import model.MyModel;
import presenter.Presenter;
import view.CLI;
import view.CommonView;

public class Run {
	public static void main(String[] args) {
		CommonModel model;
		try {
			model = new MyModel();
		} catch(ArrayIndexOutOfBoundsException | FileNotFoundException e) {
			System.err.println("error while creating MyModel");
			return;
		}
		
		CommonView view = new CLI();
		Presenter presenter = new Presenter(model, view);
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		view.start();
	}
}
