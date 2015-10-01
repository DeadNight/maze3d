package algorithms.search;

import org.junit.Assert;
import org.junit.Test;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.SimpleMaze3dGenerator;

public class AStarSearcherTest {
	@Test
	public void test() {
		Maze3dSearchable nullSearchable = new Maze3dSearchable(null);
		Maze3dSearchable searchable = new Maze3dSearchable(new SimpleMaze3dGenerator().generate(6, 5, 4));
		
		AStarSearcher<Position> nullSearcher = new AStarSearcher<Position>(null);
		
		Assert.assertNull(nullSearcher.search(null));
		Assert.assertNull(nullSearcher.search(nullSearchable));
		Assert.assertNull(nullSearcher.search(searchable));
		
		AStarSearcher<Position> searcher = new AStarSearcher<Position>(new MazeManhattanDistance());
		
		Assert.assertNull(searcher.search(null));
		Assert.assertNull(searcher.search(nullSearchable));
		Assert.assertNotNull(searcher.search(searchable));
	}
}
