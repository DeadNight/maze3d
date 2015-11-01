package boot;

import java.io.IOException;
import java.net.URISyntaxException;

import model.CommonModel;
import model.MazeServerModel;
import presenter.MazeServerPresenter;
import presenter.Presenter;
import view.CommonView;
import view.GUI;

/**
 * @author Nir Leibovitch
 * <h1>Start maze server application</h1>
 */
public class Run {
	/**
	 * Wire up MVP &amp; start the presenter
	 * @param args Startup arguments
	 */
	public static void main(String[] args) {
		CommonModel model = new MazeServerModel();
		CommonView view = new GUI();
		
		Presenter presenter;
		try {
			presenter = new MazeServerPresenter(model, view);
		} catch (IOException | URISyntaxException e) {
			System.err.println("run Setup.bat to create a defualt properties file");
			return;
		}
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		presenter.start();
	}
}
