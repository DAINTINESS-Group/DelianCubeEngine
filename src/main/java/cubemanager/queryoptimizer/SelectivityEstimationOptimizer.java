package cubemanager.queryoptimizer;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.queryoptimizer.selectivityestimation.ISelectivityEstimator;
import cubemanager.queryoptimizer.selectivityestimation.SelectivityEstimatorFactory;
import result.Result;
import java.io.File;

/**
 Query optimization strategy for selectivity estimation
 <p>
 Resolves the fact table size once - either from the estimator's pre-loaded statistics file, or via a {@code COUNT(*)} to the DB
 as fallback (if we dont have a pre-built file, e.g. FUllTableScan). The size is cached as a field so repeated calls to optimize(CubeQuery) do not
 requery the DB
 </p>
 */
public class SelectivityEstimationOptimizer implements IQueryOptimization {

    private final ISelectivityEstimator selectivityEstimator;
    private final CubeBase cubeBase;
	private int factTableSize = -1;

    public SelectivityEstimationOptimizer(CubeBase cubebase, String method, String inputFolder, String cubeName) {
        this.cubeBase = cubebase;
        checkStatisticsAvailable(method, inputFolder, cubeName);
        this.selectivityEstimator = SelectivityEstimatorFactory.create(method, cubebase, inputFolder, cubeName);
    }

    private void checkStatisticsAvailable(String method, String inputFolder, String cubeName) {
        switch (method.toUpperCase()) {
            case "HISTOGRAM":
                checkFile(inputFolder, cubeName, "histograms");
                break;
            case "SAMPLING":
                checkFile(inputFolder, cubeName, "samples");
                break;
            case "COMBINED":
                checkFile(inputFolder, cubeName, "histograms");
                checkFile(inputFolder, cubeName, "samples");
                break;
            default:
                return;
        }
    }

    private void checkFile(String inputFolder, String cubeName, String fileType) {
        File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_" + fileType + ".csv");
        if (!file.exists()) {
            throw new IllegalStateException(
                "No " + fileType + " file found for cube '" + cubeName + "' in folder '" + inputFolder + "'. " +
                "Run build" + Character.toUpperCase(fileType.charAt(0)) + fileType.substring(1) + "() first."
            );
        }
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
