package LLM.parsing;

import LLM.query.ParsedCubeQuery;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class CubeQueryJsonParserTest {

    @Test
    public void testValidJsonResponse() {
        String json =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCategory\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[\"product_dim.product_category\"],"
                        + "\"sigma\":[\"date_dim.the_year='1997'\"]"
                        + "}";

        CubeQueryParseResult result =
                CubeQueryJsonParser.parse(json);

        assertTrue(result.isValidJson());
        assertTrue(result.isFormatValid());
        assertFalse(result.hasErrors());

        ParsedCubeQuery query =
                result.getParsedQuery();

        assertEquals(
                "sales_cube",
                query.getCubeName()
        );

        assertEquals(
                "SalesByCategory",
                query.getQueryName()
        );

        assertEquals(
                "Sum",
                query.getAggregateFunction()
        );

        assertEquals(
                "store_sales",
                query.getMeasure()
        );

        assertEquals(
                Arrays.asList("product_dim.product_category"),
                query.getGammaFields()
        );

        assertEquals(
                Arrays.asList("date_dim.the_year='1997'"),
                query.getSigmaConditions()
        );
    }

    @Test
    public void testJsonResponseWithMarkdownCodeFence() {
        String json =
                "```json\n"
                        + "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCategory\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[\"product_dim.product_category\"],"
                        + "\"sigma\":[]"
                        + "}\n"
                        + "```";

        CubeQueryParseResult result =
                CubeQueryJsonParser.parse(json);

        assertTrue(result.isValidJson());
        assertTrue(result.isFormatValid());
        assertFalse(result.hasErrors());

        ParsedCubeQuery query =
                result.getParsedQuery();

        assertEquals("sales_cube", query.getCubeName());
        assertEquals("Sum", query.getAggregateFunction());
        assertEquals("store_sales", query.getMeasure());

        assertEquals(
                Arrays.asList("product_dim.product_category"),
                query.getGammaFields()
        );

        assertEquals(
                Collections.emptyList(),
                query.getSigmaConditions()
        );
    }

    @Test
    public void testMissingRequiredFieldMakesFormatInvalid() {
        String json =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCategory\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[]"
                        + "}";

        CubeQueryParseResult result =
                CubeQueryJsonParser.parse(json);

        assertTrue(result.isValidJson());
        assertFalse(result.isFormatValid());
        assertTrue(result.hasErrors());

        assertTrue(
                result.getErrors().toString()
                        .contains("measure")
        );
    }

    @Test
    public void testUnexpectedFieldMakesFormatInvalid() {
        String json =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCategory\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[],"
                        + "\"extraField\":\"unexpected\""
                        + "}";

        CubeQueryParseResult result =
                CubeQueryJsonParser.parse(json);

        assertTrue(result.isValidJson());
        assertFalse(result.isFormatValid());
        assertTrue(result.hasErrors());

        assertTrue(
                result.getErrors().toString()
                        .contains("extraField")
        );
    }

    @Test
    public void testGammaAndSigmaMustBeArrays() {
        String json =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCategory\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":\"product_dim.product_category\","
                        + "\"sigma\":\"date_dim.the_year='1997'\""
                        + "}";

        CubeQueryParseResult result =
                CubeQueryJsonParser.parse(json);

        assertTrue(result.isValidJson());
        assertFalse(result.isFormatValid());
        assertTrue(result.hasErrors());

        assertTrue(
                result.getErrors().toString()
                        .contains("gamma")
        );

        assertTrue(
                result.getErrors().toString()
                        .contains("sigma")
        );
    }

    @Test
    public void testInvalidJsonResponse() {
        String json =
                "{"
                        + "\"cubeName\":\"sales_cube\","
                        + "\"queryName\":\"SalesByCategory\","
                        + "\"aggregateFunction\":\"Sum\","
                        + "\"measure\":\"store_sales\","
                        + "\"gamma\":[],"
                        + "\"sigma\":[]";

        CubeQueryParseResult result =
                CubeQueryJsonParser.parse(json);

        assertFalse(result.isValidJson());
        assertFalse(result.isFormatValid());
        assertTrue(result.hasErrors());
    }

    @Test
    public void testNullOrEmptyResponse() {
        CubeQueryParseResult nullResult =
                CubeQueryJsonParser.parse(null);

        assertFalse(nullResult.isValidJson());
        assertFalse(nullResult.isFormatValid());
        assertTrue(nullResult.hasErrors());

        CubeQueryParseResult emptyResult =
                CubeQueryJsonParser.parse("   ");

        assertFalse(emptyResult.isValidJson());
        assertFalse(emptyResult.isFormatValid());
        assertTrue(emptyResult.hasErrors());

        assertEquals(
                "",
                emptyResult.getParsedQuery().getCubeName()
        );

        assertEquals(
                Collections.emptyList(),
                emptyResult.getParsedQuery().getGammaFields()
        );

        assertEquals(
                Collections.emptyList(),
                emptyResult.getParsedQuery().getSigmaConditions()
        );
    }
}