package boot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.CommonModel;
import model.MyModel;
import presenter.MyPresenter;
import presenter.Presenter;
import view.CLI;
import view.CommonView;
import view.GUI;

public class Run {
	public static void main(String[] args) {
		CommonModel model = new MyModel();
		Presenter presenter;
		
		try {
			presenter = new MyPresenter(model);
		} catch (FileNotFoundException e) {
			System.err.println("properties.xml not found");
			System.err.println("run Setup.bat to create a defualt properties file");
			return;
		} catch (IOException e) {
			System.err.println("error loading properties.xml");
			System.err.println("run Setup.bat to create a defualt properties file");
			return;
		}
		
		CommonView view;
		switch(presenter.getViewType()) {
		case CLI:
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter out = new PrintWriter(System.out);
			view = new CLI(in, out);
			break;
		case GUI:
			view = new GUI();
			break;
		default:
			System.err.println("missing view type");
			System.err.println("run Setup.bat to create a defualt properties file");
			return;
		}
		
		presenter.setView(view);
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		presenter.start();
	}
}
