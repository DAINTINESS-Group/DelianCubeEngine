package highlights.instance;

import intentional.result.LabeledResult;
import intentional.labeling.Labeling;

/**
 * An {@link ExecutableAlgorithm} evaluated over the {@link highlights.metamodel.EvaluationAxis#LABELING}
 * axis: executed once per per-cell {@link Labeling} in the context.
 */
public interface LabelingAlgorithm extends ExecutableAlgorithm {

    AlgorithmExecution run(LabeledResult context, Labeling labeling);
}
