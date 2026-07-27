package highlights.archetypes.outlier;

import java.util.Arrays;
import java.util.Collections;

import highlights.metamodel.Algorithm;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.CharacterRole;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.ExplanatorConstraint;
import highlights.metamodel.ExplanatorRole;
import highlights.metamodel.MainMeasureRole;
import highlights.metamodel.MeasureConstraint;
import highlights.metamodel.MeasureRole;
import highlights.metamodel.ScoreType;


/**
 * Defines the outlier archetype property: does the result contain a cell whose measure value deviates
 * strongly from the distribution of the others? Tested by {@link ZScoreOutlierAlgorithm} over any measure.
 */
public final class OutlierArchetype {

    private OutlierArchetype() {}

    public static ArchetypeProperty create() {
        ElementaryHighlightRole outlierRole = new ElementaryHighlightRole(
                "Outlier",
                Collections.singletonList(new CharacterRole("Outlier")),
                new MeasureRole("Value"),
                Collections.<ScoreType>singletonList(ZScoreOutlierAlgorithm.ZSCORE));

        Algorithm algorithm = new ZScoreOutlierAlgorithm(outlierRole);
        return new ArchetypeProperty(
                "Outlier",
                new MainMeasureRole("Value", MeasureConstraint.ANY),
                Collections.singletonList(new ExplanatorRole("Breakdown", ExplanatorConstraint.ANY)),
                Collections.singletonList(algorithm),
                Arrays.<ScoreType>asList(ZScoreOutlierAlgorithm.ZSCORE),
                Collections.singletonList(outlierRole));
    }
}
