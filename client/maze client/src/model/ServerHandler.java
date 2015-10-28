package model;

import java.io.InputStream;
import java.io.OutputStream;

public interface ServerHandler {
	public void handleServer(InputStream inFromServer, OutputStream outToServer);
}
