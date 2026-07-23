package highlights.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import highlights.metamodel.ArchetypeProperty;
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
    private final List<ElementaryHighlight> elementary = new ArrayList<>();

    public HolisticHighlight(Result dataset, ArchetypeProperty archetype, AlgorithmExecution execution,
                             Measure mainMeasure, List<Level> explanators) {
        super(dataset);
        this.archetype = Objects.requireNonNull(archetype,
                "A holistic highlight must materialize an archetype property");
        this.execution = Objects.requireNonNull(execution,
                "A holistic highlight must record the algorithm execution that produced it");
        this.mainMeasure = mainMeasure;
        this.explanators = explanators;
    }

    public HolisticHighlight addElementary(ElementaryHighlight e) { elementary.add(e); return this; }

    public List<ElementaryHighlight> elementary() { return Collections.unmodifiableList(elementary); }

    /** The algorithm instantiation that tested this highlight: the algorithm name and its parameters. */
    private String instantiation() {
        String name = execution.algorithm.name();
        String params = execution.parameters.stream()
                .map(ParameterInstantiation::toString)
                .collect(Collectors.joining(", "));
        return params.isEmpty() ? name : name + "(" + params + ")";
    }

    @Override
    public String toText() {
        String explanatorNames = explanators.stream().map(Level::getName).collect(Collectors.joining(", "));
        String scoreText = scores.stream().map(Score::toString).collect(Collectors.joining(", "));
        String measureName = mainMeasure == null ? "(unresolved measure)" : mainMeasure.getName();
        String metrics = execution.result.auxiliaryMetrics().entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
        String verdict = metrics.isEmpty()
                ? String.valueOf(execution.result.verdict())
                : execution.result.verdict() + " [" + metrics + "]";
        return String.format(
                "The %s for %s, tested via %s and supported by {%s}, results in %s with {%s}.",
                archetype.name, measureName, instantiation(),
                explanatorNames, verdict, scoreText);
    }
}
