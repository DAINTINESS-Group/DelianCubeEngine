package LLM.prompts;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class LLMPromptTechniqueCombinationGeneratorTest {

    @Test
    public void testGenerateAllCombinationsForThreeTechniques() {

        PromptTechnique techniqueA =
                new InstructionsForQueryGenerationPromptTechnique();

        PromptTechnique techniqueB =
                new PrescriptionOfResultPromptTechnique();

        PromptTechnique techniqueC =
                new ExamplesPromptTechnique();

        List<PromptTechnique> techniques =
                Arrays.asList(
                        techniqueA,
                        techniqueB,
                        techniqueC
                );

        List<List<PromptTechnique>> combinations =
                PromptTechniqueCombinationGenerator
                        .generateAllCombinations(techniques);

        /*
         * For N techniques, the generator must create 2^N
         * combinations.
         */
        int expectedNumberOfCombinations =
                (int) Math.pow(
                        2,
                        techniques.size()
                );

        assertEquals(
                expectedNumberOfCombinations,
                combinations.size()
        );

        // The first combination is the baseline.
        assertTrue(
                combinations.get(0).isEmpty()
        );

        // Single-technique combinations must exist.
        assertTrue(
                combinations.contains(
                        Collections.singletonList(
                                techniqueA
                        )
                )
        );

        assertTrue(
                combinations.contains(
                        Collections.singletonList(
                                techniqueB
                        )
                )
        );

        assertTrue(
                combinations.contains(
                        Collections.singletonList(
                                techniqueC
                        )
                )
        );

        // The complete combination must exist.
        assertTrue(
                combinations.contains(
                        Arrays.asList(
                                techniqueA,
                                techniqueB,
                                techniqueC
                        )
                )
        );
    }

    @Test
    public void testEmptyTechniqueListProducesBaseline() {

        List<List<PromptTechnique>> combinations =
                PromptTechniqueCombinationGenerator
                        .generateAllCombinations(
                                Collections
                                        .<PromptTechnique>emptyList()
                        );

        assertNotNull(combinations);

        assertEquals(
                1,
                combinations.size()
        );

        assertTrue(
                combinations.get(0).isEmpty()
        );

        assertEquals(
                "baseline",
                PromptTechniqueCombinationGenerator
                        .getCombinationName(
                                combinations.get(0)
                        )
        );
    }

    @Test
    public void testCombinationNames() {

        PromptTechnique instructions =
                new InstructionsForQueryGenerationPromptTechnique();

        PromptTechnique examples =
                new ExamplesPromptTechnique();

        String singleName =
                PromptTechniqueCombinationGenerator
                        .getCombinationName(
                                Collections.singletonList(
                                        instructions
                                )
                        );

        assertEquals(
                PromptTechniqueFactory
                        .INSTRUCTIONS_FOR_QUERY_GENERATION,
                singleName
        );

        String combinedName =
                PromptTechniqueCombinationGenerator
                        .getCombinationName(
                                Arrays.asList(
                                        instructions,
                                        examples
                                )
                        );

        assertEquals(
                PromptTechniqueFactory
                        .INSTRUCTIONS_FOR_QUERY_GENERATION
                        + "+"
                        + PromptTechniqueFactory.EXAMPLES,
                combinedName
        );
    }

    @Test
    public void testBaselineCombinationName() {

        String name =
                PromptTechniqueCombinationGenerator
                        .getCombinationName(
                                Collections
                                        .<PromptTechnique>emptyList()
                        );

        assertEquals(
                "baseline",
                name
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCombinationGeneratorRejectsNullInput() {

        PromptTechniqueCombinationGenerator
                .generateAllCombinations(null);
    }
}