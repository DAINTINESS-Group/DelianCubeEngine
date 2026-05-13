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
 * A test class for the histogram estimator
 */
public class HistogramEstimatorTest {

	private static CubeManager testCubeManager;
	private static HistogramEstimator testEstimator;

	private static final int TOTAL_ROWS = 682;

	private static final String Q_ATLANTIS =
			"CubeName:loan\nName:Q_Atlantis\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.lvl1\nSigma:account_dim.lvl2='Atlantis'";

	private static final String Q_NORTH_MORAVIA =
			"CubeName:loan\nName:Q_NorthMoravia\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.lvl1\nSigma:account_dim.lvl2='north Moravia'";

	private static final String Q_PRAGUE_AND_1998 =
			"CubeName:loan\nName:Q_PragueAnd1998\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.lvl1,date_dim.lvl3\n"
					+ "Sigma:account_dim.lvl2='Prague',date_dim.lvl3='1998'";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99");

		testCubeManager = new CubeManager(typeOfConnection, userInputList);
		Session testSession = new Session(testCubeManager);
		testSession.initialize(typeOfConnection, userInputList);

		testEstimator = new HistogramEstimator(testCubeManager.getCubeBase());
	}

	@Test
	public void testSingleSigma() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_NORTH_MORAVIA, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(1, results.size());
		assertEquals(117, results.get(0).getMatchingRows());
		assertEquals(117.0 / TOTAL_ROWS, results.get(0).getSelectivity(), 0.0001);
	}

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

	@Test
	public void testNoMatchingSigmaReturnsZeroSelectivity() throws Exception {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_ATLANTIS, new HashMap<>());
		List<SelectivityResult> results = testEstimator.estimate(query, TOTAL_ROWS);

		assertEquals(1, results.size());
		assertEquals(0, results.get(0).getMatchingRows());
		assertEquals(0.0, results.get(0).getSelectivity(), 0.0001);
	}
}
