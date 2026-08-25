package intentional.assess;

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
    /** The labeling schemes of the query's LABELS clause, in clause order; the first is the primary. */
    public final List<LabelingScheme> labelingSchemes;
    public final String outputName;

    public AssessQuery(CubeQuery targetCubeQuery,
                       Result targetCube,
                       List<AssessComparison> comparisons,
                       List<LabelingScheme> labelingSchemes,
                       String outputName) {
        if (labelingSchemes.isEmpty()) {
            throw new IllegalArgumentException("An assess query needs at least one labeling scheme");
        }
        this.targetCubeQuery = targetCubeQuery;
        this.targetCube = targetCube;
        this.comparisons = comparisons;
        this.labelingSchemes = labelingSchemes;
        this.outputName = Optional.ofNullable(outputName).
                orElse(String.valueOf(new Date().getTime()));
    }
}
