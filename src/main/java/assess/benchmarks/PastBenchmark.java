package assess.benchmarks;

import result.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PastBenchmark implements AssessBenchmark {
    private final List<Double> averageCells;
    private int currentIndex = 0;

    public PastBenchmark(List<Result> pastResults) {
        int numberOfCells = pastResults.get(0).getCells().size();
        averageCells = new ArrayList<>(Collections.nCopies(numberOfCells, 0.0));

        // Collect the values
        for (Result result : pastResults) {
            for (int i = 0; i < result.getCells().size(); i++) {
                double prevValue = averageCells.get(i);
                double newValue = result.getCells().get(i).toDouble();
                averageCells.set(i, prevValue + newValue);
            }
        }

        averageCells.replaceAll(aDouble -> aDouble / pastResults.size());
    }

    @Override
    public double getCellValue() {
        double cellValue = averageCells.get(currentIndex);
        currentIndex++;
        return cellValue;
    }
}
