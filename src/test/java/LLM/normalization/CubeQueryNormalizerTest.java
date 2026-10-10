package LLM.normalization;

import LLM.query.ParsedCubeQuery;
import org.junit.Test;

import static org.junit.Assert.*;

public class CubeQueryNormalizerTest {

    @Test
    public void testNormalizeValidJsonQuery() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCountry\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[\"customer_dim.country\"],"
                        + "\"sigma\":[\"date_dim.the_year='1997'\"]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        ParsedCubeQuery query =
                result.getParsedQuery();

        assertEquals("sales_cube", query.getCubeName());
        assertEquals("SalesByCountry", query.getQueryName());
        assertEquals("Sum", query.getAggregateFunction());
        assertEquals("store_sales", query.getMeasure());

        assertEquals(1, query.getGammaFields().size());
        assertEquals(
                "customer_dim.country",
                query.getGammaFields().get(0)
        );

        assertEquals(1, query.getSigmaConditions().size());
        assertEquals(
                "date_dim.the_year='1997'",
                query.getSigmaConditions().get(0)
        );
    }

    @Test
    public void testNormalizeSumSynonyms() {

        String[] inputs = {
                "Sum",
                "sum",
                "SUM",
                "Total",
                "total",
                "Summary",
                "summary"
        };

        for (String input : inputs) {

            String json =
                    "{"
                            + "\"cubeName\":\"sales_cube\","
                            + "\"queryName\":\"q\","
                            + "\"aggregateFunction\":\"" + input + "\","
                            + "\"measure\":\"store_sales\","
                            + "\"gamma\":[],"
                            + "\"sigma\":[]"
                            + "}";

            CubeQueryNormalizer.NormalizationResult result =
                    CubeQueryNormalizer.normalize(json);

            assertTrue(result.isSuccess());
            assertEquals(
                    "Sum",
                    result.getParsedQuery().getAggregateFunction()
            );
        }
    }

    @Test
    public void testNormalizeAverageSynonyms() {

        String[] inputs = {
                "Avg",
                "avg",
                "AVG",
                "Average",
                "average",
                "Mean",
                "mean"
        };

        for (String input : inputs) {

            String json =
                    "{"
                            + "\"cubeName\":\"sales_cube\","
                            + "\"queryName\":\"q\","
                            + "\"aggregateFunction\":\"" + input + "\","
                            + "\"measure\":\"store_sales\","
                            + "\"gamma\":[],"
                            + "\"sigma\":[]"
                            + "}";

            CubeQueryNormalizer.NormalizationResult result =
                    CubeQueryNormalizer.normalize(json);

            assertTrue(result.isSuccess());
            assertEquals(
                    "Avg",
                    result.getParsedQuery().getAggregateFunction()
            );
        }
    }

    @Test
    public void testNormalizeMinimumSynonyms() {

        String[] inputs = {
                "Min",
                "min",
                "Minimum",
                "minimum"
        };

        for (String input : inputs) {

            String json =
                    "{"
                            + "\"cubeName\":\"sales_cube\","
                            + "\"queryName\":\"q\","
                            + "\"aggregateFunction\":\"" + input + "\","
                            + "\"measure\":\"store_sales\","
                            + "\"gamma\":[],"
                            + "\"sigma\":[]"
                            + "}";

            CubeQueryNormalizer.NormalizationResult result =
                    CubeQueryNormalizer.normalize(json);

            assertTrue(result.isSuccess());
            assertEquals(
                    "Min",
                    result.getParsedQuery().getAggregateFunction()
            );
        }
    }

    @Test
    public void testNormalizeMaximumSynonyms() {

        String[] inputs = {
                "Max",
                "max",
                "Maximum",
                "maximum"
        };

        for (String input : inputs) {

            String json =
                    "{"
                            + "\"cubeName\":\"sales_cube\","
                            + "\"queryName\":\"q\","
                            + "\"aggregateFunction\":\"" + input + "\","
                            + "\"measure\":\"store_sales\","
                            + "\"gamma\":[],"
                            + "\"sigma\":[]"
                            + "}";

            CubeQueryNormalizer.NormalizationResult result =
                    CubeQueryNormalizer.normalize(json);

            assertTrue(result.isSuccess());
            assertEquals(
                    "Max",
                    result.getParsedQuery().getAggregateFunction()
            );
        }
    }

    @Test
    public void testNormalizeCount() {

        String[] inputs = {
                "Count",
                "count",
                "COUNT"
        };

        for (String input : inputs) {

            String json =
                    "{"
                            + "\"cubeName\":\"sales_cube\","
                            + "\"queryName\":\"q\","
                            + "\"aggregateFunction\":\"" + input + "\","
                            + "\"measure\":\"store_sales\","
                            + "\"gamma\":[],"
                            + "\"sigma\":[]"
                            + "}";

            CubeQueryNormalizer.NormalizationResult result =
                    CubeQueryNormalizer.normalize(json);

            assertTrue(result.isSuccess());
            assertEquals(
                    "Count",
                    result.getParsedQuery().getAggregateFunction()
            );
        }
    }

