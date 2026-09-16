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
 * Experiment 1 : the effect of the dataset size on the execution time (efficiency) of the algorithms.
 * Measures build from scratch and load from file in ms, for both Histograms and Sampling algorithms,
 * at a sample size of sqrt(n), over ten queries.
 * Takes the dataset as its only argument and writes OutputFiles/experiment1_{dataset}.csv.
 */
public class DatasetSizeVsAlgorithmEfficiencyExperiment {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static final int RUNS = 6;
	private static final String CUBE = "store_sales";

	private static final String[][] ANTAGONISTS = { { "HISTOGRAM", "-" }, { "SAMPLING", "R" }, { "SAMPLING", "L" } };

	private static final String[] QUERIES = {
			"CubeName:store_sales\nName:Q1\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.product_name\nSigma:item_dim.product_name='eingeingn stcally'",
			"CubeName:store_sales\nName:Q2\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.product_name\nSigma:item_dim.product_name='eseoughtablecallyought'",
			"CubeName:store_sales\nName:Q3\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.product_name\nSigma:item_dim.product_name='callybarcallyought'",
			"CubeName:store_sales\nName:Q4\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.month\nSigma:date_dim.month='12-1999'",
			"CubeName:store_sales\nName:Q5\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.month\nSigma:date_dim.month='8-2002'",
			"CubeName:store_sales\nName:Q6\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.quarter\nSigma:date_dim.quarter='2001Q3'",
			"CubeName:store_sales\nName:Q7\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:date_dim.quarter\nSigma:date_dim.quarter='2000Q4'",
			"CubeName:store_sales\nName:Q8\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:item_dim.category='Jewelry'",
			"CubeName:store_sales\nName:Q9\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:item_dim.category='Music'",
			"CubeName:store_sales\nName:Q10\nAggrFunc:Sum\nMeasure:ss_quantity\nGamma:item_dim.category\nSigma:item_dim.category='Men'"
	};

	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("Usage : DatasetSizeVsAlgorithmEfficiencyExperiment tpc_ds_2M | tpc_ds_10M | tpc_ds_100M");
			return;
		}
		String dataset = args[0];

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


		// NOT A MEASUREMENT, JUST TO GET THE FACT TABLE SIZE
		List<SelectivityResult> sizing = service.estimateSelectivity(QUERIES[0], "FULL_TABLE_SCAN");
		if (sizing == null || sizing.isEmpty() || sizing.get(0).getTotalRows() <= 0) {
			System.err.println("Could not size the fact table");
			return;
		}

		int factTableSize = sizing.get(0).getTotalRows();
		double sampleFraction = 1.0 / Math.sqrt(factTableSize);
		int reservoirSize = (int) (sampleFraction * factTableSize);
		userInputList.put("sampleFraction", String.format("%.8f", sampleFraction));

		System.out.println(dataset + " : " + factTableSize + " rows, reservoir of " + reservoirSize + "\n");


		File csv = new File("OutputFiles/experiment1_" + dataset + ".csv");
		String prefix = dataset + "," + factTableSize + "," + reservoirSize + ",";

		try (PrintWriter writer = new PrintWriter(new FileWriter(csv), true)) {

			writer.println("dataset,factTableSize,reservoirSize,method,algorithm,phase,query,run,ms");

			for (int run = 0; run < RUNS; run++) {
				for (String[] antagonist : ANTAGONISTS) {
					String method = antagonist[0];
					String algorithm = antagonist[1];

					long start = System.nanoTime();
					if (method.equals("HISTOGRAM")) {
						service.buildHistograms(dataset, CUBE, true);
					} else {
						service.buildSamples(dataset, CUBE, sampleFraction, true, algorithm);
					}
					double build = ms(start);

					// a fresh context has no cached estimator, so cold pays the load and warm does not
					service.initializeConnection(typeOfConnection, userInputList);

					start = System.nanoTime();
					service.estimateSelectivity(QUERIES[0], method);
					double cold = ms(start);

					start = System.nanoTime();
					service.estimateSelectivity(QUERIES[0], method);
					double warm = ms(start);

					double load = cold - warm;

					write(writer, prefix, method, algorithm, "BUILD", "-", run, build);
					write(writer, prefix, method, algorithm, "LOAD", "-", run, load);

					System.out.printf("run %d  %-10s %-2s  build %11.1f  load %10.1f%n",
							run, method, algorithm, build, load);

					for (int q = 0; q < QUERIES.length; q++) {
						start = System.nanoTime();
						service.estimateSelectivity(QUERIES[q], method);
						write(writer, prefix, method, algorithm, "ESTIMATE", "Q" + (q + 1), run, ms(start));
					}
				}
			}
		}
	}

	private static double ms(long start) {
		return (System.nanoTime() - start) / 1000000.0;
	}

	private static void write(PrintWriter writer, String prefix, String method, String algorithm, String phase, String query, int run, double ms) {
		writer.println(prefix + method + "," + algorithm + "," + phase + "," + query + "," + run + "," + ms);
	}
}