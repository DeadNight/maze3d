package io;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
	OutputStream out;
	byte lastByte;
	int counter;
	
	public MyCompressorOutputStream(OutputStream out) {
		this.out = out;
		counter = 0;
	}

	@Override
	public void write(int b) throws IOException {
		if(counter > 0) {
			counter++;
			if(lastByte == (byte) b) {
				return;
			} else {
				out.write(b);
				out.write(counter);
			}
		}
		
		lastByte = (byte) b;
		counter = 1;
	}
}
