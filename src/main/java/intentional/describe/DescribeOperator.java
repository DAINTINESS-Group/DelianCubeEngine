package intentional.describe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.describe.syntax.DescribeParserManager;
import intentional.labeling.LabelingScheme;
import intentional.labeling.MeasureLabelingModel;
import intentional.labeling.schemes.EquiDepthScheme;
import intentional.labeling.schemes.EquiWidthScheme;
import intentional.labeling.schemes.KMeansScheme;
import intentional.labeling.schemes.MedianDistanceScheme;
import intentional.labeling.schemes.ZScoreLabelingScheme;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import intentional.labeling.LabelingModel;
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

    /** Instantiates a labeling model per scheme named by the query's USING clause and computes each over the result. */
    private List<LabelingModel> buildLabelingModels(Result data, List<String> modelNames) {
        List<LabelingModel> built = new ArrayList<>();
        if (modelNames == null) {
            return built;
        }
        for (String modelName : modelNames) {
            LabelingScheme scheme = schemeFor(modelName);
            if (scheme == null) {
                continue;
            }
            MeasureLabelingModel model = new MeasureLabelingModel(data, scheme);
            if (model.compute() == 0) {
                built.add(model);
            }
        }
        return built;
    }

    /** The scheme a USING-clause name selects, or null if the name matches none. */
    private static LabelingScheme schemeFor(String modelName) {
        if (MedianDistanceScheme.NAME.equals(modelName)) return new MedianDistanceScheme();
        if (KMeansScheme.NAME.equals(modelName)) return new KMeansScheme();
        if (ZScoreLabelingScheme.NAME.equals(modelName)) return new ZScoreLabelingScheme();
        if (EquiDepthScheme.NAME.equals(modelName)) return new EquiDepthScheme();
        if (EquiWidthScheme.NAME.equals(modelName)) return new EquiWidthScheme();
        return null;
    }

    public DescribeQuery getDescribeQuery() {
        return describeQuery;
    }

    public Result getExecutionResult() {
        return this.result;
    }

}
