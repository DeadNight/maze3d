package view;

import java.io.File;
import java.io.IOException;
import java.util.StringJoiner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
	ObjectInitializer objectInitializer;
	
	public GUI() {
		mazeWindow = new MazeWindow("Maze game", 600, 300);
		objectInitializer = new ObjectInitializer();
		
		mazeWindow.addNewGameSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }

			@Override
			public void widgetSelected(SelectionEvent e) {
				Volume volume = (Volume) objectInitializer.initialize(Volume.class, new Volume(1, 1, 1));
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
		
		mazeWindow.addSolveListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Position characterPosition = mazeWindow.getCharacterPosition();
				
				StringJoiner stringJoiner = new StringJoiner(" ");
				stringJoiner
					.add("solve from")
					.add("anonymous")
					.add("" + characterPosition.getX())
					.add("" + characterPosition.getY())
					.add("" + characterPosition.getZ());
				userCommand = stringJoiner.toString();

				setChanged();
				notifyObservers();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		mazeWindow.addExitListener(new Listener() {
			@Override
			public void handleEvent(Event e) {
				e.doit = false;
				int response = mazeWindow.displayQuestion("Confirm exit", "Are you sure you want to quit ?");
				if(response == SWT.YES) {
					userCommand = "exit";
					setChanged();
					notifyObservers();
				}
			}
		});
		
		mazeWindow.addLoadMazeListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				String fileName = mazeWindow.displayFileDialog(SWT.OPEN, "Load maze"
						, new String[] { "*.maz" });
				if(fileName != null) {
					StringJoiner stringJoiner = new StringJoiner(" ");
					stringJoiner
						.add("load maze")
						.add(new File(fileName).toURI().toString())
						.add("anonymous");
					userCommand = stringJoiner.toString();
					setChanged();
					notifyObservers();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		mazeWindow.addSaveMazeListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = mazeWindow.displayFileDialog(SWT.SAVE, "Save maze"
						, new String[] { "*.maz" });
				if(fileName != null) {
					StringJoiner stringJoiner = new StringJoiner(" ");
					stringJoiner
						.add("save maze")
						.add("anonymous")
						.add(new File(fileName).toURI().toString());
					userCommand = stringJoiner.toString();
					setChanged();
					notifyObservers();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		mazeWindow.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) { }
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.keyCode) {
				case SWT.ARROW_UP:
					mazeWindow.moveForward();
					break;
				case SWT.ARROW_DOWN:
					mazeWindow.moveBack();
					break;
				case SWT.ARROW_LEFT:
					mazeWindow.moveLeft();
					break;
				case SWT.ARROW_RIGHT:
					mazeWindow.moveRight();
					break;
				case SWT.PAGE_UP:
					mazeWindow.moveUp();
					break;
				case SWT.PAGE_DOWN:
					mazeWindow.moveDown();
					break;
				}	
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
	public void displayMazeGenerated(String name) {
		displayMaze(name);
	}

	@Override
	public void displayMazeSolved(String name) {
		Position characterPosition = mazeWindow.getCharacterPosition();
		StringJoiner stringJoiner = new StringJoiner(" ");
		stringJoiner
			.add("display solution from")
			.add(name)
			.add("" + characterPosition.getX())
			.add("" + characterPosition.getY())
			.add("" + characterPosition.getZ());
		userCommand = stringJoiner.toString();
		
		setChanged();
		notifyObservers();
	}

	@Override
	public void displayMazeSolution(Solution<Position> solution) {
		mazeWindow.displaySolution(solution);
	}

	@Override
	public void displayMazeNotFound() {
		mazeWindow.displayError("Error", "Could not find maze");
	}

	@Override
	public void displayUnknownCommand() {
		mazeWindow.displayError("Protocol error", "Unknown command");
	}

	@Override
	public void displayCommandTemplate(String commandName, String template) {
		mazeWindow.displayError("Protocol error",
				"Command template:" + System.lineSeparator() + commandName + " " + template);
	}

	@Override
	public void displayFilesList(String[] filesList) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void displayDirectoryNotFound() {
		mazeWindow.displayError("Argument error", "Directory not found");
	}

	@Override
	public void displayNotDirectory() {
		mazeWindow.displayError("Argument error", "Not a directory");
	}

	@Override
	public void displayGetMazeError() {
		mazeWindow.displayError("Generate error", "Error occurred while generating maze");
	}

	@Override
	public void displayCrossSection(int[][] crossSection) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void displayIndexOutOfRange() {
		mazeWindow.displayError("Argument error", "Index out of range");
	}

	@Override
	public void displaySaveMazeError() {
		mazeWindow.displayError("Save error", "Error occurred while saving maze");
	}

	@Override
	public void displayMazeSaved() {
		mazeWindow.displayInfo("Maze saved", "Maze saved successfully");
	}

	@Override
	public void displayMazeLoaded(String name) {
		displayMaze(name);
	}

	@Override
	public void displayLoadMazeError() {
		mazeWindow.displayError("Load error", "Error occurred while loading maze");
	}

	@Override
	public void displayFileNotFound() {
		mazeWindow.displayError("IO error", "File not found");
	}

	@Override
	public void displayMazeSize(int mazeSize) {
		mazeWindow.displayInfo("Maze size", ""+mazeSize);
	}

	@Override
	public void displayDecompressionError() {
		mazeWindow.displayError("Decompression error",
				"Error occurred while decompressing data");
	}

	@Override
	public void displayFileSize(int fileSize) {
		mazeWindow.displayInfo("File size", ""+fileSize);
	}

	@Override
	public void displayInvalidAxis() {
		mazeWindow.displayError("Agrument error", "Invalid axis");
	}

	@Override
	public void displaySolveMazeError(String name) {
		mazeWindow.displayError("Solve error", "Error occurred while solving maze");
	}

	@Override
	public void displayMazeSolutionNotFound() {
		mazeWindow.displayError("Error", "Solution not found");
	}
	
	private void displayMaze(String name) {
		userCommand = "display " + name;
		setChanged();
		notifyObservers();
	}

	@Override
	public void displayFileNameError() {
		mazeWindow.displayError("File name error", "Error occurred while parsing file name");
	}
}
