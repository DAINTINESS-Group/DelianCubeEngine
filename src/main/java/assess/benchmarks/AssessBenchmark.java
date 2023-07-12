package assess.benchmarks;

import result.Cell;

import java.util.List;
import java.util.Optional;

public interface AssessBenchmark {
        Optional<Cell> matchCell(Cell targetCell);
}