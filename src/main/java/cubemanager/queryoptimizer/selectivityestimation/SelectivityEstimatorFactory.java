package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.CubeBase;

public class SelectivityEstimatorFactory {

	public static ISelectivityEstimator create(String method, CubeBase cubeBase, String inputFolder, String cubeName) {
		switch (method.toUpperCase()) {
			case "FULL_TABLE_SCAN":
				return new FullTableScanEstimator(cubeBase);
			case "SAMPLING":
				return new ReservoirSamplingEstimator(inputFolder, cubeName, cubeBase);
			case "HISTOGRAM":
				return new HistogramEstimator(inputFolder, cubeName);
			case "COMBINED":
				return new CombinedEstimator(inputFolder, cubeName, cubeBase);
			default:
				throw new IllegalArgumentException("Unknown estimation method: " + method);
		}
	}
}
