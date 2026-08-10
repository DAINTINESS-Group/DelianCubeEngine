package intentional.interestingness;

import java.util.List;

import intentional.model.ModelResult;

/**
 * The interestingness operator: scores model results before the highlight extractor presents them.
 */
public final class Interestingness {

    /** Scores the model results. */
    public List<ModelResult> score(List<ModelResult> models) {
        return models;
    }
}
