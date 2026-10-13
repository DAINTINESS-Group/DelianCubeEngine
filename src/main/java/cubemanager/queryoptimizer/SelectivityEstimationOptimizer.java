package cubemanager.queryoptimizer;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.selectivityestimation.ISelectivityEstimator;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityEstimatorFactory;
import result.Result;

/**
 Query optimization strategy for selectivity estimation
 Resolves the fact table size once - either from the estimator's pre-loaded statistics file, or via a {@code COUNT(*)} to the DB
 as fallback (if we dont have a pre-built file, e.g. FUllTableScan). The size is cached as a field so repeated calls to optimize(CubeQuery) do not
 requery the DB
 */
public class SelectivityEstimationOptimizer implements IQueryOptimization {

    private final ISelectivityEstimator selectivityEstimator;
    private final CubeBase cubeBase;
	private int factTableSize = -1;

    public SelectivityEstimationOptimizer(CubeBase cubebase, String method, String inputFolder, String cubeName) {
        this.cubeBase = cubebase;
        this.selectivityEstimator = SelectivityEstimatorFactory.create(method, cubebase, inputFolder, cubeName);
    }

    @Override
    public Object optimize(CubeQuery  query) {
        String factTable = query.getReferCube().getFactTable().getTableName();
		if (factTableSize < 0) {
			int fromEstimator = selectivityEstimator.getFactTableSize();

			if (fromEstimator >= 0) {
				factTableSize = fromEstimator;
			} else {
				factTableSize = computeFactTableSize(factTable);
			}
		}
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
