package result.highlights;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.instance.AlgorithmExecution;
import result.highlights.instance.ArchetypeResult;
import result.highlights.instance.ElementaryHighlight;
import result.highlights.instance.Highlight;
import result.highlights.instance.HolisticHighlight;
import result.highlights.instance.MeasureValue;
import result.highlights.instance.ScoredFinding;

import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;

/**
 * Stage 2 of the pipeline: runs the data-driven archetype evaluation over an {@link OperatorResult} and
 * produces highlights. For each candidate {@link ArchetypeProperty} that has an applicable candidate
 * {@link Algorithm}, it runs the algorithm and builds one {@link HolisticHighlight} (from the verdict)
 * composed of its {@link ElementaryHighlight}s (from the salient cells). Cube-schema resolution is
 * delegated to {@link CubeSchemaResolver}.
 */
public final class HighlightExtractor {

    public HighlightSet extract(OperatorResult result, List<ArchetypeProperty> candidates,
                                CubeSchemaResolver schema) {
        List<Highlight> out = new ArrayList<>();
        for (ArchetypeProperty archetype : candidates) {
            if (!measureSatisfies(archetype, result)) continue;
            Algorithm algorithm = applicableAlgorithm(archetype, result);
            if (algorithm != null) {
                out.add(buildHolistic(result, archetype, algorithm, schema));
            }
        }
        return new HighlightSet(out);
    }

    /** Whether the result's measure satisfies the archetype's Main Measure Role constraint (e.g. additivity). */
    private boolean measureSatisfies(ArchetypeProperty archetype, OperatorResult result) {
        String aggregation = result.query == null ? null : result.query.getAggregateFunction();
        return archetype.mainMeasureRole.constraint.accepts(aggregation);
    }

    private Algorithm applicableAlgorithm(ArchetypeProperty archetype, OperatorResult result) {
        for (Algorithm algorithm : archetype.candidateAlgorithms) {
            if (algorithm.appliesTo(result)) return algorithm;
        }
        return null;
    }

    private HolisticHighlight buildHolistic(OperatorResult result, ArchetypeProperty archetype,
                                            Algorithm algorithm, CubeSchemaResolver schema) {
        ArchetypeResult tested = algorithm.run(result);
        AlgorithmExecution execution = tested.execution;

        Measure mainMeasure = schema.resolveMainMeasure(result.query);
        List<Level> explanators = schema.resolveExplanators(result.query);

        HolisticHighlight holistic = new HolisticHighlight(
                result.data, archetype, execution, mainMeasure, explanators, execution.result);
        tested.holisticScores.forEach(holistic::addScore);

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
