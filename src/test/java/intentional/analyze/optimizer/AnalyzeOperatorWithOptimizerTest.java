package intentional.analyze.optimizer;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import intentional.analyze.AnalyzeManager;
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeOperatorOptimizer.AnalyzeStrategy;
import intentional.analyze.AnalyzeQuery;
import intentional.analyze.AnalyzeTranslationManager;
import intentional.operator.IntentionalStrategy;
import mainengine.Session;
import mainengine.SessionQueryProcessorEngine;
import result.ResultFileMetadata;

public class AnalyzeOperatorWithOptimizerTest {
	private static CubeManager testCubeManager;
	private static AnalyzeOperatorOptimizerQueryGenerator testOptimizerQueryGenerator;
	private static AnalyzeManager testAnalyzeManager;
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
		
		testOptimizerQueryGenerator = new AnalyzeOperatorOptimizerQueryGenerator(PRAGUE_AND_1998, testCubeManager, userInputList.get("schemaName"),typeOfConnection);
		testAnalyzeManager = new AnalyzeManager(PRAGUE_AND_1998,testCubeManager, userInputList.get("schemaName"),typeOfConnection);
	}
	
	@Test
	public void testAnalyzeQueryConstruction() {
		List<AnalyzeQuery> analyzeQueriesForEstimation = testAnalyzeManager.getAnalyzeQueriesForEstimation();
		
		assertEquals(3,analyzeQueriesForEstimation.size());
	}
	
	@Test
	public void testAnalyzeQueriesForEstimation() {
		AnalyzeOperatorOptimizer testAnalyzeOperatorOptimizer = testAnalyzeManager.getAnalyzeOperatorOptimizer();
		List<AnalyzeQuery> analyzeQueriesForEstimation = testAnalyzeOperatorOptimizer.getAnalyzeQueries();
		
		assertEquals(3,analyzeQueriesForEstimation.size());
	}
	
	@Test
	public void testSiblingMegaRatioEstimation() {
		AnalyzeOperatorOptimizer testAnalyzeOperatorOptimizer = testAnalyzeManager.getAnalyzeOperatorOptimizer();
		double siblingMegaRatio= testAnalyzeOperatorOptimizer.estimateSiblingMegaRatio();
		
		assertEquals(0.3823,siblingMegaRatio,0.001);
	}
	
	@Test
	public void testImbalanceCoefficientEstimation() {
		AnalyzeOperatorOptimizer testAnalyzeOperatorOptimizer = testAnalyzeManager.getAnalyzeOperatorOptimizer();
		double imbalanceCoefficient= testAnalyzeOperatorOptimizer.estimateImbalanceCoefficient();
		
		assertEquals(0.2307,imbalanceCoefficient,0.001);
	}
	
	@Test
	public void testOptimizedExecution() {
		AnalyzeOperatorOptimizer testAnalyzeOperatorOptimizer = testAnalyzeManager.getAnalyzeOperatorOptimizer();
		IntentionalStrategy results = testAnalyzeOperatorOptimizer.decideMQOAlgorithmWithIndependenceAssumption();
		
		assertEquals(IntentionalStrategy.MID_MQO, results);
	}	
}
