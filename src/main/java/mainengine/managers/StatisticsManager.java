package mainengine.managers;

import cubemanager.cubebase.*;
import cubemanager.relationalstarschema.Database;
import result.Result;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Builds statistics (histograms and reservoir samples) and stores them in a file.
 * If the file already exists, the build is skipped.
 */
public class StatisticsManager implements IBuilder{

	@Override
	public ResponseDTO execute(RequestCTO cto) throws Exception {
		Map<String, Object> params = (Map<String, Object>) cto.getInput();
		String inputFolder = (String) params.get("inputFolder");
		String cubeName = (String) params.get("cubeName");
		double sampleSize = (double) params.get("sampleSize");
		boolean forceRebuild = (boolean) params.get("forceRebuild");

		CubeBase cubeBase = cto.getCubeManager().getCubeBase();

		switch (cto.getCommandAlias()) {
			case "build_histograms" :
				buildHistograms(cubeBase, inputFolder, cubeName, forceRebuild);
				break;
			case "build_samples" :
				buildSamples(cubeBase, inputFolder, cubeName, sampleSize, forceRebuild);
				break;
			default:
				return ResponseDTO.error("Invalid statistics operation: " + cto.getCommandAlias());
		}
		return new ResponseDTO(true);
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
	private void buildHistograms(CubeBase cubeBase, String inputFolder, String cubeName, boolean forceRebuild) throws Exception {
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_histograms.csv");
		if (file.exists() && !forceRebuild) return; // Skip building if file already exists

		try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
			writer.println("columnName|value|count");

			for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
				String factTable = cube.getFactTable().getTableName();
				List<Dimension> dimensions = cube.getDimensionsList();
				List<String> dimRefFields = cube.getDimensionRefFieldList();

				int factTableSize = computeFactTableSize(factTable, cubeBase);
				writer.println("factTableSize = " + factTableSize);

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
	}

	/**
	 * Applies R algorithm on the JOIN result of the fact table and each dimension table,
	 * sampling the dimension level column values as they appear in the fact table rows.
	 * Writes one sample per dimension level column to {cubeName}_samples.csv is the inputFolder directory.
	 * Format :
	 * <pre>
	 * columnName|value1,value2,value3,...
	 * factTableSize = N
	 * ...
	 * </pre>
	 */
	private void buildSamples(CubeBase cubeBase, String inputFolder, String cubeName, double sampleSize, boolean forceRebuild) throws Exception{
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_samples.csv");
		if (file.exists() && !forceRebuild) return; // Skip building if file already exists

		Database db = (Database) cubeBase.getDataSourceDescription();

		try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
			writer.println("columnName|values");

			Random random = new Random();

			for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
				String factTable = cube.getFactTable().getTableName();
				List<Dimension> dimensions = cube.getDimensionsList();
				List<String> dimRefFields = cube.getDimensionRefFieldList();

				int factTableSize = computeFactTableSize(factTable, cubeBase);
				writer.println("factTableSize = " + factTableSize);
				int reservoirSize = (int) (sampleSize * factTableSize);

				for (int i = 0; i < dimensions.size(); i++) {
					Dimension dimension = dimensions.get(i);
					String dimTable = dimension.getTableName();
					String factFK = dimRefFields.get(i);
					String dimPK = dimTable + "." +
							dimension.getHierarchy().get(0).getLevels().get(0).getAttributeName(0);

					for (Level level : dimension.getHierarchy().get(0).getLevels()) {
						String filterCol = dimTable + "." + level.getAttributeName(0);

						String sql = "SELECT " + filterCol + " FROM " + factTable
								+ " JOIN " + dimTable + " ON " + factFK + " = " + dimPK;

						String[] sample = buildReservoir(sql, reservoirSize, db, random);
						writer.println(filterCol + "|" + String.join(",", sample));
					}
				}
			}
		}
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

	private String[] buildReservoir(String sql, int reservoirSize, Database db, Random random) {
		String[] reservoir = new String[reservoirSize];
		int count = 0;

		try (Statement stmt = db.getConnection().createStatement(
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

			stmt.setFetchSize(Integer.MIN_VALUE);
			try (ResultSet rs = stmt.executeQuery(sql)) {
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
			return new String[0];
		}

		return count < reservoirSize ? Arrays.copyOf(reservoir, count) : reservoir;
	}
}
