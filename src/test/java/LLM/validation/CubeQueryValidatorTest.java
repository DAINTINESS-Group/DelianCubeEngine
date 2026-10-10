package LLM.validation;

import LLM.normalization.CubeQueryNormalizer;
import LLM.normalization.CubeQueryNormalizer.NormalizationResult;
import LLM.schema.*;
import LLM.experiments.ExpectedCubeQuery;
import LLM.experiments.ValidationResult;
import LLM.experiments.CubeQueryValidator;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class CubeQueryValidatorTest {

    @Test
    public void testCorrectQueryIsValid() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Arrays.asList(
                                "product_dim.category"
                        ),
                        Arrays.asList(
                                "date_dim.year='1997'"
                        )
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[\"product_dim.category\"],"
                                + "\"sigma\":[\"date_dim.year='1997'\"]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertTrue(result.isValid());
        assertTrue(result.isFormatValid());
        assertTrue(result.isCubeCorrect());
        assertTrue(result.isAggregateFunctionCorrect());
        assertTrue(result.isMeasureCorrect());
        assertTrue(result.isGammaCorrect());
        assertTrue(result.isSigmaCorrect());

        assertEquals(0, result.getUnknownFieldsCount());
        assertEquals(0, result.getMissingGammaCount());
        assertEquals(0, result.getExtraGammaCount());
        assertEquals(0, result.getMissingSigmaCount());
        assertEquals(0, result.getExtraSigmaCount());
        assertEquals(0, result.getWrongSigmaValueCount());
    }

    @Test
    public void testAggregationFunctionIsCaseInsensitive() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList()
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertTrue(result.isValid());
        assertTrue(result.isAggregateFunctionCorrect());
    }

    @Test
    public void testWrongMeasureIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList()
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"unit_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isMeasureCorrect());
    }

    @Test
    public void testMissingGammaFieldIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Arrays.asList(
                                "product_dim.category"
                        ),
                        Collections.<String>emptyList()
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isGammaCorrect());
        assertEquals(1, result.getMissingGammaCount());
    }

    @Test
    public void testExtraGammaFieldIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Arrays.asList(
                                "product_dim.category"
                        ),
                        Collections.<String>emptyList()
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":["
                                + "\"product_dim.category\","
                                + "\"date_dim.year\""
                                + "],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isGammaCorrect());
        assertEquals(1, result.getExtraGammaCount());
    }

    @Test
    public void testUnknownGammaFieldIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Arrays.asList(
                                "product_dim.category"
                        ),
                        Collections.<String>emptyList()
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[\"unknown_dim.value\"],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isGammaCorrect());
        assertEquals(1, result.getUnknownFieldsCount());
    }

    @Test
    public void testWrongSigmaValueIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Arrays.asList(
                                "date_dim.year='1997'"
                        )
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[\"date_dim.year='1998'\"]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isSigmaCorrect());
        assertEquals(1, result.getWrongSigmaValueCount());
    }

    @Test
    public void testMissingSigmaFieldIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Arrays.asList(
                                "date_dim.year='1997'"
                        )
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isSigmaCorrect());
        assertEquals(1, result.getMissingSigmaCount());
    }

    @Test
    public void testExtraSigmaFieldIsDetected() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Arrays.asList(
                                "date_dim.year='1997'"
                        )
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":["
                                + "\"date_dim.year='1997'\","
                                + "\"product_dim.category='Food'\""
                                + "]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isSigmaCorrect());
        assertEquals(1, result.getExtraSigmaCount());
    }
    @Test
    public void testSigmaValuesCanBeIgnored() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Arrays.asList(
                                "date_dim.year='1997'"
                        )
                );

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[\"date_dim.year='1998'\"]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validateIgnoringSigmaValues(
                        normalized,
                        expected,
                        fieldValidator
                );

        assertTrue(result.isValid());
        assertTrue(result.isSigmaCorrect());
        assertEquals(1, result.getWrongSigmaValueCount());
    }

    @Test
    public void testNullExpectedQueryFailsValidation() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        NormalizationResult normalized =
                CubeQueryNormalizer.normalize(
                        "{"
                                + "\"cubeName\":\"sales_cube\","
                                + "\"queryName\":\"\","
                                + "\"aggregateFunction\":\"Sum\","
                                + "\"measure\":\"store_sales\","
                                + "\"gamma\":[],"
                                + "\"sigma\":[]"
                                + "}"
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        normalized,
                        null,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isFormatValid());
        assertTrue(result.getErrors().size() > 0);
    }

    @Test
    public void testNullNormalizationResultFailsValidation() {
        CubeSchema schema = createTestSchema();

        CubeFieldValidator fieldValidator =
                new CubeFieldValidator(schema);

        ExpectedCubeQuery expected =
                new ExpectedCubeQuery(
                        "sales_cube",
                        "Sum",
                        "store_sales",
                        Collections.<String>emptyList(),
                        Collections.<String>emptyList()
                );

        ValidationResult result =
                CubeQueryValidator.validate(
                        null,
                        expected,
                        fieldValidator
                );

        assertFalse(result.isValid());
        assertFalse(result.isFormatValid());
        assertTrue(result.getErrors().size() > 0);
    }

    private CubeSchema createTestSchema() {
        CubeSchema schema = new CubeSchema();

        schema.setCubeName("sales_cube");

        DimensionSchema productDimension =
                new DimensionSchema("product_dim");

        LevelSchema productLevel =
                new LevelSchema();

        productLevel.setLevelName("product");

        productLevel.addAttribute(
                new LevelAttributeSchema(
                        "category",
                        "Text",
                        "category"
                )
        );

        productLevel.addAttribute(
                new LevelAttributeSchema(
                        "brand_name",
                        "Text",
                        "brand_name"
                )
        );

        productDimension.addLevel(productLevel);

        DimensionSchema dateDimension =
                new DimensionSchema("date_dim");

        LevelSchema dateLevel =
                new LevelSchema();

        dateLevel.setLevelName("year");

        dateLevel.addAttribute(
                new LevelAttributeSchema(
                        "year",
                        "Number",
                        "year"
                )
        );

        dateDimension.addLevel(dateLevel);

        schema.addDimension(productDimension);
        schema.addDimension(dateDimension);

        return schema;
    }
}