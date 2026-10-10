package LLM.prompts;

import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;
import LLM.schema.MeasureSchema;

import org.junit.Test;

import static org.junit.Assert.*;

public class CubeSchemaPromptFormatterCompactTest {

    @Test
    public void testFormatCompleteCubeSchema() {

        CubeSchema schema = new CubeSchema();
        schema.setCubeName("sales_cube");

        schema.addMeasure(
                new MeasureSchema("store_sales", "sales.store_sales")
        );

        schema.addMeasure(
                new MeasureSchema("unit_sales", "sales.unit_sales")
        );

        DimensionSchema product =
                new DimensionSchema("product_dim");

        product.addHierarchyLevel("All");
        product.addHierarchyLevel("Category");
        product.addHierarchyLevel("Product");

        LevelSchema category = new LevelSchema();
        category.setLevelName("Category");
        category.addAttribute(
                new LevelAttributeSchema(
                        "product_category",
                        "Text",
                        "product_category"
                )
        );

        LevelSchema productLevel = new LevelSchema();
        productLevel.setLevelName("Product");
        productLevel.addAttribute(
                new LevelAttributeSchema(
                        "product_name",
                        "Text",
                        "product_name"
                )
        );

        product.addLevel(category);
        product.addLevel(productLevel);

        schema.addDimension(product);

        CubeSchemaPromptFormatterCompact formatter =
                new CubeSchemaPromptFormatterCompact();

        String actual = formatter.format(schema);

        String expected =
                "Cube:\n"
                        + "sales_cube\n"
                        + "\n"
                        + "Measures:\n"
                        + "- store_sales\n"
                        + "- unit_sales\n"
                        + "\n"
                        + "Dimensions:\n"
                        + "\n"
                        + "product_dim\n"
                        + "hierarchy: Category > Product\n"
                        + "levels:\n"
                        + "- Category -> product_dim.product_category\n"
                        + "- Product -> product_dim.product_name\n"
                        + "\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testFormatLevelWithMultipleAttributes() {

        CubeSchema schema = new CubeSchema();
        schema.setCubeName("product_cube");

        DimensionSchema product =
                new DimensionSchema("product_dim");

        product.addHierarchyLevel("Product");

        LevelSchema productLevel = new LevelSchema();
        productLevel.setLevelName("Product");

        productLevel.addAttribute(
                new LevelAttributeSchema(
                        "product_name",
                        "Text",
                        "product_name"
                )
        );

        productLevel.addAttribute(
                new LevelAttributeSchema(
                        "brand_name",
                        "Text",
                        "brand_name"
                )
        );

        product.addLevel(productLevel);
        schema.addDimension(product);

        CubeSchemaPromptFormatterCompact formatter =
                new CubeSchemaPromptFormatterCompact();

        String actual = formatter.format(schema);

        String expected =
                "Cube:\n"
                        + "product_cube\n"
                        + "\n"
                        + "Measures:\n"
                        + "\n"
                        + "Dimensions:\n"
                        + "\n"
                        + "product_dim\n"
                        + "hierarchy: Product\n"
                        + "levels:\n"
                        + "- Product -> product_dim.product_name, product_dim.brand_name\n"
                        + "\n";

        assertEquals(expected, actual);
    }

    @Test
    public void testAllLevelIsExcluded() {

        CubeSchema schema = new CubeSchema();
        schema.setCubeName("sales_cube");

        DimensionSchema date =
                new DimensionSchema("date_dim");

        date.addHierarchyLevel("All");
        date.addHierarchyLevel("Year");

        LevelSchema allLevel = new LevelSchema();
        allLevel.setLevelName("All");
        allLevel.addAttribute(
                new LevelAttributeSchema(
                        "all",
                        "Text",
                        "all"
                )
        );

        LevelSchema yearLevel = new LevelSchema();
        yearLevel.setLevelName("Year");
        yearLevel.addAttribute(
                new LevelAttributeSchema(
                        "the_year",
                        "Number",
                        "the_year"
                )
        );

        date.addLevel(allLevel);
        date.addLevel(yearLevel);

        schema.addDimension(date);

        CubeSchemaPromptFormatterCompact formatter =
                new CubeSchemaPromptFormatterCompact();

        String actual = formatter.format(schema);

        String expected =
                "Cube:\n"
                        + "sales_cube\n"
                        + "\n"
                        + "Measures:\n"
                        + "\n"
                        + "Dimensions:\n"
                        + "\n"
                        + "date_dim\n"
                        + "hierarchy: Year\n"
                        + "levels:\n"
                        + "- Year -> date_dim.the_year\n"
                        + "\n";

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatRejectsNullSchema() {

        CubeSchemaPromptFormatterCompact formatter =
                new CubeSchemaPromptFormatterCompact();

        formatter.format(null);
    }
}