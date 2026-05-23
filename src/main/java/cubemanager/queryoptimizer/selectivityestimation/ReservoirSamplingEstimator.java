package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.relationalstarschema.Database;
import result.Result;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;


/**
 * Reservoir sampling-based selectivity estimator.
 <p>
 At construction, loads a pre-built CSV file of sampled dimension level column values
 and inserts them into a MySQL MEMORY table named {@smpl_{cubeName}}, with columns {@code (col_name, values)}.
 </p>
 <p>
 At query time, fires a {@code COUNT} SQL query against the MEMORY table to count how many sampled values statisfy the
 sigma predicate, then scales the result back to the full table size
 </p>
 */
public class ReservoirSamplingEstimator implements ISelectivityEstimator {

	private final String memoryTableName;
	private int storedFactTableSize = -1;
	private int sampleSize;
	private final CubeBase cubeBase;

	public ReservoirSamplingEstimator(String inputFolder, String cubeName, CubeBase cubeBase) {
		this.memoryTableName = "smpl_" + cubeName;
		this.sampleSize = 0;
		this.cubeBase = cubeBase;
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_samples.csv");
		loadFromFile(file);
	}

	@Override
	public int getFactTableSize() {
		return storedFactTableSize;
	}

	/**
	 * Estimates selectivity for each sigma predicate
	 * Returns an empty list for sigmas that cannot be parsed
	 * @param query
	 * @param factTableSize
	 * @return
	 */
	@Override
	public List<SelectivityResult> estimate(CubeQuery query, int factTableSize) {
		List<SelectivityResult> results = new ArrayList<>();

		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();
		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			SigmaParser.ParsedSigma parsed = SigmaParser.parse(sigma, dimensions, dimRefFields);
			if (parsed == null || factTableSize < 0) {
				continue;
			}

			int matchingInSample = countFromMemoryTable(parsed.filterCol, sigma[1], sigma[2]);
			if (matchingInSample < 0) {
				continue;
			}

			int estimatedMatching;
			if (sampleSize == 0) {
				estimatedMatching = 0;
			} else {
				estimatedMatching = (int) Math.round((double) matchingInSample / sampleSize * factTableSize);
			}
			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, factTableSize, estimatedMatching));
		}

		return results;
	}

	/**
	 * Loads sample data from a pre-built CSV file into a single MySQL MEMORY table.
	 * Expected format:
	 * <pre>
	 * columnName|value1,value2,value3,...
	 * factTableSize = N
	 * </pre>
	 */
	private void loadFromFile(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			reader.readLine();
			String line;

			Connection conn = ((Database) cubeBase.getDataSourceDescription()).getConnection();

			conn.createStatement().executeUpdate("DROP TABLE IF EXISTS " + memoryTableName);
			conn.createStatement().executeUpdate(
				"CREATE TABLE " + memoryTableName + " (col_name VARCHAR(255), val VARCHAR(255)) ENGINE=MEMORY"
			);

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("factTableSize = ")) {
					try {
						storedFactTableSize = Integer.parseInt(line.substring("factTableSize = ".length()).trim());
					} catch (NumberFormatException ignore) {}
					continue;
				}

				String[] parts = line.split("\\|");
				if (parts.length != 2) continue;

				String columnName = parts[0];
				String[] values = parts[1].split(",");

				StringBuilder sb = new StringBuilder("INSERT INTO " + memoryTableName + " VALUES ");
				for (int i = 0; i < values.length; i++) {
					sb.append("('").append(columnName.replace("'", "\\'")).append("','")
					  .append(values[i].replace("'", "\\'")).append("')");
					if (i < values.length - 1) {
						sb.append(",");
					}
				}
				conn.createStatement().executeUpdate(sb.toString());

				if (sampleSize == 0) {
					sampleSize = values.length;
				}
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	private int countFromMemoryTable(String columnName, String operator, String value) {
		String sql = "SELECT COUNT(*) FROM " + memoryTableName
				+ " WHERE col_name = '" + columnName + "' AND val " + operator + " " + value;
		return runCountQuery(sql);
	}

	private int runCountQuery(String sql) {
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);
		String[][] resultArray = result.getResultArray();
		if (resultArray == null || resultArray.length < 3 || resultArray[2][0] == null) return -1;
		try {
			return Integer.parseInt(resultArray[2][0]);
		} catch (Exception e) {
			return -1;
		}
	}
}
