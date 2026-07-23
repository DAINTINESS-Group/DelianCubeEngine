package report;

import java.util.List;

import highlights.HighlightSet;
import intentionaloperator.OperatorResult;
import result.ResultFileMetadata;

/**
 * Renders operator results and the highlights extracted from them to a file. {@code results} and
 * {@code highlights} are parallel: {@code highlights.get(i)} are the highlights over
 * {@code results.get(i)}.
 */
public interface ReportWriter {

    ResultFileMetadata write(String query, List<OperatorResult> results, List<HighlightSet> highlights);
}
