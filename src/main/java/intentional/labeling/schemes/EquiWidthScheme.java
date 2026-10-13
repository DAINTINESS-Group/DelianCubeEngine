package intentional.labeling.schemes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;

/**
 * A data-driven {@link LabelingScheme} that splits the value range into k intervals of equal width, one per
 * label of its ordered domain. Intervals are upper-inclusive; values beyond the fitted range land in the
 * end buckets. Must be {@link #fit(Collection) fitted} before it labels.
 */
public class EquiWidthScheme implements LabelingScheme {

    /** Identifies this scheme's labelings within a result. */
    public static final String NAME = "EquiWidth";

    private static final List<String> DEFAULT_LABELS = Arrays.asList("Low", "OK", "High");

    private final LabelDomain domain;
    private double min;
    private double width;
    private boolean fitted;

    public EquiWidthScheme() {
        this(DEFAULT_LABELS);
    }

    /** The labels of the intervals, from the lowest interval up. */
    public EquiWidthScheme(List<String> orderedLabels) {
        if (orderedLabels.size() < 2) {
            throw new IllegalArgumentException("Equi-width bucketing needs at least two labels");
        }
        this.domain = new LabelDomain(orderedLabels, true);
    }

    /** Derives the interval width: the fitted range divided by the number of labels. */
    @Override
    public void fit(Collection<Double> values) {
        double lowest = Double.POSITIVE_INFINITY;
        double highest = Double.NEGATIVE_INFINITY;
        for (double value : values) {
            lowest = Math.min(lowest, value);
            highest = Math.max(highest, value);
        }
        this.min = lowest;
        this.width = (highest - lowest) / domain.labels().size();
        this.fitted = true;
    }

    /** The label of the interval the value falls in. */
    @Override
    public String applyLabels(double value) {
        if (!fitted) {
            throw new IllegalStateException(
                    "The scheme splits the value range; fit it before labeling");
        }
        int k = domain.labels().size();
        if (width <= 0.0) return domain.labels().get(0);
        for (int i = 0; i < k - 1; i++) {
            if (value <= min + (i + 1) * width) return domain.labels().get(i);
        }
        return domain.labels().get(k - 1);
    }

    @Override
    public String name() { return NAME; }

    @Override
    public LabelDomain domain() {
        return domain;
    }
}
