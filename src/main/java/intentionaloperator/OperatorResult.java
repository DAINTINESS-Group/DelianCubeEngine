package intentionaloperator;

import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import labeling.DerivedMeasure;
import labeling.Labeling;
import labeling.LabelingModel;
import result.Result;

/**
 * The product of an {@link IntentionalOperator}: the base data (the cube query and its {@link Result})
 * together with the {@link LabelingModel}s the operator ran over it (e.g. ASSESS's delta+labeling). This is
 * the Stage-1 result over which Stage-2 archetype evaluation runs; it holds data and models only, not the
 * evaluation policy.
 */
public final class OperatorResult {
    public final CubeQuery query;
    public final Result data;
    public final List<LabelingModel> models;

    public OperatorResult(CubeQuery query, Result data, List<LabelingModel> models) {
        this.query = query;
        this.data = data;
        this.models = models == null ? new ArrayList<LabelingModel>() : models;
    }

    /** The named model the operator ran, or null if it was not run. */
    public LabelingModel model(String modelName) {
        for (LabelingModel m : models) {
            if (m.getModelName().equals(modelName)) return m;
        }
        return null;
    }

    /** Every per-cell labeling produced by the models the operator ran. */
    public List<Labeling> labelings() {
        List<Labeling> out = new ArrayList<>();
        for (LabelingModel m : models) out.addAll(m.labelings());
        return out;
    }

    /** Every derived measure the models the operator ran computed over the data. */
    public List<DerivedMeasure> derivedMeasures() {
        List<DerivedMeasure> out = new ArrayList<>();
        for (LabelingModel m : models) out.addAll(m.derivedMeasures());
        return out;
    }
}
