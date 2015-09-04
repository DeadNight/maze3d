package algorithms.mazeGenerators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Nir Leibovitch
 * <h1>3-dimensional maze</h1>
 * Represents a 3d maze
 * <p>
 * Provides helper methods for generating mazes (either manually or using generators) and
 * for printing sections of or all the maze to the console 
 */
public class Maze3d {
	private byte[][][] maze;
	private Position startPosition;
	private Position goalPosition;

	/**
	 * Create a new 3d maze with a given volume filled with walls
	 * @param volume The volume
	 */
	public Maze3d(Volume volume) {
		maze = new byte[volume.getHeight()][volume.getDepth()][volume.getWidth()];
		fillMaze((byte) 1);
	}
	
	/**
	 * Create a 3d maze from given binary data
	 * @param data The binary data
	 * @throws IOException 
	 */
	public Maze3d(byte[] data) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		
		int width = in.read();
		int height = in.read();
		int depth = in.read();
		
		int startLength = in.read();
		byte[] startBytes = new byte[startLength];
		in.read(startBytes);
		startPosition = new Position(startBytes);
		
		int goalLength = in.read();
		byte[] goalBytes = new byte[goalLength];
		in.read(goalBytes);
		goalPosition = new Position(goalBytes);
		
		maze = new byte[height][depth][width];
		for(int y = 0; y < height; ++y)
			for(int z = 0; z < depth; ++z)
				for(int x = 0; x < width; ++x)
					maze[y][z][x] = (byte) in.read();
	}
	
	/**
	 * Get the internal representation of the maze
	 * @return The internal representation
	 */
	public byte[][][] getMaze() {
		return maze;
	}

	/**
	 * Set the internal representation of the maze
	 * @param maze The internal representation
	 */
	public void setMaze(byte[][][] maze) {
		this.maze = maze;
	}

	/**
	 * Get the entrance of the maze
	 * @return Position The entrance
	 */
	public Position getStartPosition() {
		if(startPosition != null)
			return new Position(startPosition);
		else
			return null;
	}

	/**
	 * Set the entrance of the maze
	 * @param startPosition The new entrance
	 */
	public void setStartPosition(Position startPosition) {
		if(this.startPosition != null)
			setCell(this.startPosition, (byte) 1);
		this.startPosition = startPosition;
		setCell(startPosition, (byte) 0);
	}

	/**
	 * Get the exit of the maze
	 * @return Position The exit
	 */
	public Position getGoalPosition() {
		if(goalPosition != null)
			return new Position(goalPosition);
		else
			return null;
	}

	/**
	 * Set the exit of the maze
	 * @param goalPosition The new exit
	 */
	public void setGoalPosition(Position goalPosition) {
		if(this.goalPosition != null)
			setCell(this.goalPosition, (byte) 1);
		this.goalPosition = goalPosition;
		setCell(goalPosition, (byte) 0);
	}
	
	/**
	 * Get the value at a given position in the maze
	 * @param pos The position
	 * @return int The value
	 */
	public int getCell(Position pos) {
		return getCell(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Get the value at a given x,y,z coordinate in the maze
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return int The value
	 */
	public int getCell(int x, int y, int z) {
		return maze[y][z][x];
	}
	
	/**
	 * Set the value at a given position in the maze
	 * @param pos The position
	 * @param fill The value
	 */
	public void setCell(Position pos, byte fill) {
		setCell(pos.getX(), pos.getY(), pos.getZ(), fill);
	}
	
	/**
	 * Set the value at a given x,y,z coordinate in the maze
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @param fill The value
	 */
	public void setCell(int x, int y, int z, byte fill) {
		maze[y][z][x] = fill;
	}
	
	/**
	 * Get the width of the maze
	 * @return int The width
	 */
	public int getWidth() {
		return maze[0][0].length;
	}
	
	/**
	 * Get the height of the maze
	 * @return int The height
	 */
	public int getHeight() {
		return maze.length;
	}
	
	/**
	 * Get the depth of the maze
	 * @return int The depth
	 */
	public int getDepth() {
		return maze[0].length;
	}
	
	/**
	 * Get the positions of walls adjacent to a given x,y,z coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return ArrayList&lt;Position&gt; The adjacent walls positions
	 */
	public ArrayList<Position> getAdjacentWalls(int x, int y, int z) {
		return getAdjacentWalls(new Position(x, y, z));
	}
	
	/**
	 * Get the positions of walls adjacent to a given position
	 * @param cell The position
	 * @return ArrayList&lt;Position&gt; The adjacent walls positions
	 */
	public ArrayList<Position> getAdjacentWalls(Position cell) {
		ArrayList<Position> walls = new ArrayList<Position>();
		if(cell.getX() > 0) {
			Position pos = new Position(cell).moveLeft();
			if(isWall(pos))
				walls.add(pos);
		}
		if(cell.getX() < getWidth() - 1) {
			Position pos = new Position(cell).moveRight();
			if(isWall(pos))
				walls.add(pos);
		}
		if(cell.getY() > 0) {
			Position pos = new Position(cell).moveDown();
			if(isWall(pos))
				walls.add(pos);
		}
		if(cell.getY() < getHeight() - 1) {
			Position pos = new Position(cell).moveUp();
			if(isWall(pos))
				walls.add(pos);
		}
		if(cell.getZ() > 0) {
			Position pos = new Position(cell).moveForward();
			if(isWall(pos))
				walls.add(pos);
		}
		if(cell.getZ() < getDepth() - 1) {
			Position pos = new Position(cell).moveBack();
			if(isWall(pos))
				walls.add(pos);
		}
		return walls;
	}
	
	/**
	 * Get the possible moves from a given x,y,z coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return ArrayList&lt;Maze3dMove&gt; The possible moves
	 */
	public ArrayList<Maze3dMove> getPossibleMoves(int x, int y, int z) {
		return getPossibleMoves(new Position(x, y, z));
	}
	
	/**
	 * Get the possible moves from a given position
	 * @param from The position
	 * @return ArrayList&lt;Maze3dMove&gt; The possible moves
	 */
	public ArrayList<Maze3dMove> getPossibleMoves(Position from) {
		ArrayList<Maze3dMove> moves = new ArrayList<Maze3dMove>();
		if(from.getX() > 0) {
			Position to = new Position(from).moveLeft();
			if(isPath(to))
				moves.add(new Maze3dMove(from, "left", to, 1));
		}
		if(from.getX() < getWidth() - 1) {
			Position to = new Position(from).moveRight();
			if(isPath(to))
				moves.add(new Maze3dMove(from, "right", to, 1));
		}
		if(from.getY() > 0) {
			Position to = new Position(from).moveDown();
			if(isPath(to))
				moves.add(new Maze3dMove(from, "down", to, 1));
		}
		if(from.getY() < getHeight() - 1) {
			Position to = new Position(from).moveUp();
			if(isPath(to))
				moves.add(new Maze3dMove(from, "up", to, 1));
		}
		if(from.getZ() > 0) {
			Position to = new Position(from).moveForward();
			if(isPath(to))
				moves.add(new Maze3dMove(from, "forward", to, 1));
		}
		if(from.getZ() < getDepth() - 1) {
			Position to = new Position(from).moveBack();
			if(isPath(to))
				moves.add(new Maze3dMove(from, "back", to, 1));
		}
		return moves;
	}
	
	/**
	 * Get the directions of paths adjacent to a given x,y,z coordinate
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return String[] The adjacent paths directions
	 */
	public String[] getPossibleMoveDirections(int x, int y, int z) {
		return getPossibleMoveDirections(new Position(x, y, z));
	}
	
	/**
	 * Get the directions of paths adjacent to a given position
	 * @param from The position
	 * @return String[] The adjacent paths directions
	 */
	public String[] getPossibleMoveDirections(Position from) {
		ArrayList<Maze3dMove> moves = getPossibleMoves(from);
		String[] directions = new String[moves.size()];
		for(int i = 0; i < moves.size(); ++i)
			directions[i] = moves.get(i).getDirection();
		return directions;
	}
	
	/**
	 * Get a cross-section of the maze with the x coordinate fixed to a given value
	 * @param x The x coordinate
	 * @return int[][] The cross-section
	 * @throws IndexOutOfBoundsException When x is out of the maze
	 */
	public int[][] getCrossSectionByX(int x) throws IndexOutOfBoundsException {
		if(x < 0 || x > getWidth())
			throw new IndexOutOfBoundsException("x is out of the maze");
		int[][] crossSection = new int[getHeight()][getDepth()];
		for(int y = 0; y < getHeight(); ++y)
			for(int z = 0; z < getDepth(); ++z)
				crossSection[y][z] = getCell(x, y, z);
		return crossSection;
	}
	
	/**
	 * Get a cross-section of the maze with the y coordinate fixed to a given value
	 * @param y The y coordinate
	 * @return int[][] The cross-section
	 * @throws IndexOutOfBoundsException When y is out of the maze
	 */
	public int[][] getCrossSectionByY(int y) throws IndexOutOfBoundsException {
		if(y < 0 || y > getHeight())
			throw new IndexOutOfBoundsException("y is out of the maze");
		int[][] crossSection = new int[getDepth()][getWidth()];
		for(int z = 0; z < getDepth(); ++z)
			for(int x = 0; x < getWidth(); ++x)
			crossSection[z][x] = getCell(x, y, z);
		return crossSection;
	}
	
	/**
	 * Get a cross-section of the maze with the z coordinate fixed to a given value
	 * @param z The z coordinate
	 * @return int[][] The cross-section
	 * @throws IndexOutOfBoundsException When z is out of the maze
	 */
	public int[][] getCrossSectionByZ(int z) throws IndexOutOfBoundsException {
		if(z < 0 || z > getDepth())
			throw new IndexOutOfBoundsException("z is out of the maze");
		int[][] crossSection = new int[getHeight()][getWidth()];
		for(int y = 0; y < getHeight(); ++y)
			for(int x = 0; x < getWidth(); ++x)
			crossSection[y][x] = getCell(x, y, z);
		return crossSection;
	}
	
	/**
	 * Helper method to print cross-sections returned from getCrossSection[X|Y|Z]() to
	 * the console
	 * @param crossSection The cross-section
	 */
	public void printCrossSection(int[][] crossSection) {
		for(int[] row : crossSection) {
			for(int cell : row)
				if(cell == 1)
					System.out.print((char)0x2593);
				else
					System.out.print(' ');
			System.out.println();
		}
	}

	/**
	 * Helper method to test whether the given position is on the maze exterior
	 * @param pos The position
	 * @return boolean
	 */
	public boolean isExterior(Position pos) {
		return isExterior(pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Helper method to test whether the given x,y,z coordinate is on the maze exterior
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return boolean
	 */
	public boolean isExterior(int x, int y, int z) {
		return x == 0 || y == 0 || z == 0
				|| x == getWidth() - 1 || y == getHeight() - 1  || z == getDepth() - 1;
	}

	/**
	 * Helper method to test whether the value at a given position in the maze represents
	 * a wall
	 * @param pos The position
	 * @return boolean
	 */
	public boolean isWall(Position pos) {
		return isWall(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Helper method to test whether the value at a given x,y,z coordinate in the maze
	 * represents a wall
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return boolean
	 */
	public boolean isWall(int x, int y, int z) {
		return getCell(x, y, z) == 1;
	}

	/**
	 * Helper method to test whether the value at a given position in the maze represents
	 * a path
	 * @param pos The position
	 * @return boolean
	 */
	public boolean isPath(Position pos) {
		return isPath(pos.getX(), pos.getY(), pos.getZ());
	}
	
	/**
	 * Helper method to test whether the value at a given x,y,z coordinate in the maze
	 * represents a path
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 * @return boolean
	 */
	public boolean isPath(int x, int y, int z) {
		return getCell(x, y, z) == 0;
	}
	
	/**
	 * Helper method to fill the maze with a given value
	 * @param fill The value
	 */
	public void fillMaze(byte fill) {
		for(int y = 0; y < getHeight(); ++y) {
			for(int z = 0; z < getDepth(); ++z) {
				Arrays.fill(maze[y][z], fill);
			}
		}
	}
	
	/**
	 * Get a binary representation of the maze 
	 * @return The binary representation
	 */
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		out.write(getWidth());
		out.write(getHeight());
		out.write(getDepth());
		
		byte[] startBytes = startPosition.toByteArray();
		out.write(startBytes.length);
		out.write(startBytes);
		
		byte[] goalBytes = goalPosition.toByteArray();
		out.write(goalBytes.length);
		out.write(goalBytes);
		
		for(byte[][] plane : maze)
			for(byte[] line : plane)
				out.write(line);
		
		return out.toByteArray();
	}
	
	/**
	 * Helper method to print the maze to the console
	 */
	public void print() {
		for(int y = getHeight() - 1; y >= 0 ; --y) {
			System.out.println("Floor " + y + ":");
			for(int z = getDepth() - 1; z >= 0; --z) {
				for(int x = 0; x < getWidth(); ++x) {
					printCell(x, y, z);
				}
				System.out.println();
			}
			System.out.println();
		}			
	}

	// helper method for print()
	private void printCell(int x, int y, int z) {
		if(isWall(x, y, z))
			System.out.print((char)0x2593);
		else if(y > 0 && y < getHeight() - 1 &&
				isPath(x, y - 1, z) && isPath(x, y + 1, z))
			System.out.print('x');
		else if(y > 0 && isPath(x, y - 1, z))
			System.out.print('\\');
		else if(y < getHeight() - 1 && isPath(x, y + 1, z))
			System.out.print('/');
		else
			System.out.print(' ');
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Maze3d)) return false;
		Maze3d other = (Maze3d) obj;
		if(getWidth() != other.getWidth()) return false;
		if(getHeight() != other.getHeight()) return false;
		if(getDepth() != other.getDepth()) return false;
		if(!startPosition.equals(other.startPosition)) return false;
		if(!goalPosition.equals(other.goalPosition)) return false;
		for(byte x = 0; x < getWidth(); ++x)
			for(byte y = 0; y < getHeight(); ++y)
				for(byte z = 0; z < getDepth(); ++z)
					if(getCell(x, y, z) != other.getCell(x, y, z)) return false;
		return true;
	}
}
