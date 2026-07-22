package highlights.archetypes.megacontributor;

import java.util.Arrays;
import java.util.Collections;

import highlights.metamodel.Algorithm;
import highlights.metamodel.ArchetypeProperty;
import highlights.metamodel.CharacterRole;
import highlights.metamodel.ElementaryHighlightRole;
import highlights.metamodel.ExplanatorConstraint;
import highlights.metamodel.ExplanatorRole;
import highlights.metamodel.InterestingnessFacet;
import highlights.metamodel.MainMeasureRole;
import highlights.metamodel.MeasureConstraint;
import highlights.metamodel.MeasureRole;
import highlights.metamodel.ScoreType;


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
                Collections.<ScoreType>singletonList(MarginalContributionAlgorithm.CONTRIBUTION_SHARE));

        Algorithm algorithm = new MarginalContributionAlgorithm(contributorRole);
        return new ArchetypeProperty(
                "MegaContributor",
                new MainMeasureRole("Mass", MeasureConstraint.ADDITIVE),
                Collections.singletonList(new ExplanatorRole("Breakdown", ExplanatorConstraint.ANY)),
                Collections.singletonList(algorithm),
                Arrays.<ScoreType>asList(MarginalContributionAlgorithm.CONTRIBUTION_SHARE, InterestingnessFacet.PECULIARITY),
                Collections.singletonList(contributorRole));
    }
}
