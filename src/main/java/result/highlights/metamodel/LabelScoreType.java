package result.highlights.metamodel;

import java.util.List;

/**
 * An enumerated significance facet: a closed, ordered set of labels (e.g.
 * {@code [low_effort, mid_effort, high]}). A label's score value is its rank in the order.
 */
public final class LabelScoreType implements ScoreType {
    private final String name;
    private final List<String> orderedLabels;

    public LabelScoreType(String name, List<String> orderedLabels) {
        this.name = name;
        this.orderedLabels = orderedLabels;
    }

    @Override
    public String name() { return name; }

    /** Rank of a label in the ordered domain, or -1 if it is not part of the scheme. */
    public int rankOf(String label) { return orderedLabels.indexOf(label); }

    @Override
    public int compare(double a, double b) { return Double.compare(a, b); }
}
