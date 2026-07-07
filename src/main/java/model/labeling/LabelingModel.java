package model.labeling;

import java.util.Collections;
import java.util.List;

/**
 * A model that annotates the data over its cells: a set of categorical {@link Labeling}s and, optionally,
 * the {@link DerivedMeasure}s it computed on the way. An operator's {@code OperatorResult} exposes these
 * from every model it ran, and archetypes consume them by kind, without seeing which operator or model
 * produced them.
 */
public interface LabelingModel {

    List<Labeling> labelings();

    default List<DerivedMeasure> derivedMeasures() {
        return Collections.emptyList();
    }
}
