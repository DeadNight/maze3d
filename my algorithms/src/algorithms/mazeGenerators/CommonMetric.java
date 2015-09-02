package algorithms.mazeGenerators;

/**
 * @author Nir Leibovitch
 * <h1>n-dimensional metric</h1>
 * Represents a n-dimensional metric 
 */
public class CommonMetric implements Metric {
	int[] metrics;
	
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
	
	@Override
	public boolean equals(Object obj) {
		CommonMetric other = (CommonMetric)obj;
		if(metrics.length != other.metrics.length)
			return false;
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
		return toString().hashCode();
	}
}
