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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import cubemanager.queryoptimizer.selectivityestimation.SigmaParser.ParsedSigma;

/**
 * A class that: (i) loads a pre-built sample of fact table rows into a MySQL InnoDB table,
 * (ii) estimates the selectivity of each sigma predicate by joining that sample to the dimension tables
 * and scaling the result back to the full fact table size
 */
public class ReservoirSamplingEstimator implements ISelectivityEstimator {

	private final String sampleTableName;
	private int storedFactTableSize = -1;
	private int sampleSize;
	private final CubeBase cubeBase;

	public ReservoirSamplingEstimator(String inputFolder, String cubeName, CubeBase cubeBase) {
		this.sampleTableName = "smpl_" + cubeName;
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
	 * Estimates the selectivity of each sigma predicate in the query.
	 * Sigmas that cannot be resolved to a physical column are skipped
	 */
	@Override
	public List<SelectivityResult> estimate(CubeQuery query, int factTableSize) {
		List<SelectivityResult> results = new ArrayList<>();

		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();
		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			ParsedSigma parsed = SigmaParser.parse(sigma, dimensions, dimRefFields);
			if (parsed == null || factTableSize < 0) {
				continue;
			}

			int matchingInSample = countFromSampleTable(parsed, sigma[1], sigma[2]);
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
	 * Loads the sampled fact rows from {cubeName}_samples.csv into a MySQL InnoDB table
	 * whose columns are the fact table's fk's
	 */
	private void loadFromFile(File file) {
		List<String> fkColumns = new ArrayList<>();
		List<String[]> rows = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("factTableSize = ")) {
					try {
						storedFactTableSize = Integer.parseInt(line.substring("factTableSize = ".length()).trim());
					} catch (NumberFormatException ignored) {}
					continue;
				}

				if (fkColumns.isEmpty()) {
					fkColumns = Arrays.asList(line.split("\\|"));
					continue;
				}

				String[] values = line.split("\\|", -1);
				if (values.length == fkColumns.size()) {
					rows.add(values);
				}
			}
			sampleSize = rows.size();

			Connection conn = ((Database) cubeBase.getDataSourceDescription()).getConnection();

			List<String> columnDefs = new ArrayList<>();
			List<String> placeholders = new ArrayList<>();
			for (String column : fkColumns) {
				columnDefs.add(column + " VARCHAR(255)");
				placeholders.add("?");
			}

			conn.createStatement().executeUpdate("DROP TABLE IF EXISTS " + sampleTableName);
			conn.createStatement().executeUpdate("CREATE TABLE  " + sampleTableName
				+ " (" + String.join(", ", columnDefs) + ") ENGINE = InnoDB");


			PreparedStatement insert = conn.prepareStatement("INSERT INTO " + sampleTableName
			 + " VALUES (" + String.join(", ", placeholders) + ")");

			for (String[] row : rows) {
				for (int i = 0; i < row.length; i++) {
					insert.setString(i + 1, row[i]);
				}

				insert.addBatch();
			}
			insert.executeBatch();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	private int countFromSampleTable(ParsedSigma parsed, String operator, String value) {
		String fkColumn = parsed.factFK.substring(parsed.factFK.indexOf('.') + 1);
		String sql = "SELECT COUNT(*) FROM " + sampleTableName
				+ " JOIN " + parsed.dimTable + " ON " + sampleTableName + "." + fkColumn + " = " + parsed.dimPK
				+ " WHERE " + parsed.filterCol + " " + operator + " " + value;
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
