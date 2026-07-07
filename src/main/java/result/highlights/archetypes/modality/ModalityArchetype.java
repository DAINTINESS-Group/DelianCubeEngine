package result.highlights.archetypes.modality;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.ExplanatorRole;
import result.highlights.metamodel.InterestingnessFacet;
import result.highlights.metamodel.MainMeasureRole;
import result.highlights.metamodel.MeasureConstraint;
import result.highlights.metamodel.ScoreType;

import java.util.Arrays;
import java.util.Collections;


/**
 * Defines the modality archetype property: is the measure mass concentrated in a few cells or spread
 * uniformly across the result? Tested by {@link NormalizedEntropyModalityAlgorithm} over an additive
 * measure. A dataset-level shape characterization — it reserves no explanators and no elementary roles,
 * carrying its verdict entirely in the holistic's concentration score.
 */
public final class ModalityArchetype {

    private ModalityArchetype() {}

    public static ArchetypeProperty create() {
        Algorithm algorithm = new NormalizedEntropyModalityAlgorithm();
        return new ArchetypeProperty(
                "Modality",
                new MainMeasureRole("Mass", MeasureConstraint.ADDITIVE),
                Collections.<ExplanatorRole>emptyList(),
                Collections.singletonList(algorithm),
                Arrays.<ScoreType>asList(NormalizedEntropyModalityAlgorithm.CONCENTRATION, InterestingnessFacet.PECULIARITY),
                Collections.<ElementaryHighlightRole>emptyList());
    }
}
