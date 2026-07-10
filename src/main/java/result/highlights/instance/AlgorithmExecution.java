package result.highlights.instance;

import result.highlights.metamodel.AlgorithmParams;
import result.highlights.metamodel.ResultType;

/**
 * A record of an algorithm run: the algorithm's identity (a handle, not the live object), the concrete
 * parameter instantiation, and the produced result.
 */
public final class AlgorithmExecution {
    public final String name;
    public final AlgorithmParams params;
    public final ResultType result;

    public AlgorithmExecution(String name, AlgorithmParams params, ResultType result) {
        this.name = name;
        this.params = params;
        this.result = result;
    }
}
