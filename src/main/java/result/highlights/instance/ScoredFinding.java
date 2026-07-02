package result.highlights.instance;

import result.highlights.metamodel.ElementaryHighlightRole;

import java.util.List;

import result.Cell;

/**
 * A salient finding an archetype surfaces as an elementary highlight: the dimension members that
 * characterize it (by group-by position), the measure value to report, the {@link ElementaryHighlightRole}
 * it plays, and its scores. A whole result cell binds every position ({@link #ofCell}); a marginal
 * contributor binds a single dimension with its marginal value ({@link #marginal}).
 */
public final class ScoredFinding {
    public final int[] dimensionIndices;
    public final String[] members;
    public final double value;
    public final ElementaryHighlightRole role;
    public final List<Score> scores;

    public ScoredFinding(int[] dimensionIndices, String[] members, double value,
                         ElementaryHighlightRole role, List<Score> scores) {
        this.dimensionIndices = dimensionIndices;
        this.members = members;
        this.value = value;
        this.role = role;
        this.scores = scores;
    }

    public static ScoredFinding ofCell(Cell cell, ElementaryHighlightRole role, List<Score> scores) {
        List<String> members = cell.getDimensionMembers();
        int[] indices = new int[members.size()];
        for (int i = 0; i < indices.length; i++) indices[i] = i;
        return new ScoredFinding(indices, members.toArray(new String[0]), cell.toDouble(), role, scores);
    }

    public static ScoredFinding marginal(int dimensionIndex, String member, double value,
                                         ElementaryHighlightRole role, List<Score> scores) {
        return new ScoredFinding(new int[]{dimensionIndex}, new String[]{member}, value, role, scores);
    }
}
