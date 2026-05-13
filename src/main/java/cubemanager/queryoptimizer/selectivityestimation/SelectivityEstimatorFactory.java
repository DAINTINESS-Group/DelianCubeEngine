package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.CubeBase;

public class SelectivityEstimatorFactory {

	public static ISelectivityEstimator create(String method, CubeBase cubeBase, double sampleSize) {
		switch (method.toUpperCase()) {
			case "FULL_TABLE_SCAN":
				return new FullTableScanEstimator(cubeBase);
			case "SAMPLING":
				return new ReservoirSamplingEstimator(cubeBase, sampleSize);
			case "HISTOGRAM":
				return new HistogramEstimator(cubeBase);
			default:
				throw new IllegalArgumentException("Unknown estimation method: " + method);
		}
	}
}
