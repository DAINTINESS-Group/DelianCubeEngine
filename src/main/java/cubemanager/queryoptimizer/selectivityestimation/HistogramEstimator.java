package cubemanager.queryoptimizer.selectivityestimation;

import cubemanager.cubebase.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Histogram-based selectivity estimator
 * At construction, loads pre-built histograms from inputFolder.
 * At query time, no DB calls are made, selectivity is estimated by looking up the counts from the loaded map
 *
 */
public class HistogramEstimator implements  ISelectivityEstimator{

	private final Map<String, ColumnHistogram> histograms;
	private int storedFactTableSize = -1;

	/**
	 * Hold the frequency distribution of a single dimension level column
	 * frequencyMap maps each distinct value to its row count in the fact table
	 *
	 */
	private static class ColumnHistogram {
		private final Map<String, Integer> frequencyMap;

		ColumnHistogram(Map<String, Integer> frequencyMap) {
			this.frequencyMap = frequencyMap;
		}
	}

	public HistogramEstimator(String inputFolder, String cubeName) {
		this.histograms = new HashMap<>();
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_histograms.csv");
		loadFromFile(file);
	}

	@Override
	public int getFactTableSize() {
		return storedFactTableSize;
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

			ColumnHistogram histogram = histograms.get(parsed.filterCol);
			if (histogram == null || factTableSize < 0) continue;

			int matchingRows = countFromHistogram(histogram, sigma[1], sigma[2]);
			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, factTableSize,
					matchingRows));
		}

		return results;
	}

	/**
	 * Loads histogram data from a pre-built CSV file into the histograms map.
	 * Expected format :
	 * <pre>
	 * columnName|value|count
	 * factTableSize = N
	 * </pre>
	 * @param file
	 */
	private void loadFromFile(File file) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			reader.readLine();
			String line;

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\\|");

				if (line.startsWith("factTableSize = ")){
					try {
						storedFactTableSize = Integer.parseInt(line.substring("factTableSize = ".length()).trim());
					} catch (NumberFormatException ignore) {}
					continue;
				}

				if (parts.length != 3) continue;

				String columnName = parts[0];
				String value = parts[1];
				int count;

				try {
					count = Integer.parseInt(parts[2].trim());
					if (!histograms.containsKey(columnName)) {
						histograms.put(columnName, new ColumnHistogram(new HashMap<>()));
					}
					histograms.get(columnName).frequencyMap.put(value, count);
				} catch (NumberFormatException e) {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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
