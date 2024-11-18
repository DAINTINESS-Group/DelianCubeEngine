package analyze;

import java.util.ArrayList;

import analyze.report.AnalyzeReport;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import result.Result;
import result.ResultFileMetadata;


/**
 * 
 * A class for the intentional operator Analyze
 * @author mariosjkb
 */

public class AnalyzeOperatorByIakovidis {

	// CubeManager object to manage the cube
	private CubeManager cubeManager;
	
	// A manager object that manages the whole translation process
	private AnalyzeTranslationManager analyzeTranslationManager;
	
	// Collection of AnalyzeCubeQueries
	private ArrayList<AnalyzeQuery> analyzeQueries;
	
	// Analyze operator result object
	private AnalyzeReport analyzeReport;
	
	public AnalyzeOperatorByIakovidis(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
		this.cubeManager = cubeManager;
		this.analyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,cubeManager,schemaName,connectionType);
		this.analyzeQueries = new ArrayList<AnalyzeQuery>();
		this.analyzeReport = new AnalyzeReport(incomingExpression,connectionType);
	}
	
	/**
	 * Auxiliary method that checks the syntax of the incoming expression and if
	 * the syntax is correct it constructs the necessary AnalyzeQueries.
	 * Constructs the base query, Iakovidis' Sibling and Drill-down queries.
	 * @return True if the AnalyzeQueries were constructed, False if not.
	 */
	private boolean constructAnalyzeQueries() {
		boolean incomingExpressionIsValid;
		
		//check if the incoming expression is written correctly and if so translate it to cube queries
		incomingExpressionIsValid = analyzeTranslationManager.validateIncomingExpression();
		if(incomingExpressionIsValid == true) {
			long startTime = System.nanoTime();
			analyzeQueries = analyzeTranslationManager.translateToCubeQueries();
			long endTime = System.nanoTime();
			double queriesGenerationTime = endTime - startTime;
			System.out.println("Analyze Cube Query Generation Time :" + Double.toString(queriesGenerationTime/1000000) + " ms");
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
		int resultTuplesCounter = 0;
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
			long startTime = System.nanoTime();
			for(AnalyzeQuery aq: analyzeQueries) {
				CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
				Result result = cubeManager.executeQuery(analyzeCubeQuery);
				String[][] resultArray = result.getResultArray();
				
				if(resultArray!=null) {
					for(int i=0; i<resultArray.length; i++)
					{
						for(int j=0; j<resultArray[i].length; j++)
						{
							//System.out.print(resultArray[i][j] + " ");
						}
						//System.out.println();
					}
					if(resultArray != null) {
						resultTuplesCounter += resultArray.length;
					}
				}

				aq.setAnalyzeQueryResult(result);
			}
			long endTime = System.nanoTime();
			double executionTime = endTime - startTime;
			System.out.println("Queries Execution Time :" + Double.toString(executionTime/1000000) + " ms");
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			
			startTime = System.nanoTime();
			analyzeReport.createTextReportFile();
			endTime = System.nanoTime();
			double reportingTime = endTime - startTime;
			System.out.println("Reporting Result Time :" + Double.toString(reportingTime/1000000) + " ms");
		}
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(analyzeReport.getLocalFolder());
		resultFile.setResultFile(analyzeReport.getReportFile());
		if(analyzeReport.getErrorStatus() == true) {
			resultFile.setErrorCheckingStatus(analyzeReport.getErrorMessage());
		}
		System.out.println("Number of generated queries: " + Integer.toString(analyzeQueries.size()) + " queries");
		System.out.println("Number of resulted tuples: " + Integer.toString(resultTuplesCounter));
		return resultFile;
	}
	
	public ArrayList<AnalyzeQuery> getAnalyzeQueries(){
		return analyzeQueries;
	}
}
