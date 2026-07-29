package intentional.labeling.schemes;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;


/**
 * A data-driven {@link LabelingScheme} that z-scores each value against the distribution of all the values
 * and labels it by the band its z-score falls in, over the fixed ordered domain far-below &lt; below &lt;
 * typical &lt; above &lt; far-above. The bands are symmetric around the mean: within the near threshold is
 * typical, strictly beyond the extreme threshold is far. Must be {@link #fit(Collection) fitted} before it
 * labels.
 */
public class ZScoreLabelingScheme implements LabelingScheme {

    /** Identifies this scheme's labelings within a result. */
    public static final String NAME = "ZScoreBased";

    public static final String FAR_BELOW = "far-below";
    public static final String BELOW = "below";
    public static final String TYPICAL = "typical";
    public static final String ABOVE = "above";
    public static final String FAR_ABOVE = "far-above";

    /** A value is typical while its absolute z-score is at most this. */
    public static final double DEFAULT_NEAR_Z = 1.0;
    /** A value is far off when its absolute z-score strictly exceeds this — keep equal to the outlier archetype's threshold so far cells and outliers coincide. */
    public static final double DEFAULT_EXTREME_Z = 2.2;

    /** Ordered so a label's index is its rank along the z spectrum. */
    private static final LabelDomain DOMAIN =
            new LabelDomain(Arrays.asList(FAR_BELOW, BELOW, TYPICAL, ABOVE, FAR_ABOVE), true);

    private final double nearZ;
    private final double extremeZ;
    private double mean;
    private double std;
    private boolean fitted;

    public ZScoreLabelingScheme() {
        this(DEFAULT_NEAR_Z, DEFAULT_EXTREME_Z);
    }

    public ZScoreLabelingScheme(double nearZ, double extremeZ) {
        if (nearZ <= 0.0 || extremeZ <= nearZ) {
            throw new IllegalArgumentException(
                    "The z bands need 0 < nearZ < extremeZ, got " + nearZ + " and " + extremeZ);
        }
        this.nearZ = nearZ;
        this.extremeZ = extremeZ;
    }

    /** Derives the mean and standard deviation the z-scores are taken against. */
    @Override
    public void fit(Collection<Double> values) {
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double value : values) stats.addValue(value);
        this.mean = stats.getMean();
        this.std = stats.getStandardDeviation();
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
        double z = std == 0.0 ? 0.0 : (value - mean) / std;
        if (z > extremeZ) return FAR_ABOVE;
        if (z < -extremeZ) return FAR_BELOW;
        if (z > nearZ) return ABOVE;
        if (z < -nearZ) return BELOW;
        return TYPICAL;
    }

    @Override
    public LabelDomain domain() {
        return DOMAIN;
    }
}
