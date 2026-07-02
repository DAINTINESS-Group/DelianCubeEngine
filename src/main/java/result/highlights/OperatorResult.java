package result.highlights;

import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import model.abstracts.AbstractModel;
import result.Result;

/**
 * The product of an {@link IntentionalOperator}: the base data (the cube query and its {@link Result})
 * together with the models the operator ran over it (e.g. ASSESS's delta+labeling, DESCRIBE's USING Rank)
 * — each a computed {@link AbstractModel}. This is the Stage-1 result over which Stage-2 archetype
 * evaluation runs; it holds data and models only, not the evaluation policy.
 */
public final class OperatorResult {
    public final CubeQuery query;
    public final Result data;
    public final List<AbstractModel> models;

    public OperatorResult(CubeQuery query, Result data, List<AbstractModel> models) {
        this.query = query;
        this.data = data;
        this.models = models == null ? new ArrayList<AbstractModel>() : models;
    }

    /** The named model the operator ran, or null if it was not run. */
    public AbstractModel model(String modelName) {
        for (AbstractModel m : models) {
            if (m.getModelName().equals(modelName)) return m;
        }
        return null;
    }
}
