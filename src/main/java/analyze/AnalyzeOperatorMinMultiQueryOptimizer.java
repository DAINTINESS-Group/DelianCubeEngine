package analyze;

import java.util.ArrayList;
import java.util.List;

import analyze.report.AnalyzeReport;
import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import highlights.*;
import highlights.archetypes.megacontributor.MegaContributorArchetype;
import highlights.archetypes.outlier.OutlierArchetype;
import highlights.archetypes.topk.TopKContributorsArchetype;
import highlights.metamodel.ArchetypeProperty;
import intentionaloperator.IntentionalOperator;
import intentionaloperator.OperatorResult;
import result.Result;
import result.ResultFileMetadata;


public class AnalyzeOperatorMinMultiQueryOptimizer implements IntentionalOperator {

	// CubeManager object to manage the cube
	private CubeManager cubeManager;
	
	// A manager object that manages the whole translation process
	private AnalyzeTranslationManager analyzeTranslationManager;
	
	// Collection of AnalyzeCubeQueries
	private ArrayList<AnalyzeQuery> analyzeQueries;

	private List<ArchetypeProperty> registeredArchetypes;

	private String outputFileName;

	// Analyze operator result object
	private AnalyzeReport analyzeReport;

		
	public AnalyzeOperatorMinMultiQueryOptimizer(String incomingExpression, CubeManager cubeManager,String connectionType,AnalyzeTranslationManager analyzeTranslationManager) {
		this.cubeManager = cubeManager;
		this.analyzeQueries = new ArrayList<AnalyzeQuery>();
		this.analyzeReport = new AnalyzeReport(incomingExpression,connectionType);
		this.analyzeTranslationManager = analyzeTranslationManager;
		this.registeredArchetypes = registeredArchetypes();
		System.out.println("$$ ---------------------------------------------------------");
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
	 * Auxiliary method that registers the Archetype Properties to be checked in the operator's query results.
	 * @return ArrayList < ArchetypeProperty >
	 */
	public ArrayList<ArchetypeProperty> registeredArchetypes(){
		ArrayList<ArchetypeProperty> archetypes = new ArrayList<ArchetypeProperty>();
		archetypes.add(MegaContributorArchetype.create());
		archetypes.add(TopKContributorsArchetype.create());
		archetypes.add(OutlierArchetype.create());
		return archetypes;
	}

	/**
	 * This method executes automatically parses the incoming expression, generates the 5 analyze queries, executes
	 * them, and create OperatorResult objects (one per analyze query) to be used for Highlights Extraction.
	 * @return ArrayList < OperatorResult >
	 */
	public ArrayList<OperatorResult> executeMinMQOQueries(){
		//this must return a Intentional Result object, not null, not void, not int
		ArrayList<OperatorResult> analyzeMinMQOResults = new ArrayList<OperatorResult>();
		int resultTuplesCounter = 0;
		double totalExecutionTime = 0.0;
		boolean translationStatus = this.constructUpdatedAnalyzeQueries();
		boolean cubeQueryGenerationStatus = analyzeTranslationManager.getCubeQueryGenerationStatus();
		if(!translationStatus) {
			System.err.println("ANALYZE operator execution is aborting...");
			analyzeReport.setErrorStatus(true);
			analyzeReport.setErrorMessage("ANALYZE incoming expression contains syntax errors!Please check.");
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			analyzeReport.createTextReportFile();
		}else if(!cubeQueryGenerationStatus){
			System.err.println("ANALYZE expression encountered errors!\nANALYZE operator execution is aborting...");
			analyzeReport.setErrorStatus(true);
			analyzeReport.setErrorMessage("Expressions or values of the given ANALYZE incoming expression are invalid!Please check.");
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			analyzeReport.createTextReportFile();
		}else if(translationStatus && cubeQueryGenerationStatus) {
			analyzeReport.setErrorStatus(false);
			for(AnalyzeQuery aq: analyzeQueries) {
				//long startTime = System.nanoTime();
				CubeQuery analyzeCubeQuery = aq.getAnalyzeCubeQuery();
				Result result = cubeManager.executeQuery(analyzeCubeQuery);
				String[][] resultArray = result.getResultArray();
				System.out.println(aq.getType());
				if(resultArray!=null) {
					// first 2 rows contain column names!
					resultTuplesCounter += resultArray.length - 2;
				}

				if(aq.getType() == AnalyzeQuery.TypeOfAnalyzeQuery.Base){
					outputFileName = "Analyze Highlights Report-" + aq.getAnalyzeCubeQuery().getName();
				}

				aq.setAnalyzeQueryResult(result);
				OperatorResult opResult = new OperatorResult(analyzeCubeQuery,result,null);
				analyzeMinMQOResults.add(opResult);
			}
			analyzeReport.setAnalyzeQueries(analyzeQueries);
			analyzeReport.createTextReportFile();
		}
		return analyzeMinMQOResults;
	}

	/**
	 * Extract highlights from the analyze query results using the registered Archetype Properties.
	 * The results are written in a markdown file.
	 * @return ResultFileMetadata
	 */
	public ResultFileMetadata executeAnalyzeWithMinMQO() {
		ResultFileMetadata resultFile = new ResultFileMetadata();
		resultFile.setLocalFolder(analyzeReport.getLocalFolder());
		resultFile.setResultFile(analyzeReport.getReportFile());
		CubeSchemaResolver schemaResolver = CubeSchemaResolver.from(cubeManager);
		if(analyzeReport.getErrorStatus()) {
			resultFile.setErrorCheckingStatus(analyzeReport.getErrorMessage());
		}
		try {
			ArrayList<OperatorResult> results = executeMinMQOQueries();
			analyzeReport.clearHighlightsReport(outputFileName);
			for (OperatorResult opResult: results) {
				HighlightSet highlights = new HighlightExtractor().extract(opResult, registeredArchetypes, schemaResolver);
				resultFile.setResultFile(AnalyzeReport.writeHighlightsReport(opResult.query.toString(), opResult, highlights, outputFileName));
			}
		} catch (RuntimeException e) {
			resultFile.setErrorCheckingStatus(e.toString());
		}
		return resultFile;
	}

	public ArrayList<AnalyzeQuery> getAnalyzeQueries(){
		return analyzeQueries;
	}

	// NULL until further notice
	@Override
	public OperatorResult toOperatorResult() {
		return null;
	}
}
