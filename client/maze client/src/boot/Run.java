package boot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.function.Function;

import common.ViewTypes;
import model.CommonModel;
import model.MazeClientModel;
import presenter.MazeClientPresenter;
import presenter.Presenter;
import view.CLI;
import view.CommonView;
import view.GUI;
import view.View;

/**
 * @author Nir Leibovitch
 * <h1>Start maze client application</h1>
 */
public class Run {
	// declare the view as a static class member for use inside Function#apply()
	static private CommonView view;
	
	/**
	 * Wire up MVP, define available views &amp; start the presenter
	 * @param args Startup arguments
	 */
	public static void main(String[] args) {
		CommonModel model = new MazeClientModel();
		Function<ViewTypes, View> createView = new Function<ViewTypes, View>() {
			@Override
			public View apply(ViewTypes t) {
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
					System.out.println("Press return to exit...");
					try {
						new BufferedReader(new InputStreamReader(System.in)).readLine();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					return null;
				}
				
				return view;
			}
		};
		
		Presenter presenter;
		try {
			presenter = new MazeClientPresenter(model, createView);
		} catch (IOException | URISyntaxException e) {
			System.err.println("run Setup.bat to create a defualt properties file");
			System.out.println("Press return to exit...");
			try {
				new BufferedReader(new InputStreamReader(System.in)).readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		presenter.start();
	}
}
