package assess.benchmarks;

import result.Cell;
import result.Result;

import java.util.*;
import java.util.stream.Collectors;

public class PastBenchmark implements AssessBenchmark {
    private final List<PastEntry> pastEntries = new ArrayList<>();
    private final int keyIndex;

    private static class PastEntry {
        final List<String> dimensions; // Except the date dimension
        double measurement;
        int resultsAdded;

        public PastEntry(List<String> dimensions, double measurement) {
            this.dimensions = dimensions;
            this.measurement = measurement;
            this.resultsAdded = 1;
        }

        boolean matches(List<String> dimensions) {
            return this.dimensions.equals(dimensions);
        }

        void update(double measurement) {
            this.measurement += measurement;
            resultsAdded++;
        }

        @Override
        public String toString() {
            return "dimensions=" + dimensions +
                    ", measurement=" + measurement +
                    ", resultsAdded=" + resultsAdded;
        }
    }

    public PastBenchmark(List<Result> pastResults, String dateLevel) {
        keyIndex = findDateIndex(pastResults.get(0).getColumnLabels(), dateLevel);
        if (keyIndex < 0) { throw new RuntimeException("Make sure that the comparison field is in the groupers"); }
        for (Result result : pastResults) {
            for (Cell cell : result.getCells()) {
                List<String> dimensionValues = new ArrayList<>(cell.getDimensionMembers());
                double measurement = cell.toDouble();
                dimensionValues.remove(keyIndex);
                Optional<PastEntry> matchingEntry = pastEntries.stream()
                        .filter(pastEntry -> pastEntry.matches(dimensionValues))
                        .findFirst();

                if (matchingEntry.isPresent()) {
                    matchingEntry.get().update(cell.toDouble());
                } else {
                    pastEntries.add(new PastEntry(dimensionValues, measurement));
                }
            }
        }
        pastEntries.forEach(pastEntry -> pastEntry.measurement = pastEntry.measurement / pastEntry.resultsAdded);
    }

    // Assuming all results have the same order of columns
    private int findDateIndex(List<String> dimensions, String dateLevel) {
        return dimensions
                .stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList())
                .indexOf(dateLevel);
    }

    @Override
    public Optional<Cell> matchCell(Cell targetCell) {
        List<String> targetDimensionValues = new ArrayList<>(targetCell.getDimensionMembers());
        targetDimensionValues.remove(keyIndex);

        for (PastEntry pastEntry : pastEntries) {
            if (pastEntry.matches(targetDimensionValues)) {
                return Optional.of(new Cell( new String[]
                        {"Average of " + pastEntry.resultsAdded,
                                Double.toString(pastEntry.measurement),
                                Integer.toString(pastEntry.resultsAdded)
                        }));
            }
        }

        return Optional.empty();
    }
}
