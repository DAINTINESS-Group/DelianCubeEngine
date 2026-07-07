package result.highlights.archetypes.outlier;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.metamodel.CharacterRole;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.ExplanatorConstraint;
import result.highlights.metamodel.ExplanatorRole;
import result.highlights.metamodel.InterestingnessFacet;
import result.highlights.metamodel.MainMeasureRole;
import result.highlights.metamodel.MeasureConstraint;
import result.highlights.metamodel.MeasureRole;
import result.highlights.metamodel.ScoreType;

import java.util.Arrays;
import java.util.Collections;


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
                Arrays.<ScoreType>asList(ZScoreOutlierAlgorithm.ZSCORE, InterestingnessFacet.PECULIARITY),
                Collections.singletonList(outlierRole));
    }
}
