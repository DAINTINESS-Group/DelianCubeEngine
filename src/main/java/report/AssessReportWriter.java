package report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import highlights.HighlightSet;
import intentional.labeling.Labeling;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Writes the ASSESS result — its comparisons, labelings, and highlights — under
 * {@code OutputFiles/assessments}.
 */
public class AssessReportWriter extends MarkdownReportWriter {

    @Override
    protected String subdirectory() {
        return "assessments";
    }

    @Override
    protected void writeBody(BufferedWriter writer, String query, LabeledResult result, HighlightSet highlights)
            throws IOException {
        Labeling labeling = result.labelings().get(0);
        List<Cell> cells = result.data.getCells();

        writer.append("## Query\n").append(query).append("\n\n");
        appendResults(writer, result);

        writer.append("## Comparisons Made (")
                .append(Integer.toString(cells.size())).append(" in total)\n");
        for (Cell cell : cells) {
            writer.append("Target Cell: ").append(cell.toString(", ")).append("\n");
            if (labeling.covers(cell)) {
                writer.append("was compared against benchmark value ")
                        .append(Double.toString(labeling.referenceOf(cell))).append("\n\n");
            } else {
                writer.append("had no match, thus will not be labeled\n\n");
            }
        }

        writer.append("## Labeling Results (")
                .append(Integer.toString(labeling.assignment().size())).append(" in total)\n");
        for (Map.Entry<Cell, String> labeled : labeling.assignment().entrySet()) {
            writer.append("Cell: ").append(labeled.getKey().toString(", ")).append("\n")
                    .append("Comparison Result: ")
                    .append(Double.toString(labeling.magnitudeOf(labeled.getKey()))).append("\n")
                    .append("Label: ").append(labeled.getValue()).append("\n\n");
        }

        appendHighlights(writer, highlights);
    }
}
