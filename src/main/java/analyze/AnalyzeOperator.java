package analyze;

import java.util.ArrayList;

import analyze.report.AnalyzeReport;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import mainengine.ResultFileMetadata;
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
	
	// Analyze operator result object
	private AnalyzeReport analyzeReport;
	
	public AnalyzeOperator(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
		this.cubeManager = cubeManager;
		this.analyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,cubeManager,schemaName,connectionType);
		this.analyzeQueries = new ArrayList<AnalyzeQuery>();
		this.analyzeReport = new AnalyzeReport(incomingExpression,connectionType);
	}
	
	/**
	 * Auxiliary method that checks the syntax of the incoming expression and if
	 * the syntax is correct it constructs the necessary AnalyzeQueries.
	 * @return True if the AnalyzeQueries were constructed, False if not.
	 */
	private boolean constructAnalyzeQueries() {
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
	 * Method that executes the AnalyzeQueries, sets the Result field of the AnalyzeQueries
	 * and creates the report file 
	 */
	public ResultFileMetadata execute() {
		//this must return a Intentional Result object, not null, not void, not int
		boolean translationStatus = this.constructAnalyzeQueries();
		boolean cubeQueryGenerationStatus = analyzeTranslationManager.getCubeQueryGenerationStatus();
		if(translationStatus == false) {
			System.err.println("ANALYZE operator execution is aborting...");
			analyzeReport.setErrorStatus(true);
			analyzeReport.setErrorMessage("ANALYZE incoming expression contains syntax errors!Please check.");
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			analyzeReport.createTextReportFile();
		}else if(cubeQueryGenerationStatus == false){
			System.err.println("ANALYZE expression encountered errors!\nANALYZE operator execution is aborting...");
			analyzeReport.setErrorStatus(true);
			analyzeReport.setErrorMessage("Expressions or values of the given ANALYZE incoming expression are invalid!Please check.");
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			analyzeReport.createTextReportFile();
		}else if(translationStatus == true && cubeQueryGenerationStatus == true) {
			analyzeReport.setErrorStatus(false);
			for(AnalyzeQuery aq: analyzeQueries) {
				CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
				Result result = cubeManager.executeQuery(analyzeCubeQuery);
				aq.setAnalyzeQueryResult(result);
			}
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			analyzeReport.createTextReportFile();
		}
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(analyzeReport.getLocalFolder());
		resultFile.setResultFile(analyzeReport.getReportFile());
		if(analyzeReport.getErrorStatus() == true) {
			resultFile.setErrorCheckingStatus(analyzeReport.getErrorMessage());
		}
		return resultFile;
	}
	
	public ArrayList<AnalyzeQuery> getAnalyzeQueries(){
		return analyzeQueries;
	}
}
