package intentional.model;

import java.util.Collections;
import java.util.List;

import intentional.result.LabeledResult;

/**
 * A model type: its name, the input signature it declares, and the run that produces its
 * {@link ModelResult}s over a {@link LabeledResult}. An operator produces its results by running models.
 */
public interface Model {

    /** The model type's name. */
    String name();

    /** The parameters the model declares, in the order it reports them. */
    default List<ParameterRole> parameterRoles() { return Collections.emptyList(); }

    List<ModelResult> run(LabeledResult context);
}
