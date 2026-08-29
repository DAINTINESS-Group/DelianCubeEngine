package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;

import java.util.*;

/**
 * Combined selectivity estimator that weights histogram and reservoir sampling estimates.
 * The final estimate is: HISTOGRAM_WEIGHT * histogramMatching + SAMPLING_WEIGHT * samplingMatching.
 * A predicate is included only if both estimators produced a result for it.
 */
public class CombinedEstimator implements ISelectivityEstimator {

	private static final double HISTOGRAM_WEIGHT = 0.5;
	private static final double SAMPLING_WEIGHT  = 0.5;

	private final HistogramEstimator histogramEstimator;
	private final ReservoirSamplingEstimator samplingEstimator;

	public CombinedEstimator(String inputFolder, String cubeName, CubeBase cubeBase) {
		this.histogramEstimator = new HistogramEstimator(inputFolder, cubeName);
		this.samplingEstimator  = new ReservoirSamplingEstimator(inputFolder, cubeName, cubeBase);
	}

	@Override
	public int getFactTableSize() {
		return histogramEstimator.getFactTableSize();
	}

	@Override
	public List<SelectivityResult> estimate(CubeQuery query, int factTableSize) {
		List<SelectivityResult> histResults = histogramEstimator.estimate(query, factTableSize);
		List<SelectivityResult> sampResults = samplingEstimator.estimate(query, factTableSize);

		Map<String, SelectivityResult> histMap = new HashMap<>();
		for (SelectivityResult result : histResults) {
			histMap.put(Arrays.toString(result.getSigmaExpression()), result);
		}

		List<SelectivityResult> combined = new ArrayList<>();
		for (SelectivityResult sampResult : sampResults) {
			SelectivityResult histResult = histMap.get(Arrays.toString(sampResult.getSigmaExpression()));
			if (histResult == null) continue;

			int combinedMatching = (int) Math.round(
				HISTOGRAM_WEIGHT * histResult.getMatchingRows() +
				SAMPLING_WEIGHT  * sampResult.getMatchingRows()
			);

			combined.add(new SelectivityResult(
				histResult.getSigmaExpression(),
				histResult.getTableName(),
				histResult.getColumnName(),
				factTableSize,
				combinedMatching
			));
		}

		return combined;
	}
}
