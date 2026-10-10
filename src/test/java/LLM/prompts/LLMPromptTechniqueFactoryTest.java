package LLM.prompts;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LLMPromptTechniqueFactoryTest {

    @Test
    public void testCreateInstructionsTechnique() {

        PromptTechnique technique =
                PromptTechniqueFactory.createByName(
                        PromptTechniqueFactory
                                .INSTRUCTIONS_FOR_QUERY_GENERATION
                );

        assertNotNull(technique);

        assertEquals(
                PromptTechniqueFactory
                        .INSTRUCTIONS_FOR_QUERY_GENERATION,
                technique.getName()
        );

        assertTrue(
                technique instanceof
                        InstructionsForQueryGenerationPromptTechnique
        );

        assertTrue(
                technique.getContent().contains(
                        "Instructions for query generation:"
                )
        );
    }

    @Test
    public void testCreatePrescriptionTechnique() {

        PromptTechnique technique =
                PromptTechniqueFactory.createByName(
                        PromptTechniqueFactory
                                .PRESCRIPTION_OF_RESULT
                );

        assertNotNull(technique);

        assertEquals(
                PromptTechniqueFactory
                        .PRESCRIPTION_OF_RESULT,
                technique.getName()
        );

        assertTrue(
                technique instanceof
                        PrescriptionOfResultPromptTechnique
        );

        assertTrue(
                technique.getContent().contains(
                        "Prescription of result:"
                )
        );
    }

    @Test
    public void testCreateExamplesTechnique() {

        PromptTechnique technique =
                PromptTechniqueFactory.createByName(
                        PromptTechniqueFactory.EXAMPLES
                );

        assertNotNull(technique);

        assertEquals(
                PromptTechniqueFactory.EXAMPLES,
                technique.getName()
        );

        assertTrue(
                technique instanceof
                        ExamplesPromptTechnique
        );

        assertTrue(
                technique.getContent().contains(
                        "Examples:"
                )
        );
    }

    @Test
    public void testTechniqueNamesAreAvailable() {

        List<String> names =
                PromptTechniqueFactory
                        .getAllPromptTechniqueNames();

        assertNotNull(names);

        assertEquals(
                3,
                names.size()
        );

        assertTrue(
                names.contains(
                        PromptTechniqueFactory
                                .INSTRUCTIONS_FOR_QUERY_GENERATION
                )
        );

        assertTrue(
                names.contains(
                        PromptTechniqueFactory
                                .PRESCRIPTION_OF_RESULT
                )
        );

        assertTrue(
                names.contains(
                        PromptTechniqueFactory.EXAMPLES
                )
        );
    }

    @Test
    public void testAllPromptTechniquesAreAvailable() {

        List<PromptTechnique> techniques =
                PromptTechniqueFactory
                        .getAllPromptTechniques();

        assertNotNull(techniques);

        assertEquals(
                3,
                techniques.size()
        );

        assertEquals(
                PromptTechniqueFactory
                        .INSTRUCTIONS_FOR_QUERY_GENERATION,
                techniques.get(0).getName()
        );

        assertEquals(
                PromptTechniqueFactory
                        .PRESCRIPTION_OF_RESULT,
                techniques.get(1).getName()
        );

        assertEquals(
                PromptTechniqueFactory.EXAMPLES,
                techniques.get(2).getName()
        );
    }

    @Test
    public void testFactoryNormalizesTechniqueName() {

        PromptTechnique technique =
                PromptTechniqueFactory.createByName(
                        "  EXAMPLES  "
                );

        assertNotNull(technique);

        assertEquals(
                PromptTechniqueFactory.EXAMPLES,
                technique.getName()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactoryRejectsUnknownTechnique() {

        PromptTechniqueFactory.createByName(
                "unknown_technique"
        );
    }
}