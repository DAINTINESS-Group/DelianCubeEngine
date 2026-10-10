package LLM.prompts;

import LLM.schema.CubeSchema;
import LLM.schema.DimensionSchema;
import LLM.schema.LevelAttributeSchema;
import LLM.schema.LevelSchema;
import LLM.schema.MeasureSchema;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class LLMPromptBuilderTest {

    private CubeSchema createTestCubeSchema() {

        CubeSchema cubeSchema = new CubeSchema();

        cubeSchema.setCubeName("test_cube");

        DimensionSchema productDimension =
                new DimensionSchema();

        productDimension.setName("product_dim");

        LevelSchema categoryLevel =
                new LevelSchema();

        categoryLevel.setLevelName("Category");
        categoryLevel.setId("category");
        categoryLevel.setDescription("Product category");

        categoryLevel.addAttribute(
                new LevelAttributeSchema(
                        "product_category",
                        "Text",
                        "product_category"
                )
        );

        productDimension.addLevel(categoryLevel);
        productDimension.addHierarchyLevel("Category");

        cubeSchema.addDimension(productDimension);

        cubeSchema.addMeasure(
                new MeasureSchema(
                        "sales",
                        "test.sales"
                )
        );

        return cubeSchema;
    }

    @Test
    public void testBuildPromptContainsExpectedSchemaAndQuestion() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        String question =
                "What are the total sales by product category?";

        PromptBuilder builder =
                new PromptBuilder();

        String prompt =
                builder.buildPrompt(
                        cubeSchema,
                        question
                );

        assertNotNull(prompt);

        assertTrue(
                prompt.contains("Cube:")
        );

        assertTrue(
                prompt.contains("test_cube")
        );

        assertTrue(
                prompt.contains("Measures:")
        );

        assertTrue(
                prompt.contains("- sales")
        );

        assertTrue(
                prompt.contains("Dimensions:")
        );

        assertTrue(
                prompt.contains("product_dim")
        );

        assertTrue(
                prompt.contains(
                        "Category -> product_dim.product_category"
                )
        );

        // Expected user question
        assertTrue(
                prompt.contains("User question:")
        );

        assertTrue(
                prompt.contains(question)
        );

        // Expected final marker
        assertTrue(
                prompt.contains("Final answer:")
        );
    }

    @Test
    public void testBuildPromptWithPrescriptionTechnique() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        String question =
                "What are the total sales by product category?";

        PromptTechnique technique =
                new PrescriptionOfResultPromptTechnique();

        List<PromptTechnique> techniques =
                Collections.singletonList(technique);

        PromptBuilder builder =
                new PromptBuilder();

        String prompt =
                builder.buildPrompt(
                        cubeSchema,
                        question,
                        techniques
                );

        assertNotNull(prompt);

        // The selected technique must be included.
        assertTrue(
                prompt.contains(
                        "Prescription of result:"
                )
        );

        // Expected output structure from the technique.
        assertTrue(
                prompt.contains(
                        "\"cubeName\""
                )
        );

        assertTrue(
                prompt.contains(
                        "\"aggregateFunction\""
                )
        );

        assertTrue(
                prompt.contains(
                        "\"measure\""
                )
        );

        assertTrue(
                prompt.contains(
                        "\"gamma\""
                )
        );

        assertTrue(
                prompt.contains(
                        "\"sigma\""
                )
        );

        // The rest of the prompt must still be present.
        assertTrue(
                prompt.contains(question)
        );

        assertTrue(
                prompt.contains("test_cube")
        );
    }

    @Test
    public void testBuildPromptWithMultipleTechniques() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        String question =
                "What are the total sales by product category?";

        PromptTechnique instructions =
                new InstructionsForQueryGenerationPromptTechnique();

        PromptTechnique examples =
                new ExamplesPromptTechnique();

        List<PromptTechnique> techniques =
                Arrays.asList(
                        instructions,
                        examples
                );

        PromptBuilder builder =
                new PromptBuilder();

        String prompt =
                builder.buildPrompt(
                        cubeSchema,
                        question,
                        techniques
                );

        assertNotNull(prompt);

        // Both selected techniques must be included.
        assertTrue(
                prompt.contains(
                        "Instructions for query generation:"
                )
        );

        assertTrue(
                prompt.contains(
                        "Examples:"
                )
        );

        // Actual schema must still be used.
        assertTrue(
                prompt.contains("test_cube")
        );

        assertTrue(
                prompt.contains("- sales")
        );

        assertTrue(
                prompt.contains(question)
        );

        assertTrue(
                prompt.contains("Final answer:")
        );
    }

    @Test
    public void testBuildPromptWithoutTechniques() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        String question =
                "What are the total sales by product category?";

        PromptBuilder builder =
                new PromptBuilder();

        String prompt =
                builder.buildPrompt(
                        cubeSchema,
                        question
                );

        assertNotNull(prompt);

        assertTrue(
                prompt.contains("test_cube")
        );

        assertTrue(
                prompt.contains("- sales")
        );

        assertTrue(
                prompt.contains(question)
        );

        assertTrue(
                prompt.contains("Output contract:")
        );

        assertTrue(
                prompt.contains("Final answer:")
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildPromptRejectsNullSchema() {

        PromptBuilder builder =
                new PromptBuilder();

        builder.buildPrompt(
                null,
                "What are the total sales?"
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildPromptRejectsEmptyQuestion() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        PromptBuilder builder =
                new PromptBuilder();

        builder.buildPrompt(
                cubeSchema,
                "   "
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildPromptRejectsNullTechniqueList() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        PromptBuilder builder =
                new PromptBuilder();

        builder.buildPrompt(
                cubeSchema,
                "What are the total sales?",
                null
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildPromptRejectsDuplicateTechnique() {

        CubeSchema cubeSchema =
                createTestCubeSchema();

        PromptTechnique technique =
                new PrescriptionOfResultPromptTechnique();

        List<PromptTechnique> techniques =
                Arrays.asList(
                        technique,
                        technique
                );

        PromptBuilder builder =
                new PromptBuilder();

        builder.buildPrompt(
                cubeSchema,
                "What are the total sales?",
                techniques
        );
    }
}