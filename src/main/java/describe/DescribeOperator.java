package describe;

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import describe.models.KMeansLabelingModel;
import describe.models.KPIMedianLabelingModel;
import describe.report.DescribeReport;
import describe.syntax.DescribeParserManager;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import intentionaloperator.IntentionalOperator;
import intentionaloperator.OperatorResult;
import labeling.LabelingModel;
import result.Result;
import result.ResultFileMetadata;

/**
 * A class for the intentional operator Describe
 * @author Nik-Pt
 */
public class DescribeOperator implements IntentionalOperator {

    private CubeManager cubeManager;
    private DescribeTranslationManager translationManager;
    private DescribeParserManager parserManager;
	private DescribeReport describeReport;
    private Result result;
    private DescribeQuery describeQuery;
    private List<LabelingModel> models = new ArrayList<>();
    private HighlightSet highlights;
    
    public DescribeOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
        this.translationManager = new DescribeTranslationManager(cubeManager);
        this.parserManager = new DescribeParserManager();
    }

    /**
     * Auxiliary method that checks the syntax, constructs the CubeQuery and executes it
     * @return True if successful, False otherwise
     * @throws RecognitionException 
     */
    public ResultFileMetadata execute(String queryString) throws RecognitionException {
    	
        //Initialize Report
        this.describeReport = new DescribeReport(queryString, "RDBMS"); 
        ResultFileMetadata resultFile = new ResultFileMetadata();
        int resultTuplesCounter = 0;

        System.out.println("Processing DESCRIBE: " + queryString);

        //Validation of the incoming Describe query and parsing
        boolean incomingExpressionIsValid = (parserManager.parse(queryString) == 0);
        
        if (!incomingExpressionIsValid) {
            System.err.println("DESCRIBE incoming expression contains syntax errors! Aborting...");
            describeReport.setErrorStatus(true);
            describeReport.setErrorMessage("Syntax Errors found in Describe expression.");
            describeReport.createTextReportFile();
            
            resultFile.setErrorCheckingStatus("Syntax Errors");
            resultFile.setResultFile(describeReport.getReportFile());
            resultFile.setLocalFolder(describeReport.getLocalFolder());
            return resultFile;
        }
        

        //Translation of said query into a Describe query
        try {
            long startTime = System.nanoTime();
            
            DescribeParams params = parserManager.getParams();
            CubeQuery cubeQuery = translationManager.translateDescribeToCubeQuery(params);
            
            this.describeQuery = new DescribeQuery();
            this.describeQuery.setCubeQuery(cubeQuery);
            
            long endTime = System.nanoTime();
            System.out.println("Describe Cube Query Generation Time: " + (endTime - startTime) / 1000000.0 + " ms");
            
        } catch (Exception e) {
            System.err.println("Translation failed: " + e.getMessage());
            describeReport.setErrorStatus(true);
            describeReport.setErrorMessage("Translation Error: " + e.getMessage());
            describeReport.createTextReportFile();
            resultFile.setErrorCheckingStatus("Translation Error");
            return resultFile;
        }

        //Execution of the newly translated query
        if (this.describeQuery != null && this.describeQuery.getCubeQuery() != null) {
            describeReport.setErrorStatus(false);
            
            try {
                long startTime = System.nanoTime();
                
                CubeQuery cq = describeQuery.getCubeQuery();
                Result result = cubeManager.executeQuery(cq);
                this.result = result;
                
                //Build the labeling models the query requested and extract highlights over the result
                DescribeParams params = parserManager.getParams();
                this.models = buildLabelingModels(result, params.getModelList());
                this.highlights = extractHighlights(cq, result, this.models);

                //Process the Result object that came from the execution
                String[][] resultArray = (result != null) ? result.getResultArray() : null;
                if (resultArray != null) {
                    resultTuplesCounter += resultArray.length;
                }

                describeQuery.setDescribeQueryResult(result);
                
                long endTime = System.nanoTime();
                System.out.println("Queries Execution Time: " + (endTime - startTime) / 1000000.0 + " ms");
                
                startTime = System.nanoTime();
                describeReport.setDescribeQuery(describeQuery);
                describeReport.setHighlights(highlights);
                describeReport.createTextReportFile();
                endTime = System.nanoTime();
                System.out.println("Reporting Result Time: " + (endTime - startTime) / 1000000.0 + " ms");
                
                if(result != null) {
                	resultFile.setLocalFolder(describeReport.getLocalFolder());
            		resultFile.setResultFile(describeReport.getReportFile());
                }

            } catch (Exception e) {
                e.printStackTrace();
                resultFile.setErrorCheckingStatus("Execution Exception: " + e.getMessage());
            }
        }

        resultFile.setLocalFolder(describeReport.getLocalFolder());
        resultFile.setResultFile(describeReport.getReportFile());
        if (describeReport.getErrorStatus()) {
            resultFile.setErrorCheckingStatus(describeReport.getErrorMessage());
        }
        
        System.out.println("Number of resulted tuples: " + resultTuplesCounter);
        return resultFile;
    }
    
    /** Instantiates the {@link LabelingModel}s named by the query's USING clause and computes each over the result. */
    private List<LabelingModel> buildLabelingModels(Result data, List<String> modelNames) {
        List<LabelingModel> built = new ArrayList<>();
        if (modelNames == null) {
            return built;
        }
        for (String modelName : modelNames) {
            if (KPIMedianLabelingModel.NAME.equals(modelName)) {
                KPIMedianLabelingModel model = new KPIMedianLabelingModel(data);
                if (model.compute() == 0) {
                    built.add(model);
                }
            } else if (KMeansLabelingModel.NAME.equals(modelName)) {
                KMeansLabelingModel model = new KMeansLabelingModel(data);
                if (model.compute() == 0) {
                    built.add(model);
                }
            }
        }
        return built;
    }

    /** Runs the registered archetypes over the operator result and returns the highlights they produce. */
    private HighlightSet extractHighlights(CubeQuery cubeQuery, Result data, List<LabelingModel> labelingModels) {
        OperatorResult operatorResult = new OperatorResult(cubeQuery, data, labelingModels);
        try {
            CubeSchemaResolver schema = CubeSchemaResolver.from(cubeManager);
            return new HighlightExtractor().extract(operatorResult, registeredArchetypes(), schema);
        } catch (Exception e) {
            System.err.println("DESCRIBE highlight extraction failed: " + e.getMessage());
            return HighlightSet.empty();
        }
    }

    public DescribeQuery getDescribeQuery() {
        return describeQuery;
    }

    public Result getExecutionResult() {
        return this.result;
    }

    @Override
    public OperatorResult toOperatorResult() {
        CubeQuery cq = describeQuery == null ? null : describeQuery.getCubeQuery();
        return new OperatorResult(cq, result, models);
    }

    // registeredArchetypes() inherits the default set from IntentionalOperator.
}