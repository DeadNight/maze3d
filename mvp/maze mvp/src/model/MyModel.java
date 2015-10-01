package model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;

public class MyModel extends CommonModel {
	String[] filesList;
	int[][] crossSection;
	int mazeSize;
	int fileSize;

	@Override
	public void listFiles(String path) {
		File file = new File(path);
		if(!file.exists()) {
			setChanged();
			notifyObservers(new String[] { "directory not found" });
			return;
		}
		if(!file.isDirectory()) {
			setChanged();
			notifyObservers(new String[] { "not a directory" });
			return;
		}
		
		filesList = file.list();
		
		setChanged();
		notifyObservers(new String[] { "files listed" });
	}

	@Override
	public String[] getFilesList() {
		return filesList;
	}

	@Override
	public void generateMaze(String name, int width, int height, int depth) {
		MyModel thisModel = this;
		runTaskInBackground(new Task<Maze3d>() {
			@Override
			public Maze3d doTask() {
				return mazeGenerator.generate(width, height, depth);
			}

			@Override
			public void handleResult(Maze3d result) {
				mazeCache.remove(name);
				mazeCache.put(name, result);
				
				thisModel.setChanged();
				thisModel.notifyObservers(new String[] { "maze generated", name });
			}

			@Override
			public void handleExecutionException(ExecutionException e) {
				System.out.println();
			}
		});
	}

	@Override
	public byte[] getMazeData(String name) throws IOException {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			return null;
		}
		return compressData(maze.toByteArray());
	}

	@Override
	public void generateCrossSection(String name, String axis, int index) {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			setChanged();
			notifyObservers(new String[] { "maze not found" } );
		}
		
		if(index < 0
				|| (axis.equals("X") && index >= maze.getWidth())
				|| (axis.equals("Y") && index >= maze.getHeight())
				|| (axis.equals("Z") && index >= maze.getDepth())) {
			setChanged();
			notifyObservers(new String[] { "index out of range" } );
		}
		
		
		switch(axis) {
		case "X":
			crossSection = maze.getCrossSectionByX(index);
			break;
		case "Y":
			crossSection = maze.getCrossSectionByY(index);
			break;
		case "Z":
			crossSection = maze.getCrossSectionByZ(index);
			break;
		default:
			setChanged();
			notifyObservers(new String[] { "invalid axis" } );
		}
	}

	@Override
	public int[][] getCrossSection() {
		return crossSection;
	}

	@Override
	public void saveMaze(String name, String fileName) {
		MyModel thisModel = this;
		
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			setChanged();
			notifyObservers(new String[] { "maze not found" });
			return;
		}
		
		byte[] compressedData;
		try {
			compressedData = compressData(maze.toByteArray());
		} catch (IOException e1) {
			setChanged();
			notifyObservers(new String[] { "decompression error" } );
			return;
		}
		
		runTaskInBackground(new Task<Object>() {
			@Override
			public Object doTask() throws IndexOutOfBoundsException, IOException, FileNotFoundException {
				BufferedOutputStream fileOut = new BufferedOutputStream(new FileOutputStream(fileName));
				try {
					fileOut.write(compressedData);
					fileOut.flush();
				} finally {
					fileOut.close();
				}
				return null;
			}

			@Override
			public void handleResult(Object result) {
				thisModel.setChanged();
				thisModel.notifyObservers(new String[] { "maze saved" });
			}

			@Override
			public void handleExecutionException(ExecutionException e) {
				Throwable cause = e.getCause();
				if(cause instanceof FileNotFoundException) {
					thisModel.setChanged();
					thisModel.notifyObservers(new String[] { "file not found" });
				} else if(cause instanceof IOException) {
					thisModel.setChanged();
					thisModel.notifyObservers(new String[] { "error saving maze" });
				}
			}
		});
	}

	@Override
	public void loadMaze(String fileName, String name) {
		MyModel thisModel = this;
		runTaskInBackground(new Task<Maze3d>() {
			@Override
			public Maze3d doTask() throws IOException, FileNotFoundException {
				BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(fileName));
				ByteArrayOutputStream compressedDataOut = new ByteArrayOutputStream();
				BufferedOutputStream bufferedDataOut = new BufferedOutputStream(compressedDataOut);
				
				int b;
				try {
					while((b = fileIn.read()) != -1)
						bufferedDataOut.write(b);
					bufferedDataOut.flush();
				} finally {
					fileIn.close();
				}
				
				byte[] data = decompressData(compressedDataOut.toByteArray());
				
				return new Maze3d(data);
			}

			@Override
			public void handleResult(Maze3d result) {
				mazeCache.remove(name);
				mazeCache.put(name, result);
				thisModel.setChanged();
				thisModel.notifyObservers(new String[] { "maze loaded" });
			}

			@Override
			public void handleExecutionException(ExecutionException e) {
				Throwable cause = e.getCause();
				if(cause instanceof FileNotFoundException) {
					thisModel.setChanged();
					thisModel.notifyObservers(new String[] { "file not found" });
				} else if(cause instanceof IOException) {
					thisModel.setChanged();
					thisModel.notifyObservers(new String[] { "error loading maze" });
				}
			}
		});
	}

	@Override
	public void calculateMazeSize(String name) {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			setChanged();
			notifyObservers(new String[] { "maze not found" } );
			return;
		}
		
		mazeSize = maze.toByteArray().length;
		
		setChanged();
		notifyObservers(new String[] { "maze size calculated", name } );
	}

	@Override
	public int getMazeSize(String name) {
		return mazeSize;
	}

	@Override
	public void calculateFileSize(String name) {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			setChanged();
			notifyObservers(new String[] { "maze not found" } );
			return;
		}
		
		try {
			fileSize = compressData(maze.toByteArray()).length;
		} catch(IOException e) {
			setChanged();
			notifyObservers(new String[] { "decompression error" } );
			return;
		}
		
		setChanged();
		notifyObservers(new String[] { "file size calculated", name } );
	}
	
	@Override
	public int getFileSize(String name) {
		return fileSize;
	}

	@Override
	public void solveMaze(String name) {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			setChanged();
			notifyObservers(new String[] { "maze not found" } );
			return;
		}
		
		if(solutionCache.containsKey(maze)) {
			setChanged();
			notifyObservers(new String[] { "maze solved", name } );
			return;
		}
		
		runTaskInBackground(new Task<Solution<Position>>() {
			@Override
			public Solution<Position> doTask() throws Exception {
				return mazeSearchAlgorithm.search(new Maze3dSearchable(maze));
			}

			@Override
			public void handleResult(Solution<Position> result) {
				if(result == null) {
					setChanged();
					notifyObservers(new String[] { "solve maze error", name } );
					return;
				}
					
				solutionCache.remove(maze);
				solutionCache.put(maze, result);
				
				setChanged();
				notifyObservers(new String[] { "maze solved", name } );
			}

			@Override
			public void handleExecutionException(ExecutionException e) { }
		});
	}

	@Override
	public Solution<Position> getMazeSolution(String name) {
		Maze3d maze = mazeCache.get(name);
		if(maze == null) {
			setChanged();
			notifyObservers(new String[] { "maze not found" } );
			return null;
		}
		
		Solution<Position> solution = solutionCache.get(maze);
		if(solution == null) {
			setChanged();
			notifyObservers(new String[] { "solution not found" } );
			return null;
		}
		
		return solution;
	}
}
