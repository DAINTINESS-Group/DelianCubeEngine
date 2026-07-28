package highlights.archetypes.labelpredominance;

import java.util.Arrays;
import java.util.Collections;

import highlights.metamodel.Algorithm;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.CharacterRole;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.EvaluationAxis;
import highlights.metamodel.ExplanatorConstraint;
import highlights.metamodel.ExplanatorRole;
import highlights.metamodel.MainMeasureRole;
import highlights.metamodel.MeasureConstraint;
import highlights.metamodel.MeasureRole;
import highlights.metamodel.ScoreType;


/**
 * Defines the label-predominance archetype property: over a per-cell labeling in the context, does one
 * label predominate? Tested by {@link LabelDistributionAlgorithm}. It fires for any operator that produces
 * a labeling — ASSESS's assessment labels, a KPI classification, a clustering, and so on.
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
                Arrays.<ScoreType>asList(LabelDistributionAlgorithm.WINNER_SHARE,
                        LabelDistributionAlgorithm.VOTING_RULE),
                Collections.singletonList(labeledCell),
                EvaluationAxis.LABELING);
    }
}
