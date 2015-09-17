package view;

public interface View {
	void start();
	
	void displayError(String message);

	void displayMaze(byte[] compressedMazeData);
}
