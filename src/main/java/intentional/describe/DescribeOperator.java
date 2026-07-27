package intentional.describe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.describe.models.KMeansLabelingModel;
import intentional.describe.models.KPIMedianLabelingModel;
import intentional.describe.syntax.DescribeParserManager;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import intentional.result.LabelingModel;
import result.Result;

/**
 * A class for the intentional operator Describe
 * @author Nik-Pt
 */
public class DescribeOperator implements IntentionalOperator {

    private CubeManager cubeManager;
    private DescribeTranslationManager translationManager;
    private DescribeParserManager parserManager;
    private Result result;
    private DescribeQuery describeQuery;
    private List<LabelingModel> models = new ArrayList<>();

    public DescribeOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
        this.translationManager = new DescribeTranslationManager(cubeManager);
        this.parserManager = new DescribeParserManager();
    }

    /**
     * Validates and translates the DESCRIBE query, runs it, builds the requested labeling models, and
     * returns the result as a single-element list. Throws on syntax or translation errors.
     */
    @Override
    public List<LabeledResult> execute(String queryString) {
        System.out.println("Processing DESCRIBE: " + queryString);

        boolean incomingExpressionIsValid = false;
        try {
            incomingExpressionIsValid = (parserManager.parse(queryString) == 0);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
        if (!incomingExpressionIsValid) {
            throw new RuntimeException("Syntax Errors found in Describe expression.");
        }

        DescribeParams params = parserManager.getParams();
        CubeQuery cubeQuery;
        try {
            cubeQuery = translationManager.translateDescribeToCubeQuery(params);
        } catch (Exception e) {
            throw new RuntimeException("Translation Error: " + e.getMessage(), e);
        }

        this.describeQuery = new DescribeQuery();
        this.describeQuery.setCubeQuery(cubeQuery);

        this.result = cubeManager.executeQuery(cubeQuery);
        this.models = buildLabelingModels(result, params.getModelList());
        this.describeQuery.setDescribeQueryResult(result);

        return Collections.singletonList(new LabeledResult(cubeQuery, result, this.models));
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

    public DescribeQuery getDescribeQuery() {
        return describeQuery;
    }

    public Result getExecutionResult() {
        return this.result;
    }

}
