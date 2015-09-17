package view;

import io.MyDecompressorInputStream;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;

import algorithms.mazeGenerators.Maze3d;

public abstract class CommonView extends Observable implements View {
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

	Maze3d createMaze(byte[] mazeData) throws IOException {
		return new Maze3d(mazeData);
	}
}
