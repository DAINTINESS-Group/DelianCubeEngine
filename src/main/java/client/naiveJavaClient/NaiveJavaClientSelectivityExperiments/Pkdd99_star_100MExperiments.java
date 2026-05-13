package client.naiveJavaClient.NaiveJavaClientSelectivityExperiments;

import cubemanager.queryoptimizer.selectivityestimation.SelectivityResult;
import mainengine.SessionQueryProcessorEngine;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

public class Pkdd99_star_100MExperiments {

	private static final String[] QUERIES = {
		"CubeName:loan\nName:Q1\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:account_dim.region='north Moravia'",
		"CubeName:loan\nName:Q2\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:account_dim.region='Prague'",
		"CubeName:loan\nName:Q3\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:account_dim.region='south Moravia'",
		"CubeName:loan\nName:Q4\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:date_dim.month='1998-01'",
		"CubeName:loan\nName:Q5\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:date_dim.month='1997-01'",
		"CubeName:loan\nName:Q6\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:date_dim.year='1998'",
		"CubeName:loan\nName:Q7\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:date_dim.year='1997'",
		"CubeName:loan\nName:Q8\nAggrFunc:Sum\nMeasure:amount\n"
			+ "Gamma:account_dim.district_name\nSigma:account_dim.region='Prague',date_dim.year='1998'"
	};

	public static void main(String[] args) throws Exception {
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star_100m");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star_100M");

		SessionQueryProcessorEngine engine = new SessionQueryProcessorEngine();
		engine.initializeConnection("RDBMS", userInputList);

		try (PrintWriter writer = new PrintWriter(new FileWriter("selectivity_results_100M.txt"))) {
			runEstimator(engine, "Full Table Scan", "FULL_TABLE_SCAN", 0.0, writer);
			runEstimator(engine, "Histogram", "HISTOGRAM", 0.0, writer);
			runEstimator(engine, "Sampling 10%", "SAMPLING", 0.1, writer);
			runEstimator(engine, "Sampling 30%", "SAMPLING", 0.3, writer);
			runEstimator(engine, "Sampling 50%", "SAMPLING", 0.5, writer);
		}
	}

	private static void runEstimator(SessionQueryProcessorEngine engine, String label,
			String method, double sampleSize, PrintWriter writer) throws Exception {

		writer.println("=== " + label + " ===");

		for (int i = 0; i < QUERIES.length; i++) {
			long start = System.nanoTime();
			List<SelectivityResult> results = engine.estimateSelectivity(QUERIES[i], method, sampleSize);
			long ms = (System.nanoTime() - start) / 1_000_000;

			writer.println("Q" + (i + 1) + " [" + ms + " ms]");
			if (results != null)
				for (SelectivityResult r : results)
					writer.println(r);
		}

		writer.println();
	}
}
