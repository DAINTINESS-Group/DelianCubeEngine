package cubemanager.queryoptimizer;

import java.util.List;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.selectivityestimation.ISelectivityEstimator;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityEstimatorFactory;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityResult;

public class QueryOptimizer {

	private final ISelectivityEstimator estimator;

	public QueryOptimizer(CubeBase cubeBase, String method, int sampleSize) {
		this.estimator = SelectivityEstimatorFactory.create(method, cubeBase, sampleSize);
	}

	public List<SelectivityResult> estimateSelectivity(CubeQuery query) {
		return estimator.estimate(query);
	}
}
