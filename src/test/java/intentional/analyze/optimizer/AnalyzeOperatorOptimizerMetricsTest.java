package intentional.analyze.optimizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeQuery;
import mainengine.SessionQueryProcessorEngine;

public class AnalyzeOperatorOptimizerMetricsTest {
	private static CubeManager testCubeManager;
	private static AnalyzeOperatorOptimizerQueryGenerator testOptimizerQueryGenerator;
	private static AnalyzeOperatorOptimizer testAnalyzeOperatorOptimizer;
	private static SessionQueryProcessorEngine testEngine;
	
	private static final String PRAGUE_AND_1998 = "ANALYZE sum(amount) FROM loan FOR region='Prague' AND "
												+ "year='1998' GROUP BY district_name, year AS TEST ";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star_100m");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star_100m");

		testEngine = new SessionQueryProcessorEngine();
		testEngine.initializeConnection(typeOfConnection, userInputList);
		testCubeManager = testEngine.getSessionContext().getCubeManager();
		
		List<AnalyzeQuery> testAnalyzeFacilitatorQueries = new ArrayList<AnalyzeQuery>();
		testOptimizerQueryGenerator = new AnalyzeOperatorOptimizerQueryGenerator(PRAGUE_AND_1998, testCubeManager, userInputList.get("schemaName"),typeOfConnection);
		boolean incomingExpressionIsValid = testOptimizerQueryGenerator.validateIncomingExpression();
		if(incomingExpressionIsValid) {
			testAnalyzeFacilitatorQueries = testOptimizerQueryGenerator.translateToAnalyzeQueries();
		}
		testAnalyzeOperatorOptimizer = new AnalyzeOperatorOptimizer(testCubeManager, testAnalyzeFacilitatorQueries);
	}
	
	@Test
	public void testFacilitatorQueriesSelectivities() {
		double estimatedQOrgSelectivity = testAnalyzeOperatorOptimizer.getNormalizedQOrgSelectivity();
		double estimatedQSib1Selectivity = testAnalyzeOperatorOptimizer.getSiblingFacilitatorQueriesNormalizedSelectivities().get("Qsib1");
		double estimatedQSib2Selectivity = testAnalyzeOperatorOptimizer.getSiblingFacilitatorQueriesNormalizedSelectivities().get("Qsib2");
		double estimatedQAllSelectivity = testAnalyzeOperatorOptimizer.getNormalizedQAllSelectivity();
		
		assertEquals(0.020144,estimatedQOrgSelectivity,0.001);
		assertEquals(0.1595,estimatedQSib1Selectivity,0.001);
		assertEquals(0.1263,estimatedQSib2Selectivity,0.001);
		assertEquals(1.0,estimatedQAllSelectivity,0.001);
	}
	
	@Test
	public void testCostMetricsSelectivities() {
		double estimatedSiblingSumSelectivity = testAnalyzeOperatorOptimizer.getNormalizedSiblingSum();
		double estimatedSiblingImbalanceSelectivity = testAnalyzeOperatorOptimizer.getNormalizedSiblingImbalance();
		double estimatedMinSibSelectivity = testAnalyzeOperatorOptimizer.getNormalizedMinSib();
		double estimatedMaxSibSelectivity = testAnalyzeOperatorOptimizer.getNormalizedMaxSib();
		double estimatedUselessSpaceSelectivity = testAnalyzeOperatorOptimizer.getNormalizedUselessSpace();
		
		assertEquals(0.2858,estimatedSiblingSumSelectivity,0.001);
		assertEquals(0.0332,estimatedSiblingImbalanceSelectivity,0.001);
		assertEquals(0.1263,estimatedMinSibSelectivity,0.001);
		assertEquals(0.1595,estimatedMaxSibSelectivity,0.001);
		assertEquals(0.7142,estimatedUselessSpaceSelectivity,0.001);
	}
	
	@Test
	public void testAdvancedCostMetricsSelectivities() {
		double estimatedSMR = testAnalyzeOperatorOptimizer.getSiblingMegaRatio();
		double estimatedSIC = testAnalyzeOperatorOptimizer.getSiblingImbalanceCoefficient();

		assertEquals(0.2858,estimatedSMR,0.001);
		assertEquals(0.116165,estimatedSIC,0.001);
	}
	
	@Test
	public void testMetricsPropertiesSelectivities() {
		double estimatedQOrgSelectivity = testAnalyzeOperatorOptimizer.getNormalizedQOrgSelectivity();
		double estimatedQSib1Selectivity = testAnalyzeOperatorOptimizer.getSiblingFacilitatorQueriesNormalizedSelectivities().get("Qsib1");
		double estimatedQSib2Selectivity = testAnalyzeOperatorOptimizer.getSiblingFacilitatorQueriesNormalizedSelectivities().get("Qsib2");
		double estimatedQAllSelectivity = testAnalyzeOperatorOptimizer.getNormalizedQAllSelectivity();
		double estimatedSiblingSumSelectivity = testAnalyzeOperatorOptimizer.getNormalizedSiblingSum();
		double estimatedSiblingImbalanceSelectivity = testAnalyzeOperatorOptimizer.getNormalizedSiblingImbalance();
		double estimatedMinSibSelectivity = testAnalyzeOperatorOptimizer.getNormalizedMinSib();
		double estimatedMaxSibSelectivity = testAnalyzeOperatorOptimizer.getNormalizedMaxSib();
		double estimatedSMR = testAnalyzeOperatorOptimizer.getSiblingMegaRatio();
		double estimatedSIC = testAnalyzeOperatorOptimizer.getSiblingImbalanceCoefficient();

		assertTrue(estimatedQAllSelectivity >= estimatedQSib1Selectivity);
		assertTrue(estimatedQAllSelectivity >= estimatedQSib2Selectivity);
		assertTrue(estimatedQAllSelectivity > estimatedQOrgSelectivity);
		
		assertTrue(estimatedQSib1Selectivity > estimatedQOrgSelectivity);
		assertTrue(estimatedQSib2Selectivity > estimatedQOrgSelectivity);
		
		assertTrue(estimatedSiblingSumSelectivity > estimatedSiblingImbalanceSelectivity);
		assertTrue(estimatedMaxSibSelectivity > estimatedMinSibSelectivity);
		
		assertTrue(estimatedQOrgSelectivity <= 1 );
		assertTrue(estimatedQSib1Selectivity <= 1);
		assertTrue(estimatedQSib2Selectivity <= 1);
		assertTrue(estimatedQAllSelectivity <= 1);
		
		assertTrue(estimatedSiblingSumSelectivity <= 1);
		assertTrue(estimatedSiblingImbalanceSelectivity <= 1);
		assertTrue(estimatedMinSibSelectivity < 1);
		assertTrue(estimatedMaxSibSelectivity < 1);
		
		assertTrue(estimatedSMR <= 1);
		assertTrue(estimatedSIC <= 1 );
		
		assertTrue(estimatedQOrgSelectivity >= 0);
		assertTrue(estimatedQSib1Selectivity >= 0);
		assertTrue(estimatedQSib2Selectivity >= 0);
		assertTrue(estimatedQAllSelectivity >= 0);
		
		assertTrue(estimatedSiblingSumSelectivity > 0);
		assertTrue(estimatedSiblingImbalanceSelectivity >= 0);
		assertTrue(estimatedMinSibSelectivity >= 0);
		assertTrue(estimatedMaxSibSelectivity >= 0);
		
		assertTrue(estimatedSMR > 0);
		assertTrue(estimatedSIC > 0);
		
		
		
	}
}
