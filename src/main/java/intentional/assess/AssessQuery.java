package intentional.assess;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.deltas.DeltaScheme;
import intentional.labeling.LabelingScheme;
import cubemanager.cubebase.CubeQuery;
import result.Result;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AssessQuery {
    public final CubeQuery targetCubeQuery;
    public final Result targetCube;
    public final AssessBenchmark benchmark;
    public final DeltaScheme deltaFunction;
    /** The labeling schemes of the query's LABELS clause, in clause order; the first is the primary. */
    public final List<LabelingScheme> labelingSchemes;
    public final String outputName;

    public AssessQuery(CubeQuery targetCubeQuery,
                       Result targetCube,
                       AssessBenchmark benchmark,
                       DeltaScheme deltaFunction,
                       List<LabelingScheme> labelingSchemes,
                       String outputName) {
        if (labelingSchemes.isEmpty()) {
            throw new IllegalArgumentException("An assess query needs at least one labeling scheme");
        }
        this.targetCubeQuery = targetCubeQuery;
        this.targetCube = targetCube;
        this.benchmark = benchmark;
        this.deltaFunction = deltaFunction;
        this.labelingSchemes = labelingSchemes;
        this.outputName = Optional.ofNullable(outputName).
                orElse(String.valueOf(new Date().getTime()));
    }
}
