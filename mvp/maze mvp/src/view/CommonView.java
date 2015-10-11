package view;

import io.MyDecompressorInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;

import model.CommonModel;
import algorithms.mazeGenerators.Maze3d;

/**
 * @author Nir Leibovitch
 * <h1>Common implementation of the View Façade</h1>
 */
public abstract class CommonView extends Observable implements View {
	String userCommand;
	
	@Override
	public String getUserCommand() {
		return userCommand;
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

	/**
	 * Utility method to instantiate a maze from byte array data
	 * @param mazeData Maze data
	 * @return Maze3d Maze instance
	 * @throws IOException When the maze could not be instantiated from the given data
	 */
	Maze3d createMaze(byte[] mazeData) throws IOException {
		return new Maze3d(mazeData);
	}
}
