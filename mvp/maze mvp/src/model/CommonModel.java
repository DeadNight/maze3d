package model;

import io.MyCompressorOutputStream;

import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import presenter.Properties;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;

public abstract class CommonModel extends Observable implements Model {
	Properties properties;
	ExecutorService threadPool;
	Maze3dGenerator mazeGenerator;
	Searcher<Position> mazeSearchAlgorithm;
	HashMap<String, Maze3d> mazeCache;
	
	public CommonModel() throws ArrayIndexOutOfBoundsException, FileNotFoundException {
		initProperties();
		
		threadPool = Executors.newFixedThreadPool(properties.getPoolSize());
		mazeCache = new HashMap<String, Maze3d>();
		
		initMazeGenerator();
		initMazeSearchAlgorithm();
	}
	
	void initProperties() throws ArrayIndexOutOfBoundsException, FileNotFoundException {
		FileInputStream settingsIn;
		try {
			settingsIn = new FileInputStream("properties.xml");
		} catch (FileNotFoundException e) {
			System.err.println("properties.xml file not found");
			throw e;
		}
		
		XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(settingsIn));
		try {
			properties = (Properties) xmlDecoder.readObject();
		} catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("error while parsing properties.xml");
			throw e;
		} finally {
			xmlDecoder.close();
		}
	}

	abstract void initMazeGenerator();
	abstract void initMazeSearchAlgorithm();
	
	<T> void runTaskInBackground(Task<T> command) {
		Future<T> future = threadPool.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return command.doTask();
			}
		});
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				boolean waiting = true;
				while(waiting)
					try {
						T result = future.get();
						waiting = false;
						command.handleResult(result);
					} catch (InterruptedException | CancellationException e) {
						// OK, stop waiting
						waiting = false;
					} catch (ExecutionException e) {
						waiting = false;
						e.printStackTrace();
						notifyObservers(new String[] { "error", "generate" });
					}
			}
		});
	}
	
	byte[] compressData(byte[] data) throws IOException {
		ByteArrayOutputStream compressedDataOut = new ByteArrayOutputStream();
		MyCompressorOutputStream compressor = new MyCompressorOutputStream(new BufferedOutputStream(compressedDataOut));
		
		try {
			compressor.write(data);
			compressor.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			compressor.close();
		}
		
		return compressedDataOut.toByteArray();
	}
}
