package report;

import java.io.BufferedWriter;
import java.io.IOException;

import highlights.HighlightSet;
import labeling.LabeledResult;

/**
 * Writes the ANALYZE results — each query and the highlights extracted over it — under
 * {@code OutputFiles/analyze}.
 */
public class AnalyzeReportWriter extends MarkdownReportWriter {

    @Override
    protected String subdirectory() {
        return "analyze";
    }

    @Override
    protected void writeBody(BufferedWriter writer, String query, LabeledResult result, HighlightSet highlights)
            throws IOException {
        writer.append("## Query\n").append(result.query.toString()).append("\n\n");
        appendResults(writer, result);
        appendHighlights(writer, highlights);
    }
}
