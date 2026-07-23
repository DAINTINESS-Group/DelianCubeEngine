package highlights.instance;

import highlights.metamodel.Algorithm;
import intentional.result.LabeledResult;

/**
 * An {@link Algorithm} that can be checked against a {@link LabeledResult} for applicability and executed
 * over it to produce an {@link AlgorithmExecution}. Executed once per main measure: {@code measureIndex} is
 * the position of that measure in the result's cells (see {@link result.Cell#toDouble(int)}).
 */
public interface ExecutableAlgorithm extends Algorithm {

    boolean appliesTo(LabeledResult context);

    AlgorithmExecution run(LabeledResult context, int measureIndex);
}
