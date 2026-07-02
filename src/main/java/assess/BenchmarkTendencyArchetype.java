package assess;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.metamodel.CharacterRole;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.ExplanatorConstraint;
import result.highlights.metamodel.ExplanatorRole;
import result.highlights.metamodel.InterestingnessFacet;
import result.highlights.metamodel.LabelScoreType;
import result.highlights.metamodel.MainMeasureRole;
import result.highlights.metamodel.MeasureConstraint;
import result.highlights.metamodel.MeasureRole;
import result.highlights.metamodel.ScoreType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Defines the ASSESS benchmark-tendency archetype property: does the measure show an overall tendency
 * relative to the benchmark? Tested by {@link LabelDistributionAlgorithm} over the query's labels.
 */
public final class BenchmarkTendencyArchetype {

    private BenchmarkTendencyArchetype() {}

    public static ArchetypeProperty create(List<String> orderedLabels) {
        LabelScoreType labelType = new LabelScoreType("AssessmentLabel", orderedLabels);
        ElementaryHighlightRole assessedCell = new ElementaryHighlightRole(
                "AssessedCell",
                Collections.singletonList(new CharacterRole("AssessedCell")),
                new MeasureRole("AssessedMeasure"),
                Arrays.<ScoreType>asList(labelType, InterestingnessFacet.PECULIARITY));
        Algorithm algorithm = new LabelDistributionAlgorithm(orderedLabels, assessedCell);
        return new ArchetypeProperty(
                "BenchmarkTendency",
                new MainMeasureRole("Assessed", MeasureConstraint.ANY),
                Collections.singletonList(new ExplanatorRole("Breakdown", ExplanatorConstraint.ANY)),
                Collections.singletonList(algorithm),
                Arrays.<ScoreType>asList(labelType, InterestingnessFacet.PECULIARITY),
                Collections.singletonList(assessedCell));
    }
}
