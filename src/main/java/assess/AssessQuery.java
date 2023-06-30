package assess;

import assess.benchmarks.AssessBenchmark;
import assess.deltas.DeltaScheme;
import assess.labelers.LabelingScheme;
import cubemanager.cubebase.CubeQuery;
import result.Result;

import java.util.Date;
import java.util.Optional;

public class AssessQuery {
    public final CubeQuery targetCubeQuery;
    public final Result targetCube;
    public final AssessBenchmark benchmark;
    public final DeltaScheme deltaFunction;
    public final LabelingScheme labelingScheme;
    public final String outputName;

    public AssessQuery(CubeQuery targetCubeQuery,
                       Result targetCube,
                       AssessBenchmark benchmark,
                       DeltaScheme deltaFunction,
                       LabelingScheme labelingScheme,
                       String outputName) {
        this.targetCubeQuery = targetCubeQuery;
        this.targetCube = targetCube;
        this.benchmark = benchmark;
        this.deltaFunction = deltaFunction;
        this.labelingScheme = labelingScheme;
        this.outputName = Optional.ofNullable(outputName).
                orElse(String.valueOf(new Date().getTime()));
    }
}
