package intentional.assess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.deltas.DeltaScheme;
import intentional.assess.utils.ComparedCell;
import intentional.labeling.Labeling;
import intentional.labeling.LabelingScheme;
import intentional.model.Model;
import intentional.model.ModelOrigin;
import intentional.model.ModelResult;
import intentional.model.ParameterInstantiation;
import intentional.model.ModelResultImpl;
import intentional.result.LabeledResult;
import result.Cell;

/**
 * Compares a cube against a benchmark: each cell's delta to its matched benchmark cell, labeled under one or
 * more schemes. One {@link ModelResult} per scheme, over the same deltas, each carrying the delta as
 * magnitude and the matched benchmark value as reference; cells without a benchmark match stay unlabeled.
 */
public final class ComparisonModel implements Model {

    public static final String NAME = "Comparison";

    private final AssessBenchmark benchmark;
    private final DeltaScheme deltaFunction;
    private final List<LabelingScheme> labelingSchemes;

    public ComparisonModel(AssessBenchmark benchmark, DeltaScheme deltaFunction,
                           List<LabelingScheme> labelingSchemes) {
        this.benchmark = benchmark;
        this.deltaFunction = deltaFunction;
        this.labelingSchemes = labelingSchemes;
    }

    @Override public String name() { return NAME; }

    @Override
    public LabeledResult run(LabeledResult context) {
        List<Cell> targetCells = context.data.getCells();
        if (targetCells.isEmpty()) {
            throw new RuntimeException("No cells collected from the target cube query");
        }
        List<ComparedCell> comparedCells = new ArrayList<>();
        Map<Cell, Double> deltas = new LinkedHashMap<>(
                deltaFunction.compareTargetToBenchmark(targetCells, benchmark, comparedCells));

        Map<Cell, Double> benchmarkValues = new LinkedHashMap<>();
        for (ComparedCell compared : comparedCells) {
            if (compared.benchmark != null) {
                benchmarkValues.put(compared.target, compared.benchmark.toDouble());
            }
        }

        List<ModelResult> produced = new ArrayList<>();
        for (LabelingScheme scheme : labelingSchemes) {
            Labeling labeling = new Labeling(scheme, deltas, 0, benchmarkValues);
            produced.add(new ModelResultImpl(labeling.schemeName(), true, labeling,
                    Collections.<ParameterInstantiation>emptyList()).origin(ModelOrigin.OPERATOR));
        }
        context.addModels(produced);
        return context;
    }
}
