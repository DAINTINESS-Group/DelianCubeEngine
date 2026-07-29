package intentional.labeling;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import result.Cell;
import result.Result;

/**
 * A {@link LabelingModel} that applies one {@link LabelingScheme} to one measure of a result: the scheme is
 * fitted on the measure's values, each cell is labeled by its value, and the value rides along as the
 * labeling's magnitude. The model is named after its scheme, so a result's labelings read as the schemes
 * that produced them.
 */
public final class MeasureLabelingModel implements LabelingModel {

    private final Result data;
    private final int measureIndex;
    private final LabelingScheme scheme;
    private Labeling labeling;

    public MeasureLabelingModel(Result data, LabelingScheme scheme) {
        this(data, 0, scheme);
    }

    public MeasureLabelingModel(Result data, int measureIndex, LabelingScheme scheme) {
        this.data = data;
        this.measureIndex = measureIndex;
        this.scheme = scheme;
    }

    /** Fits the scheme and labels the cells; returns 0 if OK, -1 if there are no cells. */
    public int compute() {
        List<Cell> cells = data.getCells();
        if (cells.isEmpty()) {
            return -1;
        }
        Map<Cell, Double> valueByCell = new LinkedHashMap<>();
        for (Cell cell : cells) valueByCell.put(cell, cell.toDouble(measureIndex));
        this.labeling = new Labeling(scheme, valueByCell, measureIndex);
        return 0;
    }

    @Override
    public String getModelName() { return scheme.name(); }

    /** The labeling the scheme produced; empty before {@link #compute()} has run. */
    @Override
    public List<Labeling> labelings() {
        return labeling == null ? Collections.<Labeling>emptyList() : Collections.singletonList(labeling);
    }
}
