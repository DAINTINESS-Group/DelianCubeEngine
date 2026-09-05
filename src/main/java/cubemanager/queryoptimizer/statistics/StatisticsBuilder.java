package cubemanager.queryoptimizer.statistics;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.relationalstarschema.Database;
import result.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * A class that builds the offline statistics used by the selectivity estimators:
 * (i) histograms, holding the frequency of every dimension level value,
 * (ii) samples of the fact table rows.
 * Both are written to csv files inside inputFolder. If a file already exists the build is
 * skipped unless forceRebuild is true
 */
public class StatisticsBuilder {

	private final IReservoirSampler sampler;

	/**
	 * Creates a builder that samples with Algorithm R
	 */
	public StatisticsBuilder() {
		this(new AlgorithmRSampler());
	}

	/**
	 * Creates a builder that samples with the given algorithm
	 * @param sampler the reservoir sampling algorithm to use
	 */
	public StatisticsBuilder(IReservoirSampler sampler) {
		this.sampler = sampler;
	}

	/**
	 * Fires one GROUP BY query per dimension level column and writes the frequency distribution
	 * to {cubeName}_histograms.csv in the inputFolder directory.
	 * Format :
	 * <pre>
	 * columnName|value|count
	 * factTableSize = N
	 * ...
	 * </pre>
	 */
	public boolean buildHistograms(CubeBase cubeBase, String inputFolder, String cubeName, boolean forceRebuild) throws Exception {
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_histograms.csv");
		if (file.exists() && !forceRebuild) return false; // Skip building if file already exists

		try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

			for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
				String factTable = cube.getFactTable().getTableName();
				List<Dimension> dimensions = cube.getDimensionsList();
				List<String> dimRefFields = cube.getDimensionRefFieldList();

				int factTableSize = computeFactTableSize(factTable, cubeBase);
				writer.println("factTableSize = " + factTableSize);
				writer.println("columnName|value|count");

				for (int i = 0; i < dimensions.size(); i++) {
					Dimension dimension = dimensions.get(i);
					String dimTable = dimension.getTableName();
					String factFK = dimRefFields.get(i);
					String dimPK = dimTable + "."
							+ (dimension.getHierarchy().get(0).getLevels().get(0).getAttributeName(0));

					for (Level level : dimension.getHierarchy().get(0).getLevels()) {
						String filterCol = dimTable + "." + level.getAttributeName(0);
						String sql = "SELECT " + filterCol + ", COUNT(*) FROM " + factTable
								+ " JOIN " + dimTable + " ON " + factFK + " = " + dimPK
								+ " GROUP BY " + filterCol;

						Result result = new Result();
						cubeBase.executeQueryToProduceResult(sql, result);

						String[][] resultArray = result.getResultArray();
						if (resultArray == null || resultArray.length < 3) continue;

						for (int row = 2; row < resultArray.length; row ++) {
							String[] currentRow = resultArray[row];
							if (currentRow == null || currentRow[0] == null || currentRow[1] == null) continue;
							writer.println(filterCol + "|" + currentRow[0] + "|" + currentRow[1]);
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * Samples the fact table only. A reservoir of surrogate keys is drawn without reading the table, and only the selected rows
	 * are then fetched by key. Dimension level values are resolved by joining the sample to the dimension tables at estimation time.
	 * The result is written to {cubeName}_samples.csv in the inputFolder directory. The format is:
	 * <pre>
	 * 	factTableSize = N
	 * 	fkColumn1|fkColumn2|fkColumn3
	 * 	value|value|value
	 * </pre>
	 **/
	public boolean buildSamples(CubeBase cubeBase, String inputFolder, String cubeName, double sampleSize, boolean forceRebuild) throws Exception{
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_samples.csv");
		if (file.exists() && !forceRebuild) return false; // Skip building if file already exists

		Database db = (Database) cubeBase.getDataSourceDescription();

		try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
			Random random = new Random();

			for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
				String factTable = cube.getFactTable().getTableName();
				List<String> dimRefFields = cube.getDimensionRefFieldList();

				List<String> fkColumns = new ArrayList<>();
				for (String refField : dimRefFields) {
					fkColumns.add(refField.substring(refField.indexOf('.') + 1));
				}

				int factTableSize = computeFactTableSize(factTable, cubeBase);
				int reservoirSize = (int) (sampleSize * factTableSize);

				if (factTableSize <= 0 || reservoirSize <= 0) continue;

				int[] keys = sampler.sample(factTableSize, reservoirSize, random);
				Arrays.sort(keys);

				writer.println("factTableSize = " + factTableSize);
				writer.println(String.join("|", fkColumns));

				writeSampledRows(writer, db, factTable, fkColumns, keys);
			}
		}
		return true;
	}

	private int computeFactTableSize(String factTable, CubeBase cubeBase) {
		String sql = "SELECT COUNT(*) FROM " + factTable;
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);
		String[][] resultArray = result.getResultArray();

		if(resultArray == null || resultArray.length < 3 || resultArray[2][0] == null) return -1;

		try {
			return Integer.parseInt(resultArray[2][0]);
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Fetches the sampled rows by surrogate key and writes them to the sample file, one row per line.
	 * The fact table is expected to carry a {@code SK_id} column, which is the column
	 * the keys returned by {@link IReservoirSampler} refer to.
	 * @param writer the sample file being written
	 * @param db the database that holds the fact table
	 * @param factTable the name of the fact table
	 * @param fkColumns the foreign key columns to retrieve for each sampled row
	 * @param keys the sampled surrogate keys
	 * @throws SQLException
	 */
	private void writeSampledRows(PrintWriter writer, Database db, String factTable,
								  List<String> fkColumns, int[] keys) throws SQLException {

		String columns = String.join(", ", fkColumns);
		String sql = "SELECT " + columns + " FROM " + factTable + " WHERE SK_id = ?";
		int columnCount = fkColumns.size();

		try (PreparedStatement statement = db.getConnection().prepareStatement(sql)) {
			for (int key : keys) {
				statement.setInt(1, key);

				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						String[] row = new String[columnCount];
						for (int col = 0; col < columnCount; col ++) {
							String value = resultSet.getString(col + 1);
							if (value == null) {
								row[col] = "";
							} else {
								row[col] = value;
							}
						}
						writer.println(String.join("|", row));
					}
				}
			}
		}
	}
}
