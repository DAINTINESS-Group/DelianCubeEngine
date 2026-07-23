package report;

import java.util.List;

import highlights.HighlightSet;
import labeling.LabeledResult;
import result.ResultFileMetadata;

/**
 * Renders operator results and the highlights extracted from them to a file. {@code results} and
 * {@code highlights} are parallel: {@code highlights.get(i)} are the highlights over
 * {@code results.get(i)}.
 */
public interface ReportWriter {

    ResultFileMetadata write(String query, List<LabeledResult> results, List<HighlightSet> highlights);
}
