package intentional.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryMeasure;
import intentional.labeling.Labeling;
import intentional.labeling.consensus.ConsensusRule;
import intentional.model.ModelResult;
import result.Result;

/**
 * A cube query's {@link Result} together with the {@link ModelResult}s the operator produced over it.
 * {@link #labelings()} is the view over their labellings; constructing the result derives their consensuses.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    private final List<ModelResult> models;

    public LabeledResult(CubeQuery query, Result data, List<? extends ModelResult> models) {
        this.query = query;
        this.data = data;
        this.models = models == null ? new ArrayList<ModelResult>() : new ArrayList<ModelResult>(models);
    }

    public List<ModelResult> models() {
        return Collections.unmodifiableList(models);
    }

    /** Every labelling produced over the result, including the consensuses. */
    public List<Labeling> labelings() {
        List<Labeling> base = labellingsOf(this.models);
        List<Labeling> labelings = new ArrayList<>(base);
        labelings.addAll(deriveConsensuses(base));
        return Collections.unmodifiableList(labelings);
    }

    public List<QueryMeasure> measures() {
        return query == null ? Collections.<QueryMeasure>emptyList() : query.getQueryMeasures();
    }

    public String measureName(int measureIndex) {
        List<QueryMeasure> measures = measures();
        return measureIndex >= 0 && measureIndex < measures.size()
                ? measures.get(measureIndex).getName() : null;
    }

    private static List<Labeling> labellingsOf(List<ModelResult> models) {
        List<Labeling> out = new ArrayList<>();
        for (ModelResult model : models) {
            Labeling labelling = model.labelling();
            if (labelling != null) out.add(labelling);
        }
        return out;
    }

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
