package highlights.archetypes.modality;

import java.util.Arrays;
import java.util.Collections;

import highlights.metamodel.Algorithm;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.ExplanatorRole;
import highlights.metamodel.MainMeasureRole;
import highlights.metamodel.MeasureConstraint;
import highlights.metamodel.ScoreType;


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
                Arrays.<ScoreType>asList(NormalizedEntropyModalityAlgorithm.CONCENTRATION),
                Collections.<ElementaryHighlightRole>emptyList());
    }
}
