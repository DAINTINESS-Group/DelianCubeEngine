package intentional.model;

import java.util.ArrayList;
import java.util.List;

import intentional.result.LabeledResult;

/**
 * Runs a set of {@link ArchetypeProperty} models over a {@link LabeledResult} and collects their
 * {@link ModelResult}s.
 */
public final class ModelExtraction {

    public List<ModelResult> run(LabeledResult context, List<ArchetypeProperty> archetypes) {
        List<ModelResult> out = new ArrayList<>();
        for (ArchetypeProperty archetype : archetypes) {
            for (Model model : archetype.models) {
                out.addAll(model.run(context));
            }
        }
        return out;
    }
}
