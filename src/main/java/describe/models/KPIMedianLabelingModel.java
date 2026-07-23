package describe.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import intentional.result.DerivedMeasure;
import intentional.result.LabelDomain;
import intentional.result.Labeling;
import intentional.result.LabelingModel;
import result.Cell;
import result.Result;

/**
 * A {@link LabelingModel} that, over one measure of a result, labels each cell {@code Low}/{@code OK}/
 * {@code High} by its distance from the cells' own median and exposes the value-to-median ratio as a
 * {@link DerivedMeasure}. An operator attaches it to its {@code LabeledResult}, where the
 * {@code labelpredominance} archetype consumes the ordered labeling to surface highlights.
 */
public final class KPIMedianLabelingModel implements LabelingModel {

    /** Identifies this model's output within an LabeledResult. */
    public static final String NAME = "KPIMedianBased";

    /** A cell is High above this multiple of the median, Low below the lower one. */
    public static final double HIGH_MEDIAN_FACTOR = 1.5;
    public static final double LOW_MEDIAN_FACTOR = 0.5;

    static final String LOW = "Low";
    static final String OK = "OK";
    static final String HIGH = "High";

    /** Ordered so a label's index is its rank: Low &lt; OK &lt; High. */
    private static final LabelDomain DOMAIN = new LabelDomain(Arrays.asList(LOW, OK, HIGH), true);

    private final Result data;
    private final int measureIndex;
    private double median;
    private Labeling kpiLabeling;
    private final Map<Cell, Double> ratios = new LinkedHashMap<>();

    public KPIMedianLabelingModel(Result data) {
        this(data, 0);
    }

    public KPIMedianLabelingModel(Result data, int measureIndex) {
        this.data = data;
        this.measureIndex = measureIndex;
    }

    /** Computes the median and the per-cell labels; returns 0 if OK, -1 if there are no cells. */
    public int compute() {
        List<Cell> cells = data.getCells();
        if (cells.isEmpty()) {
            return -1;
        }
        ratios.clear();

        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Cell cell : cells) stats.addValue(cell.toDouble(measureIndex));
        this.median = stats.getPercentile(50);

        Map<Cell, String> labelByCell = new LinkedHashMap<>();
        for (Cell cell : cells) {
            double value = cell.toDouble(measureIndex);
            labelByCell.put(cell, labelFor(value));
            ratios.put(cell, median == 0.0 ? value : value / median);
        }
        this.kpiLabeling = new Labeling(DOMAIN, labelByCell);
        return 0;
    }

    /** Low below half the median, High above 1.5x the median, OK in between. */
    private String labelFor(double value) {
        if (value > HIGH_MEDIAN_FACTOR * median) return HIGH;
        if (value < LOW_MEDIAN_FACTOR * median) return LOW;
        return OK;
    }

    @Override
    public String getModelName() { return NAME; }

    @Override
    public List<Labeling> labelings() {
        return Collections.singletonList(kpiLabeling);
    }

    /** The value/median ratio per cell, as data the archetype can rank salient cells by. */
    @Override
    public List<DerivedMeasure> derivedMeasures() {
        return Collections.singletonList(new DerivedMeasure(ratios));
    }
}
