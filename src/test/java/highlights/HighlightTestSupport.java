package highlights;

import highlights.HighlightSet;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import highlights.instance.Score;

/**
 * Shared lookups for highlight-extraction tests: locate a holistic by its archetype name and read a score
 * value off it by score-type name.
 */
public final class HighlightTestSupport {

    private HighlightTestSupport() {}

    public static HolisticHighlight holisticFor(HighlightSet highlights, String archetypeName) {
        for (Highlight h : highlights.highlights()) {
            if (h instanceof HolisticHighlight
                    && ((HolisticHighlight) h).archetype.name.equals(archetypeName)) {
                return (HolisticHighlight) h;
            }
        }
        return null;
    }

    public static double scoreOf(HolisticHighlight highlight, String scoreType) {
        for (Score score : highlight.getScores()) {
            if (score.type.name().equals(scoreType)) return score.value;
        }
        return Double.NaN;
    }
}
