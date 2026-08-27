package intentional.assess.benchmarks;

import result.Cell;
import result.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SiblingBenchmark implements AssessBenchmark {

    private final int keyIndex;
    private final Map<List<String>, Cell> cellsByDimensions = new HashMap<>();

    public SiblingBenchmark(Result cubeResult, String key) {
        keyIndex = locateSiblingField(cubeResult.getColumnLabels(), key);
        for (Cell benchmarkCell : cubeResult.getCells()) {
            cellsByDimensions.put(dimensionsWithoutKey(benchmarkCell), benchmarkCell);
        }
    }

    private int locateSiblingField(List<String> dimensions, String key) {
        return dimensions
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList())
                .indexOf(key);
    }

    private List<String> dimensionsWithoutKey(Cell cell) {
        List<String> dimensionValues = new ArrayList<>(cell.getDimensionMembers());
        dimensionValues.remove(keyIndex);
        return dimensionValues;
    }

    @Override
    public Optional<Cell> matchCell(Cell targetCell) {
        return Optional.ofNullable(cellsByDimensions.get(dimensionsWithoutKey(targetCell)));
    }
}
