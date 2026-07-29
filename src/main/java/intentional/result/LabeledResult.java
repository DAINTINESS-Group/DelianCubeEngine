package intentional.result;

import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingModel;
import result.Result;

/**
 * A cube query's {@link Result} together with the {@link LabelingModel}s computed over it. This is the
 * Stage-1 product over which Stage-2 archetype evaluation runs; it holds data and models only, not the
 * evaluation policy.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    public final List<LabelingModel> models;

    public LabeledResult(CubeQuery query, Result data, List<LabelingModel> models) {
        this.query = query;
        this.data = data;
        this.models = models == null ? new ArrayList<LabelingModel>() : models;
    }

    /** The named model, or null if it was not run. */
    public LabelingModel model(String modelName) {
        for (LabelingModel m : models) {
            if (m.getModelName().equals(modelName)) return m;
        }
        return null;
    }

    /** Every per-cell labeling produced by the models. */
    public List<Labeling> labelings() {
        List<Labeling> out = new ArrayList<>();
        for (LabelingModel m : models) out.addAll(m.labelings());
        return out;
    }
}
