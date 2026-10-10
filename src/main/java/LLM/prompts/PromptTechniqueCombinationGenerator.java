package LLM.prompts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromptTechniqueCombinationGenerator {

    private PromptTechniqueCombinationGenerator() {
        // Utility class
    }

    /*
     * Generates all possible combinations of the given prompt techniques.
     *
     * Example for 3 techniques:
     *
     * []
     * [A]
     * [B]
     * [C]
     * [A, B]
     * [A, C]
     * [B, C]
     * [A, B, C]
     *
     * The empty combination represents the baseline experiment.
     */
    public static List<List<PromptTechnique>> generateAllCombinations(
            List<PromptTechnique> techniques
    ) {
        if (techniques == null) {
            throw new IllegalArgumentException(
                    "Prompt techniques cannot be null."
            );
        }

        List<List<PromptTechnique>> combinations =
                new ArrayList<List<PromptTechnique>>();

        int techniqueCount = techniques.size();

        /*
         * For N techniques there are 2^N possible combinations.
         */
        int totalCombinations = 1 << techniqueCount;

        for (int mask = 0; mask < totalCombinations; mask++) {

            List<PromptTechnique> combination =
                    new ArrayList<PromptTechnique>();

            for (int i = 0; i < techniqueCount; i++) {

                /*
                 * If bit i is enabled, technique i
                 * belongs to this combination.
                 */
                if ((mask & (1 << i)) != 0) {
                    combination.add(techniques.get(i));
                }
            }

            combinations.add(
                    Collections.unmodifiableList(combination)
            );
        }

        return Collections.unmodifiableList(combinations);
    }

    public static String getCombinationName(
            List<PromptTechnique> combination
    ) {
        if (combination == null || combination.isEmpty()) {
            return "baseline";
        }

        StringBuilder name = new StringBuilder();

        for (int i = 0; i < combination.size(); i++) {

            PromptTechnique technique = combination.get(i);

            if (technique == null) {
                continue;
            }

            if (name.length() > 0) {
                name.append("+");
            }

            name.append(technique.getName());
        }

        if (name.length() == 0) {
            return "baseline";
        }

        return name.toString();
    }
}