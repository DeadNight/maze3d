package view;

import java.io.IOException;

import algorithms.mazeGenerators.Maze3d;

public class CLI extends CommonView {
	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	public void displayError(String message) {
		System.err.println(message);
	}

	@Override
	public void displayMaze(byte[] compressedMazeData) {
		byte[] mazeData;
		try {
			mazeData = decompressData(compressedMazeData);
		} catch (IOException e) {
			e.printStackTrace();
			displayError("error decompressing maze data");
			return;
		}
		
		Maze3d maze;
		try {
			maze = createMaze(mazeData);
		} catch (IOException e) {
			e.printStackTrace();
			displayError("error creating maze from data");
			return;
		}
		
		printMaze(maze);
	}
	
	private void printMaze(Maze3d maze) {
		for(int y = maze.getHeight() - 1; y >= 0 ; --y) {
			System.out.println("Floor " + y + ":");
			for(int z = maze.getDepth() - 1; z >= 0; --z) {
				for(int x = 0; x < maze.getWidth(); ++x) {
					if(maze.isWall(x, y, z))
						System.out.print((char)0x2593);
					else if(y > 0 && y < maze.getHeight() - 1 &&
							maze.isPath(x, y - 1, z) && maze.isPath(x, y + 1, z))
						System.out.print('x');
					else if(y > 0 && maze.isPath(x, y - 1, z))
						System.out.print('\\');
					else if(y < maze.getHeight() - 1 && maze.isPath(x, y + 1, z))
						System.out.print('/');
					else
						System.out.print(' ');
				}
				System.out.println();
			}
			System.out.println();
		}
	}
}
