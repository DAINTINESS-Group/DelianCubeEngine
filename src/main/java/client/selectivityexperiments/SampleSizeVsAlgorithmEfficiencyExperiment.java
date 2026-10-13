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
 * Experiment 3 : the effect of the sample size on the execution time (efficiency) of the reservoir sampling algorithms.
 * Sweeps sqrt(n), 0.1%, 1%, 2% and 5% on one dataset and measures build from scratch and load from file in ms
 * for both Sampling Algorithms, over ten queries.
 * Takes the dataset as its only argument and writes OutputFiles/experiment3_{dataset}.txt.
 */
public class SampleSizeVsAlgorithmEfficiencyExperiment {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static final int RUNS = 5;
	private static final String CUBE = "store_sales";

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
			System.err.println("Usage : SampleSizeVsAlgorithmEfficiencyExperiment tpc_ds_10M");
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

		// ----------------------------------------- FACT TABLE SIZE ------------------------------------------
		// NOT A MEASUREMENT, JUST TO GET THE FACT TABLE SIZE
		List<SelectivityResult> sizing = service.estimateSelectivity(QUERIES[0], "FULL_TABLE_SCAN");
		if (sizing == null || sizing.isEmpty() || sizing.get(0).getTotalRows() <= 0) {
			System.err.println("Could not size the fact table");
			return;
		}

		int factTableSize = sizing.get(0).getTotalRows();
		// ----------------------------------------------------------------------------------------------------

		// ------------------------------------------- SAMPLE SIZES -------------------------------------------
		String[] labels = { "sqrt(n)", "0.1%", "1%", "2%", "5%" };
		double[] fractions = { 1.0 / Math.sqrt(factTableSize), 0.001, 0.01, 0.02, 0.05 };

		System.out.println(dataset + " : " + factTableSize + " rows");

		for (int i = 0; i < labels.length; i++) {
			System.out.printf("%-8s %d rows%n", labels[i], (int) (fractions[i] * factTableSize));
		}
		System.out.println();
		// ----------------------------------------------------------------------------------------------------


		// --------------------------------------------- EXPERIMENT ---------------------------------------------
		File results = new File("OutputFiles/experiment3_" + dataset + ".txt");
		String prefix = dataset + "\t" + factTableSize + "\t";

		try (PrintWriter writer = new PrintWriter(new FileWriter(results), true)) {
			writer.println("dataset\tfactTableSize\tsampleLabel\tsampleSize\talgorithm\tphase\tquery\trun\tms");

			for (int run = 1; run <= RUNS; run++) {
				for (int i = 0; i < fractions.length; i++) {

					double fraction = fractions[i];
					int sampleSize = (int) (fraction * factTableSize);
					String rows = labels[i] + "\t" + sampleSize + "\t";
					userInputList.put("sampleFraction", String.format("%.8f", fraction));

					// R and L write the same sample file, so R is finished and then L overwrites it
					for (String algorithm : ALGORITHMS) {
						long start = System.nanoTime();
						service.buildSamples(dataset, CUBE, fraction, true, algorithm);
						double build = ms(start);

						service.initializeConnection(typeOfConnection, userInputList);

						start = System.nanoTime();
						service.estimateSelectivity(QUERIES[0], "SAMPLING");
						double cold = ms(start);

						start = System.nanoTime();
						service.estimateSelectivity(QUERIES[0], "SAMPLING");
						double warm = ms(start);

						double load = cold - warm;

						write(writer, prefix + rows, algorithm, "BUILD", "-", run, build);
						write(writer, prefix + rows, algorithm, "LOAD", "-", run, load);

						System.out.printf("run %d  %-8s %8d rows  %-2s  build %11.1f  load %10.1f%n",
								run, labels[i], sampleSize, algorithm, build, load);

						for (int q = 0; q < QUERIES.length; q++) {
							start = System.nanoTime();
							service.estimateSelectivity(QUERIES[q], "SAMPLING");
							write(writer, prefix + rows, algorithm, "ESTIMATE", "Q" + (q + 1), run, ms(start));
						}
					}
				}
			}
		}
		// ----------------------------------------------------------------------------------------------------
		System.out.println("Experiment ended. Results written to " + results.getPath() + " !!!!");
	}

	private static double ms(long start) {
		return (System.nanoTime() - start) / 1000000.0;
	}

	private static void write(PrintWriter writer, String prefix, String algorithm, String phase, String query, int run, double ms) {
		writer.println(prefix + algorithm + "\t" + phase + "\t" + query + "\t" + run + "\t" + ms);
	}
}
