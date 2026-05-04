package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.*;
import result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Histogram-based selectivity estimator
 * <p> At construction, fires one GROUP BY query per dimension level column to build
 * a frequency map (value ---> row count)
 * <p> At query time, no DB calls are made, selectivity is estimated by looking up the counts from the map
 *
 */
public class HistogramEstimator implements  ISelectivityEstimator{

	private final CubeBase cubeBase;
	private final Map<String, ColumnHistogram> histograms;

	/**
	 * Hold the frequency distribution of a single dimension level column
	 * frequencyMap maps each distinct value to its row count in the fact table
	 * totalRows is the sum of all counts
	 *
	 */
	private static class ColumnHistogram {
		private final Map<String, Integer> frequencyMap;
		private final int totalRows;

		ColumnHistogram(Map<String, Integer> frequencyMap, int totalRows) {
			this.frequencyMap = frequencyMap;
			this.totalRows = totalRows;
		}
	}

	public HistogramEstimator(CubeBase cubeBase) {
		this.cubeBase = cubeBase;
		this.histograms = new HashMap<>();
		buildAllHistograms();
	}

	@Override
	public List<SelectivityResult> estimate(CubeQuery query) {
		List<SelectivityResult> results = new ArrayList<>();

		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();
		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			SigmaParser.ParsedSigma parsed = SigmaParser.parse(sigma, dimensions, dimRefFields);
			if (parsed == null) continue;

			ColumnHistogram histogram = histograms.get(parsed.filterCol);
			if (histogram == null || histogram.totalRows < 0) continue;

			int matchingRows = countFromHistogram(histogram, sigma[1], sigma[2]);
			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, histogram.totalRows,
					matchingRows));
		}

		return results;
	}

	/**
	 * Iterates all registered cubes and their dimension level columns,
	 * firing one GROUP BY query per column to populate histogram map
	 * It is called once at construction
	 *
	 */
	private void buildAllHistograms() {
		for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
			String factTable = cube.getFactTable().getTableName();
			List<Dimension> dimensions = cube.getDimensionsList();
			List<String> dimRefFields = cube.getDimensionRefFieldList();

			for (int i = 0; i < dimensions.size(); i++) {
				Dimension dimension = dimensions.get(i);
				String dimTable = dimension.getTableName();
				String factFK = dimRefFields.get(i);
				String dimPK = dimTable + "."
						+ ((LinearHierarchy) dimension.getHierarchy().get(0)).getLevels().get(0).getAttributeName(0);

				for (Level level : ((LinearHierarchy) dimension.getHierarchy().get(0)).getLevels()) {
					String filterCol = dimTable + "." + level.getAttributeName(0);
					String sql = "SELECT " + filterCol + ", COUNT(*) FROM " + factTable
							+ " JOIN " + dimTable + " ON " + factFK + " = " + dimPK
							+ " GROUP BY " + filterCol;
					histograms.put(filterCol, buildColumnHistogram(sql));
				}
			}
		}
	}

	/**
	 * Runs a GROUP BY query and builds a (value ---> count) map from the result
	 * @param sql the GROUP BY query
	 * @return a {@link ColumnHistogram} with the frequency map and total row count or
	 * an empty histogram with totalRows = -1 if the query returns no data
	 *
	 */
	private ColumnHistogram buildColumnHistogram(String sql) {
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);

		String[][] resultArray = result.getResultArray();
		if (resultArray == null || resultArray.length < 3) {
			return new ColumnHistogram(new HashMap<>(), -1);
		}

		Map<String, Integer> frequencyMap = new HashMap<>();
		int total = 0;

		// Data starts from [2][*]
		for (int row = 2; row < resultArray.length; row++) {
			String[] currentRow = resultArray[row];
			if (currentRow == null) {
				continue;
			}
			String value = currentRow[0];
			String countStr = currentRow[1];

			if (value == null || countStr == null) {
				continue;
			}
			try {
				int count = Integer.parseInt(countStr);
				frequencyMap.put(value, count);
				total += count;
			} catch (NumberFormatException ignored) {
				// Skip invalid rows
			}
		}
		return new ColumnHistogram(frequencyMap, total);
	}

	// Counts the estimated matching rows for a sigma predicate by iterating the histogram
	private int countFromHistogram(ColumnHistogram histogram, String operator, String value) {
		String cleanValue = value.trim().replace("'", "");
		int matching = 0;

		for (Map.Entry<String, Integer> entry : histogram.frequencyMap.entrySet()) {
			if (evaluateCondition(entry.getKey(), operator, cleanValue)) {
				matching += entry.getValue();
			}
		}

		return matching;
	}

	private boolean evaluateCondition(String colValue, String operator, String cleanValue) {
		switch (operator.trim().toUpperCase()) {
			case "=":  return colValue.trim().equals(cleanValue);
			case ">":  return compareValues(colValue, cleanValue) > 0;
			case "<":  return compareValues(colValue, cleanValue) < 0;
			case ">=": return compareValues(colValue, cleanValue) >= 0;
			case "<=": return compareValues(colValue, cleanValue) <= 0;
			case "IN": {
				for (String v : cleanValue.replaceAll("[(){}]", "").split(","))
					if (colValue.trim().equals(v.trim())) return true;
				return false;
			}
			case "NOT IN": {
				for (String v : cleanValue.replaceAll("[(){}]", "").split(","))
					if (colValue.trim().equals(v.trim())) return false;
				return true;
			}
			case "BETWEEN": {
				String[] bounds = cleanValue.split("AND");
				if (bounds.length != 2) return false;
				return compareValues(colValue, bounds[0].trim()) >= 0
						&& compareValues(colValue, bounds[1].trim()) <= 0;
			}
			default:
				return false;
		}
	}

	private int compareValues(String a, String b) {
		try {
			return Double.compare(Double.parseDouble(a.trim()), Double.parseDouble(b.trim()));
		} catch (NumberFormatException e) {
			return a.trim().compareTo(b.trim());
		}
	}
}
