package intentional.labeling.schemes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import intentional.labeling.LabelDomain;
import intentional.labeling.LabelingScheme;

/**
 * A data-driven {@link LabelingScheme} that cuts the sorted values into k buckets of equal count, one per
 * label of its ordered domain. When the value count is not a multiple of k, the extremes are set aside —
 * split between the two ends, the high end first — so the remainder cuts evenly, and land back in their
 * end buckets when labeled. Must be {@link #fit(Collection) fitted} before it labels.
 */
public class EquiDepthScheme implements LabelingScheme {

    /** Identifies this scheme's labelings within a result. */
    public static final String NAME = "EquiDepth";

    private static final List<String> DEFAULT_LABELS = Arrays.asList("Low", "OK", "High");

    private final LabelDomain domain;
    private double[] upperBounds;

    public EquiDepthScheme() {
        this(DEFAULT_LABELS);
    }

    /** The labels of the buckets, from the lowest-valued bucket up. */
    public EquiDepthScheme(List<String> orderedLabels) {
        if (orderedLabels.size() < 2) {
            throw new IllegalArgumentException("Equi-depth bucketing needs at least two labels");
        }
        this.domain = new LabelDomain(orderedLabels, true);
    }

    /** Derives the bucket boundaries: each of the k buckets takes an equal share of the sorted values. */
    @Override
    public void fit(Collection<Double> values) {
        List<Double> sorted = new ArrayList<>(values);
        Collections.sort(sorted);

        int k = domain.labels().size();
        int spare = sorted.size() % k;
        int fromLow = spare / 2;
        int fromHigh = spare - fromLow;
        List<Double> kept = sorted.subList(fromLow, sorted.size() - fromHigh);

        int perBucket = Math.max(1, kept.size() / k);
        this.upperBounds = new double[k - 1];
        for (int i = 0; i < k - 1; i++) {
            int lastIndex = Math.min((i + 1) * perBucket - 1, kept.size() - 1);
            upperBounds[i] = kept.isEmpty() ? Double.NaN : kept.get(lastIndex);
        }
    }

    /** The label of the bucket the value falls in; values beyond the kept range land in the end buckets. */
    @Override
    public String applyLabels(double value) {
        if (upperBounds == null) {
            throw new IllegalStateException(
                    "The scheme buckets the value distribution; fit it before labeling");
        }
        for (int i = 0; i < upperBounds.length; i++) {
            if (value <= upperBounds[i]) return domain.labels().get(i);
        }
        return domain.labels().get(domain.labels().size() - 1);
    }

    @Override
    public String name() { return NAME; }

    @Override
    public LabelDomain domain() {
        return domain;
    }
}
