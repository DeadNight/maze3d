package io;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
	InputStream in;
	int counter;
	byte lastByte;
	
	public MyDecompressorInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		if(counter <= 0) {
			lastByte = (byte) in.read();
			if(lastByte == -1)
				return -1;
			counter = in.read();
			if(counter == -1)
				throw new IOException("expected counter after value counter");
			if(counter == 0)
				throw new IOException("expected positive counter");
		}
		counter--;
		return lastByte;
	}
}
