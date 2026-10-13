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
 * Experiment 7 : the effect of the dataset size on the accuracy (effectiveness) of the algorithms,
 * for queries with two sigmas. Every algorithm estimates each sigma on its own and the selectivity of the query
 * is the product of the two (attribute value independence assumption), so the Full Table Scan is no longer exact.
 * The ground truth is obtained by asking the engine to answer each query with a COUNT and the RMSE is the metric.
 * Takes the dataset as its only argument and writes OutputFiles/experiment7_{dataset}.txt.
 * Full Table Scan and the Histogram are measured once since they are deterministic.
 * The sample is random so it is redrawn SAMPLES times and each draw gets its own RMSE.
 */
public class TwoSigmaDatasetSizeVsAlgorithmAccuracyExperiment {
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

	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("Usage : TwoSigmaDatasetSizeVsAlgorithmAccuracyExperiment tpc_ds_2M | tpc_ds_10M | tpc_ds_100M");
			return;
		}

		String dataset = args[0];

		// --------------------------------------------- CONNECTION ---------------------------------------------
		Registry registry = LocateRegistry.getRegistry(HOST, PORT);
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());

		// connection to datasets
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
		// the full table scan gives the size of the fact table, and its estimate is the product of the two sigmas
		double[] fullTableScan = new double[QUERIES.length];
		int factTableSize = 0;

		for (int q = 0; q < QUERIES.length; q++) {
			List<SelectivityResult> foundSelectivity = service.estimateSelectivity(QUERIES[q], "FULL_TABLE_SCAN");
			if (foundSelectivity == null || foundSelectivity.size() != SIGMAS) {
				System.err.println("Q" + (q + 1) + " did not resolve to " + SIGMAS + " predicates.");
				return;
			}
			factTableSize = foundSelectivity.get(0).getTotalRows();
			fullTableScan[q] = SelectivityResult.conjunctiveCubeQuerySelectivity(foundSelectivity);
		}

		// the engine itself counts the real rows: answering the query with Count applies both sigmas together
		int[] realRows = new int[QUERIES.length];
		double[] truth = new double[QUERIES.length];
		for (int q = 0; q < QUERIES.length; q++) {
			realRows[q] = realCount(service, QUERIES[q], q, "Exp7");
			truth[q] = (double) realRows[q] / factTableSize;
		}

		double sampleFraction = 1.0 / Math.sqrt(factTableSize);
		int reservoirSize = (int) (sampleFraction * factTableSize);
		userInputList.put("sampleFraction", String.format("%.8f", sampleFraction));

		System.out.println(dataset + " : " + factTableSize + " rows, reservoir of " + reservoirSize + "\n");

		for (int q = 0; q < QUERIES.length; q++) {
			System.out.printf("Q%-3d real rows %8d   true selectivity %.8f   product %.8f%n", q + 1, realRows[q], truth[q], fullTableScan[q]);
		}
		System.out.println();
		// ----------------------------------------------------------------------------------------------------


		// --------------------------------------------- EXPERIMENT ---------------------------------------------
		File results = new File("OutputFiles/experiment7_" + dataset + ".txt");
		String prefix = dataset + "\t" + factTableSize + "\t" + reservoirSize + "\t";

		try (PrintWriter writer = new PrintWriter(new FileWriter(results), true)) {

			writer.println("dataset\tfactTableSize\treservoirSize\tmethod\talgorithm\tsample\tquery\ttrueSelectivity\testimatedSelectivity");

			// the full table scan multiplies the two sigmas, so it is measured against the truth like the rest
			double squares = 0;
			for (int q = 0; q < QUERIES.length; q++) {
				squares += (fullTableScan[q] - truth[q]) * (fullTableScan[q] - truth[q]);
				write(writer, prefix, "FULL_TABLE_SCAN", "-", 1, "Q" + (q + 1), truth[q], fullTableScan[q]);
			}
			System.out.printf("%-16s %-2s     RMSE %.8f%n", "FULL_TABLE_SCAN", "-", Math.sqrt(squares / QUERIES.length));

			// the histogram is also deterministic, so only one pass
			service.buildHistograms(dataset, CUBE, false);
			service.initializeConnection(typeOfConnection, userInputList);

			squares = 0;
			for (int q = 0; q < QUERIES.length; q++) {
				double estimate = getSelectivity(service, QUERIES[q], "HISTOGRAM");
				squares += (estimate - truth[q]) * (estimate - truth[q]);
				write(writer, prefix, "HISTOGRAM", "-", 1, "Q" + (q + 1), truth[q], estimate);
			}
			System.out.printf("%-16s %-2s     RMSE %.8f%n", "HISTOGRAM", "-", Math.sqrt(squares / QUERIES.length));

			// the sample is random, so it is redrawn and measured again
			for (String algorithm : ALGORITHMS) {
				for (int sample = 1; sample <= SAMPLES; sample++) {
					service.buildSamples(dataset, CUBE, sampleFraction, true, algorithm);
					service.initializeConnection(typeOfConnection, userInputList);

					squares = 0;
					for (int q = 0; q < QUERIES.length; q++) {
						double estimate = getSelectivity(service, QUERIES[q], "SAMPLING");
						squares += (estimate - truth[q]) * (estimate - truth[q]);
						write(writer, prefix, "SAMPLING", algorithm, sample, "Q" + (q + 1), truth[q], estimate);
					}
					System.out.printf("%-16s %-2s sample %d  RMSE %.8f%n", "SAMPLING", algorithm, sample, Math.sqrt(squares / QUERIES.length));
				}
			}
		}
		// ----------------------------------------------------------------------------------------------------

		System.out.println("Experiment ended. Results written to " + results.getPath() + " !!!!");
	}

	// the engine skips a sigma it cannot resolve, so a product of fewer terms would otherwise go unnoticed
	private static double getSelectivity(IMainEngine service, String query, String method) throws Exception {
		List<SelectivityResult> found = service.estimateSelectivity(query, method);
		if (found == null || found.size() != SIGMAS) {
			throw new IllegalStateException(method + " did not resolve " + SIGMAS + " predicates in\n" + query);
		}
		return SelectivityResult.conjunctiveCubeQuerySelectivity(found);
	}

	private static void write(PrintWriter writer, String prefix, String method, String algorithm, int sample, String query, double truth, double estimate) {
		writer.println(prefix + method + "\t" + algorithm + "\t" + sample + "\t" + query + "\t" + truth + "\t" + estimate);
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
