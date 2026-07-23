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
import highlights.instance.ArchetypeResult;
import highlights.instance.ElementaryHighlight;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.MeasureValue;
import highlights.instance.Score;
import highlights.instance.ScoredFinding;
import highlights.metamodel.Algorithm;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.EvaluationAxis;
import highlights.metamodel.MeasureConstraint;
import labeling.LabeledResult;
import labeling.Labeling;

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

    /** Uses the given interestingness source to score each holistic's declared interestingness facets. */
    public HighlightExtractor(Interestingness interestingness) { this.interestingness = interestingness; }

    public HighlightSet extract(LabeledResult result, List<ArchetypeProperty> candidates,
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

            // The archetype's interestingness facets depend only on the query result, so score them once.
            List<Score> facetScores = interestingness == null
                    ? Collections.<Score>emptyList()
                    : interestingness.scores(archetype.hhScoreTypes, result.query, result.data);

            if (archetype.axis == EvaluationAxis.LABELING) {
                for (int i = 0; i < labelings.size(); i++) {
                    out.add(buildHolistic(result, archetype, algorithm, schema, i,
                            schema.resolveMeasure(null), explanators, facetScores));
                }
            } else if (measures.isEmpty()) {
                evaluateMeasure(result, archetype, algorithm, schema, 0, AggregationFunction.UNKNOWN,
                        schema.resolveMeasure(null), explanators, out, facetScores);
            } else {
                for (int index = 0; index < measures.size(); index++) {
                    QueryMeasure qm = measures.get(index);
                    evaluateMeasure(result, archetype, algorithm, schema, index, qm.getAggregationFunction(),
                            schema.resolveMeasure(qm.getName()), explanators, out, facetScores);
                }
            }
        }
        return new HighlightSet(out);
    }

    /** Evaluates one candidate archetype against a single main measure (its column index + resolved Measure). */
    private void evaluateMeasure(LabeledResult result, ArchetypeProperty archetype, Algorithm algorithm,
                                 CubeSchemaResolver schema, int measureIndex, AggregationFunction aggregation,
                                 Measure mainMeasure, List<Level> explanators, List<Highlight> out,
                                 List<Score> facetScores) {
        if (archetype.mainMeasureRole.constraint == MeasureConstraint.ADDITIVE && !aggregation.additive) return;
        out.add(buildHolistic(result, archetype, algorithm, schema, measureIndex, mainMeasure, explanators,
                facetScores));
    }

    private Algorithm applicableAlgorithm(ArchetypeProperty archetype, LabeledResult result) {
        for (Algorithm algorithm : archetype.candidateAlgorithms) {
            if (algorithm.appliesTo(result)) return algorithm;
        }
        return null;
    }

    private HolisticHighlight buildHolistic(LabeledResult result, ArchetypeProperty archetype,
                                            Algorithm algorithm, CubeSchemaResolver schema,
                                            int measureIndex, Measure mainMeasure, List<Level> explanators,
                                            List<Score> facetScores) {
        ArchetypeResult tested = (ArchetypeResult) algorithm.run(result, measureIndex);
        AlgorithmExecution execution = new AlgorithmExecution(algorithm.name(), algorithm.params(), tested);

        HolisticHighlight holistic = new HolisticHighlight(
                result.data, archetype, execution, mainMeasure, explanators);
        tested.holisticScores().forEach(holistic::addScore);
        facetScores.forEach(holistic::addScore);

        for (ScoredFinding sf : tested.elementary()) {
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
