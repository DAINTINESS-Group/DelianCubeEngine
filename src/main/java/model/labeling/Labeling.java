package model.labeling;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import result.Cell;

/**
 * A per-cell labeling produced by a model: each cell is assigned a label from a fixed domain. When the
 * domain is {@link #ordered()}, a label's position in {@link #domain()} is its rank — the property an
 * archetype uses to reason about tendency or severity. Cell-keyed by identity over the result's cells.
 */
public final class Labeling {

    private final LabelDomain domain;
    private final Map<Cell, String> assignment;

    public Labeling(LabelDomain domain, Map<Cell, String> assignment) {
        this.domain = domain;
        this.assignment = assignment;
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
}
