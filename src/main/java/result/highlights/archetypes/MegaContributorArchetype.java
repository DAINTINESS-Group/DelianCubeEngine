package result.highlights.archetypes;

import result.highlights.metamodel.Algorithm;
import result.highlights.metamodel.ArchetypeProperty;
import result.highlights.metamodel.CharacterRole;
import result.highlights.metamodel.ElementaryHighlightRole;
import result.highlights.metamodel.ExplanatorConstraint;
import result.highlights.metamodel.ExplanatorRole;
import result.highlights.metamodel.InterestingnessFacet;
import result.highlights.metamodel.MainMeasureRole;
import result.highlights.metamodel.MeasureConstraint;
import result.highlights.metamodel.MeasureRole;
import result.highlights.metamodel.ScoreType;

import java.util.Collections;


/**
 * Defines the mega-contributor archetype property: does a single cell hold a dominant share of the measure
 * mass along a breakdown dimension? Tested by {@link MarginalContributionAlgorithm} over an additive measure.
 */
public final class MegaContributorArchetype {

    private MegaContributorArchetype() {}

    public static ArchetypeProperty create() {
        ElementaryHighlightRole contributorRole = new ElementaryHighlightRole(
                "MegaContributor",
                Collections.singletonList(new CharacterRole("Contributor")),
                new MeasureRole("Contribution"),
                Collections.<ScoreType>singletonList(InterestingnessFacet.PECULIARITY));
        
        Algorithm algorithm = new MarginalContributionAlgorithm(contributorRole);
        return new ArchetypeProperty(
                "MegaContributor",
                new MainMeasureRole("Mass", MeasureConstraint.ADDITIVE),
                Collections.singletonList(new ExplanatorRole("Breakdown", ExplanatorConstraint.ANY)),
                Collections.singletonList(algorithm),
                Collections.<ScoreType>singletonList(InterestingnessFacet.PECULIARITY),
                Collections.singletonList(contributorRole));
    }
}
