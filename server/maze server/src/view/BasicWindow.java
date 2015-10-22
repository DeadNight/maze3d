package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eliahu Khalastchi
 * @author Nir Leibovitch
 * <h1>Basic SWT Window</h1>
 * Create a window and open it by calling {@link BasicWindow#run()}
 */
public abstract class BasicWindow implements Runnable {
	boolean ownDisplay = false;
	Display display;
	Shell shell;
	
 	/**
 	 * Instantiate or reuse a Display, then instantiate a Shell &amp; the window with the given title &amp; dimensions
 	 * @param title Title of the window
 	 * @param width Width of the window
 	 * @param height Height of the window
 	 */
 	public BasicWindow(String title, int width,int height) {
 		display = Display.getCurrent();
 		if(display == null) {
 			display = new Display();
 			ownDisplay = true;
 		}
		
		shell = new Shell(display);
 		shell.setSize(width, height);
 		shell.setText(title);
 		
 		initWidgets();
	}
 	
 	/**
 	 * Initialize window widgets. Does nothing by defualt.
 	 * <br>Override this to customise the window.
 	 */
 	abstract void initWidgets();

	@Override
	public void run() {
		shell.open();
		
		// main event loop
		while(!shell.isDisposed()){ // while window isn't closed
			// 1. read events, put then in a queue.
		    // 2. dispatch the assigned listener
		    if(!display.readAndDispatch()){ // if the queue is empty
		       display.sleep(); // sleep until an event occurs 
		    }

		} // shell is disposed

		if(ownDisplay)
			display.dispose(); // dispose OS components
	}
	
	/**
	 * Dispose the shell &amp; close the window
	 */
	public void close() {
		shell.dispose();
	}
	
	/**
	 * Display an info message box with the given title &amp; message
	 * @param title Message box title
	 * @param message Message box message
	 */
	public void displayInfo(String title, String message) {
		displayMessage(SWT.ICON_INFORMATION, title, message);
	}
	
	/**
	 * Display an error message box with the given title &amp; message
	 * @param title Message box title
	 * @param message Message box message
	 */
	public void displayError(String title, String message) {
		displayMessage(SWT.ICON_ERROR, title, message);
	}

	/**
	 * Display a message box with the given icon, title &amp; message
	 * @param icon Message box icon
	 * @param title Message box title
	 * @param message Message box message
	 */
	public void displayMessage(int icon, String title, String message) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox messageBox = new MessageBox(shell, icon);
				messageBox.setText(title);
				messageBox.setMessage(message);
				messageBox.open();
			}
		});
	}

	/**
	 * Display a message box with the given title &amp; message with yes &amp; no buttons 
	 * @param title Message box title
	 * @param message Message box message
	 * @return int Response of the message box
	 */
	public int displayQuestion(String title, String message) {
		Response<Integer> response = new Response<Integer>();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText(title);
				messageBox.setMessage(message);
				response.set(messageBox.open());
			}
		});
		return response.get();
	}
	
	/**
	 * Display a file dialog with the given style, title &amp; extensions filter
	 * @param style File dialog style
	 * @param title File dialog title
	 * @param filterExtensions File dialog extensions filter
	 * @return String Path of selected file
	 */
	public String displayFileDialog(int style, String title, String[] filterExtensions) {
		Response<String> response = new Response<String>();
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				FileDialog fileDialog = new FileDialog(shell, style);
				fileDialog.setText(title);
				fileDialog.setFilterExtensions(filterExtensions);
				response.set(fileDialog.open());
			}
		});
		return (String) response.get();
	}
	
	// encapsulate a value for use inside Runnable#run()
	static private class Response<T> {
		private T value;
		public void set(T value) { this.value = value; }
		public T get() { return value; }
	}
}
