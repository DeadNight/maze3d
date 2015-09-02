package algorithms.demo;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.AStarSearcher;
import algorithms.search.BFSearcher;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
import algorithms.search.Searcher;
import algorithms.search.Solution;

/**
 * @author Nir Leibovitch
 * <h1>Demonstrate maze algorithms</h1>
 * The demo will generate a maze and then solve it using various methods.
 * <p>
 * The maze, solution and number of nodes evaluated while solving using each method will be
 * printed to the console. 
 */
public class Demo {
	/**
	 * Used to run the demo.
	 */
	public void run() {
		System.out.print("Generating maze... ");
		Maze3d maze = new MyMaze3dGenerator().generate(100, 6, 8);
		System.out.println("done!");
		maze.print();
		
		Maze3dSearchable searchable = new Maze3dSearchable(maze);
		
		System.out.print("Searching solution using BFS... ");
		Searcher<Position> bfSearcher = new BFSearcher<Position>();
		bfSearcher.search(searchable);
		System.out.println("done!");
		
		System.out.print("Searching solution using A* with manhattan distance heuristic... ");
		Searcher<Position> aStarManhattanSearcher = new AStarSearcher<Position>(new MazeManhattanDistance());
		aStarManhattanSearcher.search(searchable);
		System.out.println("done!");
		
		System.out.print("Searching solution using A* with air distance heuristic... ");
		Searcher<Position> aStarAirSearcher = new AStarSearcher<Position>(new MazeAirDistance());
		Solution<Position> solution = aStarAirSearcher.search(searchable);
		System.out.println("done!");
		
		System.out.println("Solution:");
		solution.print();
		
		System.out.println("Number of nodes evaluated:");
		System.out.println("\tUsing BFS: " + bfSearcher.getNumberOfNodesEvaluated());
		System.out.println("\tUsing A* with manhattan distance heuristic: " + aStarManhattanSearcher.getNumberOfNodesEvaluated());
		System.out.println("\tUsing A* with air distance heuristic: " + aStarAirSearcher.getNumberOfNodesEvaluated());
	}
}
