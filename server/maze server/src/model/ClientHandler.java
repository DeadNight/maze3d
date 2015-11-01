package model;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Nir
 * Defines a communication protocol
 */
public interface ClientHandler {
	/**
	 * Handle communication to a client.
	 * <br>Override this to customize behavior.
	 * @param inFromClient client input stream
	 * @param outToClient client output stream
	 */
	public void handleClient(InputStream inFromClient, OutputStream outToClient);
}
