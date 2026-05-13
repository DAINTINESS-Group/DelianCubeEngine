package cubemanager.queryoptimizer;

import cubemanager.cubebase.CubeBase;

public class QueryOptimizerFactory {

    public static IQueryOptimization create(String operationType, CubeBase cubebase, String method, double sampleSize) {
        switch (operationType.toUpperCase()) {
            case "SELECTIVITY_ESTIMATION" :
                return new SelectivityEstimationOptimizer(cubebase, method, sampleSize);
            default:
                throw new  IllegalArgumentException("Unknown operation type: " + operationType);
        }
    }
}
