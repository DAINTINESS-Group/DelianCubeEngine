package result.highlights.instance;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import result.highlights.metamodel.ResultType;


/**
 * The typed outcome of running an {@code Algorithm} for an {@code ArchetypeProperty}: a dataset-level
 * verdict plus the salient cells that detail it:
 * <ul>
 *   <li>{@link #execution} — the algorithm execution (its name, params, and {@code AlgorithmResult}
 *       verdict): the holistic;</li>
 *   <li>{@link #holisticScores} — the significance scores for the holistic;</li>
 *   <li>{@link #elementary} — the salient cells, each with the scores the archetype assigned it.</li>
 * </ul>
 */
public final class ArchetypeResult implements ResultType {

    /** The algorithm execution: name + params + the verdict it produced — the holistic. */
    public final AlgorithmExecution execution;

    /** Significance scores for the holistic. */
    public final List<Score> holisticScores;

    /** The salient cells that compose the holistic, with their scores. */
    public final List<ScoredFinding> elementary;

    public ArchetypeResult(AlgorithmExecution execution,
                           List<Score> holisticScores, List<ScoredFinding> elementary) {
        if (execution == null) {
            throw new IllegalArgumentException("an archetype must produce an AlgorithmExecution");
        }
        this.execution = execution;
        this.holisticScores = holisticScores == null ? new ArrayList<>() : holisticScores;
        this.elementary = elementary == null ? new ArrayList<>() : elementary;
    }

    @Override
    public boolean verdict() { return execution.result.verdict; }

    @Override
    public Map<String, Double> auxiliaryMetrics() { return execution.result.auxiliaryMetrics; }
}
