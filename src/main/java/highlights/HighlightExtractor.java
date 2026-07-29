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
import highlights.instance.LabelingAlgorithm;
import highlights.instance.MeasureAlgorithm;
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
 * produces highlights. Each candidate {@link ArchetypeProperty} is evaluated once per subject of its
 * evaluation axis — per query measure whose aggregation satisfies the archetype's Main Measure Role
 * constraint (e.g. additivity), or per labeling in the context — and each execution is built into one
 * {@link HolisticHighlight} composed of its {@link ElementaryHighlight}s. A non-holding execution still
 * yields a highlight: its verdict travels on the highlight for ranking and pruning to weigh. Cube-schema
 * resolution is delegated to {@link CubeSchemaResolver}.
 */
public final class HighlightExtractor {


    public HighlightSet extract(LabeledResult result, List<ArchetypeProperty> candidates,
                                CubeSchemaResolver schema) {
        List<Highlight> out = new ArrayList<>();
        List<Level> explanators = schema.resolveExplanators(result.query);

        List<QueryMeasure> measures = result.query == null
                ? Collections.<QueryMeasure>emptyList() : result.query.getQueryMeasures();

        for (ArchetypeProperty archetype : candidates) {
            ExecutableAlgorithm algorithm = applicableAlgorithm(archetype, result);
            if (algorithm == null) continue;

            if (archetype.axis == EvaluationAxis.LABELING) {
                LabelingAlgorithm overLabelings = (LabelingAlgorithm) algorithm;
                for (Labeling labeling : result.labelings()) {
                    out.add(buildHolistic(result, archetype, overLabelings.run(result, labeling), schema,
                            schema.resolveMeasure(null), explanators));
                }
            } else if (measures.isEmpty()) {
                evaluateMeasure(result, archetype, (MeasureAlgorithm) algorithm, schema, 0,
                        AggregationFunction.UNKNOWN, schema.resolveMeasure(null), explanators, out);
            } else {
                for (int index = 0; index < measures.size(); index++) {
                    QueryMeasure qm = measures.get(index);
                    evaluateMeasure(result, archetype, (MeasureAlgorithm) algorithm, schema, index,
                            qm.getAggregationFunction(), schema.resolveMeasure(qm.getName()), explanators, out);
                }
            }
        }
        return new HighlightSet(out);
    }

    /** Evaluates one candidate archetype against a single main measure (its column index + resolved Measure). */
    private void evaluateMeasure(LabeledResult result, ArchetypeProperty archetype, MeasureAlgorithm algorithm,
                                 CubeSchemaResolver schema, int measureIndex, AggregationFunction aggregation,
                                 Measure mainMeasure, List<Level> explanators, List<Highlight> out) {
        if (archetype.mainMeasureRole.constraint == MeasureConstraint.ADDITIVE && !aggregation.additive) return;
        out.add(buildHolistic(result, archetype, algorithm.run(result, measureIndex), schema,
                mainMeasure, explanators));
    }

    /**
     * The first candidate algorithm applicable to the result, or {@code null} when none applies. A candidate
     * whose execution contract does not match the archetype's evaluation axis is a catalog error, not a
     * non-applicability, and fails loudly.
     */
    private ExecutableAlgorithm applicableAlgorithm(ArchetypeProperty archetype, LabeledResult result) {
        Class<? extends ExecutableAlgorithm> required = archetype.axis == EvaluationAxis.LABELING
                ? LabelingAlgorithm.class : MeasureAlgorithm.class;
        for (Algorithm algorithm : archetype.candidateAlgorithms) {
            if (!required.isInstance(algorithm)) {
                throw new IllegalStateException("Archetype " + archetype.name + " evaluates over the "
                        + archetype.axis + " axis but its candidate " + algorithm.name()
                        + " is not a " + required.getSimpleName());
            }
            ExecutableAlgorithm executable = required.cast(algorithm);
            if (executable.appliesTo(result)) return executable;
        }
        return null;
    }

    private HolisticHighlight buildHolistic(LabeledResult result, ArchetypeProperty archetype,
                                            AlgorithmExecution execution, CubeSchemaResolver schema,
                                            Measure mainMeasure, List<Level> explanators) {
        HolisticHighlight holistic = new HolisticHighlight(
                result.data, archetype, execution, mainMeasure, explanators);

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
