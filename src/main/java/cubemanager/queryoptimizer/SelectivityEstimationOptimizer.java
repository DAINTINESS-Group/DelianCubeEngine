package cubemanager.queryoptimizer;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.selectivityestimation.ISelectivityEstimator;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityEstimatorFactory;
import result.Result;

/**
 * Query optimization strategy for selectivity estimation.
 * Computes the fact table size once and delegates to the chosen {@link ISelectivityEstimator}
 * implementation to estimate the selectivity for each sigma predicate in the query
 *
 */
public class SelectivityEstimationOptimizer implements IQueryOptimization {

    private final ISelectivityEstimator selectivityEstimator;
    private final CubeBase cubeBase;

    public SelectivityEstimationOptimizer(CubeBase cubebase, String method, double sampleSize) {
        this.cubeBase = cubebase;
        this.selectivityEstimator = SelectivityEstimatorFactory.create(method, cubebase, sampleSize);
    }

    @Override
    public Object optimize(CubeQuery  query) {
        String factTable = query.getReferCube().getFactTable().getTableName();
        int factTableSize = computeFactTableSize(factTable);
        return selectivityEstimator.estimate(query, factTableSize);
    }

    private int computeFactTableSize(String factTable) {
        String sql = "SELECT COUNT(*) FROM " + factTable;
        Result result = new Result();
        cubeBase.executeQueryToProduceResult(sql, result);
        String[][] resultArray = result.getResultArray();
        if (resultArray == null ||  resultArray.length < 3 || resultArray[2][0] == null) return -1;
        try {
            return Integer.parseInt(resultArray[2][0]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
