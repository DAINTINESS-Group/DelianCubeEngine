package report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import intentional.assess.models.AssessModel;
import intentional.assess.utils.ComparedCell;
import intentional.assess.utils.LabeledCell;
import highlights.HighlightSet;
import intentional.result.LabeledResult;

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
        AssessModel model = (AssessModel) result.model(AssessModel.NAME);
        List<ComparedCell> comparedCells = model.getComparedCells();
        List<LabeledCell> labeledCells = model.getLabeledCells();

        writer.append("## Query\n").append(query).append("\n\n");
        appendResults(writer, result);

        writer.append("## Comparisons Made (")
                .append(Integer.toString(comparedCells.size())).append(" in total)\n");
        for (ComparedCell comparedCell : comparedCells) {
            writer.append(comparedCell.toString()).append("\n\n");
        }

        writer.append("## Labeling Results (")
                .append(Integer.toString(labeledCells.size())).append(" in total)\n");
        for (LabeledCell cell : labeledCells) {
            writer.append(cell.toString()).append("\n\n");
        }

        appendHighlights(writer, highlights);
    }
}
