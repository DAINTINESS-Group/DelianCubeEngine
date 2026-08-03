package intentional.describe;

import java.util.Collections;
import java.util.List;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import intentional.describe.syntax.DescribeParserManager;
import intentional.labeling.Labeling;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
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
     * Validates and translates the DESCRIBE query, runs it, and returns the result as a single-element
     * list. Throws on syntax or translation errors.
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
                new LabeledResult(cubeQuery, result, Collections.<Labeling>emptyList()));
    }

    public DescribeQuery getDescribeQuery() {
        return describeQuery;
    }

    public Result getExecutionResult() {
        return this.result;
    }

}
