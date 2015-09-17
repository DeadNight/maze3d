package model;

import java.io.IOException;

public interface Model {
	void stop();
	void generateMaze(String name, int width, int height, int depth);
	byte[] getMazeData(String name) throws IOException;
}
