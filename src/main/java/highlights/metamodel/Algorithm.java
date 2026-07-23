package highlights.metamodel;

import intentional.result.LabeledResult;

/**
 * The testing logic an {@link ArchetypeProperty} utilizes to evaluate its hypothesis. It declares whether
 * it can run over a given {@link LabeledResult} via {@link #appliesTo(LabeledResult)} (its input/model
 * requirements), reads the models in the context (and/or runs its own config-free model over the data),
 * and produces a {@link ResultType}. Evaluated once per main measure: {@code measureIndex} is the
 * position of that measure in the result's cells (see {@link result.Cell#toDouble(int)}).
 */
public interface Algorithm {

    String name();

    AlgorithmParams params();

    boolean appliesTo(LabeledResult context);

    ResultType run(LabeledResult context, int measureIndex);
}
