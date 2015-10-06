package model;

import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import algorithms.demo.Maze3dSearchable;
import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.Maze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.Searcher;
import algorithms.search.Solution;

public abstract class CommonModel extends Observable implements Model {
	byte[] propertiesData;
	ExecutorService threadPool;
	Maze3dGenerator mazeGenerator;
	Searcher<Position> mazeSearchAlgorithm;
	HashMap<String, Maze3d> mazeCache;
	HashMap<Maze3dSearchable, Solution<Position>> solutionCache;
	
	public CommonModel() {
		mazeCache = new HashMap<String, Maze3d>();
		solutionCache = new HashMap<Maze3dSearchable, Solution<Position>>();
	}

	@Override
	public void setMazeGenerator(Maze3dGenerator mazeGenerator) {
		this.mazeGenerator = mazeGenerator;
	}

	@Override
	public void setMazeSearchAlgorithm(Searcher<Position> mazeSearchAlgorithm) {
		this.mazeSearchAlgorithm = mazeSearchAlgorithm;
	}
	
	@Override
	public void start(int poolSize) {
		threadPool = Executors.newFixedThreadPool(poolSize);
	}
	
	@Override
	public void stop() {
		threadPool.shutdownNow();
		boolean terminated = false;
		while(!terminated)
			try {
				terminated = threadPool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				// OK, keep waiting
			}
	}
	
	@Override
	public byte[] getPropertiesData() throws IOException {
		FileInputStream settingsIn;
		settingsIn = new FileInputStream("properties.xml");
		
		BufferedInputStream in = new BufferedInputStream(settingsIn);
		
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		BufferedOutputStream out = new BufferedOutputStream(byteArrayOut);
		
		int b;
		try {
			while((b = in.read()) != -1)
				out.write(b);
			out.flush();
		} finally {
			in.close();
			out.close();
		}
		
		return byteArrayOut.toByteArray();
	}
	
	<T> void runTaskInBackground(Task<T> task) {
		Future<T> future = threadPool.submit(new Callable<T>() {
			@Override
			public T call() throws Exception {
				return task.doTask();
			}
		});
		
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				boolean waiting = true;
				while(waiting)
					try {
						T result = future.get(10, TimeUnit.SECONDS);
						waiting = false;
						task.handleResult(result);
					} catch (ExecutionException e) {
						waiting = false;
						task.handleExecutionException(e);
					} catch (InterruptedException e) {
						waiting = false;
						future.cancel(true);
					} catch (TimeoutException e) {
						// OK, keep waiting
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
	
	byte[] decompressData(byte[] compressedData) throws IOException {
		MyDecompressorInputStream decompressor = new MyDecompressorInputStream(new BufferedInputStream(new ByteArrayInputStream(compressedData)));
		ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
		
		int b;
		try {
			while((b = decompressor.read()) != -1)
				dataOut.write(b);
			dataOut.flush();
		} finally {
			decompressor.close();
		}
		
		return dataOut.toByteArray();
	}
}
