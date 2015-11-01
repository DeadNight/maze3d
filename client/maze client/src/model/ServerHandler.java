package model;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Nir
 * Defines a communication protocol
 */
public interface ServerHandler {
	/**
	 * Handle communication to a server.
	 * <br/>Override this to customize behavior.
	 * @param inFromServer server input stream
	 * @param outToServer server output stream
	 */
	public void handleServer(InputStream inFromServer, OutputStream outToServer);
}
