package model;

import io.MyCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.Controller;

/**
 * @author nirleibo
 * <h1>Common implementation of the Model Façade</h1>
 */
public abstract class CommonModel implements Model {
	Controller controller;
	ExecutorService threadPool;
	
	/**
	 * Initiate the Model Façade instance
	 * @param controller Controller Façade instance
	 * @param poolSize Size of the underlying thread pool
	 */
	public CommonModel(Controller controller, int poolSize) {
		this.controller = controller;
		threadPool = Executors.newFixedThreadPool(poolSize);
	}

	/**
	 * Compress byte array data
	 * @param data The data
	 * @return The compressed data
	 * @throws IOException When an error occurs while compressing the data or closing the
	 * 		underlying stream
	 */
	protected byte[] compressData(byte[] data) throws IOException {
		ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
		MyCompressorOutputStream dataCompressor = new MyCompressorOutputStream(new BufferedOutputStream(dataOut));
		try {
			dataCompressor.write(data);
			dataCompressor.flush();
		} catch (IOException e) {
			controller.displayError("error occurred while compressing maze data");
			throw e;
		} finally {
			try {
				dataCompressor.close();
			} catch (IOException e) {
				controller.displayError("error occurred while closing the stream");
				throw e;
			}
		}
		
		return dataOut.toByteArray();
	}
}
