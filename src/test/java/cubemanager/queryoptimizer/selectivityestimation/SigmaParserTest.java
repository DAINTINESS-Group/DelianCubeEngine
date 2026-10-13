package cubemanager.queryoptimizer.selectivityestimation;


import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import mainengine.Session;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * A test class for the sigma parser
 */
public class SigmaParserTest {

	private static CubeManager testCubeManager;
	private static List<Dimension> dimensions;
	private static List<String> dimRefFields;

	private static final String Q_NORTH_MORAVIA =
			"CubeName:loan\nName:Q_NorthMoravia\nAggrFunc:Sum\nMeasure:amount\n"
					+ "Gamma:account_dim.district_name\nSigma:account_dim.region='north Moravia'";

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

		// We only need dimensions and refFields from the query
		CubeQuery query = testCubeManager.createCubeQueryFromString(Q_NORTH_MORAVIA, new HashMap<>());
		dimensions = query.getReferCube().getDimensionsList();
		dimRefFields = query.getReferCube().getDimensionRefFieldList();
	}

	// Test the "=" parsing
	@Test
	public void testEqualsParsingResolvesCorrectly() {
		String[] sigma = {"account_dim.region", "=", "north Moravia"};
		SigmaParser.ParsedSigma result = SigmaParser.parse(sigma, dimensions, dimRefFields);

		assertNotNull(result);
		assertEquals("account.region", result.filterCol);
		assertEquals("account", result.dimTable);
		assertEquals("loan.l_account_id", result.factFK);
		assertEquals("account.account_id", result.dimPK);
	}

	// Test the ">" parsing
	@Test
	public void testGTresolvesCorrectly() {
		String[] sigma = {"date_dim.year", ">", "1997"};
		SigmaParser.ParsedSigma result = SigmaParser.parse(sigma, dimensions, dimRefFields);

		assertNotNull(result);
		assertEquals("date.Year", result.filterCol);
	}

	// Test the ">=" parsing
	@Test
	public void testGTorEqualsResolvesCorrectly() {
		String[] sigma = {"date_dim.year", ">=", "1997"};
		SigmaParser.ParsedSigma result = SigmaParser.parse(sigma, dimensions, dimRefFields);

		assertNotNull(result);
		assertEquals("date.Year", result.filterCol);
	}

	// Test the "IN" parsing
	@Test
	public void testINresolvesCorrectly() {
		String[] sigma = {"account_dim.region", "IN", "('Prague','Brno')"};
		SigmaParser.ParsedSigma result = SigmaParser.parse(sigma, dimensions, dimRefFields);

		assertNotNull(result);
		assertEquals("account.region", result.filterCol);
	}

	// Test the "NOT IN" parsing
	@Test
	public void testNOTINresolvesCorrectly() {
		String[] sigma = {"account_dim.region", "NOT IN", "('Prague','Brno')"};
		SigmaParser.ParsedSigma result = SigmaParser.parse(sigma, dimensions, dimRefFields);

		assertNotNull(result);
		assertEquals("account.region", result.filterCol);
	}

	// Test the "BETWEEN" parsing
	@Test
	public void testBETWEENresolvesCorrectly() {
		String[] sigma = {"date_dim.year", "BETWEEN", "1997 AND 1999"};
		SigmaParser.ParsedSigma result = SigmaParser.parse(sigma, dimensions, dimRefFields);

		assertNotNull(result);
		assertEquals("date.Year", result.filterCol);
		assertEquals("date", result.dimTable);
		assertEquals("loan.date", result.factFK);
		assertEquals("date.SK_Day", result.dimPK);
	}


	// EDGE CASES FOR INVALID / NULL SIGMAS
	@Test
	public void testNullSigmaReturnsNull() {
		assertNull(SigmaParser.parse(null, dimensions, dimRefFields));
	}

	@Test
	public void testNullSigmaExprReturnsNull() {
		String[] sigma = {null, "=", "'Prague'"};
		assertNull(SigmaParser.parse(sigma, dimensions, dimRefFields));
	}

	@Test
	public void testNoDotReturnsNull() {
		String[] sigma = {"account_dim", "=", "'Prague'"};
		assertNull(SigmaParser.parse(sigma, dimensions, dimRefFields));
	}

	@Test
	public void testUnknownDimensionReturnsNull() {
		String[] sigma = {"unknown_dim.region", "=", "'Prague'"};
		assertNull(SigmaParser.parse(sigma, dimensions, dimRefFields));
	}

	@Test
	public void testUnknownLevelReturnsNull() {
		String[] sigma = {"account_dim.lvl99", "=", "'Prague'"};
		assertNull(SigmaParser.parse(sigma, dimensions, dimRefFields));
	}
}
