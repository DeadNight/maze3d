package algorithms.mazeGenerators;

/**
 * @author Nir Leibovitch
 * <h1>Simple 3d maze generator</h1>
 * The simplest form of a 3d maze generator.
 * <p>
 * Randomly distributes walls &amp; paths in the interior of the maze, then creates a path
 * from a random entrance to a random exit.
 */
public class SimpleMaze3dGenerator extends CommonMaze3dGenerator {

	@Override
	public Maze3d generate(Volume volume) {
		Maze3d maze = new Maze3d(volume);
		
		// randomly distribute walls & empty spaces inside the maze
		for(int x = 1; x < volume.getWidth() - 1; ++x)
			for(int y = 1; y < volume.getHeight() - 1; ++y)
				for(int z = 1; z < volume.getDepth() - 1; ++z)
					maze.setCell(new Position(x, y, z), (byte) rand.nextInt(2));
		
		maze.setStartPosition(getRandomEntrance(volume));
		
		Position pos = maze.getStartPosition();
		
		String[] directions = new String[3];
		
		/*
		 * check on which outer wall of the maze the entrance is, and set
		 * an appropriate direction so we will walk into the maze
		 */
		if(pos.getX() == 0) {
			directions[0] = "right";
		} else if(pos.getX() == volume.getWidth() - 1) {
			directions[0] = "left";
		} else if(pos.getY() == 0) {
			directions[1] = "up";
		} else if(pos.getY() == volume.getHeight() - 1) {
			directions[1] = "down";
		} else if(pos.getZ() == 0) {
			directions[2] = "back";
		} else if(pos.getZ() == volume.getDepth() - 1) {
			directions[2] = "forward";
		}
		
		/*
		 * choose a random direction for the 2 remaining axes, and start
		 * carving our way into the maze
		 */
		if(directions[0] == null)
			if(rand.nextInt(2) == 1)
				directions[0] = "right";
			else
				directions[0] = "left";
		else
			pos.move(directions[0]);
		
		if(directions[1] == null)
			if(rand.nextInt(2) == 1)
				directions[1] = "up";
			else
				directions[1] = "down";
		else
			pos.move(directions[1]);
		
		if(directions[2] == null)
			if(rand.nextInt(2) == 1)
				directions[2] = "forward";
			else
				directions[2] = "back";
		else
			pos.move(directions[2]);
		
		/*
		 * randomly carve our way in the maze in one of the pre-chosen directions
		 * until we find our way out
		 */
		while(pos.getX() > 0 && pos.getY() > 0 && pos.getZ() > 0 &&
				pos.getX() < volume.getWidth() - 1 &&
				pos.getY() < volume.getHeight() - 1 &&
				pos.getZ() < volume.getDepth() - 1) {
			maze.setCell(pos, (byte) 0);
			pos.move(directions[rand.nextInt(3)]);
		}
		
		maze.setGoalPosition(pos);
		
		return maze;
	}

}
