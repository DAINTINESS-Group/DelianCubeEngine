package LLM.experiments.similarity;

import LLM.experiments.ExpectedCubeQuery;
import LLM.query.ParsedCubeQuery;
import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class QuerySimilarityEvaluatorTest {

    @Test
    public void testIdenticalQueriesHaveFullSimilarity() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        ExpectedCubeQuery expected = new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Arrays.asList(
                        "date_dim.year_quarter"
                ),
                Arrays.asList(
                        "date_dim.the_year='1997'"
                )
        );

        ParsedCubeQuery actual = new ParsedCubeQuery(
                "sales_cube",
                "q1",
                "Sum",
                "store_sales",
                Arrays.asList(
                        "date_dim.year_quarter"
                ),
                Arrays.asList(
                        "date_dim.the_year='1997'"
                )
        );

        QuerySimilarityResult result =
                evaluator.evaluate(expected, actual);

        assertTrue(result.isAvailable());
        assertEquals(1.0, result.getCubeSimilarity(), 0.0);
        assertEquals(1.0, result.getAggregateSimilarity(), 0.0);
        assertEquals(1.0, result.getMeasureSimilarity(), 0.0);
        assertEquals(1.0, result.getGammaSimilarity(), 0.0);
        assertEquals(1.0, result.getSigmaSimilarity(), 0.0);
        assertEquals(1.0, result.getOverallSimilarity(), 0.0);
    }

    @Test
    public void testAggregateFunctionIsCaseInsensitive() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        ExpectedCubeQuery expected = new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Collections.<String>emptyList(),
                Collections.<String>emptyList()
        );

        ParsedCubeQuery actual = new ParsedCubeQuery(
                "sales_cube",
                "q1",
                "sum",
                "store_sales",
                Collections.<String>emptyList(),
                Collections.<String>emptyList()
        );

        QuerySimilarityResult result =
                evaluator.evaluate(expected, actual);

        assertEquals(
                1.0,
                result.getAggregateSimilarity(),
                0.0
        );
    }

    @Test
    public void testDifferentCubeAndMeasureHaveZeroSimilarity() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        ExpectedCubeQuery expected = new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Collections.<String>emptyList(),
                Collections.<String>emptyList()
        );

        ParsedCubeQuery actual = new ParsedCubeQuery(
                "other_cube",
                "q1",
                "Sum",
                "store_cost",
                Collections.<String>emptyList(),
                Collections.<String>emptyList()
        );

        QuerySimilarityResult result =
                evaluator.evaluate(expected, actual);

        assertEquals(0.0, result.getCubeSimilarity(), 0.0);
        assertEquals(0.0, result.getMeasureSimilarity(), 0.0);
    }

    @Test
    public void testGammaFieldsAreMatchedIndependentlyOfOrder() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        ExpectedCubeQuery expected = new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Arrays.asList(
                        "date_dim.year_quarter",
                        "date_dim.year_and_month"
                ),
                Collections.<String>emptyList()
        );

        ParsedCubeQuery actual = new ParsedCubeQuery(
                "sales_cube",
                "q1",
                "Sum",
                "store_sales",
                Arrays.asList(
                        "date_dim.year_and_month",
                        "date_dim.year_quarter"
                ),
                Collections.<String>emptyList()
        );

        QuerySimilarityResult result =
                evaluator.evaluate(expected, actual);

        assertEquals(
                1.0,
                result.getGammaSimilarity(),
                0.0
        );
    }

    @Test
    public void testSigmaConditionsAreMatchedIndependentlyOfOrder() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        ExpectedCubeQuery expected = new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Collections.<String>emptyList(),
                Arrays.asList(
                        "date_dim.the_year='1997'",
                        "date_dim.year_quarter='1997-Q3'"
                )
        );

        ParsedCubeQuery actual = new ParsedCubeQuery(
                "sales_cube",
                "q1",
                "Sum",
                "store_sales",
                Collections.<String>emptyList(),
                Arrays.asList(
                        "date_dim.year_quarter='1997-Q3'",
                        "date_dim.the_year='1997'"
                )
        );

        QuerySimilarityResult result =
                evaluator.evaluate(expected, actual);

        assertEquals(
                1.0,
                result.getSigmaSimilarity(),
                0.0
        );
    }

    @Test
    public void testDifferentSigmaValueGetsPartialSimilarity() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        ExpectedCubeQuery expected = new ExpectedCubeQuery(
                "sales_cube",
                "Sum",
                "store_sales",
                Collections.<String>emptyList(),
                Arrays.asList(
                        "date_dim.the_year='1997'"
                )
        );

        ParsedCubeQuery actual = new ParsedCubeQuery(
                "sales_cube",
                "q1",
                "Sum",
                "store_sales",
                Collections.<String>emptyList(),
                Arrays.asList(
                        "date_dim.the_year='1998'"
                )
        );

        QuerySimilarityResult result =
                evaluator.evaluate(expected, actual);

        assertTrue(
                result.getSigmaSimilarity() > 0.0
        );

        assertTrue(
                result.getSigmaSimilarity() < 1.0
        );
    }

    @Test
    public void testNullQueriesReturnUnavailableResult() {
        CubeSchema schema = createSchema();
        QuerySimilarityEvaluator evaluator =
                new QuerySimilarityEvaluator(
                        new HierarchyMetadataIndex(schema)
                );

        QuerySimilarityResult result =
                evaluator.evaluate(null, null);

        assertFalse(result.isAvailable());
        assertEquals(
                0.0,
                result.getOverallSimilarity(),
                0.0
        );
    }

    private CubeSchema createSchema() {
        CubeSchema schema = new CubeSchema();

        DimensionSchema dateDimension =
                new DimensionSchema("date_dim");

        LevelSchema yearLevel = new LevelSchema();
        yearLevel.setLevelName("the_year");
        yearLevel.addAttribute(
                new LevelAttributeSchema(
                        "the_year",
                        "String",
                        "date_dim.the_year"
                )
        );

        LevelSchema quarterLevel = new LevelSchema();
        quarterLevel.setLevelName("year_quarter");
        quarterLevel.addAttribute(
                new LevelAttributeSchema(
                        "year_quarter",
                        "String",
                        "date_dim.year_quarter"
                )
        );

        LevelSchema monthLevel = new LevelSchema();
        monthLevel.setLevelName("year_and_month");
        monthLevel.addAttribute(
                new LevelAttributeSchema(
                        "year_and_month",
                        "String",
                        "date_dim.year_and_month"
                )
        );

        dateDimension.addLevel(yearLevel);
        dateDimension.addLevel(quarterLevel);
        dateDimension.addLevel(monthLevel);

        dateDimension.addHierarchyLevel("the_year");
        dateDimension.addHierarchyLevel("year_quarter");
        dateDimension.addHierarchyLevel("year_and_month");

        schema.addDimension(dateDimension);

        return schema;
    }
}