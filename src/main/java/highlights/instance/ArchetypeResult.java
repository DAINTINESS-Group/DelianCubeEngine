package highlights.instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import highlights.metamodel.ResultType;

/**
 * The outcome of running an algorithm: the verdict, the auxiliary metrics behind it, the holistic
 * significance scores, and the salient elementary findings.
 */
public final class ArchetypeResult implements ResultType {

    private final boolean verdict;
    private final Map<String, Double> auxiliaryMetrics = new LinkedHashMap<>();
    private final List<Score> holisticScores;
    private final List<ScoredFinding> elementary;

    public ArchetypeResult(boolean verdict, List<Score> holisticScores, List<ScoredFinding> elementary) {
        this.verdict = verdict;
        this.holisticScores = holisticScores == null ? new ArrayList<>() : new ArrayList<>(holisticScores);
        this.elementary = elementary == null ? new ArrayList<>() : new ArrayList<>(elementary);
    }

    /** Records a technical metric behind the verdict; returns this for chaining. */
    public ArchetypeResult metric(String key, double value) {
        auxiliaryMetrics.put(key, value);
        return this;
    }

    @Override
    public boolean verdict() { return verdict; }

    @Override
    public Map<String, Double> auxiliaryMetrics() { return Collections.unmodifiableMap(auxiliaryMetrics); }

    public List<Score> holisticScores() { return Collections.unmodifiableList(holisticScores); }

    public List<ScoredFinding> elementary() { return Collections.unmodifiableList(elementary); }
}
