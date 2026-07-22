package highlights.metamodel;

import java.util.List;


/**
 * A pattern hypothesis that may hold over a dataset (trend, mega-contributor, modality, ...). It operates
 * over a {@link MainMeasureRole}, reserves {@link ExplanatorRole}s, owns a family of candidate
 * {@link Algorithm}s that test it, and characterizes the resulting holistic with holistic {@link ScoreType}s
 * and a set of {@link ElementaryHighlightRole}s.
 */
public final class ArchetypeProperty {
    public final String name;
    public final MainMeasureRole mainMeasureRole;
    public final List<ExplanatorRole> explanatorRoles;
    public final List<Algorithm> candidateAlgorithms;
    public final List<ScoreType> hhScoreTypes;
    public final List<ElementaryHighlightRole> elementaryHighlightRoles;
    public final EvaluationAxis axis;

    public ArchetypeProperty(String name, MainMeasureRole mainMeasureRole, List<ExplanatorRole> explanatorRoles,
                             List<Algorithm> candidateAlgorithms, List<ScoreType> hhScoreTypes,
                             List<ElementaryHighlightRole> elementaryHighlightRoles) {
        this(name, mainMeasureRole, explanatorRoles, candidateAlgorithms, hhScoreTypes,
                elementaryHighlightRoles, EvaluationAxis.MEASURE);
    }

    public ArchetypeProperty(String name, MainMeasureRole mainMeasureRole, List<ExplanatorRole> explanatorRoles,
                             List<Algorithm> candidateAlgorithms, List<ScoreType> hhScoreTypes,
                             List<ElementaryHighlightRole> elementaryHighlightRoles, EvaluationAxis axis) {
        this.name = name;
        this.mainMeasureRole = mainMeasureRole;
        this.explanatorRoles = explanatorRoles;
        this.candidateAlgorithms = candidateAlgorithms;
        this.hhScoreTypes = hhScoreTypes;
        this.elementaryHighlightRoles = elementaryHighlightRoles;
        this.axis = axis;
    }
}
