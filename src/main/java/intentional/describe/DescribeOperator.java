package intentional.describe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.describe.syntax.DescribeParserManager;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import intentional.labeling.schemes.EquiDepthScheme;
import intentional.labeling.schemes.EquiWidthScheme;
import intentional.labeling.schemes.KMeansScheme;
import intentional.labeling.schemes.MedianDistanceScheme;
import intentional.labeling.schemes.ZScoreLabelingScheme;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import result.Cell;
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

    public DescribeOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
        this.translationManager = new DescribeTranslationManager(cubeManager);
        this.parserManager = new DescribeParserManager();
    }

    /**
     * Validates and translates the DESCRIBE query, runs it, labels the studied measure under the requested
     * schemes, and returns the result as a single-element list. Throws on syntax or translation errors.
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
        this.describeQuery.setDescribeQueryResult(result);

        return Collections.singletonList(
                new LabeledResult(cubeQuery, result, buildLabelings(result, params.getModelList())));
    }

    /** Labels the studied measure under each scheme named by the query's USING clause. */
    private List<Labeling> buildLabelings(Result data, List<String> modelNames) {
        List<Labeling> built = new ArrayList<>();
        if (modelNames == null || data.getCells().isEmpty()) {
            return built;
        }
        for (String modelName : modelNames) {
            LabelingScheme scheme = schemeFor(modelName);
            if (scheme == null) {
                continue;
            }
            Map<Cell, Double> valueByCell = new LinkedHashMap<>();
            for (Cell cell : data.getCells()) {
                valueByCell.put(cell, cell.toDouble(0));
            }
            built.add(new Labeling(scheme, valueByCell, 0));
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
