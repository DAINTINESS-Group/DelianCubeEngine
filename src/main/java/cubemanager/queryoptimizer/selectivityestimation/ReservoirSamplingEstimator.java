package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.LinearHierarchy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import result.Result;


/**
 * Reservoir sampling-based selectivity estimator
 * <p> At construction, applies R algorithm to build a random sample per dimension level column
 * <p> At query time, no DB calls are made, selectivity is estimated by counting the matches in the sample
 * and projecting to the total rows
 *
*/
public class ReservoirSamplingEstimator implements ISelectivityEstimator {

	private final CubeBase cubeBase;
	private final int sampleSize;
	private final Random random;
	private final Map<String, ColumnSample> columnSamples; // Samples for every column (the keys are column names e.g. "account.region")

	/**
	 * Hold the reservoir sample for a single dimension level column
	 * values contains the sampled rows
	 * totalRows is the actual total row count of the full population
	 *
	 */
	private static class ColumnSample {
		private final String[] values;
		private final int totalRows;

		ColumnSample(String[] values, int totalRows) {
			this.values = values;
			this.totalRows = totalRows;
		}
	}

	public ReservoirSamplingEstimator(CubeBase cubeBase, int sampleSize) {
		this.cubeBase = cubeBase;
		this.sampleSize = sampleSize;
		this.random = new Random();
		this.columnSamples = new HashMap<>();
		buildAllSamples();
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

			ColumnSample sample = columnSamples.get(parsed.filterCol);
			if (sample == null || sample.totalRows < 0) continue;

			int matchingInSample = countMatches(sample.values, sigma[1], sigma[2]);
			int estimatedMatching;

			if (sample.values.length == 0) {
				estimatedMatching = 0;
			} else {
				double ratio = (double) matchingInSample / sample.values.length;
				double scaled = ratio * sample.totalRows;
				estimatedMatching = (int) Math.round(scaled);
			}

			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, sample.totalRows, estimatedMatching));
		}

		return results;
	}

	/**
	 * Iterates all registered cubes and their dimension level columns,
	 * firing one SELECT query per column to build a reservor sample
	 * It is called once at construction
	 *
	*/
	private void buildAllSamples() {
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
					String sql = "SELECT " + filterCol + " FROM " + factTable
							+ " JOIN " + dimTable + " ON " + factFK + " = " + dimPK;
					columnSamples.put(filterCol, buildSample(sql));
				}
			}
		}
	}

	/**
	 * Runs Algorithm R on the SQL result to build a reservoir of at most sampleSize rows.
	 * @param sql the SELECT query
	 * @return a {@link ColumnSample} with the reservoir and total row count or
	 * an empty sample with totalRows = -1 if the query returns no data
	 *
	 */
	private ColumnSample buildSample(String sql) {
		cubemanager.relationalstarschema.Database db =
				(cubemanager.relationalstarschema.Database) cubeBase.getDataSourceDescription();

		String[] reservoir = new String[sampleSize];
		int count = 0;

		try (java.sql.Statement stmt = db.getConnection().createStatement(
				java.sql.ResultSet.TYPE_FORWARD_ONLY,
				java.sql.ResultSet.CONCUR_READ_ONLY)) {

			stmt.setFetchSize(Integer.MIN_VALUE);
			try (java.sql.ResultSet rs = stmt.executeQuery(sql)) {
				while (rs.next()) {
					String value = rs.getString(1);
					if (value == null) continue;
					count++;
					if (count <= sampleSize) {
						reservoir[count - 1] = value;
					} else {
						int j = random.nextInt(count);
						if (j < sampleSize) reservoir[j] = value;
					}
				}
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			return new ColumnSample(new String[0], -1);
		}

		String[] finalSample = count < sampleSize ? Arrays.copyOf(reservoir, count) : reservoir;
		return new ColumnSample(finalSample, count);
	}

	// Counts the estimated matching rows for a sigma predicate by iterating the sample
	private int countMatches(String[] sample, String operator, String value) {
		String cleanValue = value.trim().replace("'", "");
		int matching = 0;
		for (String v : sample) {
			if (v != null && evaluateCondition(v, operator, cleanValue)) {
				matching++;
			}
		}
		return matching;
	}

	private boolean evaluateCondition(String colValue, String operator, String cleanValue) {
		switch (operator.trim().toUpperCase()) {
			case "=": return colValue.trim().equals(cleanValue);
			case ">": return compareValues(colValue, cleanValue) > 0;
			case "<": return compareValues(colValue, cleanValue) < 0;
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
				return compareValues(colValue, bounds[0]) >= 0 && compareValues(colValue, bounds[1]) <= 0;
			}
			default: return false;
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
