package result.highlights.instance;

import java.util.ArrayList;
import java.util.List;

import result.Result;

/**
 * The result type of the intentional model. A highlight is computed over a dataset (the query
 * {@link Result}) and carries the significance scores behind it. Specialized as a
 * {@link HolisticHighlight} (a property of the whole dataset) or an {@link ElementaryHighlight}
 * (a specific fact within it).
 */
public abstract class Highlight {
    
    protected final Result dataset;
    protected final List<Score> scores = new ArrayList<>();

    protected Highlight(Result dataset) { this.dataset = dataset; }

    public Result getDataset() { return dataset; }

    public List<Score> getScores() { return scores; }

    public Highlight addScore(Score score) { scores.add(score); return this; }

    /** A human-readable rendering, following the conceptual model's textual templates. */
    public abstract String toText();
}
