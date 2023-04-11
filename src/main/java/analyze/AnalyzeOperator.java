package analyze;

import java.util.ArrayList;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import result.Result;


/**
 * 
 * A class for the intentional operator Analyze
 * @author mariosjkb
 */

public class AnalyzeOperator {

	// CubeManager object to manage the cube
	private CubeManager cubeManager;
	
	// A manager object that manages the whole translation process
	private AnalyzeTranslationManager analyzeTranslationManager;
	
	// Collection of AnalyzeCubeQueries
	private ArrayList<AnalyzeQuery> analyzeQueries;
	
	public AnalyzeOperator(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
		this.cubeManager = cubeManager;
		this.analyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,cubeManager,schemaName,connectionType);
		this.analyzeQueries = new ArrayList<AnalyzeQuery>();
	}
	
	/**
	 * Auxiliary method that checks the syntax of the incoming expression and if
	 * the syntax is correct it constructs the necessary AnalyzeQueries.
	 * @return True if the AnalyzeQueries were constructed, False if not.
	 */
	private boolean constructAnalyzeCubeQueries() {
		boolean incomingExpressionIsValid;
		
		//check if the incoming expression is written correctly and if so translate it to cube queries
		incomingExpressionIsValid = analyzeTranslationManager.validateIncomingExpression();
		if(incomingExpressionIsValid == true) {
			analyzeQueries = analyzeTranslationManager.translateToCubeQueries();
			return true;
		}else {
			System.err.println("ANALYZE incoming expression contains syntax errors!Please check.");
			return false;
		}
	}
	
	/**
	 * Method that executes the AnalyzeQueries and set the Result field of the AnalyzeQueries
	 */
	public void execute() {
		//this must return a Intentional Result object, not null, not void, not int
		boolean translationStatus = this.constructAnalyzeCubeQueries();
		if(translationStatus == false) {
			System.err.println("ANALYZE operator execution is aborting...");
			return;
		}else {
			for(AnalyzeQuery aq: analyzeQueries) {
				CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
				Result result = cubeManager.executeQuery(analyzeCubeQuery);
				aq.setAnalyzeQueryResult(result);
			}
		}
	}
	
	public ArrayList<AnalyzeQuery> getAnalyzeQueries(){
		return analyzeQueries;
	}
}
