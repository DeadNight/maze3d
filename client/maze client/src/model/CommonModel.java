package model;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import algorithms.search.Solution;
import common.MazeGeneratorTypes;
import common.Properties;
import common.ViewTypes;
import io.MyCompressorOutputStream;
import io.MyDecompressorInputStream;

/**
 * @author Nir Leibovitch
 * <h1>Common implementation of the Model Façade</h1>
 */
public abstract class CommonModel extends Observable implements Model {
	Properties properties;
	ExecutorService threadPool;
	Maze3dGenerator mazeGenerator;
	HashMap<String, Maze3d> mazeCache;
	HashMap<Maze3dSearchable, Solution<Position>> solutionCache;
	
	/**
	 * Initiate the Model Façade instance
	 */
	public CommonModel() {
		mazeCache = new HashMap<String, Maze3d>();
		solutionCache = new HashMap<Maze3dSearchable, Solution<Position>>();
	}

	@Override
	public void setMazeGenerator(Maze3dGenerator mazeGenerator) {
		this.mazeGenerator = mazeGenerator;
	}
	
	@Override
	public boolean start(int poolSize) {
		threadPool = Executors.newFixedThreadPool(poolSize);
		return true;
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
	public void loadProperties(String fileName) throws URISyntaxException, FileNotFoundException, IOException {
		try {
			fileName = new URI(fileName).getPath();
		} catch (URISyntaxException e) {
			notifyObservers(new String[] { "properties not found" });
			throw e;
		}
		XMLDecoder xmlDecoder;
		try {
			xmlDecoder = new XMLDecoder(new FileInputStream(fileName));
		} catch(FileNotFoundException e) {
			setChanged();
			notifyObservers(new String[] { "properties not found" });
			throw e;
		}
		
		try {
			properties = (Properties) xmlDecoder.readObject();
			setChanged();
			notifyObservers(new String[] { "properties loaded" });
		} catch(ArrayIndexOutOfBoundsException e) {
			setChanged();
			notifyObservers(new String[] { "bad file format" });
			throw e;
		} finally {
			xmlDecoder.close();
		}
	}
	
	@Override
	public Properties getProperties() {
		return properties;
	}
	
	@Override
	public void saveProperties(String fileName, int poolSize, MazeGeneratorTypes generator
			, ViewTypes viewType) {
		try {
			fileName = new URI(fileName).getPath();
		} catch (URISyntaxException e) {
			notifyObservers(new String[] { "properties not found" });
		}
		
		properties = new Properties();
		properties.setPoolSize(poolSize);
		properties.setMazeGeneratorType(generator);
		properties.setViewType(viewType);
		
		XMLEncoder xmlEncoder;
		try {
			xmlEncoder = new XMLEncoder(new FileOutputStream(fileName));
		} catch(FileNotFoundException e) {
			setChanged();
			notifyObservers(new String[] { "properties not found" });
			return;
		}
		
		xmlEncoder.writeObject(properties);
		xmlEncoder.close();
		
		setChanged();
		notifyObservers(new String[] { "properties saved" });
	}
	
	/**
	 * Utility to run tasks in the background, handle cancelation by cancel or interrupt &amp;
	 * handle exceptions
	 * @param task Task to run
	 * @see Task
	 */
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
	
	/**
	 * Utility to compress byte array data
	 * @param data Data to compress
	 * @return byte[] Compressed data
	 * @throws IOException When an error occurs while compressing the data
	 * @see CommonModel#decompressData(byte[])
	 */
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
	
	/**
	 * Utility to decompress byte array data
	 * @param compressedData Compressed data to decompress
	 * @return Decompressed data
	 * @throws IOException When an error occurs while decompressing the data
	 * @see CommonModel#compressData(byte[])
	 */
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
