package LLM.experiments.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import LLM.experiments.ExpectedCubeQuery;
import LLM.experiments.LLMExperimentCase;

public class ExperimentCaseOrdererTest {

    @Test
    public void testCreatePairedOrderCreatesCorrectPairs() {
        ExpectedCubeQuery query1 = createExpectedQuery();
        ExpectedCubeQuery query2 = createExpectedQuery();

        LLMExperimentCase s1 = createCase(
                "s1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query1,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase n1 = createCase(
                "n1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query1,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase s2 = createCase(
                "s2",
                "pair2",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query2,
                LLMExperimentCase.DIFFICULTY_MEDIUM
        );

        LLMExperimentCase n2 = createCase(
                "n2",
                "pair2",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query2,
                LLMExperimentCase.DIFFICULTY_MEDIUM
        );

        List<LLMExperimentCase> input = Arrays.asList(
                s1,
                n1,
                s2,
                n2
        );

        List<LLMExperimentCase> result =
                ExperimentCaseOrderer.createPairedOrder(
                        input,
                        42L
                );

        assertEquals(4, result.size());

        assertSame(
                result.get(0).getExpectedQuery(),
                result.get(1).getExpectedQuery()
        );

        assertSame(
                result.get(2).getExpectedQuery(),
                result.get(3).getExpectedQuery()
        );

        assertTrue(
                result.get(0).getPairId()
                        .equals(result.get(1).getPairId())
        );

        assertTrue(
                result.get(2).getPairId()
                        .equals(result.get(3).getPairId())
        );
    }

    @Test
    public void testCreatePairedOrderIsDeterministic() {
        ExpectedCubeQuery query1 = createExpectedQuery();
        ExpectedCubeQuery query2 = createExpectedQuery();

        List<LLMExperimentCase> input =
                createValidCases(query1, query2);

        List<LLMExperimentCase> result1 =
                ExperimentCaseOrderer.createPairedOrder(
                        input,
                        42L
                );

        List<LLMExperimentCase> result2 =
                ExperimentCaseOrderer.createPairedOrder(
                        input,
                        42L
                );

        assertEquals(
                result1.size(),
                result2.size()
        );

        for (int i = 0; i < result1.size(); i++) {
            assertEquals(
                    result1.get(i).getTestId(),
                    result2.get(i).getTestId()
            );
        }
    }

    @Test
    public void testCreatePairedOrderDoesNotModifyInput() {
        ExpectedCubeQuery query1 = createExpectedQuery();
        ExpectedCubeQuery query2 = createExpectedQuery();

        List<LLMExperimentCase> input =
                createValidCases(query1, query2);

        List<LLMExperimentCase> original =
                new ArrayList<LLMExperimentCase>(input);

        List<LLMExperimentCase> result =
                ExperimentCaseOrderer.createPairedOrder(
                        input,
                        42L
                );

        assertEquals(
                original,
                input
        );

        assertNotSame(
                input,
                result
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateTestIdIsRejected() {
        ExpectedCubeQuery query = createExpectedQuery();

        LLMExperimentCase structured = createCase(
                "same",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase natural = createCase(
                "same",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        ExperimentCaseOrderer.createPairedOrder(
                Arrays.asList(structured, natural),
                42L
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingStructuredCaseIsRejected() {
        ExpectedCubeQuery query = createExpectedQuery();

        LLMExperimentCase natural = createCase(
                "n1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        ExperimentCaseOrderer.createPairedOrder(
                Arrays.asList(natural),
                42L
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingNaturalCaseIsRejected() {
        ExpectedCubeQuery query = createExpectedQuery();

        LLMExperimentCase structured = createCase(
                "s1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        ExperimentCaseOrderer.createPairedOrder(
                Arrays.asList(structured),
                42L
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentExpectedQueriesAreRejected() {
        ExpectedCubeQuery query1 = createExpectedQuery();
        ExpectedCubeQuery query2 = createExpectedQuery();

        LLMExperimentCase structured = createCase(
                "s1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query1,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase natural = createCase(
                "n1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query2,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        ExperimentCaseOrderer.createPairedOrder(
                Arrays.asList(structured, natural),
                42L
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDifferentDifficultiesAreRejected() {
        ExpectedCubeQuery query = createExpectedQuery();

        LLMExperimentCase structured = createCase(
                "s1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase natural = createCase(
                "n1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query,
                LLMExperimentCase.DIFFICULTY_COMPLEX
        );

        ExperimentCaseOrderer.createPairedOrder(
                Arrays.asList(structured, natural),
                42L
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidWordingTypeIsRejected() {
        ExpectedCubeQuery query = createExpectedQuery();

        LLMExperimentCase invalid = new LLMExperimentCase(
                "test1",
                "question",
                query,
                "",
                LLMExperimentCase.DIFFICULTY_SIMPLE,
                "",
                "pair1",
                "invalid"
        );

        ExperimentCaseOrderer.createPairedOrder(
                Arrays.asList(invalid),
                42L
        );
    }

    private static List<LLMExperimentCase> createValidCases(
            ExpectedCubeQuery query1,
            ExpectedCubeQuery query2
    ) {
        LLMExperimentCase structured1 = createCase(
                "s1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query1,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase natural1 = createCase(
                "n1",
                "pair1",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query1,
                LLMExperimentCase.DIFFICULTY_SIMPLE
        );

        LLMExperimentCase structured2 = createCase(
                "s2",
                "pair2",
                LLMExperimentCase.WORDING_TYPE_STRUCTURED,
                query2,
                LLMExperimentCase.DIFFICULTY_MEDIUM
        );

        LLMExperimentCase natural2 = createCase(
                "n2",
                "pair2",
                LLMExperimentCase.WORDING_TYPE_NATURAL,
                query2,
                LLMExperimentCase.DIFFICULTY_MEDIUM
        );

        return Arrays.asList(
                structured1,
                natural1,
                structured2,
                natural2
        );
    }

    private static LLMExperimentCase createCase(
            String testId,
            String pairId,
            String wordingType,
            ExpectedCubeQuery expectedQuery,
            String difficulty
    ) {
        return new LLMExperimentCase(
                testId,
                "Test question " + testId,
                expectedQuery,
                "",
                difficulty,
                "",
                pairId,
                wordingType
        );
    }

    private static ExpectedCubeQuery createExpectedQuery() {
        return new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Arrays.asList(
                        "product_dim.product_category"
                ),
                Arrays.asList(
                        "date_dim.the_year='1997'"
                )
        );
    }
}