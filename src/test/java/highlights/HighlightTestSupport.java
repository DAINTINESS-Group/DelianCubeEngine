package highlights;

import java.util.List;

import cubemanager.CubeManager;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import intentional.model.ArchetypeProperty;
import intentional.model.ModelExtraction;
import intentional.model.ModelResult;
import intentional.result.LabeledResult;

/**
 * Shared runs for archetype tests: run the model-extraction operator over an operator result, and, for the
 * integration tests, extract highlights with a real cube.
 */
public final class HighlightTestSupport {

    private HighlightTestSupport() {}

    /** The model results a set of archetypes produces over an operator result. */
    public static List<ModelResult> models(LabeledResult result, List<ArchetypeProperty> archetypes) {
       new ModelExtraction().run(result, archetypes);
       return result.archetypeModels();
    }

    /** The model result of the model with the given name, or {@code null}. */
    public static ModelResult resultNamed(List<ModelResult> results, String modelName) {
        for (ModelResult result : results) {
            if (modelName.equals(result.modelName())) return result;
        }
        return null;
    }

    /** Runs the model-extraction sweep and the highlight extractor over an operator result with a cube. */
    public static HighlightSet highlights(LabeledResult result, List<ArchetypeProperty> archetypes,
                                          CubeManager cubeManager) {
        models(result, archetypes);
        return new HighlightExtractor().extract(result, HighlightRecipes.defaults(), cubeManager);
    }

    public static HolisticHighlight holisticFor(HighlightSet highlights, String archetypeName) {
        for (Highlight h : highlights.highlights()) {
            if (h instanceof HolisticHighlight
                    && ((HolisticHighlight) h).archetypeName.equals(archetypeName)) {
                return (HolisticHighlight) h;
            }
        }
        return null;
    }
}
