package report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import highlights.HighlightSet;
import intentional.assess.ComparisonModel;
import intentional.labeling.Labeling;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Writes the ASSESS result — one section per comparison labeling, the consensuses derived over them, and the
 * highlights — under {@code OutputFiles/assessments}.
 */
public class AssessReportWriter extends MarkdownReportWriter {

    @Override
    protected String subdirectory() {
        return "assessments";
    }

    @Override
    protected void writeBody(BufferedWriter writer, String query, LabeledResult result, HighlightSet highlights)
            throws IOException {
        List<Cell> cells = result.data.getCells();

        writer.append("## Query\n").append(query).append("\n\n");
        appendResults(writer, result);

        List<ModelResult> comparisons = comparisonModels(result);
        for (ModelResult comparison : comparisons) {
            Labeling labeling = comparison.labelling();
            writer.append("## Labeling ").append(labeling.schemeName());
            String benchmark = benchmarkOf(comparison);
            if (benchmark != null) {
                writer.append(" vs ").append(benchmark);
            }
            writer.append(" (").append(Integer.toString(labeling.assignment().size()))
                    .append(" of ").append(Integer.toString(cells.size())).append(" cells labeled)\n");
            for (Cell cell : cells) {
                writer.append("Cell: ").append(cell.toString(", ")).append("\n");
                if (!labeling.covers(cell)) {
                    writer.append("had no match, thus will not be labeled\n\n");
                    continue;
                }
                double reference = labeling.referenceOf(cell);
                if (!Double.isNaN(reference)) {
                    writer.append("was compared against benchmark value ")
                            .append(Double.toString(reference)).append("\n");
                }
                writer.append("Comparison Result: ")
                        .append(Double.toString(labeling.magnitudeOf(cell))).append("\n")
                        .append("Label: ").append(labeling.of(cell)).append("\n\n");
            }
        }

        for (Labeling consensus : consensusesOf(result, comparisons)) {
            writer.append("## ").append(consensus.schemeName()).append(" (")
                    .append(Integer.toString(consensus.assignment().size())).append(" cells)\n");
            for (Map.Entry<Cell, String> labeled : consensus.assignment().entrySet()) {
                writer.append("Cell: ").append(labeled.getKey().toString(", ")).append("\n")
                        .append("Label: ").append(labeled.getValue()).append("\n\n");
            }
        }

        appendHighlights(writer, highlights);
    }

    /** The operator's comparison results, in production order. */
    private static List<ModelResult> comparisonModels(LabeledResult result) {
        List<ModelResult> out = new ArrayList<>();
        for (ModelResult model : result.models()) {
            if (model.origin() == ModelOrigin.OPERATOR && model.labelling() != null) {
                out.add(model);
            }
        }
        return out;
    }

    /** The benchmark the comparison ran against, or null when it had none. */
    private static String benchmarkOf(ModelResult comparison) {
        for (ParameterInstantiation parameter : comparison.parameters()) {
            if (ComparisonModel.BENCHMARK_ROLE.name.equals(parameter.role.name)) {
                return parameter.label;
            }
        }
        return null;
    }

    /** The derived consensuses: every labeling of the result that no comparison produced directly. */
    private static List<Labeling> consensusesOf(LabeledResult result, List<ModelResult> comparisons) {
        Set<Labeling> direct = Collections.newSetFromMap(new IdentityHashMap<Labeling, Boolean>());
        for (ModelResult comparison : comparisons) {
            direct.add(comparison.labelling());
        }
        List<Labeling> out = new ArrayList<>();
        for (Labeling labeling : result.labelings()) {
            if (!direct.contains(labeling)) {
                out.add(labeling);
            }
        }
        return out;
    }
}
