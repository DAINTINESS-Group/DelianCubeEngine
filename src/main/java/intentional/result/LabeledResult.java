package intentional.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.CubeQuery;
import intentional.labeling.Labeling;
import intentional.labeling.consensus.ConsensusRule;
import result.Result;

/**
 * A cube query's {@link Result} together with the {@link Labeling}s computed over it. This is the Stage-1
 * product over which Stage-2 archetype evaluation runs; it holds data and labelings only — each labeling
 * says which scheme produced it, and no producer crosses this boundary. Constructing the result derives
 * the {@link #consensuses()} of its labelings, the way a labeling derives its own assignment.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    private final List<Labeling> labelings;
    private final List<Labeling> consensuses;

    public LabeledResult(CubeQuery query, Result data, List<Labeling> labelings) {
        this.query = query;
        this.data = data;
        this.labelings = labelings == null ? new ArrayList<Labeling>() : labelings;
        this.consensuses = deriveConsensuses(this.labelings);
    }

    /** Every per-cell labeling computed over the data. */
    public List<Labeling> labelings() {
        return Collections.unmodifiableList(labelings);
    }

    /** One consensus labeling per group of labelings sharing an ordered domain; empty below two members. */
    public List<Labeling> consensuses() {
        return Collections.unmodifiableList(consensuses);
    }

    /** Groups the labelings by their ordered domain and consenses every group of at least two. */
    private static List<Labeling> deriveConsensuses(List<Labeling> labelings) {
        Map<List<String>, List<Labeling>> groups = new LinkedHashMap<>();
        for (Labeling labeling : labelings) {
            if (!labeling.ordered()) {
                continue;
            }
            groups.computeIfAbsent(labeling.domain(), domain -> new ArrayList<>()).add(labeling);
        }
        List<Labeling> out = new ArrayList<>();
        for (List<Labeling> group : groups.values()) {
            if (group.size() >= 2) {
                out.add(ConsensusRule.KEMENY.consense(group));
            }
        }
        return out;
    }
}
