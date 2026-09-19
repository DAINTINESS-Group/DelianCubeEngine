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
 * Experiment 6 : the accuracy (effectiveness) of every estimation algorithm, on every dataset.
 * The Full Table Scan is the ground truth. For every query the estimated selectivity is compared against it,
 * and the RMSE over the ten queries is the metric, at a sample size of sqrt(n).
 * Full Table Scan and the Histogram are measured once since they are deterministic.
 * The sample is random so it is redrawn SAMPLES times and each draw gets its own RMSE.
 * Takes the datasets as its arguments, or runs all five if given none, and writes one
 * OutputFiles/experiment6_{dataset}.txt per dataset.
 */
public class DatasetSizeVsALLAlgorithmsAccuracyExperiment {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static final int SAMPLES = 5;

	private static final String[] ALL_DATASETS = { "tpc_ds_2M", "tpc_ds_10M", "tpc_ds_100M", "foodmart_reduced", "pkdd99_star_100M" };

	private static final String[] ALGORITHMS = { "R", "L" };

	private static final String[] QUERIES_TPC_DS = {
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

	private static final String[] QUERIES_FOODMART = {
			"CubeName:sales\nName:Q1\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:customer_dim.customer\nSigma:customer_dim.customer='27'",
			"CubeName:sales\nName:Q2\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:customer_dim.customer\nSigma:customer_dim.customer='98'",
			"CubeName:sales\nName:Q3\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:customer_dim.customer\nSigma:customer_dim.customer='74'",
			"CubeName:sales\nName:Q4\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:customer_dim.customer\nSigma:customer_dim.customer='2905'",
			"CubeName:sales\nName:Q5\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:customer_dim.customer\nSigma:customer_dim.customer='4391'",
			"CubeName:sales\nName:Q6\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:date_dim.date\nSigma:date_dim.date='956'",
			"CubeName:sales\nName:Q7\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:date_dim.date\nSigma:date_dim.date='1072'",
			"CubeName:sales\nName:Q8\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:product_dim.product_subcategory\nSigma:product_dim.product_subcategory='Wine'",
			"CubeName:sales\nName:Q9\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:product_dim.product_subcategory\nSigma:product_dim.product_subcategory='Fresh Fruit'",
			"CubeName:sales\nName:Q10\nAggrFunc:Sum\nMeasure:unit_sales\nGamma:product_dim.product_category\nSigma:product_dim.product_category='Snack Foods'"
	};

	private static final String[] QUERIES_PKDD99 = {
			"CubeName:loan\nName:Q1\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.account\nSigma:account_dim.account='1'",
			"CubeName:loan\nName:Q2\nAggrFunc:Sum\nMeasure:amount\nGamma:date_dim.day\nSigma:date_dim.day='1993-01-01'",
			"CubeName:loan\nName:Q3\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.district_name\nSigma:account_dim.district_name='Jihlava'",
			"CubeName:loan\nName:Q4\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.district_name\nSigma:account_dim.district_name='Trebic'",
			"CubeName:loan\nName:Q5\nAggrFunc:Sum\nMeasure:amount\nGamma:date_dim.month\nSigma:date_dim.month='1993-01'",
			"CubeName:loan\nName:Q6\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.district_name\nSigma:account_dim.district_name='Zlin'",
			"CubeName:loan\nName:Q7\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.district_name\nSigma:account_dim.district_name='Karvina'",
			"CubeName:loan\nName:Q8\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.region\nSigma:account_dim.region='\"south Bohemia\"'",
			"CubeName:loan\nName:Q9\nAggrFunc:Sum\nMeasure:amount\nGamma:account_dim.region\nSigma:account_dim.region='Prague'",
			"CubeName:loan\nName:Q10\nAggrFunc:Sum\nMeasure:amount\nGamma:status_dim.status\nSigma:status_dim.status='\"Running Contract/OK\"'"
	};

	public static void main(String[] args) throws Exception {
		String[] datasets = ALL_DATASETS;
		if (args.length > 0) {
			datasets = args;
		}

		Registry registry = LocateRegistry.getRegistry(HOST, PORT);
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());

		for (String dataset : datasets) {
			runDataset(service, dataset);
		}
	}

