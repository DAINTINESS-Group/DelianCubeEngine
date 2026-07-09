package result.highlights;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.metamodel.EvaluationAxis;
import result.highlights.metamodel.MeasureConstraint;
import result.highlights.instance.AlgorithmExecution;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.ElementaryHighlight;
import result.highlights.instance.Highlight;
import result.highlights.instance.HolisticHighlight;
import result.highlights.instance.MeasureValue;
import result.highlights.instance.ScoredFinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cubemanager.cubebase.AggregationFunction;
import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import cubemanager.cubebase.QueryMeasure;
import model.labeling.Labeling;

/**
 * Stage 2 of the pipeline: runs the data-driven archetype evaluation over an {@link OperatorResult} and
 * produces highlights. Each candidate {@link ArchetypeProperty} is evaluated once per query measure: for a
 * measure whose aggregation satisfies the archetype's Main Measure Role constraint (e.g. additivity) and
 * that has an applicable candidate {@link Algorithm}, it runs the algorithm over that measure and builds
 * one {@link HolisticHighlight} composed of its {@link ElementaryHighlight}s. Cube-schema resolution is
 * delegated to {@link CubeSchemaResolver}.
 */
public final class HighlightExtractor {

    private final Interestingness interestingness;

    public HighlightExtractor() { this(null); }

    /** Uses the given interestingness source to score each holistic's declared interestingness facets. */
    public HighlightExtractor(Interestingness interestingness) { this.interestingness = interestingness; }

    public HighlightSet extract(OperatorResult result, List<ArchetypeProperty> candidates,
                                CubeSchemaResolver schema) {
        List<Highlight> out = new ArrayList<>();
        List<Level> explanators = schema.resolveExplanators(result.query);
        if (interestingness != null) interestingness.observe(result.query, result.data);

        List<QueryMeasure> measures = result.query == null
                ? Collections.<QueryMeasure>emptyList() : result.query.getQueryMeasures();
        List<Labeling> labelings = result.labelings();

        for (ArchetypeProperty archetype : candidates) {
            Algorithm algorithm = applicableAlgorithm(archetype, result);
            if (algorithm == null) continue;

            if (archetype.axis == EvaluationAxis.LABELING) {
                for (int i = 0; i < labelings.size(); i++) {
                    out.add(buildHolistic(result, archetype, algorithm, schema, i, null, explanators));
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
        return new HighlightSet(out);
    }

    /** Evaluates one candidate archetype against a single main measure (its column index + resolved Measure). */
    private void evaluateMeasure(OperatorResult result, ArchetypeProperty archetype, Algorithm algorithm,
                                 CubeSchemaResolver schema, int measureIndex, AggregationFunction aggregation,
                                 Measure mainMeasure, List<Level> explanators, List<Highlight> out) {
        if (archetype.mainMeasureRole.constraint == MeasureConstraint.ADDITIVE && !aggregation.additive) return;
        out.add(buildHolistic(result, archetype, algorithm, schema, measureIndex, mainMeasure, explanators));
    }

    private Algorithm applicableAlgorithm(ArchetypeProperty archetype, OperatorResult result) {
        for (Algorithm algorithm : archetype.candidateAlgorithms) {
            if (algorithm.appliesTo(result)) return algorithm;
        }
        return null;
    }

    private HolisticHighlight buildHolistic(OperatorResult result, ArchetypeProperty archetype,
                                            Algorithm algorithm, CubeSchemaResolver schema,
                                            int measureIndex, Measure mainMeasure, List<Level> explanators) {
        ArchetypeResult tested = algorithm.run(result, measureIndex);
        AlgorithmExecution execution = tested.execution;

        HolisticHighlight holistic = new HolisticHighlight(
                result.data, archetype, execution, mainMeasure, explanators, execution.result);
        tested.holisticScores.forEach(holistic::addScore);
        if (interestingness != null) {
            interestingness.scores(archetype.hhScoreTypes, result.query, result.data).forEach(holistic::addScore);
        }

        for (ScoredFinding sf : tested.elementary) {
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
