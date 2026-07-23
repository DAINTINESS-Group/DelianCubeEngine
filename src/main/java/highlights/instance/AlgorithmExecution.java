package highlights.instance;

import java.util.Collections;
import java.util.List;

import highlights.metamodel.Algorithm;

/**
 * The model-level materialization of an {@link Algorithm}: the algorithm it ran, the
 * {@link ParameterInstantiation}s it executed with, and the {@link AlgorithmResult} it produced. It also
 * carries the holistic significance {@link Score}s and salient {@link ScoredFinding}s the run yielded, which
 * the extractor lifts onto the Holistic Highlight and its Elementary Highlights.
 */
public final class AlgorithmExecution {
    public final Algorithm algorithm;
    public final List<ParameterInstantiation> parameters;
    public final AlgorithmResult result;
    public final List<Score> holisticScores;
    public final List<ScoredFinding> salient;

    public AlgorithmExecution(Algorithm algorithm, List<ParameterInstantiation> parameters,
                              AlgorithmResult result, List<Score> holisticScores, List<ScoredFinding> salient) {
        this.algorithm = algorithm;
        this.parameters = parameters == null ? Collections.<ParameterInstantiation>emptyList() : parameters;
        this.result = result;
        this.holisticScores = holisticScores == null ? Collections.<Score>emptyList() : holisticScores;
        this.salient = salient == null ? Collections.<ScoredFinding>emptyList() : salient;
    }
}
