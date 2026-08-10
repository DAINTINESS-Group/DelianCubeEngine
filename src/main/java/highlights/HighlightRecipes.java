package highlights;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import highlights.metamodel.CharacterRole;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.MeasureRole;
import highlights.metamodel.ScoreType;
import intentional.model.ModelResult;
import intentional.model.archetypes.labelpredominance.LabelDistributionAlgorithm;
import intentional.model.archetypes.megacontributor.MarginalContributionAlgorithm;
import intentional.model.archetypes.modality.NormalizedEntropyModalityAlgorithm;
import intentional.model.archetypes.outlier.ZScoreOutlierAlgorithm;
import intentional.model.archetypes.topk.TopKContributionAlgorithm;

/**
 * For each model (by {@link ModelResult#modelName()}) the archetype's display name and the highlight role
 * each of its labels materializes. A label with no role bears no highlight.
 */
public final class HighlightRecipes {

    private final Map<String, Recipe> byModelName = new LinkedHashMap<>();

    private HighlightRecipes() {}

    private HighlightRecipes add(Recipe recipe) {
        byModelName.put(recipe.modelName, recipe);
        return this;
    }

    public Recipe forResult(ModelResult result) {
        return byModelName.get(result.modelName());
    }

    public static HighlightRecipes defaults() {
        return new HighlightRecipes()
                .add(Recipe.of(NormalizedEntropyModalityAlgorithm.NAME, "Modality"))
                .add(Recipe.of(ZScoreOutlierAlgorithm.NAME, "Outlier")
                        .role(ZScoreOutlierAlgorithm.OUTLIER, role("Outlier")))
                .add(Recipe.of(MarginalContributionAlgorithm.NAME, "MegaContributor")
                        .role(MarginalContributionAlgorithm.CONTRIBUTOR, role("MegaContributor")))
                .add(Recipe.of(TopKContributionAlgorithm.NAME, "TopKContributors")
                        .role(TopKContributionAlgorithm.TOP_CONTRIBUTOR, role("TopContributor")))
                .add(Recipe.of(LabelDistributionAlgorithm.NAME, "LabelPredominance")
                        .role(LabelDistributionAlgorithm.EXEMPLAR, role("Exemplar"))
                        .role(LabelDistributionAlgorithm.EXCEPTION, role("Exception")));
    }

    private static ElementaryHighlightRole role(String name) {
        return new ElementaryHighlightRole(name, Collections.singletonList(new CharacterRole(name)),
                new MeasureRole(name), Collections.<ScoreType>emptyList());
    }

    public static final class Recipe {
        public final String modelName;
        public final String displayName;
        private final Map<String, ElementaryHighlightRole> rolesByLabel = new LinkedHashMap<>();

        private Recipe(String modelName, String displayName) {
            this.modelName = modelName;
            this.displayName = displayName;
        }

        static Recipe of(String modelName, String displayName) {
            return new Recipe(modelName, displayName);
        }

        Recipe role(String label, ElementaryHighlightRole role) {
            rolesByLabel.put(label, role);
            return this;
        }

        /** The role a label materializes, or {@code null} when the label bears no highlight. */
        public ElementaryHighlightRole roleFor(String label) {
            return rolesByLabel.get(label);
        }
    }
}
