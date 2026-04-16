package mainengine.selectivityestimation;

import java.util.ArrayList;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.LinearHierarchy;
import result.Result;

/**
 * The ground truth selectivity estimator. For each sigma predicate it fires two
 * COUNT queries against the database and computes the exact selectivity.
 */
public class FullTableScanEstimator implements ISelectivityEstimator {

	@Override
	public List<SelectivityResult> estimate(CubeQuery query, CubeManager cubeManager) {
		List<SelectivityResult> results = new ArrayList<>();

		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();
		int totalRows = executeTotalCount(factTable, cubeManager);

		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			String[] parts = sigma[0].split("\\.");
			if (parts.length < 2) {
				System.out.println("Should be in the form of: dimName.dimLevelName !!!");
				continue;
			}

			String dimName = parts[0].trim();
			String levelName = parts[1].trim();

			for (int i = 0; i < dimensions.size(); i++) {
				Dimension dimension = dimensions.get(i);
				if (!dimension.hasSameName(dimName)) {
					continue;
				}

				String dimTable = dimension.getTableName();
				String factFK = dimRefFields.get(i);

				String dimPK = dimTable + "."
						+ ((LinearHierarchy) dimension.getHierarchy().get(0)).getLevels().get(0).getAttributeName(0);

				String filterCol = null;
				for (Level level : ((LinearHierarchy) dimension.getHierarchy().get(0)).getLevels()) {
					if (level.getName().equals(levelName)) {
						filterCol = dimTable + "." + level.getAttributeName(0);
						break;
					}
				}

				if (filterCol == null) {
					break;
				}

				int matchingRows = executeFilteredCount(factTable, factFK, dimTable, dimPK, filterCol, sigma[1],
						sigma[2], cubeManager);
				results.add(new SelectivityResult(sigma, factTable, filterCol, totalRows, matchingRows));
				break;
			}
		}

		return results;
	}

	private int executeTotalCount(String factTable, CubeManager cubeManager) {
		String sql = "SELECT COUNT(*) FROM " + factTable;
		return runCountQuery(sql, cubeManager);
	}

	private int executeFilteredCount(String factTable, String factFK, String dimTable, String dimPK, String filterCol,
			String operator, String value, CubeManager cubeManager) {

		String join = factTable + " JOIN " + dimTable + " ON " + factFK + " = " + dimPK;
		String where = buildWhereClause(filterCol, operator, value);
		String sql = "SELECT COUNT(*) FROM " + join + " WHERE " + where;
		return runCountQuery(sql, cubeManager);
	}

	// Builds the WHERE clause for the sigma predicate
	private String buildWhereClause(String filterCol, String operator, String value) {
		switch (operator.trim().toUpperCase()) {
		case "IN":
		case "NOT IN":
			return filterCol + " " + operator + " " + value;
		case "BETWEEN":
			return filterCol + " BETWEEN " + value;
		default:
			return filterCol + " " + operator + " " + value;
		}
	}

	// Runs a COUNT(*) query and returns the number
	// resultArray[2][0] is the count we want
	private int runCountQuery(String sql, CubeManager cubeManager) {
		Result result = new Result();
		cubeManager.getCubeBase().executeQueryToProduceResult(sql, result);

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
