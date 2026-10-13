package highlights.metamodel;

import java.util.List;

/**
 * A role an elementary highlight plays within its holistic highlight (peak, mega-contributor, top-k,
 * assessed cell, ...): the contextualizing {@link CharacterRole}s, the {@link MeasureRole} it reports, and
 * the {@link ScoreType}s of its scores.
 */
public final class ElementaryHighlightRole {
    public final String name;
    public final List<CharacterRole> characterRoles;
    public final MeasureRole measureRole;
    public final List<ScoreType> scoreTypes;

    public ElementaryHighlightRole(String name, List<CharacterRole> characterRoles,
                                   MeasureRole measureRole, List<ScoreType> scoreTypes) {
        this.name = name;
        this.characterRoles = characterRoles;
        this.measureRole = measureRole;
        this.scoreTypes = scoreTypes;
    }

    public String name() { return name; }

    @Override
    public String toString() { return name; }
}
