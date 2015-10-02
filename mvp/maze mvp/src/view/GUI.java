package view;

import java.io.IOException;
import java.util.StringJoiner;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.Volume;
import algorithms.search.Solution;

public class GUI extends CommonView {
	MazeWindow mazeWindow;
	
	public GUI() {
		mazeWindow = new MazeWindow("Maze game", 600, 300);
		mazeWindow.addStartSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }

			@Override
			public void widgetSelected(SelectionEvent e) {
				Volume volume = (Volume) new ObjectInitializer().initialize(Volume.class);
				if(volume == null)
					return;
				
				StringJoiner stringJoiner = new StringJoiner(" ");
				stringJoiner
					.add("generate 3d maze")
					.add("anonymous")
					.add("" + volume.getWidth())
					.add("" + volume.getHeight())
					.add("" + volume.getDepth());
				userCommand = stringJoiner.toString();
				
				setChanged();
				notifyObservers();
			}
		});
		
		mazeWindow.addExitListener(new Listener() {
			@Override
			public void handleEvent(Event e) {
				e.doit = false;
				userCommand = "exit";
				setChanged();
				notifyObservers();
			}
		});
	}

	@Override
	public void start() {
		mazeWindow.run();
	}
	
	@Override
	public void stop() {
		mazeWindow.close();
	}

	@Override
	public void displayMaze(byte[] compressedMazeData) {
		byte[] mazeData;
		try {
			mazeData = decompressData(compressedMazeData);
		} catch (IOException e) {
			mazeWindow.displayError("Decompression error",
					"Error occurred while decompressing maze data");
			return;
		}
		
		Maze3d maze;
		try {
			maze = new Maze3d(mazeData);
		} catch (IOException e) {
			mazeWindow.displayError("Instansiation error",
					"Error occurred instantiating maze from maze data");
			return;
		}
		
		mazeWindow.displayMaze(maze);
	}

	@Override
	public void displayMazeNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayUnknownCommand() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayCommandTemplate(String template, String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayFilesList(String[] filesList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayDirectoryNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayNotDirectory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayGetMazeError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeGenerated(String name) {
		userCommand = "display " + name;
		setChanged();
		notifyObservers();
	}

	@Override
	public void displayCrossSection(int[][] crossSection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayIndexOutOfRange() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displaySaveMazeError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeSaved() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeLoaded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayLoadMazeError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayFileNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeSize(int mazeSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayDecompressionError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayFileSize(int fileSize) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayInvalidAxis() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeSolved(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displaySolveMazeError(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeSolutionNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void displayMazeSolution(Solution<Position> solution) {
		// TODO Auto-generated method stub

	}
}
