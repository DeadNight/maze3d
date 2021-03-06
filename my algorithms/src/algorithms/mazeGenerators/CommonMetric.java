package algorithms.mazeGenerators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Nir Leibovitch
 * <h1>n-dimensional metric</h1>
 * Represents a n-dimensional metric 
 */
public abstract class CommonMetric implements Metric {
	private static final long serialVersionUID = -2281169035692259666L;
	int[] metrics;
	
	/**
	 * Initialize a metric of size dimensions with 0's
	 * @param size dimension of the metric
	 */
	public CommonMetric(int size) {
		metrics = new int[size];
	}

	/**
	 * Initialize the metric
	 * @param metrics An array with n numbers, 1 for each dimension
	 */
	public CommonMetric(int[] metrics) {
		this.metrics = new int[metrics.length];
		System.arraycopy(metrics, 0, this.metrics, 0, metrics.length);
	}
	
	/**
	 * Copy another metric
	 * @param other The instance to copy
	 */
	public CommonMetric(CommonMetric other) {
		this.metrics = new int[other.metrics.length];
		System.arraycopy(other.metrics, 0, metrics, 0, metrics.length);
	}
	
	/**
	 * Create a metric from given binary data
	 * @param data The binary data
	 * @throws IOException When reaches the end of data before reading the entire Metric
	 */
	public CommonMetric(byte[] data) throws IOException {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
		try {
			metrics = new int[in.readInt()];
			for(int i = 0; i < metrics.length; ++i) {
				metrics[i] = in.readInt();
			}
		} finally {
			in.close();
		}
	}

	/**
	 * Get the internal metrics (used for serialization)
	 * @return int[] metrics
	 */
	public int[] getMetrics() {
		return metrics;
	}

	/**
	 * Set the internal metrics (used for deserialization)
	 * @param metrics metrics
	 */
	public void setMetrics(int[] metrics) {
		this.metrics = metrics;
	}

	public byte[] toByteArray() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		try {
			dataOut.writeInt(metrics.length);
			for(int i = 0; i < metrics.length; i++) {
				dataOut.writeInt(metrics[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof CommonMetric)) return false;
		CommonMetric other = (CommonMetric)obj;
		
		if(metrics.length != other.metrics.length) return false;
		
		for(int i = 0; i < metrics.length; ++i)
			if(metrics[i] != other.metrics[i])
				return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		for(int i = 0; i < metrics.length - 1; ++i)
			sb.append(metrics[i] + ", ");
		sb.append(metrics[metrics.length - 1] + "}");
		return sb.toString();
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(metrics);
	}
}
