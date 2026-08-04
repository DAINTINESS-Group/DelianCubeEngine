package highlights.archetypes;

import java.util.ArrayList;
import java.util.List;

import highlights.archetypes.labelpredominance.ElectionSpec;
import highlights.archetypes.labelpredominance.LabelPredominanceArchetype;
import highlights.archetypes.megacontributor.MegaContributorArchetype;
import highlights.archetypes.modality.ModalityArchetype;
import highlights.archetypes.outlier.OutlierArchetype;
import highlights.archetypes.topk.TopKContributorsArchetype;
import highlights.metamodel.ArchetypeProperty;


/**
 * The generic, data-driven archetype properties every intentional operator tests by default. They are
 * operator-agnostic — each declares its own applicability (measure constraints, config-free models), so the
 * extractor gates the ones that do not fit a given result. {@link #all()} returns a fresh, mutable list so
 * an operator can append the archetypes specific to it (e.g. ASSESS's benchmark tendency, which needs the
 * query's label scheme) on top of the defaults.
 */
public final class DefaultArchetypes {

    private DefaultArchetypes() {}

    /** The default archetypes with the default election running label-predominance. */
    public static List<ArchetypeProperty> all() {
        return all(ElectionSpec.DEFAULT);
    }

    /** The default archetypes, with label-predominance running the given election. */
    public static List<ArchetypeProperty> all(ElectionSpec election) {
        List<ArchetypeProperty> archetypes = new ArrayList<>();
        archetypes.add(MegaContributorArchetype.create());
        archetypes.add(TopKContributorsArchetype.create());
        archetypes.add(OutlierArchetype.create());
        archetypes.add(ModalityArchetype.create());
        archetypes.add(LabelPredominanceArchetype.create(election));
        return archetypes;
    }
}
