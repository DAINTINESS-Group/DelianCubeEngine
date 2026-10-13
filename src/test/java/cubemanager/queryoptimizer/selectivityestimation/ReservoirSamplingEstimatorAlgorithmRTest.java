package cubemanager.queryoptimizer.selectivityestimation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import mainengine.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import mainengine.SessionQueryProcessorEngine;

/**
 * A test class for the reservoir sampling estimator with samples built with the R algorithm
 */
public class ReservoirSamplingEstimatorAlgorithmRTest {

	private static CubeManager testCubeManager;
	private static ReservoirSamplingEstimator testEstimator;

	private static final int TOTAL_ROWS = 682;
	private static final double SAMPLE_SIZE = 0.5;

	private static final String Q_ATLANTIS =
			"CubeName:loan\nName:Q_Atlantis\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.district_name\nSigma:account_dim.region='Atlantis'";

	private static final String Q_NORTH_MORAVIA =
			"CubeName:loan\nName:Q_NorthMoravia\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.district_name\nSigma:account_dim.region='north Moravia'";

	private static final String Q_PRAGUE_AND_1998 =
			"CubeName:loan\nName:Q_PragueAnd1998\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.district_name,date_dim.year\n"
					+ "Sigma:account_dim.region='Prague',date_dim.year='1998'";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star");

		testCubeManager = new CubeManager(typeOfConnection, userInputList);
		Session testSession = new Session(testCubeManager);
		testSession.initialize(typeOfConnection, userInputList);

		SessionQueryProcessorEngine engine = new SessionQueryProcessorEngine();
		engine.initializeConnection(typeOfConnection, userInputList);
		engine.buildSamples("pkdd99_star", "loan", SAMPLE_SIZE, true, "R");

		testEstimator = new ReservoirSamplingEstimator("pkdd99_star", "loan", testCubeManager.getCubeBase());
	}

	@Test
	public void testGetFactTableSize() {
		assertEquals(TOTAL_ROWS, testEstimator.getFactTableSize());
	}

	// Test the validity of the single sigma qeury results
	@Test
	public void testSingleSigma() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_NORTH_MORAVIA, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(1, results.size());
		assertTrue(results.get(0).getTotalRows() > 0 && results.get(0).getTotalRows() <= TOTAL_ROWS);
		assertTrue(results.get(0).getMatchingRows() >= 0 && results.get(0).getMatchingRows() <= results.get(0).getTotalRows());
	}

	// Test that the sampling result is not too far from the truth (FullTableScan)
	@Test
	public void testSamplingResultNotTooFarFromTruth() throws Exception {
		FullTableScanEstimator ftsEstimator = new FullTableScanEstimator(testCubeManager.getCubeBase());

		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_NORTH_MORAVIA, new HashMap<>());
		double truth = ftsEstimator.estimate(query, TOTAL_ROWS).get(0).getSelectivity();

		query = testCubeManager.createCubeQueryFromString(Q_NORTH_MORAVIA, new HashMap<>());
		double estimate = testEstimator.estimate(query, TOTAL_ROWS).get(0).getSelectivity();

		assertTrue(Math.abs(estimate - truth) < 0.10);
	}

	// Test the validity of the results for 2 sigmas
	@Test
	public void testDualSigma() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_PRAGUE_AND_1998, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(2, results.size());
		for (SelectivityResult r : results) {
			assertNotNull(r.getColumnName());
			assertTrue(r.getTotalRows() > 0 && r.getTotalRows() <= TOTAL_ROWS);
		}
	}

	// Test the results if the sigma is not matched against anything in the db
	@Test
	public void testNoMatchingSigmaReturnsZeroResult() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_ATLANTIS, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(1, results.size());
		assertEquals(0, results.get(0).getMatchingRows());
	}
}
