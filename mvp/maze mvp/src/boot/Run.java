package boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.function.Function;

import model.CommonModel;
import model.MyModel;
import presenter.MyPresenter;
import presenter.Presenter;
import presenter.ViewTypes;
import view.CLI;
import view.CommonView;
import view.GUI;
import view.View;

public class Run {
	public static void main(String[] args) {
		CommonModel model = new MyModel();
		CommonView[] viewContainer = new CommonView[1];
		
		Function<ViewTypes, View> createView = new Function<ViewTypes, View>() {
			@Override
			public View apply(ViewTypes t) {
				CommonView view;
				switch(t) {
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
					return null;
				}
				
				viewContainer[0] = view;
				return view;
			}
		};
		
		Presenter presenter;
		try {
			presenter = new MyPresenter(model, createView);
		} catch (IOException | URISyntaxException e) {
			System.err.println("run Setup.bat to create a defualt properties file");
			return;
		}
		
		model.addObserver(presenter);
		viewContainer[0].addObserver(presenter);
		
		presenter.start();
	}
}
