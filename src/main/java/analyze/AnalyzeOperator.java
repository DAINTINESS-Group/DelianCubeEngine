package analyze;

import java.util.ArrayList;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

/**
 * 
 * A class for the intentional operator Analyze
 * Implemented so far: Check the validity of incoming expression and fetch the cube queries
 * from the AnalyzeTranslationMaanger
 * @author mariosjkb
 */

public class AnalyzeOperator {

	// CubeManager object to manage the cube
	private CubeManager cubeManager;
	
	// A manager object that manages the whole translation process
	private AnalyzeTranslationManager analyzeTranslationManager;
	
	// The name of the dataset loaded in Delian
	private String schemaName;
	
	
	public AnalyzeOperator(String incomingExpression, CubeManager cubeManager, String schemaName) {
		this.cubeManager = cubeManager;
		this.analyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,cubeManager,schemaName);
		this.schemaName = schemaName;
	}
	
	/** 
	 * At this point the method translates the incoming expressions and fills the analyzeCubeQueries ArrayList.
	 * @return (for now) it returns the status of the method.
	 */
	public int execute() {
		// TODO Auto-generated method stub
		//this must return a Intentional Result object, not null, not void, not int
		int status = -1;
		int incomingExpressionIsValid;
		ArrayList<CubeQuery> analyzeCubeQueries = new ArrayList<CubeQuery>();
		
		//check if the incoming expression is written correctly and if so translate it to cube queries
		incomingExpressionIsValid = analyzeTranslationManager.validateIncomingExpression();
		if(incomingExpressionIsValid == 0) {
			analyzeCubeQueries = analyzeTranslationManager.translateToCubeQueries();
		}else {
			System.out.println("ANALYZE incoming expression contains syntax errors!Please check.");
			return -1;
		}
		
		return status;
	}
}
