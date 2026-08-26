package intentional.analyze.optimizer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import mainengine.SessionQueryProcessorEngine;
import result.ResultFileMetadata;

public class AnalyzeOperatorOptimizerCompletePipelineTest {
	private static CubeManager testCubeManager;
	private static SessionQueryProcessorEngine testEngine;
	
	private static final String PRAGUE_AND_1998 = "ANALYZE sum(amount) FROM loan FOR region='Prague' AND "
												+ "year='1998' GROUP BY district_name, year AS TEST ";

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
	}
	
	@Test
	public void testOptimizedExecutionFromService() throws Exception {
		ResultFileMetadata results = testEngine.analyzeWithOptimizer(PRAGUE_AND_1998);
		File testResultFile = new File("OutputFiles/analyze/TEST-BaseAndDrillDownDuoQueryOptimizer.md");
		boolean testResultFileExists = testResultFile.exists();
		assertTrue(testResultFileExists);
	}
}
