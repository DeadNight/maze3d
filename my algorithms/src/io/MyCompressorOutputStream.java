package io;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
	OutputStream out;
	byte lastByte;
	long counter;
	
	public MyCompressorOutputStream(OutputStream out) {
		this.out = out;
	}

	@Override
	public void write(int b) throws IOException {
		if(counter > 0) {
			if(lastByte == (byte) b) {
				counter++;
				return;
			} else {
				write(lastByte, counter);
			}
		}
		
		lastByte = (byte) b;
		counter = 1;
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		if(counter > 0) {
			write(lastByte, counter);
			counter = 0;
		}
	}
	
	@Override
	public void flush() throws IOException {
		if(counter > 0) {
			write(lastByte, counter);
			counter = 0;
		}
		out.flush();
	}
	
	// handle case when counter is greater than byte
	private void write(byte lastByte, long counter) throws IOException {
		while(counter > 255) {
			counter -= 255;
			out.write(lastByte);
			out.write(255);
		}
		if(counter > 0) {
			out.write(lastByte);
			out.write((byte) counter);
		}
	}
}
