package report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

import highlights.HighlightSet;
import intentional.labeling.Labeling;
import intentional.labeling.consensus.ConsensusRule;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
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
            writer.append("## Labeling ").append(result.labelingTag(labeling));
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
            List<Labeling> voters = votersOf(consensus, comparisons);
            writer.append("## ").append(consensus.schemeName()).append(" (")
                    .append(Integer.toString(consensus.assignment().size())).append(" cells)\n")
                    .append("Consensed via ").append(ConsensusRule.KEMENY.name()).append(" over: ");
            StringJoiner voterTags = new StringJoiner(", ");
            for (Labeling voter : voters) {
                voterTags.add(result.labelingTag(voter));
            }
            writer.append(voterTags.toString()).append("\n\n");
            for (Cell cell : cells) {
                if (!consensus.covers(cell)) {
                    continue;
                }
                StringJoiner votes = new StringJoiner(", ");
                for (Labeling voter : voters) {
                    String vote = voter.of(cell);
                    votes.add(vote == null ? "-" : vote);
                }
                writer.append("Cell: ").append(cell.toString(", ")).append("\n")
                        .append("Votes: ").append(votes.toString()).append("\n")
                        .append("Label: ").append(consensus.of(cell)).append("\n\n");
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

    /** The labelings the consensus was derived over: the ordered comparison labelings sharing its domain. */
    private static List<Labeling> votersOf(Labeling consensus, List<ModelResult> comparisons) {
        List<Labeling> voters = new ArrayList<>();
        for (ModelResult comparison : comparisons) {
            Labeling labeling = comparison.labelling();
            if (labeling.ordered() && labeling.domain().equals(consensus.domain())) {
                voters.add(labeling);
            }
        }
        return voters;
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
