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
 * A cube query's {@link Result} together with the {@link ModelResult}s produced over it: the ones the
 * operator produced, and the ones the model-extraction sweep appended. {@link #labelings()} is the view over
 * the operator's labellings, including their derived consensuses.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    private final List<ModelResult> operatorModels;
    private final List<ModelResult> archetypeModels = new ArrayList<>();

    public LabeledResult(CubeQuery query, Result data, List<? extends ModelResult> operatorModels) {
        this.query = query;
        this.data = data;
        this.operatorModels = operatorModels == null
                ? new ArrayList<ModelResult>() : new ArrayList<ModelResult>(operatorModels);
    }

    /** The models the operator produced. */
    public List<ModelResult> operatorModels() {
        return Collections.unmodifiableList(operatorModels);
    }

    /** The models the model-extraction sweep appended. */
    public List<ModelResult> archetypeModels() {
        return Collections.unmodifiableList(archetypeModels);
    }

    /** Every model over this result: the operator's, then the archetypes'. */
    public List<ModelResult> models() {
        List<ModelResult> all = new ArrayList<>(operatorModels);
        all.addAll(archetypeModels);
        return Collections.unmodifiableList(all);
    }

    /** Appends the models produced by the model-extraction sweep. */
    public void addArchetypeModels(List<? extends ModelResult> models) {
        if (models != null) archetypeModels.addAll(models);
    }

    /** Every labelling the operator produced, including the consensuses derived over them. */
    public List<Labeling> labelings() {
        List<Labeling> base = labellingsOf(this.operatorModels);
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
