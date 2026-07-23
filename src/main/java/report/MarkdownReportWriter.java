package report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryMeasure;
import highlights.HighlightSet;
import highlights.instance.ElementaryHighlight;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;
import result.ResultFileMetadata;

/**
 * Shared skeleton for the Markdown report writers: creates the operator's output file, renders one body
 * per result, and returns its {@link ResultFileMetadata}. Subclasses supply only the output
 * subdirectory and the per-result body.
 */
abstract class MarkdownReportWriter implements ReportWriter {

    /** The subdirectory under {@code OutputFiles} where this operator's reports are written. */
    protected abstract String subdirectory();

    /** Renders one result — its query and operator-specific sections — including its highlights. */
    protected abstract void writeBody(BufferedWriter writer, String query, LabeledResult result,
            HighlightSet highlights) throws IOException;

    @Override
    public final ResultFileMetadata write(String query, List<LabeledResult> results, List<HighlightSet> highlights) {
        File dir = new File("OutputFiles/" + subdirectory());
        dir.mkdirs();
        File out = new File(dir, results.get(0).query.getName() + ".md");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
            for (int i = 0; i < results.size(); i++) {
                writeBody(writer, query, results.get(i), highlights.get(i));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to export to MarkDown");
        }

        ResultFileMetadata metadata = new ResultFileMetadata();
        metadata.setResultFile(out.getPath());
        return metadata;
    }

    /** Appends the highlights section: each holistic as a heading with its elementary highlights beneath. */
    protected void appendHighlights(BufferedWriter writer, HighlightSet highlights) throws IOException {
        if (highlights.isEmpty()) {
            return;
        }
        writer.append("## Highlights\n");
        for (Highlight h : highlights.highlights()) {
            writer.append("### ").append(h.toText()).append("\n");
            if (h instanceof HolisticHighlight) {
                for (ElementaryHighlight eh : ((HolisticHighlight) h).elementary()) {
                    writer.append("- ").append(eh.toText()).append("\n");
                }
            }
            writer.append("\n");
        }
    }

    /** Appends the result table: the cells headed by the query's grouper dimensions and measures. */
    protected void appendResults(BufferedWriter writer, LabeledResult result) throws IOException {
        writer.append("## Results\n").append(buildTable(result.query, result.data)).append("\n\n");
    }

    private String buildTable(CubeQuery cubeQuery, Result data) {
        ArrayList<Cell> cells = (data != null) ? data.getCells() : null;
        if (cells == null || cells.isEmpty()) {
            return "No results found.";
        }

        StringBuilder table = new StringBuilder("|");
        ArrayList<String[]> gamma = cubeQuery.getGammaExpressions();
        if (gamma != null) {
            for (String[] dim : gamma) {
                table.append(String.join(".", dim)).append("|");
            }
        } else {
            table.append("Dimension|");
        }

        int numMeasures = cells.get(0).getMeasures().size();
        ArrayList<QueryMeasure> definedMeasures = cubeQuery.getQueryMeasures();
        for (int i = 0; i < numMeasures; i++) {
            String header = "Measure_" + (i + 1);
            if (definedMeasures != null && i < definedMeasures.size()) {
                QueryMeasure qm = definedMeasures.get(i);
                header = (qm.getAlias() != null && !qm.getAlias().isEmpty()) ? qm.getAlias() : qm.getName();
            }
            table.append(header).append("|");
        }

        table.append("\n|");
        int totalCols = ((gamma != null) ? gamma.size() : 1) + numMeasures;
        for (int i = 0; i < totalCols; i++) {
            table.append("---|");
        }
        table.append("\n");

        for (Cell c : cells) {
            table.append("|");
            for (String dim : c.getDimensionMembers()) {
                table.append(dim).append("|");
            }
            for (String val : c.getMeasures()) {
                table.append(val).append("|");
            }
            table.append("\n");
        }
        return table.toString();
    }
}
