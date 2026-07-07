package result.highlights.archetypes.labelpredominance;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.metamodel.CharacterRole;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.EvaluationAxis;
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
 * Defines the label-predominance archetype property: over an ordered per-cell labeling in the context, does
 * one label predominate? Tested by {@link LabelDistributionAlgorithm}. It fires for any operator that
 * produces an ordered labeling — ASSESS's assessment labels, an outlier classification, and so on.
 */
public final class LabelPredominanceArchetype {

    private LabelPredominanceArchetype() {}

    public static ArchetypeProperty create() {
        ElementaryHighlightRole labeledCell = new ElementaryHighlightRole(
                "LabeledCell",
                Collections.singletonList(new CharacterRole("LabeledCell")),
                new MeasureRole("LabeledMeasure"),
                Collections.<ScoreType>singletonList(LabelDistributionAlgorithm.MAGNITUDE));
        Algorithm algorithm = new LabelDistributionAlgorithm(labeledCell);
        return new ArchetypeProperty(
                "LabelPredominance",
                new MainMeasureRole("Labeled", MeasureConstraint.ANY),
                Collections.singletonList(new ExplanatorRole("Breakdown", ExplanatorConstraint.ANY)),
                Collections.singletonList(algorithm),
                Arrays.<ScoreType>asList(LabelDistributionAlgorithm.DOMINANT_SHARE, InterestingnessFacet.PECULIARITY),
                Collections.singletonList(labeledCell),
                EvaluationAxis.LABELING);
    }
}
