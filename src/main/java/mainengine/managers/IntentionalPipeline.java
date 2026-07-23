package mainengine.managers;

import java.util.ArrayList;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import intentionaloperator.IntentionalOperator;
import labeling.LabeledResult;
import result.ResultFileMetadata;

/**
 * Runs an intentional operator and renders its report: executes the operator, extracts highlights over
 * each result using the profile's archetypes, and writes the result with the profile's writer. A
 * failure during execution is returned as the {@code errorCheckingStatus} of an otherwise empty result.
 */
public final class IntentionalPipeline {

    private IntentionalPipeline() {}

    public static ResultFileMetadata run(IntentionalOperator operator, String query,
            IntentionalProfile profile, CubeManager cubeManager) {
        try {
            List<LabeledResult> results = operator.execute(query);
            CubeSchemaResolver schema = CubeSchemaResolver.from(cubeManager);
            HighlightExtractor extractor = new HighlightExtractor();
            List<HighlightSet> highlights = new ArrayList<>();
            for (LabeledResult result : results) {
                highlights.add(extractor.extract(result, profile.archetypes(), schema));
            }
            return profile.writer().write(query, results, highlights);
        } catch (RuntimeException e) {
            ResultFileMetadata metadata = new ResultFileMetadata();
            metadata.setErrorCheckingStatus(e.toString());
            return metadata;
        }
    }
}
