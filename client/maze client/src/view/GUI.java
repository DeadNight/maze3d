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

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.Volume;
import algorithms.search.Solution;
import algorithms.search.State;

/**
 * @author Nir Leibovitch
 * <h1>Graphical User Interface implementation of the View Façade</h1>
 */
public class GUI extends CommonView {
	Maze3d maze;
	Position characterPosition;
	String viewPlane;
	MazeWindow mazeWindow;
	ObjectInitializer objectInitializer;
	Thread solutionDisplayerThread;
	boolean solved;
	
	/**
	 * Initiate the GUI View Façade instance
	 */
	public GUI() {
		objectInitializer = new ObjectInitializer();
		viewPlane = "XZ";
		mazeWindow = new MazeWindow("Maze game", 600, 600);
		mazeWindow.setViewPlane(viewPlane);
		
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
		
		mazeWindow.addViewPlaneSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(viewPlane != mazeWindow.getSelectedViewPlane()) {
					viewPlane = mazeWindow.getSelectedViewPlane();
					mazeWindow.setViewPlane(viewPlane);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		mazeWindow.addSolveListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
		
		mazeWindow.addEditPropertiesListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				userCommand = "get properties";
				setChanged();
				notifyObservers();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		mazeWindow.addImportPropertiesListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = mazeWindow.displayFileDialog(SWT.OPEN, "Open properties"
						, new String[] { "*.xml" });
				if(fileName != null) {
					StringJoiner stringJoiner = new StringJoiner(" ");
					stringJoiner
						.add("load properties")
						.add(new File(fileName).toURI().toString());
					userCommand = stringJoiner.toString();
					setChanged();
					notifyObservers();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) { }
		});
		
		mazeWindow.addExportPropertiesListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String fileName = mazeWindow.displayFileDialog(SWT.SAVE, "Save properties"
						, new String[] { "*.xml" });
				if(fileName != null) {
					StringJoiner stringJoiner = new StringJoiner(" ");
					stringJoiner
						.add("save properties")
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
				if(characterPosition != null && !solved)
					switch(e.keyCode) {
					case SWT.ARROW_UP:
						switch(viewPlane) {
						case "XZ":
							moveForward();
							break;
						case "XY":
						case "ZY":
							moveUp();
							break;
						}
						break;
					case SWT.ARROW_DOWN:
						switch(viewPlane) {
						case "XZ":
							moveBack();
							break;
						case "XY":
						case "ZY":
							moveDown();
							break;
						}
						break;
					case SWT.ARROW_LEFT:
						switch(viewPlane) {
						case "XZ":
						case "XY":
							moveLeft();
							break;
						case "ZY":
							moveBack();
							break;
						}
						break;
					case SWT.ARROW_RIGHT:
						switch(viewPlane) {
						case "XZ":
						case "XY":
							moveRight();
							break;
						case "ZY":
							moveForward();
							break;
						}
						break;
					case SWT.PAGE_UP:
						if(viewPlane.equals("XZ"))
							moveUp();
						break;
					case SWT.PAGE_DOWN:
						if(viewPlane.equals("XZ"))
							moveDown();
						break;
					case SWT.HOME:
						switch(viewPlane) {
						case "XY":
							moveBack();
							break;
						case "ZY":
							moveLeft();
							break;
						}
						break;
					case SWT.END:
						switch(viewPlane) {
						case "XY":
							moveForward();
							break;
						case "ZY":
							moveRight();
							break;
						}
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
		if(solutionDisplayerThread != null)
			solutionDisplayerThread.interrupt();
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
		
		try {
			maze = new Maze3d(mazeData);
		} catch (IOException e) {
			mazeWindow.displayError("Instansiation error",
					"Error occurred instantiating maze from maze data");
			return;
		}
		
		if(solutionDisplayerThread != null)
			solutionDisplayerThread.interrupt();
		characterPosition = maze.getStartPosition();
		mazeWindow.setMaze(maze, characterPosition);
		mazeWindow.setSolved(solved = false);
	}

	@Override
	public void displayMazeGenerated(String name) {
		displayMaze(name);
	}

	@Override
	public void displayMazeSolved(String name) {
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
		if(solutionDisplayerThread != null)
			return;
		solutionDisplayerThread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(State<Position> s : solution.getSequence()) {
					// enable thread interruption
					if(Thread.interrupted())
						break;
					
					characterPosition = new Position(s.getState());
					mazeWindow.setCharacterPosition(characterPosition);
					if(maze.getGoalPosition().equals(characterPosition))
						mazeWindow.setSolved(solved = true);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// enable thread interruption
						break;
					}
				}
				solutionDisplayerThread = null;
			}
		});
		solutionDisplayerThread.start();
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

	@Override
	public void displayFileNameError() {
		mazeWindow.displayError("File name error", "Error occurred while parsing file name");
	}

	@Override
	public void displayProperties(Properties properties) {
		Properties newProperties = (Properties) objectInitializer.initialize(Properties.class, properties);
		if(newProperties != null) {
			if(properties.getViewType() != newProperties.getViewType()
					|| properties.getPoolSize() != newProperties.getPoolSize())
				mazeWindow.displayInfo("Changes will take effect later"
						, "View type and pool size change will take only effect after restarting the game");
			StringJoiner stringJoiner = new StringJoiner(" ");
			stringJoiner
				.add("edit properties")
				.add(""+newProperties.getPoolSize())
				.add(""+newProperties.getMazeGeneratorType())
				.add(""+newProperties.getMazeSearcherType())
				.add(""+newProperties.getViewType());
			userCommand = stringJoiner.toString();
			setChanged();
			notifyObservers();
		}
	}

	@Override
	public void displayPropertiesSaved() {
		mazeWindow.displayInfo("Properties saved", "Properties saved successfully");
	}
	
	private void displayMaze(String name) {
		userCommand = "display " + name;
		setChanged();
		notifyObservers();
	}

	private void moveForward() {
		moveCharacter(new Position(characterPosition).moveForward());
	}

	private void moveBack() {
		moveCharacter(new Position(characterPosition).moveBack());
	}

	private void moveLeft() {
		moveCharacter(new Position(characterPosition).moveLeft());
	}

	private void moveRight() {
		moveCharacter(new Position(characterPosition).moveRight());
	}

	private void moveUp() {
		moveCharacter(new Position(characterPosition).moveUp());
	}
	
	private void moveDown() {
		moveCharacter(new Position(characterPosition).moveDown());
	}
	
	private void moveCharacter(Position target) {
		if(solutionDisplayerThread != null)
			solutionDisplayerThread.interrupt();
		if(maze.inBounds(target) && maze.isPath(target)) {
			characterPosition = target;
			mazeWindow.setCharacterPosition(target);
			if(maze.getGoalPosition().equals(target))
				mazeWindow.setSolved(solved = true);
		}
	}
}
