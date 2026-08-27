package intentional.assess;

import intentional.assess.fetch.FetchStats;
import intentional.labeling.LabelingScheme;
import cubemanager.cubebase.CubeQuery;
import result.Result;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class AssessQuery {
    public final CubeQuery targetCubeQuery;
    public final Result targetCube;
    /** The comparisons to run, in AGAINST clause order; a query with no AGAINST holds one benchmark-less comparison. */
    public final List<AssessComparison> comparisons;
    /** The labeling scheme of the query's LABELS clause. */
    public final LabelingScheme labelingScheme;
    public final String outputName;
    /** The scans the query's cubes cost, under the strategy that fetched them. */
    public final FetchStats fetchStats;

    public AssessQuery(CubeQuery targetCubeQuery,
                       Result targetCube,
                       List<AssessComparison> comparisons,
                       LabelingScheme labelingScheme,
                       String outputName,
                       FetchStats fetchStats) {
        if (labelingScheme == null) {
            throw new IllegalArgumentException("An assess query needs a labeling scheme");
        }
        this.targetCubeQuery = targetCubeQuery;
        this.targetCube = targetCube;
        this.comparisons = comparisons;
        this.labelingScheme = labelingScheme;
        this.outputName = Optional.ofNullable(outputName).
                orElse(String.valueOf(new Date().getTime()));
        this.fetchStats = fetchStats;
    }
}
