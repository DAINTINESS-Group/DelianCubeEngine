package assess.benchmarks;

import result.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PastBenchmark implements AssessBenchmark {
    private final Iterator<Double> benchmarkCells;

    public PastBenchmark(List<Result> pastResults) {
        int numberOfCells = Collections.max(
                pastResults.stream()
                        .map(result -> result.getCells().size())
                        .collect(Collectors.toList())
        );
        ArrayList<Double> averageCells =
                new ArrayList<>(Collections.nCopies(numberOfCells, 0.0)); // Init values to 0.0

        // Get cell value, add it to the appropriate cell index in averageCells
        for (Result result : pastResults) {
            for (int i = 0; i < result.getCells().size(); i++) {
                double prevValue = averageCells.get(i);
                double newValue = result.getCells().get(i).toDouble();
                averageCells.set(i, prevValue + newValue);
            }
        }

        averageCells.replaceAll(aDouble -> aDouble / pastResults.size());
        benchmarkCells = averageCells.iterator();
    }

    @Override
    public double getCellValue() {
        return benchmarkCells.next();
    }
}
