package presenter;

import java.util.Observer;

import view.View;

/**
 * @author Nir Leibovitch
 * <h1>Presenter layer Façade</h1>
 */
public interface Presenter extends Observer {
	/**
	 * Set the View Façade instance used by the application
	 * @param view View Façade instance
	 */
	void setView(View view);
	
	/**
	 * Start the application by starting the Model Façade instance &amp; the View Façade instance
	 */
	void start();
}
