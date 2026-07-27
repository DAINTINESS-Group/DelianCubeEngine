package highlights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.AggregationFunction;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import cubemanager.cubebase.QueryMeasure;
import highlights.instance.AlgorithmExecution;
import highlights.instance.ElementaryHighlight;
import highlights.instance.ExecutableAlgorithm;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.MeasureValue;
import highlights.instance.ScoredFinding;
import highlights.metamodel.Algorithm;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.EvaluationAxis;
import highlights.metamodel.MeasureConstraint;
import intentional.result.LabeledResult;
import intentional.result.Labeling;

/**
 * Stage 2 of the pipeline: runs the data-driven archetype evaluation over an {@link LabeledResult} and
 * produces highlights. Each candidate {@link ArchetypeProperty} is evaluated once per query measure: for a
 * measure whose aggregation satisfies the archetype's Main Measure Role constraint (e.g. additivity) and
 * that has an applicable candidate {@link Algorithm}, it runs the algorithm over that measure and builds
 * one {@link HolisticHighlight} composed of its {@link ElementaryHighlight}s. Cube-schema resolution is
 * delegated to {@link CubeSchemaResolver}.
 */
public final class HighlightExtractor {

    private final Interestingness interestingness;

    public HighlightExtractor() { this(null); }

    /** Uses the given interestingness source as a post-pass to score the extracted highlights' facets. */
    public HighlightExtractor(Interestingness interestingness) { this.interestingness = interestingness; }

    public HighlightSet extract(LabeledResult result, List<ArchetypeProperty> candidates,
                                CubeSchemaResolver schema) {
        List<Highlight> out = new ArrayList<>();
        List<Level> explanators = schema.resolveExplanators(result.query);

        List<QueryMeasure> measures = result.query == null
                ? Collections.<QueryMeasure>emptyList() : result.query.getQueryMeasures();
        List<Labeling> labelings = result.labelings();

        for (ArchetypeProperty archetype : candidates) {
            ExecutableAlgorithm algorithm = applicableAlgorithm(archetype, result);
            if (algorithm == null) continue;

            if (archetype.axis == EvaluationAxis.LABELING) {
                for (int i = 0; i < labelings.size(); i++) {
                    out.add(buildHolistic(result, archetype, algorithm, schema, i,
                            schema.resolveMeasure(null), explanators));
                }
            } else if (measures.isEmpty()) {
                evaluateMeasure(result, archetype, algorithm, schema, 0, AggregationFunction.UNKNOWN,
                        schema.resolveMeasure(null), explanators, out);
            } else {
                for (int index = 0; index < measures.size(); index++) {
                    QueryMeasure qm = measures.get(index);
                    evaluateMeasure(result, archetype, algorithm, schema, index, qm.getAggregationFunction(),
                            schema.resolveMeasure(qm.getName()), explanators, out);
                }
            }
        }

        // Interestingness is a post-pass over the whole set: facets are relative (peculiarity vs the
        // rest, novelty vs the session), so they cannot be scored per holistic in isolation.
        if (interestingness != null) interestingness.score(out);
        return new HighlightSet(out);
    }

    /** Evaluates one candidate archetype against a single main measure (its column index + resolved Measure). */
    private void evaluateMeasure(LabeledResult result, ArchetypeProperty archetype, ExecutableAlgorithm algorithm,
                                 CubeSchemaResolver schema, int measureIndex, AggregationFunction aggregation,
                                 Measure mainMeasure, List<Level> explanators, List<Highlight> out) {
        if (archetype.mainMeasureRole.constraint == MeasureConstraint.ADDITIVE && !aggregation.additive) return;
        out.add(buildHolistic(result, archetype, algorithm, schema, measureIndex, mainMeasure, explanators));
    }

    private ExecutableAlgorithm applicableAlgorithm(ArchetypeProperty archetype, LabeledResult result) {
        for (Algorithm algorithm : archetype.candidateAlgorithms) {
            ExecutableAlgorithm executable = (ExecutableAlgorithm) algorithm;
            if (executable.appliesTo(result)) return executable;
        }
        return null;
    }

    private HolisticHighlight buildHolistic(LabeledResult result, ArchetypeProperty archetype,
                                            ExecutableAlgorithm algorithm, CubeSchemaResolver schema,
                                            int measureIndex, Measure mainMeasure, List<Level> explanators) {
        AlgorithmExecution execution = algorithm.run(result, measureIndex);

        HolisticHighlight holistic = new HolisticHighlight(
                result.data, archetype, execution, mainMeasure, explanators);
        execution.holisticScores.forEach(holistic::addScore);

        for (ScoredFinding sf : execution.salient) {
            ElementaryHighlight elementary = new ElementaryHighlight(
                    result.data, schema.charactersOf(sf.dimensionIndices, sf.members, explanators),
                    new MeasureValue(mainMeasure, sf.value),
                    sf.role);
            sf.scores.forEach(elementary::addScore);
            holistic.addElementary(elementary);
        }
        return holistic;
    }
}
