package result.highlights.instance;

import result.highlights.metamodel.ArchetypeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import result.Result;

/**
 * A structured testimony that an {@link ArchetypeProperty} holds over the entire dataset,
 * produced by an algorithm execution over a Main Measure and a set of Explanators, carrying
 * the result, significance scores, and any elementary highlights that detail it.
 *
 * A holistic highlight always materializes an archetype property (via an algorithm execution):
 * archetype-less holistics are not allowed.
 */
public class HolisticHighlight extends Highlight {
    public final ArchetypeProperty archetype;
    public final AlgorithmExecution execution;
    public final Measure mainMeasure;
    public final List<Level> explanators;
    public final AlgorithmResult result;
    public final List<ElementaryHighlight> elementary = new ArrayList<>();

    public HolisticHighlight(Result dataset, ArchetypeProperty archetype, AlgorithmExecution execution,
                             Measure mainMeasure, List<Level> explanators, AlgorithmResult result) {
        super(dataset);
        this.archetype = Objects.requireNonNull(archetype,
                "A holistic highlight must materialize an archetype property");
        this.execution = Objects.requireNonNull(execution,
                "A holistic highlight must record the algorithm execution that produced it");
        this.mainMeasure = mainMeasure;
        this.explanators = explanators;
        this.result = result;
    }

    public HolisticHighlight addElementary(ElementaryHighlight e) { elementary.add(e); return this; }

    @Override
    public String toText() {
        String explanatorNames = explanators.stream().map(Level::getName).collect(Collectors.joining(", "));
        String scoreText = scores.stream().map(Score::toString).collect(Collectors.joining(", "));
        String measureName = mainMeasure == null ? "(unresolved measure)" : mainMeasure.getName();
        return String.format(
                "The %s for %s, tested via %s and supported by {%s}, results in %s with {%s}.",
                archetype.name, measureName, execution.name,
                explanatorNames, result.verdict, scoreText);
    }
}
