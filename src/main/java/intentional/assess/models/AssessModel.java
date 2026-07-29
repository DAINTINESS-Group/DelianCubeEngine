package intentional.assess.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.deltas.DeltaScheme;
import intentional.labeling.LabelingScheme;
import intentional.assess.utils.ComparedCell;
import intentional.assess.utils.LabeledCell;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingModel;
import result.Cell;
import result.Result;

/**
 * The ASSESS model: compares each target cell to a benchmark via the delta scheme and labels the outcome,
 * producing a {@code delta} and a {@code label} per cell. It is config-parametric (benchmark + delta +
 * labeling).
 */
public final class AssessModel implements LabelingModel {

    /** Identifies this model's output within an LabeledResult. */
    public static final String NAME = "AssessDelta";

    private final AssessBenchmark benchmark;
    private final DeltaScheme delta;
    private final LabelingScheme scheme;
    private final Result data;
    private final List<ComparedCell> comparedCells = new ArrayList<>();

    private final Map<Cell, Double> deltas = new LinkedHashMap<>();
    private Labeling assessmentLabeling;

    public AssessModel(AssessBenchmark benchmark, DeltaScheme delta, LabelingScheme scheme, Result data) {
        this.benchmark = benchmark;
        this.delta = delta;
        this.scheme = scheme;
        this.data = data;
    }

    public int compute() {
        List<Cell> targetCells = data.getCells();
        if (targetCells.isEmpty()) {
            return -1;
        }
        comparedCells.clear();
        deltas.clear();
        HashMap<Cell, Double> computed =
                delta.compareTargetToBenchmark(targetCells, benchmark, comparedCells);
        deltas.putAll(computed);
        Map<Cell, Double> benchmarkValues = new LinkedHashMap<>();
        for (ComparedCell compared : comparedCells) {
            if (compared.benchmark != null) {
                benchmarkValues.put(compared.target, compared.benchmark.toDouble());
            }
        }
        this.assessmentLabeling = new Labeling(scheme, new LinkedHashMap<>(deltas), 0, benchmarkValues);
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

    /** The assessment label per cell, over the labeling scheme's ordered domain, carrying the delta as magnitude and the benchmark value as reference; empty before {@link #compute()} has run. */
    @Override
    public List<Labeling> labelings() {
        return assessmentLabeling == null
                ? Collections.<Labeling>emptyList()
                : Collections.singletonList(assessmentLabeling);
    }

    @Override
    public String getModelName() { return NAME; }
}