    @Test
    public void testNormalizeGammaFields() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":["
                        + "\"customer_dim.country\", "
                        + "\"product_dim.brand_name\""
                        + "],"
                        + "\"sigma\":[]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        assertEquals(2, result.getParsedQuery()
                .getGammaFields().size());

        assertEquals(
                "customer_dim.country",
                result.getParsedQuery()
                        .getGammaFields()
                        .get(0)
        );

        assertEquals(
                "product_dim.brand_name",
                result.getParsedQuery()
                        .getGammaFields()
                        .get(1)
        );
    }

    @Test
    public void testNormalizeSigmaCondition() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[\"customer_dim.country = \\\"USA\\\"\"]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        assertEquals(
                "customer_dim.country='USA'",
                result.getParsedQuery()
                        .getSigmaConditions()
                        .get(0)
        );
    }

    @Test
    public void testNormalizeMultipleSigmaConditions() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":["
                        + "\"customer_dim.country='USA',"
                        + "date_dim.the_year='1997'\""
                        + "]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        assertEquals(
                2,
                result.getParsedQuery()
                        .getSigmaConditions()
                        .size()
        );

        assertEquals(
                "customer_dim.country='USA'",
                result.getParsedQuery()
                        .getSigmaConditions()
                        .get(0)
        );

        assertEquals(
                "date_dim.the_year='1997'",
                result.getParsedQuery()
                        .getSigmaConditions()
                        .get(1)
        );
    }

    @Test
    public void testNormalizeEmptyGammaAndSigma() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        assertTrue(
                result.getParsedQuery()
                        .getGammaFields()
                        .isEmpty()
        );

        assertTrue(
                result.getParsedQuery()
                        .getSigmaConditions()
                        .isEmpty()
        );
    }

    @Test
    public void testNormalizeJsonWithMarkdownFormatting() {

        String input =
                "```json\n"
                        + "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"average\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[]"
                        + "}"
                        + "\n```";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        assertEquals(
                "Avg",
                result.getParsedQuery()
                        .getAggregateFunction()
        );
    }

    @Test
    public void testNormalizeTextFormat() {

        String input =
                "CubeName: sales_cube\n"
                        + "Name: SalesByCountry\n"
                        + "AggrFunc: total\n"
                        + "Measure: store_sales\n"
                        + "Gamma: customer_dim.country\n"
                        + "Sigma: customer_dim.country='USA'";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        ParsedCubeQuery query =
                result.getParsedQuery();

        assertEquals("sales_cube", query.getCubeName());
        assertEquals("SalesByCountry", query.getQueryName());
        assertEquals("Sum", query.getAggregateFunction());
        assertEquals("store_sales", query.getMeasure());

        assertEquals(
                "customer_dim.country",
                query.getGammaFields().get(0)
        );

        assertEquals(
                "customer_dim.country='USA'",
                query.getSigmaConditions().get(0)
        );
    }

    @Test
    public void testNormalizeEmptyInput() {

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize("");

        assertFalse(result.isSuccess());

        assertFalse(result.getErrors().isEmpty());

        assertEquals(
                "LLM answer is empty.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void testNormalizeNullInput() {

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(null);

        assertFalse(result.isSuccess());

        assertFalse(result.getErrors().isEmpty());

        assertEquals(
                "LLM answer is empty.",
                result.getErrors().get(0)
        );
    }

    @Test
    public void testRejectInvalidSigmaCondition() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[\"customer_dim.country\"]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertFalse(result.isSuccess());

        assertFalse(result.getErrors().isEmpty());

        assertTrue(
                result.getErrors()
                        .get(0)
                        .contains("Invalid sigma condition")
        );
    }

    @Test
    public void testCanonicalQueryContainsNormalizedValues() {

        String input =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"q\","
                        + "\"aggregateFunction\":\"summary\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[\"customer_dim.country\"],"
                        + "\"sigma\":[\"date_dim.the_year=\\\"1997\\\"\"]"
                        + "}";

        CubeQueryNormalizer.NormalizationResult result =
                CubeQueryNormalizer.normalize(input);

        assertTrue(result.isSuccess());

        String expected =
                "CubeName:sales_cube\n"
                        + "Name:q\n"
                        + "AggrFunc:Sum\n"
                        + "Measure:store_sales\n"
                        + "Gamma:customer_dim.country\n"
                        + "Sigma:date_dim.the_year='1997'";

        assertEquals(
                expected,
                result.getCanonicalQuery()
        );
    }
}