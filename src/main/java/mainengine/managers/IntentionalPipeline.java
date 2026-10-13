package mainengine.managers;

import java.util.ArrayList;
import java.util.List;

import cubemanager.CubeManager;
import highlights.HighlightExtractor;
import highlights.HighlightRecipes;
import highlights.HighlightSet;
import intentional.interestingness.Interestingness;
import intentional.model.ModelExtraction;
import intentional.operator.IntentionalOperator;
import intentional.result.LabeledResult;
import result.ResultFileMetadata;

/**
 * Runs an intentional operator and renders its report: executes the operator, runs the model-extraction sweep
 * with the profile's archetypes over each result, scores the model results, extracts highlights from them and
 * writes them. A failure during execution is returned as the {@code errorCheckingStatus} of an otherwise empty
 * result.
 */
public final class IntentionalPipeline {

    private IntentionalPipeline() {}

    public static ResultFileMetadata run(IntentionalOperator operator, String query,
            IntentionalProfile profile, CubeManager cubeManager) {
        try {
            List<LabeledResult> results = operator.execute(query);
            ModelExtraction modelExtraction = new ModelExtraction();
            Interestingness interestingness = new Interestingness();
            HighlightExtractor extractor = new HighlightExtractor();
            HighlightRecipes recipes = HighlightRecipes.defaults();

            List<HighlightSet> highlights = new ArrayList<>();
            for (LabeledResult result : results) {
                modelExtraction.run(result, profile.archetypes());
                interestingness.score(result.models());
                highlights.add(extractor.extract(result, recipes, cubeManager));
            }
            return profile.writer().write(query, results, highlights);
        } catch (RuntimeException e) {
            ResultFileMetadata metadata = new ResultFileMetadata();
            metadata.setErrorCheckingStatus(e.toString());
            return metadata;
        }
    }
}
