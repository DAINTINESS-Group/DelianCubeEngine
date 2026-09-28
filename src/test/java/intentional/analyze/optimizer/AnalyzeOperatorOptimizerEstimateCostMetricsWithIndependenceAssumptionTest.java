package intentional.analyze.optimizer;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.BeforeClass;
import org.junit.Test;


import cubemanager.CubeManager;
import intentional.analyze.AnalyzeOperatorOptimizer;
import intentional.analyze.AnalyzeQuery;
import mainengine.SessionQueryProcessorEngine;

public class AnalyzeOperatorOptimizerEstimateCostMetricsWithIndependenceAssumptionTest {
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
	public void testEstimateCostMetricsWithIndependenceAssumption() throws IOException {
		Path csvFilePath = Paths.get("src/test/resources/InputFiles/pkdd99_star_100m/pkdd99+_metrics.csv");
		List<String> csvFileLines = Files.readAllLines(csvFilePath);
		List<String> errors = new ArrayList<String>();
		
		for(int i = 1;i<csvFileLines.size();i++) {
			String[] metrics = csvFileLines.get(i).split(",");
			
			double normalizedQOrgSelectivity = Double.parseDouble(metrics[1]);
			double normalizedQSib1Selectivity = Double.parseDouble(metrics[2]);
			double normalizedQSib2Selectivity = Double.parseDouble(metrics[3]);
			double normalizedQAllSelectivity = Double.parseDouble(metrics[4]);
			double normalizedSiblingSum = Double.parseDouble(metrics[5]);
			double normalizedSiblingImbalance = Double.parseDouble(metrics[6]);
			double normalizedMaxSib = Double.parseDouble(metrics[7]);
			double normalizedMinSib = Double.parseDouble(metrics[8]);
			double normalizedUselessSpace = Double.parseDouble(metrics[9]);
			double normalizedUselessSpaceIncludingQOrg = Double.parseDouble(metrics[10]);
			double siblingMegaRatio = Double.parseDouble(metrics[11]);
			double siblingImbalanceCoefficient = Double.parseDouble(metrics[12]);
			
			if(normalizedQOrgSelectivity < 0 || normalizedQSib1Selectivity < 0 || normalizedQSib2Selectivity < 0 || normalizedQAllSelectivity < 0) {
				errors.add("Negative Facilitator Normalized Selectivity");
			}
			if(normalizedQOrgSelectivity > 1 || normalizedQSib1Selectivity > 1 || normalizedQSib2Selectivity > 1 || normalizedQAllSelectivity >1 ){
				errors.add("Facilitator Normalized Selectivity > 1");
			}
			if(normalizedSiblingImbalance > normalizedSiblingSum) {
				errors.add("Normalized Sibling Imbalance > normalizedSiblingSum");
			}
			if(normalizedUselessSpace > normalizedQAllSelectivity) {
				errors.add("Normalized Useless Space > normalizedQAllSelectivity");
			}
			if(normalizedUselessSpaceIncludingQOrg > normalizedQAllSelectivity) {
				errors.add("Normalized Useless Space including Qorg > normalizedQAllSelectivity");
			}
			if(siblingImbalanceCoefficient < 0 || siblingImbalanceCoefficient > 1) {
				errors.add("SIC outside of [0,1]");
			}
			if(siblingMegaRatio < 0 || siblingMegaRatio > 2) {
				errors.add("SMR outside of [0,2]");
			}
			if(normalizedMinSib > normalizedMaxSib) {
				errors.add("MinSib > MaxSib");
			}
		}
		if(!errors.isEmpty()) {
			fail(errors.size() + " problem(s):\n" + String.join("\n", errors));
		}
	}
}
