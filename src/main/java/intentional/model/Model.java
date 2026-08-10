package intentional.model;

import java.util.Collections;
import java.util.List;

import intentional.result.LabeledResult;

/**
 * A model type: its name, the input signature it declares, and the run that augments a {@link LabeledResult}
 * with the {@link ModelResult}s it produces and returns it. An operator produces its results by running models.
 */
public interface Model {

    /** The model type's name. */
    String name();

    /** The parameters the model declares, in the order it reports them. */
    default List<ParameterRole> parameterRoles() { return Collections.emptyList(); }

    LabeledResult run(LabeledResult context);
}
