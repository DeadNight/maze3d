package view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public abstract class BasicWindow implements Runnable {
	boolean ownDisplay = false;
	Display display;
	Shell shell;
	
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
	
	public void close() {
		shell.dispose();
	}

	public void displayError(String title, String message) {
		MessageBox messageBox = new MessageBox(shell, SWT.ERROR);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}
}
