package report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import highlights.HighlightSet;
import intentional.assess.ConsensusModel;
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

        List<ModelResult> comparisons = new ArrayList<>();
        List<ModelResult> consensuses = new ArrayList<>();
        for (ModelResult model : result.models()) {
            if (model.origin() != ModelOrigin.OPERATOR || model.labelling() == null) {
                continue;
            }
            if (model.modelName().equals(ConsensusModel.NAME)) {
                consensuses.add(model);
            } else {
                comparisons.add(model);
            }
        }

        for (ModelResult comparison : comparisons) {
            Labeling labeling = comparison.labelling();
            writer.append("## Labeling ").append(comparison.tag());
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

        for (ModelResult consensusResult : consensuses) {
            Labeling consensus = consensusResult.labelling();
            List<ModelResult> voters = votersOf(consensus, comparisons);
            writer.append("## ").append(consensus.schemeName()).append(" (")
                    .append(Integer.toString(consensus.assignment().size())).append(" cells)\n")
                    .append("Consensed via ").append(ruleOf(consensusResult)).append(" over: ");
            StringJoiner voterTags = new StringJoiner(", ");
            for (ModelResult voter : voters) {
                voterTags.add(voter.tag());
            }
            writer.append(voterTags.toString()).append("\n\n");
            for (Cell cell : cells) {
                if (!consensus.covers(cell)) {
                    continue;
                }
                StringJoiner votes = new StringJoiner(", ");
                for (ModelResult voter : voters) {
                    String vote = voter.labelling().of(cell);
                    votes.add(vote == null ? "-" : vote);
                }
                writer.append("Cell: ").append(cell.toString(", ")).append("\n")
                        .append("Votes: ").append(votes.toString()).append("\n")
                        .append("Label: ").append(consensus.of(cell)).append("\n\n");
            }
        }

        appendHighlights(writer, highlights);
    }

    /** The comparisons the consensus was derived over: those whose ordered labeling shares its domain. */
    private static List<ModelResult> votersOf(Labeling consensus, List<ModelResult> comparisons) {
        List<ModelResult> voters = new ArrayList<>();
        for (ModelResult comparison : comparisons) {
            Labeling labeling = comparison.labelling();
            if (labeling.ordered() && labeling.domain().equals(consensus.domain())) {
                voters.add(comparison);
            }
        }
        return voters;
    }

    /** The rule the consensus result carries as its parameter. */
    private static String ruleOf(ModelResult consensus) {
        for (ParameterInstantiation parameter : consensus.parameters()) {
            if (ConsensusModel.RULE.name.equals(parameter.role.name)) {
                return parameter.label;
            }
        }
        return "";
    }
}
