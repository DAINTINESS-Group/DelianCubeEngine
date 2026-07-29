package intentional.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import intentional.labeling.Labeling;
import result.Result;

/**
 * A cube query's {@link Result} together with the {@link Labeling}s computed over it. This is the Stage-1
 * product over which Stage-2 archetype evaluation runs; it holds data and labelings only — each labeling
 * says which scheme produced it, and no producer crosses this boundary.
 */
public final class LabeledResult {
    public final CubeQuery query;
    public final Result data;
    private final List<Labeling> labelings;

    public LabeledResult(CubeQuery query, Result data, List<Labeling> labelings) {
        this.query = query;
        this.data = data;
        this.labelings = labelings == null ? new ArrayList<Labeling>() : labelings;
    }

    /** Every per-cell labeling computed over the data. */
    public List<Labeling> labelings() {
        return Collections.unmodifiableList(labelings);
    }
}
