package intentional.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import intentional.labeling.Labeling;

/**
 * A model's output over a dataset: a verdict, an optional dataset-level characterization, and the per-cell
 * annotation as a {@link Labeling}. {@link #labelling()} is {@code null} for a holistic-only model.
 */
public interface ModelResult {

    /** The name of the model that produced this result. */
    String modelName();

    /** Whether this result was produced by the operator or by the model-extraction sweep. */
    default ModelOrigin origin() { return ModelOrigin.ARCHETYPE; }

    /** Whether the tested property holds over the dataset. */
    boolean verdict();

    /** The per-cell annotation, or {@code null} when the model produces none. */
    Labeling labelling();

    /** The dataset-level label (a winner), or {@code null}. */
    default String holisticLabel() { return null; }

    /** The dataset-level magnitude (a concentration, a max z-score, a top share), or {@code NaN}. */
    default double holisticMagnitude() { return Double.NaN; }

    /** The name of the measure the model studied, or {@code null}. */
    default String measureName() { return null; }

    /** The parameter values the run executed with. */
    default List<ParameterInstantiation> parameters() { return Collections.emptyList(); }

    /**
     * How this result announces itself: its labeling's scheme — the model's name when it has none —
     * qualified by its labeled parameters.
     */
    default String tag() {
        StringBuilder tag = new StringBuilder(labelling() != null ? labelling().schemeName() : modelName());
        for (ParameterInstantiation parameter : parameters()) {
            if (parameter.label != null) {
                tag.append(" vs ").append(parameter.label);
            }
        }
        return tag.toString();
    }

    /** The auxiliary metrics behind the verdict. */
    default Map<String, Double> metrics() { return Collections.emptyMap(); }
}
