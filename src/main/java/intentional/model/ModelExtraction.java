package intentional.model;

import java.util.List;

import intentional.result.LabeledResult;

/**
 * Runs a set of {@link ArchetypeProperty} models over a {@link LabeledResult}, each augmenting it with its
 * results, and returns it.
 */
public final class ModelExtraction {

    public LabeledResult run(LabeledResult context, List<ArchetypeProperty> archetypes) {
        for (ArchetypeProperty archetype : archetypes) {
            for (Model model : archetype.models) {
                context = model.run(context);
            }
        }
        return context;
    }
}
