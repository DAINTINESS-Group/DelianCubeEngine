package assess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import assess.benchmarks.AssessBenchmark;
import assess.deltas.DeltaScheme;
import assess.labelers.LabelingScheme;
import assess.utils.ComparedCell;
import assess.utils.LabeledCell;
import model.abstracts.AbstractModel;
import model.labeling.DerivedMeasure;
import model.labeling.Labeling;
import model.labeling.LabelingModel;
import result.Cell;
import result.Result;

/**
 * The ASSESS model: compares each target cell to a benchmark via the delta scheme and labels the outcome,
 * producing a {@code delta} and a {@code label} per cell. It is config-parametric (benchmark + delta +
 * labeling) and is constructed directly with the data rather than through {@link model.ModelFactory}.
 */
public final class AssessModel extends AbstractModel implements LabelingModel {

    /** Identifies this model's output within an OperatorResult. */
    public static final String NAME = "AssessDelta";

    private final AssessBenchmark benchmark;
    private final DeltaScheme delta;
    private final LabelingScheme labeling;
    private final List<ComparedCell> comparedCells = new ArrayList<>();
    private final List<String> assessmentDomain;

    private final Map<Cell, Double> deltas = new LinkedHashMap<>();
    private Labeling assessmentLabeling;

    public AssessModel(AssessBenchmark benchmark, DeltaScheme delta, LabelingScheme labeling, Result data) {
        super(data);
        this.benchmark = benchmark;
        this.delta = delta;
        this.labeling = labeling;
        this.assessmentDomain = labeling.getOrderedLabels();
    }

    @Override
    public int compute() {
        List<Cell> targetCells = result.getCells();
        if (targetCells.isEmpty()) {
            return -1;
        }
        comparedCells.clear();
        deltas.clear();
        Map<Cell, String> labelByCell = new LinkedHashMap<>();
        HashMap<Cell, Double> computed =
                delta.compareTargetToBenchmark(targetCells, benchmark, comparedCells);
        for (Map.Entry<Cell, Double> entry : computed.entrySet()) {
            deltas.put(entry.getKey(), entry.getValue());
            labelByCell.put(entry.getKey(), labeling.applyLabels(entry.getValue()));
        }
        this.assessmentLabeling = new Labeling(assessmentDomain, true, labelByCell);
        return 0;
    }

    /** The assessed cells with their label. */
    public Map<Cell, String> getLabels() { return assessmentLabeling.assignment(); }

    public double deltaOf(Cell cell) { return deltas.getOrDefault(cell, Double.NaN); }

    public String labelOf(Cell cell) { return assessmentLabeling.of(cell); }

    /** Audit trail of target/benchmark comparisons from the last run (incl. no-match cells). */
    public List<ComparedCell> getComparedCells() { return comparedCells; }

    /** The assessed cells as {@link LabeledCell}s (cell + delta + label), insertion-ordered. */
    public List<LabeledCell> getLabeledCells() {
        List<LabeledCell> labeledCells = new ArrayList<>();
        for (Map.Entry<Cell, Double> entry : deltas.entrySet()) {
            Cell cell = entry.getKey();
            labeledCells.add(new LabeledCell(cell, entry.getValue(), assessmentLabeling.of(cell)));
        }
        return labeledCells;
    }

    /** The assessment label per cell, over the labeling scheme's ordered domain. */
    @Override
    public List<Labeling> labelings() {
        return Collections.singletonList(assessmentLabeling);
    }

    /** The delta per cell, as data an archetype can rank by. */
    @Override
    public List<DerivedMeasure> derivedMeasures() {
        return Collections.singletonList(new DerivedMeasure(deltas));
    }

    @Override
    public String getModelName() { return NAME; }

    /** A view over the per-cell output for the legacy text report: {@code [cell, delta, label]}. */
    @Override
    public String[][] printAs2DStringArray() {
        String[][] out = new String[deltas.size() + 1][3];
        out[0][0] = "Cell";
        out[0][1] = "delta";
        out[0][2] = "label";
        int i = 1;
        for (Map.Entry<Cell, Double> entry : deltas.entrySet()) {
            Cell cell = entry.getKey();
            out[i][0] = cell == null ? "" : cell.toString();
            out[i][1] = Double.toString(entry.getValue());
            String label = assessmentLabeling.of(cell);
            out[i][2] = label == null ? "" : label;
            i++;
        }
        return out;
    }

    @Override
    public String getInfoContent() {
        return getModelName() + "\n-------------------------\n\n"
                + "ASSESS compares each target cell to a benchmark via the delta scheme and labels "
                + "the outcome. Each row is a cell with its delta and the assigned label.";
    }

    @Override
    public void setFileName(String filename, String fileLocation) {
    }
}
