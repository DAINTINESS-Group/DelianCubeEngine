package describe;

import java.util.ArrayList;
import java.util.Collections;
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
     * Stage-1 producer: validates and translates the DESCRIBE query, runs it, builds the requested
     * labeling models, and returns the result as a single-element list. Throws on syntax/translation
     * errors so the file path ({@link #executeToReport}) can surface them.
     */
    @Override
    public List<OperatorResult> execute(String queryString) {
        System.out.println("Processing DESCRIBE: " + queryString);

        //Validation of the incoming Describe query and parsing
        boolean incomingExpressionIsValid = false;
        try {
            incomingExpressionIsValid = (parserManager.parse(queryString) == 0);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
        if (!incomingExpressionIsValid) {
            throw new RuntimeException("Syntax Errors found in Describe expression.");
        }

        //Translation of said query into a Describe query
        DescribeParams params = parserManager.getParams();
        CubeQuery cubeQuery;
        try {
            cubeQuery = translationManager.translateDescribeToCubeQuery(params);
        } catch (Exception e) {
            throw new RuntimeException("Translation Error: " + e.getMessage(), e);
        }

        this.describeQuery = new DescribeQuery();
        this.describeQuery.setCubeQuery(cubeQuery);

        //Execution of the newly translated query and model building
        Result result = cubeManager.executeQuery(cubeQuery);
        this.result = result;
        this.models = buildLabelingModels(result, params.getModelList());
        this.describeQuery.setDescribeQueryResult(result);

        return Collections.singletonList(new OperatorResult(cubeQuery, result, this.models));
    }

    /**
     * Legacy file path: runs {@link #execute(String)}, extracts highlights over the result, and writes
     * the DESCRIBE text report, returning the {@link ResultFileMetadata} pointing at it.
     */
    @Override
    public ResultFileMetadata executeToReport(String queryString) {
        this.describeReport = new DescribeReport(queryString, "RDBMS");
        ResultFileMetadata resultFile = new ResultFileMetadata();

        OperatorResult operatorResult;
        try {
            operatorResult = execute(queryString).get(0);
        } catch (RuntimeException e) {
            System.err.println("DESCRIBE aborting: " + e.getMessage());
            describeReport.setErrorStatus(true);
            describeReport.setErrorMessage(e.getMessage());
            describeReport.createTextReportFile();
            resultFile.setErrorCheckingStatus(e.getMessage());
            resultFile.setResultFile(describeReport.getReportFile());
            resultFile.setLocalFolder(describeReport.getLocalFolder());
            return resultFile;
        }

        describeReport.setErrorStatus(false);
        this.highlights = extractHighlights(operatorResult);

        String[][] resultArray = (operatorResult.data != null) ? operatorResult.data.getResultArray() : null;
        int resultTuplesCounter = (resultArray != null) ? resultArray.length : 0;

        describeReport.setDescribeQuery(this.describeQuery);
        describeReport.setHighlights(highlights);
        describeReport.createTextReportFile();

        resultFile.setLocalFolder(describeReport.getLocalFolder());
        resultFile.setResultFile(describeReport.getReportFile());

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
    private HighlightSet extractHighlights(OperatorResult operatorResult) {
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

}