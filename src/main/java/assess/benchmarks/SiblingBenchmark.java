package assess.benchmarks;

import result.Cell;
import result.Result;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SiblingBenchmark implements AssessBenchmark {

    private final Result resultingCube;
    private final int keyIndex;

    public SiblingBenchmark(Result cubeResult, String key) {
        resultingCube = cubeResult;
        keyIndex = locateSiblingField(cubeResult.getColumnLabels(), key);
    }

    private int locateSiblingField(List<String> dimensions, String key) {
        return dimensions
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList())
                .indexOf(key);
    }

    @Override
    public Optional<Cell> matchCell(Cell targetCell) {
        List<String> targetDimensionValues = new ArrayList<>(targetCell.getDimensionMembers());
        targetDimensionValues.remove(keyIndex);

        for (Cell benchmarkCell : resultingCube.getCells()) {
            List<String> benchmarkDimensionValues = new ArrayList<>(benchmarkCell.getDimensionMembers());
            benchmarkDimensionValues.remove(keyIndex);
            if (targetDimensionValues.equals(benchmarkDimensionValues)) {
                return Optional.of(benchmarkCell);
            }
        }

        return Optional.empty();
    }
}
