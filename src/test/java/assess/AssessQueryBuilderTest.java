package assess;

import cubemanager.CubeManager;
import mainengine.Session;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * In this test we are using a mocked CubeManager that only has a single
 * cube (created in createSalesCube method).
 */
public class AssessQueryBuilderTest {
	@Test
	public void testWithRealDatabase() throws RemoteException {
		CubeManager cubeManager = initializeCubeManager();
		AssessQuery query = initializeBuilder(cubeManager).build();
		System.out.println(query.targetCubeQuery);
		cubeManager.createCubeQueryFromString(query.targetCubeQuery, new HashMap<>());
		assertEquals(100.0, query.benchmark.getCellValue(), 0.001);
	}

	// This is dirt, please remove when possible
	private static CubeManager initializeCubeManager() {
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star");
		CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
		Session session = new Session(cubeManager);
		try {
			session.initialize(typeOfConnection, userInputList);
		} catch (RemoteException re) {
			fail();
		}
		return cubeManager;
	}

	/* The query under test is:
		with loan for region = 'south Moravia'
		by month, status assess avg(amount)
		against 100 using ratio(amount, benchmark.amount)
		labels {(-inf, -10.0):bad, [-10.0, 10.0]:average, (10.0, inf]: good}
	 */
	private static AssessQueryBuilder initializeBuilder(CubeManager cubeManager) {
		AssessQueryBuilder builder = new AssessQueryBuilder(cubeManager);
		builder.setTargetCubeName("loan");
		builder.setGroupBySet(
				Stream.of("month", "status")
						.collect(Collectors.toCollection(HashSet::new))
		);
		builder.setSelectionPredicates(
				Stream.of(new String[][] {{"region", "south Moravia"}})
						.collect(Collectors.toMap(predicate -> predicate[0],
								predicate -> predicate[1]))
		);
		builder.setMeasurement("amount")
				.setAggregationFunction("avg")
				.setBenchmarkDetails(Arrays.asList("Constant", "100"))
				.setDeltaFunctions(Arrays.asList("ratio"));

		List<List<String>> rules = new ArrayList<>();
		rules.add(Arrays.asList("(", "-inf", "-10.0", ")", "bad"));
		rules.add(Arrays.asList("[", "-10.0", "10.0", "]", "average"));
		rules.add(Arrays.asList("(", "10.0", "inf", "]", "good"));
		builder.setLabelingRules(rules);
		return builder;
	}

}
