package intentional.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import intentional.labeling.Labeling;

/**
 * The generic {@link ModelResult} an archetype model produces.
 */
public final class ModelResultImpl implements ModelResult {

    private final String modelName;
    private final boolean verdict;
    private final Labeling labelling;
    private final List<ParameterInstantiation> parameters;
    private final Map<String, Double> metrics = new LinkedHashMap<>();
    private String holisticLabel;
    private double holisticMagnitude = Double.NaN;
    private String measureName;
    private ModelOrigin origin = ModelOrigin.ARCHETYPE;

    public ModelResultImpl(String modelName, boolean verdict, Labeling labelling,
                    List<ParameterInstantiation> parameters) {
        this.modelName = modelName;
        this.verdict = verdict;
        this.labelling = labelling;
        this.parameters = parameters == null ? Collections.<ParameterInstantiation>emptyList() : parameters;
    }

    /** Sets the dataset-level characterization; returns this for chaining. */
    public ModelResultImpl holistic(String label, double magnitude) {
        this.holisticLabel = label;
        this.holisticMagnitude = magnitude;
        return this;
    }

    /** Sets the studied measure name; returns this for chaining. */
    public ModelResultImpl measure(String measureName) {
        this.measureName = measureName;
        return this;
    }

    /** Sets which side produced this result; returns this for chaining. */
    public ModelResultImpl origin(ModelOrigin origin) {
        this.origin = origin;
        return this;
    }

    /** Records an auxiliary metric; returns this for chaining. */
    public ModelResultImpl metric(String key, double value) {
        metrics.put(key, value);
        return this;
    }

    @Override public String modelName() { return modelName; }
    @Override public ModelOrigin origin() { return origin; }
    @Override public boolean verdict() { return verdict; }
    @Override public Labeling labelling() { return labelling; }
    @Override public String holisticLabel() { return holisticLabel; }
    @Override public double holisticMagnitude() { return holisticMagnitude; }
    @Override public String measureName() { return measureName; }
    @Override public List<ParameterInstantiation> parameters() { return parameters; }
    @Override public Map<String, Double> metrics() { return Collections.unmodifiableMap(metrics); }
}
