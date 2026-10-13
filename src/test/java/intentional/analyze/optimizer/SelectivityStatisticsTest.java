package intentional.analyze.optimizer;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.statistics.selectivity.SelectivityCustomKey;
import cubemanager.statistics.selectivity.FilterEstimator;
import cubemanager.statistics.selectivity.SelectivityResult;
import mainengine.Session;
import mainengine.SessionQueryProcessorEngine;

public class SelectivityStatisticsTest {
	private static CubeManager testCubeManager;
	private static FilterEstimator testEstimator;
	private static SessionQueryProcessorEngine testEngine;

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

		testEngine = new SessionQueryProcessorEngine();
		testEngine.initializeConnection(typeOfConnection, userInputList);
		testCubeManager = testEngine.getSessionContext().getCubeManager();
			
		testEstimator = new FilterEstimator(testCubeManager);
		
	}
	
	@Test
	public void testSelectivityStats() {
		HashMap<String, HashMap<String, Integer>> selectivities = testCubeManager.getValueCardinalityProfile();
		for(Entry e: selectivities.entrySet()) {
			System.out.println(e.getKey());
		}
		assertEquals(13, selectivities.size());
	}
	
	@Test
	public void testQueryMatchingTuples() throws RemoteException {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_PRAGUE_AND_1998, new HashMap<>());
		List<SelectivityResult> results = (List<SelectivityResult>) testEstimator.estimate(query);
		int matchingRowsPrague = results.get(0).getMatchingRows();
		int matchingRows1998 = results.get(1).getMatchingRows();
		assertEquals(2, results.size());
		assertEquals(4, matchingRowsPrague);
		assertEquals(2, matchingRows1998);
	}
	
	@Test
	public void testQuerySelectivities() throws RemoteException {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_PRAGUE_AND_1998, new HashMap<>());
		List<SelectivityResult> results = (List<SelectivityResult>) testEstimator.estimate(query);
		double selectivityPrague = results.get(0).getSelectivity();
		double selectivity1998 = results.get(1).getSelectivity();
		assertEquals(2, results.size());
		assertEquals(0.1538, selectivityPrague, 0.001);
		assertEquals(0.0769, selectivity1998,0.001);
	}

}