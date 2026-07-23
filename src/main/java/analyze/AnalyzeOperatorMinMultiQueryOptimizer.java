package analyze;

import java.util.ArrayList;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import result.Result;

public class AnalyzeOperatorMinMultiQueryOptimizer implements IntentionalOperator {

	// CubeManager object to manage the cube
	private CubeManager cubeManager;

	// A manager object that manages the whole translation process
	private AnalyzeTranslationManager analyzeTranslationManager;

	// Collection of AnalyzeCubeQueries
	private ArrayList<AnalyzeQuery> analyzeQueries;

	public AnalyzeOperatorMinMultiQueryOptimizer(CubeManager cubeManager, AnalyzeTranslationManager analyzeTranslationManager) {
		this.cubeManager = cubeManager;
		this.analyzeQueries = new ArrayList<AnalyzeQuery>();
		this.analyzeTranslationManager = analyzeTranslationManager;
	}

	/**
	 * Auxiliary method that checks the syntax of the incoming expression and if
	 * the syntax is correct it constructs the necessary AnalyzeQueries.
	 * Constructs the base query, Updated Sibling queries and traditional Drill-down queries.
	 * @return True if the AnalyzeQueries were constructed, False if not.
	 */
	private boolean constructUpdatedAnalyzeQueries() {
		boolean incomingExpressionIsValid;
		//check if the incoming expression is written correctly and if so translate it to cube queries
		incomingExpressionIsValid = this.analyzeTranslationManager.validateIncomingExpression();
		if(incomingExpressionIsValid) {
			long startTime = System.nanoTime();
			analyzeQueries = analyzeTranslationManager.translateToUpdatedCubeQueries();
			long endTime = System.nanoTime();
			double queriesGenerationTime = endTime - startTime;
			System.out.println("$$ Analyze Cube Query Generation Time \t\t\t" + Double.toString(queriesGenerationTime/1000000));// + " ms");
			return true;
		}else {
			System.err.println("ANALYZE incoming expression contains syntax errors!Please check.");
			return false;
		}
	}

	/**
	 * Parses the incoming expression, generates the analyze queries, executes them, and returns one
	 * {@link LabeledResult} per query. Throws on syntax or query-generation errors.
	 * @return List < LabeledResult >
	 */
	@Override
	public List<LabeledResult> execute(String query){
		if (!constructUpdatedAnalyzeQueries()) {
			throw new RuntimeException("ANALYZE incoming expression contains syntax errors!");
		}
		if (!analyzeTranslationManager.getCubeQueryGenerationStatus()) {
			throw new RuntimeException("Expressions or values of the given ANALYZE incoming expression are invalid!");
		}

		List<LabeledResult> results = new ArrayList<LabeledResult>();
		for(AnalyzeQuery aq: analyzeQueries) {
			CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
			Result result = cubeManager.executeQuery(analyzeCubeQuery);
			aq.setAnalyzeQueryResult(result);
			results.add(new LabeledResult(analyzeCubeQuery, result, null));
		}
		return results;
	}

	public ArrayList<AnalyzeQuery> getAnalyzeQueries(){
		return analyzeQueries;
	}
}
