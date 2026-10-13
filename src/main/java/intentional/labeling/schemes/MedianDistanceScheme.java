package intentional.labeling.schemes;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;

/**
 * A data-driven {@link LabelingScheme} that labels each value {@code Low}/{@code OK}/{@code High} by its
 * distance from the values' own median, OK within half the median's magnitude. A zero median fails.
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

    private double lowerBound;
    private double upperBound;
    private boolean fitted;

    @Override
    public void fit(Collection<Double> values) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double value : values) stats.addValue(value);
        double median = stats.getPercentile(50);
        if (median == 0.0) {
            throw new IllegalStateException(NAME + " labels relative to the median, which is zero here;"
                    + " use ZScoreBased or EquiDepth for values centered on zero");
        }
        this.lowerBound = median - (1.0 - LOW_MEDIAN_FACTOR) * Math.abs(median);
        this.upperBound = median + (HIGH_MEDIAN_FACTOR - 1.0) * Math.abs(median);
        this.fitted = true;
    }

    @Override
    public String name() { return NAME; }

    @Override
    public String applyLabels(double value) {
        if (!fitted) {
            throw new IllegalStateException(
                    "The scheme labels relative to the value distribution; fit it before labeling");
        }
        if (value > upperBound) return HIGH;
        if (value < lowerBound) return LOW;
        return OK;
    }

    @Override
    public LabelDomain domain() {
        return DOMAIN;
    }
}
