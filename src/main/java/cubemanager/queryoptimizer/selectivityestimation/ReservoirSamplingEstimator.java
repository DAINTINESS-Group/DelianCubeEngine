package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;

import java.util.*;

import result.Result;


/**
 * Reservoir sampling-based selectivity estimator.
 * <p> At construction, applies R algorithm to build a random sample of FK values from the fact table,
 * one reservoir per FK column.
 * </p>
 * <p> At query time, it fetches all matching dimension PKs from the DB and scans the sample to count FK matches.
 * Selectivity is estimated as (matchingInSample / sampleSize)
 * </p>
 *
*/
public class ReservoirSamplingEstimator implements ISelectivityEstimator {

	private final CubeBase cubeBase;
	private final double sampleSize;
	private final Random random;
	private final Map<String, ColumnSample> columnSamples; // Samples for every FK column of the fact table (keys are FK column names e.g. "loan.account_id").

	/**
	 * Hold the reservoir sample for a single FK column of the fact table.
	 * {@code values} contains the sampled FK values
	 *
	 */
	private static class ColumnSample {
		private final String[] values;

		ColumnSample(String[] values) {
			this.values = values;
		}
	}

	public ReservoirSamplingEstimator(CubeBase cubeBase, double sampleSize) {
		this.cubeBase = cubeBase;
		this.sampleSize = sampleSize;
		this.random = new Random();
		this.columnSamples = new HashMap<>();
		buildAllSamples();
	}

	@Override
	public List<SelectivityResult> estimate(CubeQuery query, int factTableSize) {
		List<SelectivityResult> results = new ArrayList<>();

		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();
		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			SigmaParser.ParsedSigma parsed = SigmaParser.parse(sigma, dimensions, dimRefFields);
			if (parsed == null) continue;

			ColumnSample sample = columnSamples.get(parsed.factFK);
			if (sample == null || factTableSize < 0) continue;

			Set<String> matchingPKs = getMatchingPKs(parsed.dimTable, parsed.dimPK, parsed.filterCol, sigma[1], sigma[2]);

			int matchingInSample = countFKMatches(sample.values, matchingPKs);
			int estimatedMatching;
			if (sample.values.length == 0) {
				estimatedMatching = 0;
			} else {
				estimatedMatching = (int) Math.round((double) matchingInSample / sample.values.length * factTableSize);
			}

			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, factTableSize, estimatedMatching));
		}

		return results;
	}

	private Set<String> getMatchingPKs(String dimTable, String dimPK, String filterCol, String operator, String value) {
		String sql = "SELECT " + dimPK + " FROM " + dimTable + " WHERE " + filterCol + " " + operator + " " + value;
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);

		Set<String> matchingPKs = new HashSet<>();
		String[][] resultArray = result.getResultArray();
		if (resultArray == null || resultArray.length < 3) return matchingPKs;

		for (int row = 2; row < resultArray.length; row++) {
			if (resultArray[row] != null && resultArray[row][0] != null) {
				matchingPKs.add(resultArray[row][0].trim());
			}
		}
		return matchingPKs;
	}

	private int countFKMatches(String[] sample, Set<String> matchingPKs) {
		int count = 0;
		for (String fk : sample) {
			if (fk != null && matchingPKs.contains(fk.trim())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Iterates all registered cubes and their FK columns from the fact table,
	 * firing one SELECT query per FK column to build a reservoir sample.
	 * Called once at construction.
	 *
	*/
	private void buildAllSamples() {
		for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
			String factTable = cube.getFactTable().getTableName();
			List<String> dimRefFields = cube.getDimensionRefFieldList();

			int factTableSize = computeFactTableSize(factTable);
			int reservoirSize = (int)(sampleSize * factTableSize);

			for (String factFK : dimRefFields) {
				String sql = "SELECT " + factFK + " FROM " + factTable;
				columnSamples.put(factFK, buildSample(sql, reservoirSize));
			}
		}
	}

	private int computeFactTableSize(String factTable) {
		String sql = "SELECT COUNT(*) FROM " + factTable;
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);
		String[][] resultArray = result.getResultArray();
		if (resultArray == null || resultArray.length < 3 || resultArray[2][0] == null) return -1;
		try {
			return Integer.parseInt(resultArray[2][0]);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Runs Algorithm R on the SQL result to build a reservoir of at most reservoirSize rows.
	 * @param sql the SELECT query
	 * @param reservoirSize the maximum number of rows to keep in the reservoir
	 * @return a {@link ColumnSample} with the reservoir or an empty sample if the query fails
	 */
	private ColumnSample buildSample(String sql, int reservoirSize) {
		cubemanager.relationalstarschema.Database db =
				(cubemanager.relationalstarschema.Database) cubeBase.getDataSourceDescription();

		String[] reservoir = new String[reservoirSize];
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
					if (count <= reservoirSize) {
						reservoir[count - 1] = value;
					} else {
						int j = random.nextInt(count);
						if (j < reservoirSize) reservoir[j] = value;
					}
				}
			}
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			return new ColumnSample(new String[0]);
		}

		String[] finalSample = count < reservoirSize ? Arrays.copyOf(reservoir, count) : reservoir;
		return new ColumnSample(finalSample);
	}
}
