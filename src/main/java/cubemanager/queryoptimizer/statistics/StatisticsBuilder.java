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
import java.util.ArrayList;
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
	 * Samples the fact table only, applying Algorithm R to build a reservoir of whole rows
	 * Dimension level values are resolved by joining the sample to the dimension tables at estimation time
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

				String select = "SELECT " + String.join(", ", fkColumns) + " FROM " + factTable;

				String[][] sample = sampler.sample(select, reservoirSize, db, random);

				writer.println("factTableSize = " + factTableSize);
				writer.println(String.join("|", fkColumns));

				for (String[] row : sample) {
					writer.println(String.join("|", row));
				}
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
}
