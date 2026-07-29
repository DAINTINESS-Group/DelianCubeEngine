package highlights.instance;

import intentional.result.LabeledResult;

/**
 * An {@link ExecutableAlgorithm} evaluated over the {@link highlights.metamodel.EvaluationAxis#MEASURE}
 * axis: executed once per main measure, with {@code measureIndex} the position of that measure in the
 * result's cells (see {@link result.Cell#toDouble(int)}).
 */
public interface MeasureAlgorithm extends ExecutableAlgorithm {

    AlgorithmExecution run(LabeledResult context, int measureIndex);
}
