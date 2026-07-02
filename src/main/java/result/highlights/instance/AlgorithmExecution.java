package result.highlights.instance;

import result.highlights.metamodel.AlgorithmParams;


/**
 * A serializable record of an algorithm run: the algorithm's identity (a handle, not the live
 * object), the concrete parameter instantiations, and the produced {@link AlgorithmResult}.
 */
public final class AlgorithmExecution {
    public final String name;
    public final AlgorithmParams params;
    public final AlgorithmResult result;

    public AlgorithmExecution(String name, AlgorithmParams params, AlgorithmResult result) {
        this.name = name;
        this.params = params;
        this.result = result;
    }
}
