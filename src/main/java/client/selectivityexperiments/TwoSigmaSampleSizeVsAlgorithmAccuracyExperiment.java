package client.selectivityexperiments;

import cubemanager.queryoptimizer.selectivityestimation.SelectivityResult;
import mainengine.IMainEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import client.ClientRMITransferer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Experiment 8 : the effect of the sample size on the accuracy (effectiveness) of the reservoir sampling algorithms,
 * for queries with two sigmas. Sweeps sqrt(n), 0.1%, 1%, 2% and 5% on one dataset. The ground truth is the real number
 * of fact rows that satisfy both sigmas together and is obtained by asking the engine to answer each query. The RMSE
 * is the metric. Every sample size is redrawn SAMPLES times per algorithm, because the sample is random and each sample gets its own RMSE.
 * Takes the dataset as its only argument and writes OutputFiles/experiment8_{dataset}.txt.
 */
public class TwoSigmaSampleSizeVsAlgorithmAccuracyExperiment {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static final String CUBE = "store_sales";
	private static final int SAMPLES = 5;
	private static final int SIGMAS = 2;

	private static final String[] ALGORITHMS = { "R", "L" };

	private static final String[] QUERIES = {
			"CubeName:store_sales\nName:Q1\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.month='8-2002',item_dim.category='Jewelry'",
			"CubeName:store_sales\nName:Q2\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.month='8-2002',item_dim.category='Men'",
			"CubeName:store_sales\nName:Q3\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.month='8-2002',item_dim.category='Music'",
			"CubeName:store_sales\nName:Q4\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.month='12-1999',item_dim.category='Jewelry'",
			"CubeName:store_sales\nName:Q5\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.month='12-1999',item_dim.category='Men'",
			"CubeName:store_sales\nName:Q6\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.month='12-1999',item_dim.category='Music'",
			"CubeName:store_sales\nName:Q7\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.quarter='2001Q3',item_dim.category='Jewelry'",
			"CubeName:store_sales\nName:Q8\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.quarter='2001Q3',item_dim.category='Men'",
			"CubeName:store_sales\nName:Q9\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.quarter='2001Q3',item_dim.category='Music'",
			"CubeName:store_sales\nName:Q10\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.quarter='2000Q4',item_dim.category='Jewelry'",
			"CubeName:store_sales\nName:Q11\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.quarter='2000Q4',item_dim.category='Men'",
			"CubeName:store_sales\nName:Q12\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:date_dim.quarter='2000Q4',item_dim.category='Music'"
	};

	public static void main(String[] args) throws Exception{
		if (args.length < 1) {
			System.err.println("Usage : TwoSigmaSampleSizeVsAlgorithmAccuracyExperiment tpc_ds_10M");
			return;
		}

		String dataset = args[0];

		// --------------------------------------------- CONNECTION ---------------------------------------------
		Registry registry = LocateRegistry.getRegistry(HOST, PORT);
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());

		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", dataset);
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", CUBE);
		userInputList.put("inputFolder", dataset);

		service.initializeConnection(typeOfConnection, userInputList);
		// ----------------------------------------------------------------------------------------------------

		// ------------------------------------------- GROUND TRUTH -------------------------------------------
		// the full table scan is only asked for the size of the fact table
		List<SelectivityResult> foundSelectivity = service.estimateSelectivity(QUERIES[0], "FULL_TABLE_SCAN");
		if (foundSelectivity == null || foundSelectivity.size() != SIGMAS) {
			System.err.println("Q1 did not resolve to " + SIGMAS + " predicates.");
			return;
		}
		int factTableSize = foundSelectivity.get(0).getTotalRows();

		// the engine itself counts the real rows: answering the query with Count applies both sigmas together
		int[] realRows = new int[QUERIES.length];
		double[] truth = new double[QUERIES.length];
		for (int q = 0; q < QUERIES.length; q++) {
			realRows[q] = realCount(service, QUERIES[q], q, "Exp8");
			truth[q] = (double) realRows[q] / factTableSize;
		}
		// ----------------------------------------------------------------------------------------------------


		// ------------------------------------------- SAMPLE SIZES -------------------------------------------
		String[] labels = { "sqrt(n)", "0.1%", "1%", "2%", "5%" };
		double[] fractions = { 1.0 / Math.sqrt(factTableSize), 0.001, 0.01, 0.02, 0.05 };

