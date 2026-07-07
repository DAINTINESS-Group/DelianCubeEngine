package result.highlights.archetypes;

import result.highlights.archetypes.labelpredominance.LabelPredominanceArchetype;
import result.highlights.archetypes.megacontributor.MegaContributorArchetype;
import result.highlights.archetypes.modality.ModalityArchetype;
import result.highlights.archetypes.outlier.OutlierArchetype;
import result.highlights.archetypes.topk.TopKContributorsArchetype;
import result.highlights.metamodel.ArchetypeProperty;

import java.util.ArrayList;
import java.util.List;


/**
 * The generic, data-driven archetype properties every intentional operator tests by default. They are
 * operator-agnostic — each declares its own applicability (measure constraints, config-free models), so the
 * extractor gates the ones that do not fit a given result. {@link #all()} returns a fresh, mutable list so
 * an operator can append the archetypes specific to it (e.g. ASSESS's benchmark tendency, which needs the
 * query's label scheme) on top of the defaults.
 */
public final class DefaultArchetypes {

    private DefaultArchetypes() {}

    public static List<ArchetypeProperty> all() {
        List<ArchetypeProperty> archetypes = new ArrayList<>();
        archetypes.add(MegaContributorArchetype.create());
        archetypes.add(TopKContributorsArchetype.create());
        archetypes.add(OutlierArchetype.create());
        archetypes.add(ModalityArchetype.create());
        archetypes.add(LabelPredominanceArchetype.create());
        return archetypes;
    }
}
