package result.highlights.metamodel;

import result.highlights.OperatorResult;
import result.highlights.instance.ArchetypeResult;


/**
 * The testing logic an {@link ArchetypeProperty} utilizes to evaluate its hypothesis. It declares whether
 * it can run over a given {@link OperatorResult} via {@link #appliesTo(OperatorResult)} (its input/model
 * requirements), reads the models in the context (and/or runs its own config-free model over the data),
 * and produces an {@link ArchetypeResult} — a dataset-level verdict plus the salient cells that detail it.
 * It is evaluated once per main measure: {@code measureIndex} is the position of that measure in the
 * result's cells (see {@link result.Cell#toDouble(int)}).
 */
public interface Algorithm {

    String name();

    AlgorithmParams params();

    boolean appliesTo(OperatorResult context);

    ArchetypeResult run(OperatorResult context, int measureIndex);
}
