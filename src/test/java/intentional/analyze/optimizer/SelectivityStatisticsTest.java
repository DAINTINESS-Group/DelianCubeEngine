package intentional.analyze.optimizer;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.analyze.optimizer.selectivityestimation.CustomKey;
import intentional.analyze.optimizer.selectivityestimation.ReservoirSamplingEstimator;
import intentional.analyze.optimizer.selectivityestimation.SelectivityResult;
import mainengine.Session;
import mainengine.SessionQueryProcessorEngine;

public class SelectivityStatisticsTest {
	private static CubeManager testCubeManager;
	private static ReservoirSamplingEstimator testEstimator;

	private static final String Q_PRAGUE_AND_1998 =
			"CubeName:loan\nName:Q_PragueAnd1998\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.distict_name,date_dim.year\n"
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
		
		testCubeManager.setUpSelectivityStatistics(userInputList.get("inputFolder"), userInputList.get("cubeName"));

		SessionQueryProcessorEngine engine = new SessionQueryProcessorEngine();
		engine.initializeConnection(typeOfConnection, userInputList);
	
		testEstimator = new ReservoirSamplingEstimator(testCubeManager);
		
	}
	
	@Test
	public void testSelectivityStats() {
		HashMap<CustomKey,Integer> selectivities = testCubeManager.getSelectivity();
		for(Map.Entry<CustomKey, Integer> e: selectivities.entrySet()) {
			System.out.println(e.getKey() + " " + e.getValue());
		}
		assertEquals(175, selectivities.size());
	}
	
	@Test
	public void testQueryMatchingTuples() throws RemoteException {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_PRAGUE_AND_1998, new HashMap<>());
		List<SelectivityResult> results = (List<SelectivityResult>) testEstimator.estimate(query);
		int matchingRowsPrague = results.get(0).getMatchingRows();
		int matchingRows1998 = results.get(1).getMatchingRows();
		assertEquals(2, results.size());
		assertEquals(3, matchingRowsPrague);
		assertEquals(10, matchingRows1998);
	}
	
	@Test
	public void testQuerySelectivities() throws RemoteException {
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_PRAGUE_AND_1998, new HashMap<>());
		List<SelectivityResult> results = (List<SelectivityResult>) testEstimator.estimate(query);
		double selectivityPrague = results.get(0).getSelectivity();
		double selectivity1998 = results.get(1).getSelectivity();
		assertEquals(2, results.size());
		assertEquals(0.0882, selectivityPrague, 0.001);
		assertEquals(0.2941, selectivity1998,0.001);
	}

}
