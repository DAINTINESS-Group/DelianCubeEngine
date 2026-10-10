package LLM.validation;

import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class CubeFieldValidatorTest {

    @Test
    public void testAllowedFieldsAreCollectedFromSchema() {

        CubeSchema schema = createTestSchema();

        CubeFieldValidator validator =
                new CubeFieldValidator(schema);

        Set<String> expectedFields = new HashSet<String>(
                Arrays.asList(
                        "product_dim.category",
                        "product_dim.brand_name",
                        "date_dim.year"
                )
        );

        assertEquals(
                expectedFields,
                validator.getAllowedFields()
        );
    }

    @Test
    public void testExistingFieldsAreRecognized() {

        CubeSchema schema = createTestSchema();

        CubeFieldValidator validator =
                new CubeFieldValidator(schema);

        assertTrue(
                validator.fieldExists("product_dim.category")
        );

        assertTrue(
                validator.fieldExists("product_dim.brand_name")
        );

        assertTrue(
                validator.fieldExists("date_dim.year")
        );
    }

    @Test
    public void testUnknownAndInvalidFieldsAreRejected() {

        CubeSchema schema = createTestSchema();

        CubeFieldValidator validator =
                new CubeFieldValidator(schema);

        assertFalse(
                validator.fieldExists("product_dim.unknown")
        );

        assertFalse(
                validator.fieldExists("unknown_dim.category")
        );

        assertFalse(
                validator.fieldExists(null)
        );

        assertFalse(
                validator.fieldExists("")
        );

        assertFalse(
                validator.fieldExists("   ")
        );
    }

    @Test
    public void testFindUnknownFieldsReturnsOnlyUnknownFields() {

        CubeSchema schema = createTestSchema();

        CubeFieldValidator validator =
                new CubeFieldValidator(schema);

        Set<String> fields = new HashSet<String>(
                Arrays.asList(
                        "product_dim.category",
                        "product_dim.unknown",
                        "date_dim.year",
                        "unknown_dim.value"
                )
        );

        Set<String> expectedUnknownFields = new HashSet<String>(
                Arrays.asList(
                        "product_dim.unknown",
                        "unknown_dim.value"
                )
        );

        Set<String> actualUnknownFields =
                validator.findUnknownFields(fields);

        assertEquals(
                expectedUnknownFields,
                actualUnknownFields
        );
    }

    @Test
    public void testNullOrEmptyFieldSetContainsNoUnknownFields() {

        CubeSchema schema = createTestSchema();

        CubeFieldValidator validator =
                new CubeFieldValidator(schema);

        assertTrue(
                validator.findUnknownFields(null).isEmpty()
        );

        assertTrue(
                validator.findUnknownFields(
                        new HashSet<String>()
                ).isEmpty()
        );
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