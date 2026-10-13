package cubemanager.queryoptimizer.selectivityestimation;

import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import result.Result;

/**
 * The ground truth selectivity estimator. For each sigma predicate it fires one
 * COUNT query against the database and computes the exact selectivity.
 * The total row count of the fact table is computed in {@link cubemanager.queryoptimizer.SelectivityEstimationOptimizer}
 * and is getting passed as {@code factTableSize}
 */
public class FullTableScanEstimator implements ISelectivityEstimator {

	private final CubeBase cubeBase;

	public FullTableScanEstimator(CubeBase cubeBase) {this.cubeBase = cubeBase;}

	@Override
	public List<SelectivityResult> estimate(CubeQuery query, int factTableSize) {
		List<SelectivityResult> results = new ArrayList<>();

		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();

		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			SigmaParser.ParsedSigma parsed = SigmaParser.parse(sigma, dimensions, dimRefFields);
			if (parsed == null) {
				System.out.println("Couldnt resolve sigma: " + sigma[0]);
				continue;
			}

			int matchingRows = executeFilteredCount(factTable, parsed.factFK, parsed.dimTable,
					parsed.dimPK, parsed.filterCol, sigma[1], sigma[2]);
			if (matchingRows < 0) continue;

			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, factTableSize, matchingRows));
		}

		return results;
	}

	private int executeFilteredCount(String factTable, String factFK, String dimTable, String dimPK, String filterCol,
			String operator, String value) {

		String join = factTable + " JOIN " + dimTable + " ON " + factFK + " = " + dimPK;
		String where = filterCol + " " + operator + " " + value;
		String sql = "SELECT COUNT(*) FROM " + join + " WHERE " + where;
		return runCountQuery(sql);
	}

	private int runCountQuery(String sql) {
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);

		String[][] resultArray = result.getResultArray();
		if (resultArray == null || resultArray.length < 3 || resultArray[2] == null || resultArray[2][0] == null) {
			return -1;
		}

		try {
			return Integer.parseInt(resultArray[2][0]);
		} catch (Exception e) {
			return -1;
		}
	}
}
