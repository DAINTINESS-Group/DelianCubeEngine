package highlights.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import cubemanager.cubebase.Level;
import cubemanager.cubebase.Measure;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import result.Result;

/**
 * A highlight over the entire dataset, built from a {@link ModelResult} over a Main Measure and a set of
 * Explanators, carrying its elementary highlights.
 */
public class HolisticHighlight extends Highlight {
    public final String archetypeName;
    public final ModelResult modelResult;
    public final Measure mainMeasure;
    public final List<Level> explanators;
    private final List<ElementaryHighlight> elementary = new ArrayList<>();

    public HolisticHighlight(Result dataset, String archetypeName, ModelResult modelResult,
                             Measure mainMeasure, List<Level> explanators) {
        super(dataset);
        this.archetypeName = Objects.requireNonNull(archetypeName);
        this.modelResult = Objects.requireNonNull(modelResult);
        this.mainMeasure = mainMeasure;
        this.explanators = explanators;
    }

    public HolisticHighlight addElementary(ElementaryHighlight e) { elementary.add(e); return this; }

    public List<ElementaryHighlight> elementary() { return Collections.unmodifiableList(elementary); }

    private String instantiation() {
        String name = modelResult.modelName();
        String params = modelResult.parameters().stream()
                .map(ParameterInstantiation::toString)
                .collect(Collectors.joining(", "));
        return params.isEmpty() ? name : name + "(" + params + ")";
    }

    @Override
    public String toText() {
        String explanatorNames = explanators.stream().map(Level::getName).collect(Collectors.joining(", "));
        String measureName = mainMeasure == null ? "(unresolved measure)" : mainMeasure.getName();
        String metrics = modelResult.metrics().entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
        String verdict = metrics.isEmpty()
                ? String.valueOf(modelResult.verdict())
                : modelResult.verdict() + " [" + metrics + "]";
        String characterization = modelResult.holisticLabel() != null
                ? modelResult.holisticLabel()
                : String.valueOf(modelResult.holisticMagnitude());
        return String.format(
                "The %s for %s, tested via %s and supported by {%s}, results in %s with %s.",
                archetypeName, measureName, instantiation(), explanatorNames, verdict, characterization);
    }
}
