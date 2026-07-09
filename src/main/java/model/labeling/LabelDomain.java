package model.labeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An ordered (or unordered) set of labels. When {@link #ordered()}, a label's index in {@link #labels()}
 * is its rank — the property an archetype uses to reason about tendency or severity. Both the labeling
 * policy that decides labels and the {@link Labeling} that stores them speak in terms of this domain.
 */
public final class LabelDomain {

    private final List<String> labels;
    private final boolean ordered;

    public LabelDomain(List<String> labels, boolean ordered) {
        this.labels = Collections.unmodifiableList(new ArrayList<>(labels));
        this.ordered = ordered;
    }

    /** The label set; in order when {@link #ordered()}. */
    public List<String> labels() {
        return labels;
    }

    public boolean ordered() {
        return ordered;
    }

    /** The rank of a label in an ordered domain, or -1 if it is not part of the set. */
    public int rankOf(String label) {
        return labels.indexOf(label);
    }
}
