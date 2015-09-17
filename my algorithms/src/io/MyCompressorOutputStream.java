package io;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
	OutputStream out;
	byte lastByte;
	int counter;
	
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
				out.write(lastByte);
				out.write(counter);
			}
		}
		
		lastByte = (byte) b;
		counter = 1;
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		if(counter > 0) {
			out.write(lastByte);
			out.write(counter);
			counter = 0;
		}
	}
	
	@Override
	public void flush() throws IOException {
		if(counter > 0) {
			out.write(lastByte);
			out.write(counter);
			counter = 0;
		}
		out.flush();
	}
}
