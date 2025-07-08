package analyze;

import java.util.ArrayList;

import analyze.mqoaggregateadapt.AggregateAdapter;
import analyze.mqoaggregateadapt.AggregateAdapterFactory;
import analyze.report.AnalyzeReport;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import result.Cell;
import result.Result;
import result.ResultFileMetadata;

public class AnalyzeOperatorMultiQueryToDuoQueryOptimizer {
	
	// CubeManager object to manage the cube
			private CubeManager cubeManager;
			
			// A manager object that manages the whole translation process
			private AnalyzeTranslationManager analyzeTranslationManager;
			
			// Collection of AnalyzeCubeQueries
			private ArrayList<AnalyzeQuery> analyzeQueries;
			
			// Analyze operator result object
			private AnalyzeReport analyzeReport;
			
			
			public AnalyzeOperatorMultiQueryToDuoQueryOptimizer(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
				this.cubeManager = cubeManager;
				this.analyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression,cubeManager,schemaName,connectionType);
				this.analyzeQueries = new ArrayList<AnalyzeQuery>();
				this.analyzeReport = new AnalyzeReport(incomingExpression,connectionType);
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
				incomingExpressionIsValid = analyzeTranslationManager.validateIncomingExpression();
				if(incomingExpressionIsValid == true) {
					long startTime = System.nanoTime();
					analyzeQueries = analyzeTranslationManager.translateToOptimizedDuoCubeQueries();
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
			//#Strategy2
			public ResultFileMetadata executeAnalyzeWithMultiQueryToDuoQueryOptimizer() {
				//this must return a Intentional Result object, not null, not void, not int
				int resultTuplesCounter = 0;
				int mqoResultSize =0;
				boolean translationStatus = this.constructUpdatedAnalyzeQueries();
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
					AggregateAdapterFactory aggrAdapterFactory = new AggregateAdapterFactory();
					AggregateAdapter aggrAdapter = aggrAdapterFactory.createAdapter(analyzeTranslationManager.getAggrFunc());
					
					
					long startTime = System.nanoTime();
					double mqoProcessingTime = 0;
					
					for(int i=0; i<analyzeQueries.size(); i++) {
						AnalyzeQuery aq = analyzeQueries.get(i);
						CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
						Result result = cubeManager.executeSimpleSqlQuery(analyzeCubeQuery);//executeSimpleSqlQuery() method for a simpler version of SQL or executeQuery() for the old SQL query version
						String[][] resultArray = result.getResultArray();
						if(resultArray!=null) {
							resultTuplesCounter += resultArray.length;
						}
						aq.setAnalyzeQueryResult(result);
						
						
						ArrayList<Cell> resultCellsMQO = result.getCells(); 
						long mqoStartTime = System.nanoTime();
						ArrayList<String> mqoResult = new ArrayList<String>();
						if(i==0) {//DRILL-DOWN AND BASE QUERY
							AnalyzeDuoQueryOptimizerDDAndORGAuxiliaryQueryResultBuilder auxResultBuilder = new AnalyzeDuoQueryOptimizerDDAndORGAuxiliaryQueryResultBuilder();
							mqoResult = auxResultBuilder.feedTheAuxiliaryQueriesfromMQO(resultCellsMQO, 
									analyzeTranslationManager.getSigmaExpressions(), 
									analyzeTranslationManager.getSigmaExpressionsToValues(),
									aggrAdapter);
							
						} else {//SIBLING QUERIES
							AnalyzeDuoQueryOptimizerDDAndORGAuxiliaryQueryResultBuilder auxResultBuilder = new AnalyzeDuoQueryOptimizerDDAndORGAuxiliaryQueryResultBuilder();
							/*mqoResult = auxResultBuilder.feedTheAuxiliaryQueriesfromMQO(resultCellsMQO, 
									analyzeTranslationManager.getSigmaExpressions(), 
									analyzeTranslationManager.getSigmaExpressionsToValues(),
									aggrAdapter);*/
							Result siblingResult = aq.getAnalyzeQueryResult();
							String[][] siblingResultArray = siblingResult.getResultArray();
							for(int j = 0;j<siblingResultArray.length;j++) {
								if(j > 1) {
									String resultRow = "";
									for(int k = 0;k<siblingResultArray[j].length;k++) {
										if(k ==0) {
											resultRow += siblingResultArray[j][k];
										}else {
											resultRow += ',' + siblingResultArray[j][k];
										}
									}
									mqoResult.add(resultRow);
								}
							}
							
						}
						
						aq.setAnalyzeMQOResult(mqoResult);
						mqoResultSize += mqoResult.size();
						long mqoEndTime = System.nanoTime();
						mqoProcessingTime += mqoEndTime - mqoStartTime;

					}
					long endTime = System.nanoTime();
					
					double executionTime = endTime - startTime - mqoProcessingTime;
					System.out.println("Queries Execution Time :" + Double.toString(executionTime/1000000) + " ms");
					System.out.println("Multi-Query Optimization with Duo Query Strategy Processing Time :" + Double.toString(mqoProcessingTime/1000000) + " ms");
					analyzeReport.setAnalyzeQueries(analyzeQueries);
					
					//startTime = System.nanoTime();
					//analyzeReport.createTextReportFile();
					//endTime = System.nanoTime();
					//double reportingTime = endTime - startTime;
					//System.out.println("Reporting Result Time :" + Double.toString(reportingTime/1000000) + " ms");
				}
				ResultFileMetadata resultFile = new ResultFileMetadata();
				resultFile.setLocalFolder(analyzeReport.getLocalFolder());
				resultFile.setResultFile(analyzeReport.getReportFile());
				if(analyzeReport.getErrorStatus() == true) {
					resultFile.setErrorCheckingStatus(analyzeReport.getErrorMessage());
				}
				System.out.println("Number of generated queries: " + Integer.toString(analyzeQueries.size()) + " queries");
				System.out.println("Number of resulted tuples: " + Integer.toString(resultTuplesCounter));
				System.out.println("Number of resulted tuples after the multi-query optimization: " + Integer.toString(mqoResultSize));
				return resultFile;
			}
			
			public ArrayList<AnalyzeQuery> getAnalyzeQueries(){
				return analyzeQueries;
			}
}
