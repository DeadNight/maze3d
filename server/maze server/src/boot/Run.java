package boot;

import model.CommonModel;
import model.MazeServerModel;
import presenter.MazeServerPresenter;
import presenter.Presenter;
import view.CommonView;
import view.GUI;

public class Run {
	public static void main(String[] args) {
		CommonModel model = new MazeServerModel();
		CommonView view = new GUI();
		Presenter presenter = new MazeServerPresenter(model, view);
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		presenter.start();
	}
}
