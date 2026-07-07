package result.highlights;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.CubeQuery;
import interestingnessengine.InterestingnessManager;
import result.Result;
import result.highlights.instance.Score;
import result.highlights.metamodel.InterestingnessFacet;
import result.highlights.metamodel.ScoreType;

/**
 * Computes the significance of holistic highlights via the engine's {@code interestingnessengine}: each
 * {@link InterestingnessFacet} maps to an interestingness measure evaluated over a query result. The
 * measures are session-relative, so {@link #observe(CubeQuery, Result)} registers the current result before
 * scoring; facets that cannot be computed for the available session state are omitted.
 */
public final class Significance {

    private static final Map<InterestingnessFacet, String> ENGINE_MEASURE =
            new EnumMap<>(InterestingnessFacet.class);
    static {
        ENGINE_MEASURE.put(InterestingnessFacet.PECULIARITY, "Partial Syntactic Average Peculiarity");
        ENGINE_MEASURE.put(InterestingnessFacet.NOVELTY, "Direct Novelty");
        ENGINE_MEASURE.put(InterestingnessFacet.RELEVANCE, "Partial Detailed Extensional Relevance");
        ENGINE_MEASURE.put(InterestingnessFacet.SURPRISE, "Value Surprise");
    }

    private final InterestingnessManager manager;

    public Significance(InterestingnessManager manager) { this.manager = manager; }

    /** Registers the query result as the current session state for subsequent facet computations. */
    public void observe(CubeQuery query, Result data) { manager.updateState(query, data); }

    /** Scores every interestingness facet among the given score types over the current query result. */
    public List<Score> scores(List<ScoreType> scoreTypes, CubeQuery query, Result data) {
        List<Score> out = new ArrayList<>();
        for (ScoreType type : scoreTypes) {
            if (type instanceof InterestingnessFacet) {
                Score score = score((InterestingnessFacet) type, query, data);
                if (score != null) out.add(score);
            }
        }
        return out;
    }

    /** The score for one facet over the current query result, or null if it cannot be computed. */
    public Score score(InterestingnessFacet facet, CubeQuery query, Result data) {
        String measure = ENGINE_MEASURE.get(facet);
        if (measure == null) return null;
        try {
            double value = manager.computeMeasure(measure, query, data);
            return Double.isNaN(value) ? null : new Score(facet, value);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
