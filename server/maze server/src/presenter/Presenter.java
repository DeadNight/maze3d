package presenter;

import java.util.Observer;

/**
 * @author Nir Leibovitch
 * <h1>Presenter layer Façade</h1>
 */
public interface Presenter extends Observer {
	/**
	 * Start the application by starting the Model Façade instance &amp; the View Façade instance
	 */
	void start();
}
