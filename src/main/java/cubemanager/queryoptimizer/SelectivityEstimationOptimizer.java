package cubemanager.queryoptimizer;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.selectivityestimation.ISelectivityEstimator;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityEstimatorFactory;

/**
 * Query optimization strategy for selectivity estimation
 * Delegates every query to the estimator that was selected for the session. The size of the fact table, is
 * supplied by the estimator itself. Those that read a pre-built statistics file take it from there
 * while the full table scan counts the fact table on first use.
 */
public class SelectivityEstimationOptimizer implements IQueryOptimization {

    private final ISelectivityEstimator selectivityEstimator;

    public SelectivityEstimationOptimizer(CubeBase cubebase, String method, String inputFolder, String cubeName) {
        this.selectivityEstimator = SelectivityEstimatorFactory.create(method, cubebase, inputFolder, cubeName);
    }

    @Override
    public Object optimize(CubeQuery  query) {
        return selectivityEstimator.estimate(query, selectivityEstimator.getFactTableSize());
    }
}
