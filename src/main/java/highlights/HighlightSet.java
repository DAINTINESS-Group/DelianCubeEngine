package highlights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import highlights.instance.Character;
import highlights.instance.ElementaryHighlight;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.Score;

/**
 * A first-class relation of highlights and the closed algebra over it: every operation returns a
 * new HighlightSet, so analyses compose (prune, expand, top-k, union, project). This is the value
 * that flows through the highlight pipeline and that operators can consume.
 *
 * For the SQL-like environment it also exposes a normalized (1NF) relational view over three
 * relations: HIGHLIGHT, SCORE (multi-valued), CHARACTER (multi-valued). The row index is the id.
 */
public final class HighlightSet {

    private final List<Highlight> highlights;

    public HighlightSet(List<Highlight> highlights) {
        this.highlights = new ArrayList<>(highlights);
    }

    public static HighlightSet of(List<Highlight> highlights) { return new HighlightSet(highlights); }

    public static HighlightSet empty() { return new HighlightSet(new ArrayList<>()); }

    public List<Highlight> highlights() { return Collections.unmodifiableList(highlights); }

    public int size() { return highlights.size(); }

    public boolean isEmpty() { return highlights.isEmpty(); }

    // ----- algebra (closed: each returns a HighlightSet) -----

    /** sigma / WHERE: keep highlights satisfying the predicate. */
    public HighlightSet prune(Predicate<Highlight> keep) {
        return new HighlightSet(highlights.stream().filter(keep).collect(Collectors.toList()));
    }

    /** Keep highlights carrying a score of the given type with |value| >= theta. */
    public HighlightSet pruneByScore(String scoreType, double theta) {
        return prune(h -> h.getScores().stream()
                .anyMatch(s -> s.type.name().equals(scoreType) && Math.abs(s.value) >= theta));
    }

    /** flatMap: replace each highlight by zero or more highlights (e.g. holistic -> its elementary). */
    public HighlightSet expand(Function<Highlight, List<Highlight>> f) {
        List<Highlight> out = new ArrayList<>();
        for (Highlight h : highlights) out.addAll(f.apply(h));
        return new HighlightSet(out);
    }

    /** ORDER BY |score| DESC, LIMIT k. Highlights lacking the score sort last; k is clamped to [0, size]. */
    public HighlightSet topK(String scoreType, int k) {
        List<Highlight> sorted = new ArrayList<>(highlights);
        sorted.sort((a, b) -> Double.compare(scoreValue(b, scoreType), scoreValue(a, scoreType)));
        int limit = Math.max(0, Math.min(k, sorted.size()));
        return new HighlightSet(sorted.subList(0, limit));
    }

    /** Set union with another HighlightSet. */
    public HighlightSet union(HighlightSet other) {
        List<Highlight> out = new ArrayList<>(highlights);
        out.addAll(other.highlights);
        return new HighlightSet(out);
    }

    /** Restrict to a kind/shape of highlight. */
    public HighlightSet project(Predicate<Highlight> kindFilter) { return prune(kindFilter); }

    /** The magnitude of a highlight's score of the given type, matching {@link #pruneByScore}; -inf if absent. */
    private static double scoreValue(Highlight h, String scoreType) {
        return h.getScores().stream()
                .filter(s -> s.type.name().equals(scoreType))
                .mapToDouble(s -> Math.abs(s.value))
                .findFirst()
                .orElse(Double.NEGATIVE_INFINITY);
    }

    // ----- normalized relational views (1NF) for the SQL-like surface -----

    /** HIGHLIGHT(id, kind, archetype, role, mainMeasure, value, datasetRef) */
    public List<String[]> asHighlightRelation() {
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < highlights.size(); i++) {
            Highlight h = highlights.get(i);
            String id = String.valueOf(i);
            if (h instanceof HolisticHighlight) {
                HolisticHighlight hh = (HolisticHighlight) h;
                rows.add(new String[]{ id, "HOLISTIC", hh.archetype.name, "",
                        hh.mainMeasure == null ? "" : hh.mainMeasure.getName(), "", datasetRef(h) });
            } else {
                ElementaryHighlight eh = (ElementaryHighlight) h;
                String measureName = (eh.measureValue == null || eh.measureValue.measureType == null)
                        ? "" : eh.measureValue.measureType.getName();
                String value = eh.measureValue == null ? "" : String.valueOf(eh.measureValue.value);
                rows.add(new String[]{ id, "ELEMENTARY", "", eh.role.name(), measureName, value, datasetRef(h) });
            }
        }
        return rows;
    }

    /** SCORE(highlightId, scoreType, value, label) */
    public List<String[]> asScoreRelation() {
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < highlights.size(); i++) {
            for (Score s : highlights.get(i).getScores()) {
                rows.add(new String[]{ String.valueOf(i), s.type.name(),
                        String.valueOf(s.value), s.label == null ? "" : s.label });
            }
        }
        return rows;
    }

    /** CHARACTER(highlightId, level, member) — elementary highlights only. */
    public List<String[]> asCharacterRelation() {
        List<String[]> rows = new ArrayList<>();
        for (int i = 0; i < highlights.size(); i++) {
            Highlight h = highlights.get(i);
            if (h instanceof ElementaryHighlight) {
                for (Character c : ((ElementaryHighlight) h).characters) {
                    rows.add(new String[]{ String.valueOf(i), c.type.getName(), c.id });
                }
            }
        }
        return rows;
    }

    private static String datasetRef(Highlight h) {
        return h.getDataset() == null ? "" : Integer.toHexString(System.identityHashCode(h.getDataset()));
    }

}
