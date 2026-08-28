package intentional.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryMeasure;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import result.Result;

/**
 * A cube query's {@link Result} together with the {@link ModelResult}s produced over it. Each result carries
 * its {@link ModelResult#origin()}, so {@link #archetypeModels()} views the model-extraction sweep's results.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    private final List<ModelResult> models = new ArrayList<>();

    public LabeledResult(CubeQuery query, Result data, List<? extends ModelResult> models) {
        this.query = query;
        this.data = data;
        if (models != null) this.models.addAll(models);
    }

    /** Every model over this result. */
    public List<ModelResult> models() {
        return Collections.unmodifiableList(models);
    }

    /** The models the model-extraction sweep produced. */
    public List<ModelResult> archetypeModels() {
        List<ModelResult> out = new ArrayList<>();
        for (ModelResult model : models) {
            if (model.origin() == ModelOrigin.ARCHETYPE) out.add(model);
        }
        return out;
    }

    /** Appends the models produced over this result. */
    public void addModels(List<? extends ModelResult> produced) {
        if (produced != null) models.addAll(produced);
    }

    public List<QueryMeasure> measures() {
        return query == null ? Collections.<QueryMeasure>emptyList() : query.getQueryMeasures();
    }

    public String measureName(int measureIndex) {
        List<QueryMeasure> measures = measures();
        return measureIndex >= 0 && measureIndex < measures.size()
                ? measures.get(measureIndex).getName() : null;
    }

}
