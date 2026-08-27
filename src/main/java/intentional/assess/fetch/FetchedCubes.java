package intentional.assess.fetch;

import java.util.List;

import cubemanager.cubebase.CubeQuery;
import intentional.assess.benchmarks.AssessBenchmark;
import result.Result;

/** The materialized cubes of one assess query: the target, its benchmarks in clause order, and the fetch's cost. */
public final class FetchedCubes {

    public final CubeQuery targetQuery;
    public final Result targetCube;
    public final List<AssessBenchmark> benchmarks;
    public final FetchStats stats;

    FetchedCubes(CubeQuery targetQuery, Result targetCube, List<AssessBenchmark> benchmarks, FetchStats stats) {
        this.targetQuery = targetQuery;
        this.targetCube = targetCube;
        this.benchmarks = benchmarks;
        this.stats = stats;
    }
}
