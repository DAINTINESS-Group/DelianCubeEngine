package report;

import java.io.BufferedWriter;
import java.io.IOException;

import highlights.HighlightSet;
import intentional.result.LabeledResult;

/**
 * Writes the DESCRIBE result — its query, result table, and highlights — under {@code OutputFiles/describe}.
 */
public class DescribeReportWriter extends MarkdownReportWriter {

    @Override
    protected String subdirectory() {
        return "describe";
    }

    @Override
    protected void writeBody(BufferedWriter writer, String query, LabeledResult result, HighlightSet highlights)
            throws IOException {
        writer.append("## Query\n").append(query).append("\n\n");
        appendResults(writer, result);
        appendHighlights(writer, highlights);
    }
}
