package boot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import model.CommonModel;
import model.MyModel;
import presenter.MyPresenter;
import presenter.Presenter;
import view.CLI;
import view.CommonView;

public class Run {
	public static void main(String[] args) {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter out = new PrintWriter(System.out);
		
		CommonModel model = new MyModel();
		CommonView view = new CLI(in, out);
		
		Presenter presenter = new MyPresenter(view, model);
		
		model.addObserver(presenter);
		view.addObserver(presenter);
		
		presenter.start();
	}
}
