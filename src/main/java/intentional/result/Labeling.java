package intentional.result;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import result.Cell;

/**
 * A per-cell labeling produced by a model: each cell is assigned a label from a fixed domain. When the
 * domain is {@link #ordered()}, a label's position in {@link #domain()} is its rank — the property an
 * archetype uses to reason about tendency or severity. Cell-keyed by identity over the result's cells.
 *
 * <p>A labeling is about one measure of the result, named by {@link #measureIndex()}. The model may also
 * attach per-cell data it computed while labeling: the magnitude of what it found (a delta, a ratio), read
 * via {@link #magnitudeOf(Cell)} e.g. to rank the salient cells, and the reference value it judged the cell
 * against (a benchmark, an expectation), read via {@link #referenceOf(Cell)}.
 */
public final class Labeling {

    private final LabelDomain domain;
    private final Map<Cell, String> assignment;
    private final int measureIndex;
    private final Map<Cell, Double> magnitudes;
    private final Map<Cell, Double> references;

    public Labeling(LabelDomain domain, Map<Cell, String> assignment) {
        this(domain, assignment, 0, Collections.<Cell, Double>emptyMap(), Collections.<Cell, Double>emptyMap());
    }

    public Labeling(LabelDomain domain, Map<Cell, String> assignment, int measureIndex,
                    Map<Cell, Double> magnitudes) {
        this(domain, assignment, measureIndex, magnitudes, Collections.<Cell, Double>emptyMap());
    }

    public Labeling(LabelDomain domain, Map<Cell, String> assignment, int measureIndex,
                    Map<Cell, Double> magnitudes, Map<Cell, Double> references) {
        this.domain = domain;
        this.assignment = assignment;
        this.measureIndex = measureIndex;
        this.magnitudes = magnitudes;
        this.references = references;
    }

    /** The label set; in order when {@link #ordered()}. */
    public List<String> domain() {
        return domain.labels();
    }

    public boolean ordered() {
        return domain.ordered();
    }

    /** The label assigned to the cell, or {@code null} if it is not labeled. */
    public String of(Cell cell) {
        return assignment.get(cell);
    }

    public boolean covers(Cell cell) {
        return assignment.containsKey(cell);
    }

    /** The per-cell assignment, read-only and in production order. */
    public Map<Cell, String> assignment() {
        return Collections.unmodifiableMap(assignment);
    }

    /** The rank of a label in an ordered domain, or -1 if it is not part of the set. */
    public int rankOf(String label) {
        return domain.rankOf(label);
    }

    /** Which measure of the result this labeling is about. */
    public int measureIndex() {
        return measureIndex;
    }

    /** The magnitude the model computed at the cell, or {@code NaN} if it attached none. */
    public double magnitudeOf(Cell cell) {
        Double magnitude = magnitudes.get(cell);
        return magnitude == null ? Double.NaN : magnitude;
    }

    /** The reference value the model judged the cell against, or {@code NaN} if it attached none. */
    public double referenceOf(Cell cell) {
        Double reference = references.get(cell);
        return reference == null ? Double.NaN : reference;
    }
}
