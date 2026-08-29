package cubemanager.queryoptimizer.selectivityestimation;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import mainengine.Session;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test class for the full table scan estimator
 */
public class FullTableScanEstimatorTest {

	private static CubeManager testCubeManager;
	private static FullTableScanEstimator testEstimator;

	private static final int TOTAL_ROWS = 682;

	// A query with a sigma that will not match anything in the db
	private static final String Q_ATLANTIS =
			"CubeName:loan\nName:Q_Atlantis\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.district_name\nSigma:account_dim.region='Atlantis'";

	// A single sigma query
	private static final String Q_NORTH_MORAVIA =
			"CubeName:loan\nName:Q_NorthMoravia\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.district_name\nSigma:account_dim.region='north Moravia'";

	// A two sigmas query
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

		testEstimator = new FullTableScanEstimator((testCubeManager.getCubeBase()));
	}

	// Test the results for a single sigma
	@Test
	public void testSingleSigma() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_NORTH_MORAVIA, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(1, results.size());
		assertEquals(117, results.get(0).getMatchingRows());
		assertEquals(117.0 / TOTAL_ROWS, results.get(0).getSelectivity(), 0.0001);
	}

	// Test the results for two sigmas
	@Test
	public void testTwoSigmas() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_PRAGUE_AND_1998, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(2, results.size());
		assertEquals(84, results.get(0).getMatchingRows());
		assertEquals(84.0 / TOTAL_ROWS, results.get(0).getSelectivity(), 0.0001);
		assertEquals(158, results.get(1).getMatchingRows());
		assertEquals(158.0 / TOTAL_ROWS, results.get(1).getSelectivity(), 0.0001);
	}

	// Test the results if the sigma is not matched against anything in the db
	@Test
	public void testNoMatchingSigmaReturnsZeroSelectivity() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_ATLANTIS, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(1, results.size());
		assertEquals(0, results.get(0).getMatchingRows());
		assertEquals(0.0, results.get(0).getSelectivity(), 0.0001);
	}
}
