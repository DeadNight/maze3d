package algorithms.mazeGenerators;

import java.util.ArrayList;

/**
 * @author Nir Leibovitch
 * <h1>My 3d maze generator</h1>
 * Generate 3d mazes using a randomized version of Prim's algorithm adapted for 3 dimensions
 * 
 * @see <a href="https://en.wikipedia.org/wiki/Prim%27s_algorithm">Prim's algorithm</a>
 * @see <a href="https://en.wikipedia.org/wiki/Maze_generation_algorithm">Maze generation algorithm</a>
 */
public class MyMaze3dGenerator extends CommonMaze3dGenerator {

	@Override
	public Maze3d generate(Volume volume) {
		Maze3d maze = new Maze3d(volume);
		ArrayList<Position> walls = new ArrayList<Position>();
		
		/*
		 * choose a random position inside the maze, turn it into a path
		 * and add its neighboring walls to the walls list 
		 */
		addPath(maze, getRandomInteriorPosition(volume), walls);
		
		while(!walls.isEmpty()) {
			if(Thread.interrupted())
				// enable thread to be interrupted
				return null;
			/*
			 * choose a random wall and remove it from the walls list.
			 * if it doesn't create an exit or connect 2 existing paths, turn it
			 * into a path and add its neighboring walls to the walls list
			 */
			Position wall = walls.get(rand.nextInt(walls.size()));
			walls.remove(wall);
			if(!maze.isExterior(wall) && maze.getPossibleMoves(wall).size() < 2) {
				addPath(maze, wall, walls);
			}
		}
		
		// choose a random entrance, and make sure it's connected to a path
		Position pos;
		do {
			pos = getRandomEntrance(volume);
		} while(maze.getPossibleMoves(pos).size() == 0);
		
		maze.setStartPosition(pos);
		
		// choose a different random exit, and make sure it's connected to a path
		do {
			pos = getRandomEntrance(volume);
		} while(pos.equals(maze.getStartPosition()) || maze.getPossibleMoves(pos).size() == 0);
		
		maze.setGoalPosition(pos);
		
		return maze;
	}

	private void addPath(Maze3d maze, Position cell, ArrayList<Position> walls) {
		maze.setCell(cell, (byte) 0);
		walls.addAll(maze.getAdjacentWalls(cell));
	}
	
	private Position getRandomInteriorPosition(Volume volume) {
		return new Position(1 + rand.nextInt(volume.getWidth() - 2),
				1 + rand.nextInt(volume.getHeight() - 2),
				1 + rand.nextInt(volume.getDepth() - 2));
	}

}
