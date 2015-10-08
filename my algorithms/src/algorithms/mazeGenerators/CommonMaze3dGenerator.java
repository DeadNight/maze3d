package algorithms.mazeGenerators;

import java.util.Random;

/**
 * @author Nir Leibovitch
 * <h1>General 3-dimensional maze generator</h1>
 * Generates 3d mazes of any volume 
 */
public abstract class CommonMaze3dGenerator implements Maze3dGenerator {
	
	protected Random rand;
	
	/**
	 * Create a new 3d maze generator
	 */
	public CommonMaze3dGenerator() {
		rand = new Random();
	}
	
	@Override
	public Maze3d generate(int width, int height, int depth) {
		return generate(new Volume(width, height, depth));
	}

	@Override
	public String measureAlgorithmTime(int width, int height, int depth) {
		return measureAlgorithmTime(new Volume(width, height, depth));
	}

	@Override
	public String measureAlgorithmTime(Volume volume) {
		long time = System.currentTimeMillis();
		generate(volume);
		time = System.currentTimeMillis() - time;
		return getDurationString(time);
	}
	
	/**
	 * Helper method to get a random 3d position on the exterior of a given volume, but not
	 * at the corners
	 * @param volume The volume
	 * @return Position The position
	 */
	protected Position getRandomEntrance(Volume volume) {
		int entranceX;
		int entranceY;
		int entranceZ;
		
		int outerWallAxis = rand.nextInt(3);
		switch(outerWallAxis) {
		case 0:
			entranceX = rand.nextInt(2) * (volume.getWidth() - 1);
			
			if(volume.getHeight() == 3)
				entranceY = 1;
			else
				entranceY = 1 + rand.nextInt(volume.getHeight() - 3);
			
			if(volume.getDepth() == 3)
				entranceZ = 1;
			else
				entranceZ = 1 + rand.nextInt(volume.getDepth() - 3);
			break;
		case 1:
			if(volume.getWidth() == 3)
				entranceX = 1;
			else
				entranceX = 1 + rand.nextInt(volume.getWidth() - 3);
			
			entranceY = rand.nextInt(2) * (volume.getHeight() - 1);
			
			if(volume.getDepth() == 3)
				entranceZ = 1;
			else
				entranceZ = 1 + rand.nextInt(volume.getDepth() - 3);
			break;
		case 2:
			if(volume.getWidth() == 3)
				entranceX = 1;
			else
				entranceX = 1 + rand.nextInt(volume.getWidth() - 3);
			
			if(volume.getHeight() == 3)
				entranceY = 1;
			else
				entranceY = 1 + rand.nextInt(volume.getHeight() - 3);
			
			entranceZ = rand.nextInt(2) * (volume.getDepth() - 1);
			break;
		default:
			// this shouldn't happen, outerWallAxis must be 0, 1 or 2
			return null;
		}
		
		return new Position(entranceX, entranceY, entranceZ);
	}
	
	private String getDurationString(long duration) {
		StringBuilder sb = new StringBuilder();
		
		if(duration == 0) {
			sb.append("0 milliseconds");
		} else {
			if(duration > 3600000) {
				sb.append(duration / 3600000 + " hours ");
				duration %= 3600000;
			}
			if(duration > 60000) {
				sb.append(duration / 60000 + " minutes ");
				duration %= 60000;
			}
			if(duration > 1000) {
				sb.append(duration / 1000 + " seconds ");
				duration %= 1000;
			}
			if(duration != 0)
				sb.append(duration + " milliseconds");
		}
		
		return sb.toString();
	}

}
