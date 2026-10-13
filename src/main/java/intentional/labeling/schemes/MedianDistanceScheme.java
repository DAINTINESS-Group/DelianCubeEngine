package intentional.labeling.schemes;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;

/**
 * A data-driven {@link LabelingScheme} that labels each value {@code Low}/{@code OK}/{@code High} by its
 * distance from the values' own median. Must be {@link #fit(Collection) fitted} before it labels.
 */
public class MedianDistanceScheme implements LabelingScheme {

    /** Identifies this scheme's labelings within a result. */
    public static final String NAME = "KPIMedianBased";

    /** A value is High above this multiple of the median, Low below the lower one. */
    public static final double HIGH_MEDIAN_FACTOR = 1.5;
    public static final double LOW_MEDIAN_FACTOR = 0.5;

    static final String LOW = "Low";
    static final String OK = "OK";
    static final String HIGH = "High";

    /** Ordered so a label's index is its rank: Low &lt; OK &lt; High. */
    private static final LabelDomain DOMAIN = new LabelDomain(Arrays.asList(LOW, OK, HIGH), true);

    private double median;
    private boolean fitted;

    /** Derives the median the distances are taken from. */
    @Override
    public void fit(Collection<Double> values) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double value : values) stats.addValue(value);
        this.median = stats.getPercentile(50);
        this.fitted = true;
    }

    @Override
    public String name() { return NAME; }

    /** Low below half the median, High above 1.5x the median, OK in between. */
    @Override
    public String applyLabels(double value) {
        if (!fitted) {
            throw new IllegalStateException(
                    "The scheme labels relative to the value distribution; fit it before labeling");
        }
        if (value > HIGH_MEDIAN_FACTOR * median) return HIGH;
        if (value < LOW_MEDIAN_FACTOR * median) return LOW;
        return OK;
    }

    @Override
    public LabelDomain domain() {
        return DOMAIN;
    }
}
