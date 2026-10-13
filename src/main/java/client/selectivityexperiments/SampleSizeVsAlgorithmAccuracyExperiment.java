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

/**
 * Experiment 4 : the effect of the sample size on the accuracy (effectiveness) of the reservoir
 * sampling algorithms. Sweeps sqrt(n), 0.1%, 1%, 2% and 5% on one dataset. The Full Table Scan is the ground truth
 * and the RMSE over the ten queries is the metric. Every sample size is redrawn SAMPLES times per algorithm, because
 * the sample is random and each draw gets its own RMSE.
 * Takes the dataset as its only argument and writes OutputFiles/experiment4_{dataset}.txt.
 */
public class SampleSizeVsAlgorithmAccuracyExperiment {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static final String CUBE = "store_sales";
	private static final int SAMPLES = 5;

	private static final String[] ALGORITHMS = { "R", "L" };

	private static final String[] QUERIES = {
			"CubeName:store_sales\nName:Q1\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.product_name\nSigma:item_dim.product_name='eingeingn stcally'",
			"CubeName:store_sales\nName:Q2\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.product_name\nSigma:item_dim.product_name='callybarcallyought'",
			"CubeName:store_sales\nName:Q3\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.product_name\nSigma:item_dim.product_name='eseoughtablecallyought'",
			"CubeName:store_sales\nName:Q4\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.month\nSigma:date_dim.month='8-2002'",
			"CubeName:store_sales\nName:Q5\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.month\nSigma:date_dim.month='12-1999'",
			"CubeName:store_sales\nName:Q6\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.quarter\nSigma:date_dim.quarter='2001Q3'",
			"CubeName:store_sales\nName:Q7\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.quarter\nSigma:date_dim.quarter='2000Q4'",
			"CubeName:store_sales\nName:Q8\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:item_dim.category='Jewelry'",
			"CubeName:store_sales\nName:Q9\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:item_dim.category='Men'",
			"CubeName:store_sales\nName:Q10\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:item_dim.category='Music'"
	};

	public static void main(String[] args) throws Exception{
		if (args.length < 1) {
			System.err.println("Usage : SampleSizeVsAlgorithmAccuracyExperiment tpc_ds_10M");
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
		double[] truth = new double[QUERIES.length];
		int factTableSize = 0;

		for (int q = 0; q < QUERIES.length; q++) {
			List<SelectivityResult> foundSelectivity = service.estimateSelectivity(QUERIES[q], "FULL_TABLE_SCAN");
			if (foundSelectivity == null || foundSelectivity.isEmpty()) {
				System.err.println("Q" + (q + 1) + " did not resolve to any predicate.");
				return;
			}
			factTableSize = foundSelectivity.get(0).getTotalRows();
			truth[q] = SelectivityResult.conjunctiveCubeQuerySelectivity(foundSelectivity);
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
			System.out.printf("Q%-3d true selectivity %.8f%n", q + 1, truth[q]);
		}
		System.out.println();
		// ----------------------------------------------------------------------------------------------------

		// --------------------------------------------- EXPERIMENT ---------------------------------------------
		File results = new File("OutputFiles/experiment4_" + dataset + ".txt");
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
		return SelectivityResult.conjunctiveCubeQuerySelectivity(service.estimateSelectivity(query, method));
	}

	private static void write(PrintWriter writer, String prefix, String algorithm, int sample, String query, double truth, double estimate) {
		writer.println(prefix + algorithm + "\t" + sample + "\t" + query + "\t" + truth + "\t" + estimate);
	}
}
