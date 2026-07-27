package highlights;

import java.util.List;

import highlights.instance.Highlight;

/**
 * Post-pass over a result's highlights: attaches interestingness to each holistic. Runs over the
 * whole set — not per holistic — because interestingness is relative: a highlight is peculiar
 * relative to the rest, novel relative to the session, relevant relative to the query, and
 * surprising relative to an expectation. Injected optionally into {@link HighlightExtractor}; when
 * absent, highlights carry only their algorithm scores.
 *
 * <p>Interestingness is defined by its <em>context</em> (data / query / session / expectation), not
 * by a fixed score-type enum, so no facet type is modelled here. Facet computation itself is not
 * yet implemented — this is the model-level seam.
 */
public final class Interestingness {

    public void score(List<Highlight> highlights) {
        // TODO: compute + attach interestingness (context-driven: data / query / session / expectation).
    }
}