	private static void runDataset(IMainEngine service, String dataset) throws Exception {

		// -------------------------------------------- CUBE CONFIG ---------------------------------------------
		String cube;
		String[] queries;

		if (dataset.startsWith("tpc_ds")) {
			cube = "store_sales";
			queries = QUERIES_TPC_DS;
		} else if (dataset.equals("foodmart_reduced")) {
			cube = "sales";
			queries = QUERIES_FOODMART;
		} else if (dataset.equals("pkdd99_star_100M")) {
			cube = "loan";
			queries = QUERIES_PKDD99;
		} else {
			System.err.println("Unknown dataset : " + dataset);
			return;
		}
		// ----------------------------------------------------------------------------------------------------


		// --------------------------------------------- CONNECTION ---------------------------------------------
		// connection to datasets
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", dataset);
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", cube);
		userInputList.put("inputFolder", dataset);

		service.initializeConnection(typeOfConnection, userInputList);
		// ----------------------------------------------------------------------------------------------------


		// ------------------------------------------- GROUND TRUTH -------------------------------------------
		double[] truth = new double[queries.length];
		int factTableSize = 0;

		for (int q = 0; q < queries.length; q++) {
			List<SelectivityResult> foundSelectivity = service.estimateSelectivity(queries[q], "FULL_TABLE_SCAN");
			if (foundSelectivity == null || foundSelectivity.isEmpty()) {
				System.err.println(dataset + " Q" + (q + 1) + " did not resolve to any predicate.");
				return;
			}
			factTableSize = foundSelectivity.get(0).getTotalRows();
			truth[q] = SelectivityResult.conjunctiveCubeQuerySelectivity(foundSelectivity);
		}

		double sampleFraction = 1.0 / Math.sqrt(factTableSize);
		int reservoirSize = (int) (sampleFraction * factTableSize);
		userInputList.put("sampleFraction", String.format("%.8f", sampleFraction));

		System.out.println(dataset + " : " + factTableSize + " rows, reservoir of " + reservoirSize + "\n");

		for (int q = 0; q < queries.length; q++) {
			System.out.printf("Q%-3d true selectivity %.8f%n", q + 1, truth[q]);
		}
		System.out.println();
		// ----------------------------------------------------------------------------------------------------


		// --------------------------------------------- EXPERIMENT ---------------------------------------------
		File results = new File("OutputFiles/experiment6_" + dataset + ".txt");
		String prefix = dataset + "\t" + factTableSize + "\t" + reservoirSize + "\t";

		try (PrintWriter writer = new PrintWriter(new FileWriter(results), true)) {

			writer.println("dataset\tfactTableSize\treservoirSize\tmethod\talgorithm\tsample\tquery\ttrueSelectivity\testimatedSelectivity");

			// the full table scan is the ground truth, so it is exact
			for (int q = 0; q < queries.length; q++) {
				write(writer, prefix, "FULL_TABLE_SCAN", "-", 1, "Q" + (q + 1), truth[q], truth[q]);
			}
			System.out.printf("%-16s %-2s     RMSE %.8f%n", "FULL_TABLE_SCAN", "-", 0.0);

			// the histogram is also deterministic, so only one pass. false reuses the one experiment 5 built
			service.buildHistograms(dataset, cube, false);
			service.initializeConnection(typeOfConnection, userInputList);

			double squares = 0;
			for (int q = 0; q < queries.length; q++) {
				double estimate = getSelectivity(service, queries[q], "HISTOGRAM");
				squares += (estimate - truth[q]) * (estimate - truth[q]);
				write(writer, prefix, "HISTOGRAM", "-", 1, "Q" + (q + 1), truth[q], estimate);
			}
			System.out.printf("%-16s %-2s     RMSE %.8f%n", "HISTOGRAM", "-", Math.sqrt(squares / queries.length));

			// the sample is random, so it is redrawn and measured again
			for (String algorithm : ALGORITHMS) {
				for (int sample = 1; sample <= SAMPLES; sample++) {
					service.buildSamples(dataset, cube, sampleFraction, true, algorithm);
					service.initializeConnection(typeOfConnection, userInputList);

					squares = 0;
					for (int q = 0; q < queries.length; q++) {
						double estimate = getSelectivity(service, queries[q], "SAMPLING");
						squares += (estimate - truth[q]) * (estimate - truth[q]);
						write(writer, prefix, "SAMPLING", algorithm, sample, "Q" + (q + 1), truth[q], estimate);
					}
					System.out.printf("%-16s %-2s sample %d  RMSE %.8f%n", "SAMPLING", algorithm, sample, Math.sqrt(squares / queries.length));
				}
			}
		}
		// ----------------------------------------------------------------------------------------------------

		System.out.println("Experiment ended. Results written to " + results.getPath() + " !!!!\n");
	}

	private static double getSelectivity(IMainEngine service, String query, String method) throws Exception {
		return SelectivityResult.conjunctiveCubeQuerySelectivity(service.estimateSelectivity(query, method));
	}

	private static void write(PrintWriter writer, String prefix, String method, String algorithm, int sample, String query, double truth, double estimate) {
		writer.println(prefix + method + "\t" + algorithm + "\t" + sample + "\t" + query + "\t" + truth + "\t" + estimate);
	}
}