package boot;

import model.Model;
import model.MyModel;
import view.MyView;
import view.View;
import controller.Controller;
import controller.MyController;

/**
 * @author Nir Leibovitch
 * Used to run the application
 */
public class Run {
	/**
	 * Initialize the MVC design and start the application
	 * @param args Command line arguments
	 */
	public static void main(String[] args) {
		Controller controller = new MyController();
		Model model = new MyModel(controller, 10);
		View view = new MyView(controller);
		controller.setModel(model);
		controller.setView(view);
		view.start();
	}
}
