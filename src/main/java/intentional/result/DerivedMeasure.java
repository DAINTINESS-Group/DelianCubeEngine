package intentional.result;

import java.util.Map;

import result.Cell;

/**
 * A per-cell numeric quantity a model computes over the data (a delta, a z-score). Data, not a label — it
 * rides alongside the query's base measures so an archetype can read it (e.g. to rank the salient cells)
 * without it entering the base-measure iteration. Cell-keyed by identity over the result's cells.
 */
public final class DerivedMeasure {

    private final Map<Cell, Double> values;

    public DerivedMeasure(Map<Cell, Double> values) {
        this.values = values;
    }

    /** The value at the cell, or {@code NaN} if it has none. */
    public double of(Cell cell) {
        return values.getOrDefault(cell, Double.NaN);
    }

    public boolean covers(Cell cell) {
        return values.containsKey(cell);
    }
}
