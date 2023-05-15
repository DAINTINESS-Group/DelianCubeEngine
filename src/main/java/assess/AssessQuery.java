package assess;

import assess.benchmarks.AssessBenchmark;
import assess.deltas.DeltaScheme;
import assess.labelers.LabelingScheme;
import cubemanager.cubebase.CubeQuery;

import java.util.Date;
import java.util.Optional;

public class AssessQuery {
    public final CubeQuery targetCubeQuery;
    public final AssessBenchmark benchmark;
    public final DeltaScheme deltaFunction;
    public final LabelingScheme labelingScheme;
    public final String outputName;

    public AssessQuery(CubeQuery targetCubeQuery,
                       AssessBenchmark benchmark,
                       DeltaScheme deltaFunction,
                       LabelingScheme labelingScheme,
                       String outputName) {
        this.targetCubeQuery = targetCubeQuery;
        this.benchmark = benchmark;
        this.deltaFunction = deltaFunction;
        this.labelingScheme = labelingScheme;
        this.outputName = Optional.ofNullable(outputName).
                orElse(String.valueOf(new Date().getTime()));
    }
}
