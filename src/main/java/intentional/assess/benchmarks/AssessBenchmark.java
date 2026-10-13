package intentional.assess.benchmarks;

import result.Cell;

import java.util.Optional;

/**
 * The benchmark a target cube is assessed against: it matches each target cell to the benchmark cell it is
 * compared against, or none when the target has no match.
 */
public interface AssessBenchmark {

    Optional<Cell> matchCell(Cell targetCell);
}
