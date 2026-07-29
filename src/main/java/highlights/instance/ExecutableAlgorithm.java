package highlights.instance;

import highlights.metamodel.Algorithm;
import intentional.result.LabeledResult;

/**
 * An {@link Algorithm} that can be checked against a {@link LabeledResult} for applicability. Execution
 * itself is declared by the typed sub-contract matching the archetype's
 * {@link highlights.metamodel.EvaluationAxis}: a {@link MeasureAlgorithm} runs once per main measure, a
 * {@link LabelingAlgorithm} once per labeling in the context. The extractor rejects a candidate whose
 * contract does not match its archetype's axis.
 */
public interface ExecutableAlgorithm extends Algorithm {

    boolean appliesTo(LabeledResult context);
}
