package intentional.analyze;

import java.util.ArrayList;
import java.util.List;

import intentional.analyze.mqoaggregateadapt.AggregateAdapter;
import intentional.analyze.mqoaggregateadapt.AggregateAdapterFactory;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;



public class AnalyzeOperatorMaxMQO implements IntentionalOperator {
	
	// CubeManager object to manage the cube
	private CubeManager cubeManager;
	
	// A manager object that manages the whole translation process
	private AnalyzeTranslationManager analyzeTranslationManager;
	
	// Collection of AnalyzeCubeQueries
	private List<AnalyzeQuery> analyzeQueries;


	public AnalyzeOperatorMaxMQO(CubeManager cubeManager, AnalyzeTranslationManager analyzeTranslationManager) {
		this.cubeManager = cubeManager;
		this.analyzeQueries = new ArrayList<AnalyzeQuery>();
		this.analyzeTranslationManager = analyzeTranslationManager;
	}
	
	public List<AnalyzeQuery> getAnalyzeQueries(){
		return analyzeQueries;
	}
	
	public void setAnalyzeQueries(List<AnalyzeQuery> analyzeQueries){
		this.analyzeQueries = analyzeQueries;
	}
	
	/**
	 * Auxiliary method that checks the syntax of the incoming expression and if
	 * the syntax is correct it constructs the necessary AnalyzeQueries.
	 * Constructs the base query, Updated Sibling queries and traditional Drill-down queries.
	 * @return True if the AnalyzeQueries were constructed, False if not.
	 */
	private boolean constructUpdatedAnalyzeQueries() {	
		boolean incomingExpressionIsValid;
		
		incomingExpressionIsValid = this.analyzeTranslationManager.validateIncomingExpression();
		if(incomingExpressionIsValid) {
			analyzeQueries = analyzeTranslationManager.translateToOptimizedSingleCubeQueries();
			return true;
		}else {
			System.err.println("ANALYZE incoming expression contains syntax errors!Please check.");
			return false;
		}
	}
	/**
	 * Parses the incoming expression, generates the analyze queries, executes them,
	 * distributes the MQO results and returns one
	 * {@link LabeledResult} per query. Throws on syntax or query-generation errors.
	 * @return List < LabeledResult >
	 */
	@Override
	public List<LabeledResult> execute(String query) {
		if (!constructUpdatedAnalyzeQueries()) {
			throw new RuntimeException("ANALYZE incoming expression contains syntax errors!");
		}
		if (!analyzeTranslationManager.getCubeQueryGenerationStatus()) {
			throw new RuntimeException("Expressions or values of the given ANALYZE incoming expression are invalid!");
		}
		AggregateAdapterFactory aggrAdapterFactory = new AggregateAdapterFactory();
		AggregateAdapter aggrAdapter = aggrAdapterFactory.createAdapter(analyzeTranslationManager.getAggrFunc());
		List<LabeledResult> results = new ArrayList<LabeledResult>();

		for(AnalyzeQuery aq: analyzeQueries) {
			CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
			Result result = cubeManager.executeQuery(analyzeCubeQuery);
			aq.setAnalyzeQueryResult(result);
			ArrayList<Cell> resultCellsMQO = result.getCells();
			AnalyzeMaxMQOAuxiliaryQueryResultBuilder auxResultBuilder = new AnalyzeMaxMQOAuxiliaryQueryResultBuilder();
			ArrayList<String> mqoResult = auxResultBuilder.feedTheAuxiliaryQueriesfromMQO(resultCellsMQO,
					analyzeTranslationManager.getSigmaExpressions(),
					analyzeTranslationManager.getSigmaExpressionsToValues(),
					aggrAdapter);
			aq.setAnalyzeMQOResult(mqoResult);
			results.add(new LabeledResult(analyzeCubeQuery, result, null));
		}
		return results;
	}
}