		System.out.println(dataset + " : " + factTableSize + " rows\n");

		for (int i = 0; i < labels.length; i++) {
			System.out.printf("%-8s %d rows%n", labels[i], (int) (fractions[i] * factTableSize));
		}
		System.out.println();

		for (int q = 0; q < QUERIES.length; q++) {
			System.out.printf("Q%-3d real rows %8d   true selectivity %.8f%n", q + 1, realRows[q], truth[q]);
		}
		System.out.println();
		// ----------------------------------------------------------------------------------------------------

		// --------------------------------------------- EXPERIMENT ---------------------------------------------
		File results = new File("OutputFiles/experiment8_" + dataset + ".txt");
		String prefix = dataset + "\t" + factTableSize + "\t";

		try (PrintWriter writer = new PrintWriter(new FileWriter(results), true)) {

			writer.println("dataset\tfactTableSize\tsampleLabel\tsampleSize\talgorithm\tsample\tquery\ttrueSelectivity\testimatedSelectivity");

			for (int i = 0; i < fractions.length; i++) {

				double fraction = fractions[i];
				int sampleSize = (int) (fraction * factTableSize);
				String rows = labels[i] + "\t" + sampleSize + "\t";
				userInputList.put("sampleFraction", String.format("%.8f", fraction));

				// R and L write the same sample file, so R is finished and then L overwrites it
				for (String algorithm : ALGORITHMS) {
					for (int sample = 1; sample <= SAMPLES; sample++) {

						service.buildSamples(dataset, CUBE, fraction, true, algorithm);
						service.initializeConnection(typeOfConnection, userInputList);

						double squares = 0;
						for (int q = 0; q < QUERIES.length; q++) {
							double estimate = getSelectivity(service, QUERIES[q], "SAMPLING");
							squares += (estimate - truth[q]) * (estimate - truth[q]);
							write(writer, prefix + rows, algorithm, sample, "Q" + (q + 1), truth[q], estimate);
						}

						System.out.printf("%-8s %8d rows  %-2s  sample %d  RMSE %.8f%n",
								labels[i], sampleSize, algorithm, sample, Math.sqrt(squares / QUERIES.length));
					}
				}
			}
		}
		// ----------------------------------------------------------------------------------------------------
		System.out.println("Experiment ended. Results written to " + results.getPath() + " !!!!");
	}

	private static double getSelectivity(IMainEngine service, String query, String method) throws Exception {
		List<SelectivityResult> found = service.estimateSelectivity(query, method);
		if (found == null || found.size() != SIGMAS) {
			throw new IllegalStateException(method + " did not resolve " + SIGMAS + " predicates in\n" + query);
		}
		return SelectivityResult.conjunctiveCubeQuerySelectivity(found);
	}

	private static void write(PrintWriter writer, String prefix, String algorithm, int sample, String query, double truth, double estimate) {
		writer.println(prefix + algorithm + "\t" + sample + "\t" + query + "\t" + truth + "\t" + estimate);
	}

	// the engine answers a cube query by joining all its sigmas with AND, so asking it to Count the fact rows of the
	// query gives the real number of rows that satisfy both sigmas, without the independence assumption
	private static int realCount(IMainEngine service, String query, int q, String experiment) throws Exception {
		String name = experiment + "_Q" + (q + 1) + "_real";
		String countQuery = query.replace("Name:Q" + (q + 1) + "\n", "Name:" + name + "\n")
				.replace("AggrFunc:Sum", "AggrFunc:Count");

		String remoteFile = service.answerCubeQueryFromString(countQuery);
		File localFile = new File("ClientCache" + File.separator + name + ".tab");
		ClientRMITransferer.download(service, new File(remoteFile), localFile);

		// the first line holds the column labels, then one line per group: the grouper and then the measure
		int rows = 0;
		List<String> lines = Files.readAllLines(localFile.toPath(), StandardCharsets.UTF_8);
		for (int i = 1; i < lines.size(); i++) {
			if (lines.get(i).trim().isEmpty()) {
				continue;
			}
			String[] cells = lines.get(i).split("\t");
			rows += (int) Math.round(Double.parseDouble(cells[1]));
		}
		return rows;
	}
}
