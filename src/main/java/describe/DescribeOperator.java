package describe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import describe.report.DescribeReport;
import describe.syntax.DescribeParserManager;
import mainengine.ModelManager;
import model.abstracts.AbstractModel;
import result.Result;
import result.ResultFileMetadata;
import result.highlights.CubeSchemaResolver;
import result.highlights.IntentionalOperator;
import result.highlights.OperatorResult;
import result.highlights.archetypes.MegaContributorArchetype;
import result.highlights.metamodel.ArchetypeProperty;

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
    private List<AbstractModel> launchedModels = new ArrayList<>();
    
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
                
                //Apply models after getting the results
                DescribeParams params = parserManager.getParams();
                if(params.getModelList() != null && !params.getModelList().isEmpty()) {
                    resultFile = applyModels(resultFile, result, params.getModelList());
                }

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
    
    private ResultFileMetadata applyModels(ResultFileMetadata resMetadata, Result data, ArrayList<String> models) {
        String[] modelArray = models.toArray(new String[0]);
        
        ModelManager modelManager = new ModelManager(data);
        
        ArrayList<AbstractModel> launchedModels = modelManager.selectModelsToLaunch(modelArray);

        int modelGenFlag = modelManager.executeModelConstruction("Describe Query");

        if (modelGenFlag == 0) {
            modelManager.addComponentsToResultMetadata(resMetadata);

            if (launchedModels != null) {
                this.launchedModels = launchedModels;
                for (AbstractModel model : launchedModels) {
                    String[][] output = model.printAs2DStringArray();
                    this.describeQuery.addModelOutput(output);
                }
            }
        }
        return resMetadata;
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
        return new OperatorResult(cq, result, launchedModels);
    }

    @Override
    public List<ArchetypeProperty> registeredArchetypes() {
        return Collections.singletonList(MegaContributorArchetype.create());
    }
}