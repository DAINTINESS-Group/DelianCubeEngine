package intentional.result;

import java.util.Collections;
import java.util.List;

/**
 * A model that annotates the data over its cells with a set of categorical {@link Labeling}s and, optionally,
 * the {@link DerivedMeasure}s it computed on the way. An operator's {@code LabeledResult} holds these and
 * exposes them by kind, so archetypes consume them without seeing which operator or model produced them.
 */
public interface LabelingModel {

    /** Identifies this model's output within an {@code LabeledResult}. */
    String getModelName();

    List<Labeling> labelings();

    default List<DerivedMeasure> derivedMeasures() {
        return Collections.emptyList();
    }
}
