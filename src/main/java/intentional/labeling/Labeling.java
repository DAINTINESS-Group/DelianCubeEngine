package intentional.labeling;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import result.Cell;

/**
 * The record of labeling a result: a per-cell quantity — a measure's values, a delta against a benchmark —
 * labeled under a {@link LabelingScheme}. Constructing the labeling is the act of labeling: the scheme is
 * fitted on the quantity's values and applied to each cell right here, so the labels, the domain and the
 * magnitudes cannot disagree. The scheme itself is not retained; its name and {@link LabelDomain} are
 * snapshot at construction. Cell-keyed by identity over the result's cells.
 *
 * <p>A labeling is about one measure of the result, named by {@link #measureIndex()}. The labeled quantity
 * is readable per cell as its magnitude via {@link #magnitudeOf(Cell)} — e.g. to rank the salient cells —
 * and the model may attach the reference value each cell was judged against (a benchmark, an expectation),
 * read via {@link #referenceOf(Cell)}. When the domain is {@link #ordered()}, a label's position in
 * {@link #domain()} is its rank — the property an archetype uses to reason about tendency or severity.
 */
public final class Labeling {

    private final LabelingScheme scheme;
    private final Map<Cell, String> assignment;
    private final int measureIndex;
    private final Map<Cell, Double> magnitudes;
    private final Map<Cell, Double> references;

    public Labeling(LabelingScheme scheme, Map<Cell, Double> quantityByCell) {
        this(scheme, quantityByCell, 0, Collections.<Cell, Double>emptyMap());
    }

    public Labeling(LabelingScheme scheme, Map<Cell, Double> quantityByCell, int measureIndex) {
        this(scheme, quantityByCell, measureIndex, Collections.<Cell, Double>emptyMap());
    }

    /** Labels the quantity under the scheme: fits the scheme on the values, then labels each cell by its own. */
    public Labeling(LabelingScheme scheme, Map<Cell, Double> quantityByCell, int measureIndex,
                    Map<Cell, Double> referencesByCell) {
        this(scheme, quantityByCell, measureIndex, referencesByCell, quantityByCell);
    }

    /**
     * A labeling whose labels are driven by one quantity but whose magnitudes and references come from
     * elsewhere — the consensus, whose labels follow its bucket ranks while its magnitudes and references are
     * inherited from the group it summarizes, so a weighted election reads real volume, not the ranks.
     */
    public static Labeling withInheritedMagnitudes(LabelingScheme scheme, Map<Cell, Double> labelDriver,
            int measureIndex, Map<Cell, Double> magnitudesByCell, Map<Cell, Double> referencesByCell) {
        return new Labeling(scheme, labelDriver, measureIndex, referencesByCell, magnitudesByCell);
    }

    private Labeling(LabelingScheme scheme, Map<Cell, Double> quantityByCell, int measureIndex,
                     Map<Cell, Double> referencesByCell, Map<Cell, Double> magnitudesByCell) {
        scheme.fit(quantityByCell.values());
        Map<Cell, String> labeled = new LinkedHashMap<>();
        for (Map.Entry<Cell, Double> entry : quantityByCell.entrySet()) {
            labeled.put(entry.getKey(), scheme.applyLabels(entry.getValue()));
        }
        this.scheme = scheme;
        this.assignment = labeled;
        this.measureIndex = measureIndex;
        this.magnitudes = magnitudesByCell;
        this.references = referencesByCell;
    }

    /** The name of the scheme the cells were labeled under. */
    public String schemeName() {
        return scheme.name();
    }

    /** The label set; in order when {@link #ordered()}. */
    public List<String> domain() {
        return scheme.domain().labels();
    }

    public boolean ordered() {
        return scheme.domain().ordered();
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
        return scheme.domain().rankOf(label);
    }

    /** Which measure of the result this labeling is about. */
    public int measureIndex() {
        return measureIndex;
    }

    /** The labeled quantity at the cell, or {@code NaN} if the cell is not labeled. */
    public double magnitudeOf(Cell cell) {
        Double magnitude = magnitudes.get(cell);
        return magnitude == null ? Double.NaN : magnitude;
    }

    /** The reference value the cell was judged against, or {@code NaN} if none was attached. */
    public double referenceOf(Cell cell) {
        Double reference = references.get(cell);
        return reference == null ? Double.NaN : reference;
    }
}
