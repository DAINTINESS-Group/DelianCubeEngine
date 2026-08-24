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
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeOperatorOptimizer.AnalyzeStrategy;
import intentional.analyze.AnalyzeQuery;
import intentional.analyze.AnalyzeTranslationManager;
import mainengine.Session;
import mainengine.SessionQueryProcessorEngine;
import result.ResultFileMetadata;

public class AnalyzeOperatorWithOptimizerTest {
	private static CubeManager testCubeManager;
	private static AnalyzeOperatorOptimizerQueryGenerator testOptimizerQueryGenerator;
	private static AnalyzeOperatorOptimizer testAnalyzeOperatorWithOptimizer;
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
		testAnalyzeOperatorWithOptimizer = new AnalyzeOperatorOptimizer(testCubeManager, testOptimizerQueryGenerator);
	}
	
	@Test
	public void testAnalyzeQueryConstruction() {
		List<AnalyzeQuery> analyzeQueriesForEstimation = testAnalyzeOperatorWithOptimizer.getAnalyzeQueriesForEstimation();
		
		assertEquals(3,analyzeQueriesForEstimation.size());
	}
	
	@Test
	public void testAnalyzeQueriesForEstimation() {
		AnalyzeQueryCostMetricEstimator testSelectivityEstimationOptimizer = testAnalyzeOperatorWithOptimizer.getSelectivityEstimationOptimizer();
		List<AnalyzeQuery> analyzeQueriesForEstimation = testSelectivityEstimationOptimizer.getAnalyzeQueries();
		
		assertEquals(3,analyzeQueriesForEstimation.size());
	}
	
	@Test
	public void testSiblingMegaRatioEstimation() {
		AnalyzeQueryCostMetricEstimator testSelectivityEstimationOptimizer = testAnalyzeOperatorWithOptimizer.getSelectivityEstimationOptimizer();
		double siblingMegaRatio= testSelectivityEstimationOptimizer.estimateSiblingMegaRatioWithIndependenceAssumption();
		
		assertEquals(0.3823,siblingMegaRatio,0.001);
	}
	
	@Test
	public void testImbalanceCoefficientEstimation() {
		AnalyzeQueryCostMetricEstimator testSelectivityEstimationOptimizer = testAnalyzeOperatorWithOptimizer.getSelectivityEstimationOptimizer();
		double imbalanceCoefficient= testSelectivityEstimationOptimizer.estimateImbalanceCoefficientWithIndependenceAssumption();
		
		assertEquals(0.5384,imbalanceCoefficient,0.001);
	}
	
	@Test
	public void testOptimizedExecution() {
		AnalyzeStrategy results = testAnalyzeOperatorWithOptimizer.decideMQOAlgorithmWithIndependenceAssumption();
		
		assertEquals(AnalyzeStrategy.MID_MQO, results);
	}	
}
