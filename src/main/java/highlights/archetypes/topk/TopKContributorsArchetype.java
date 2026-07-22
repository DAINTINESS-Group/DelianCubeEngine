package highlights.archetypes.topk;

import java.util.Arrays;
import java.util.Collections;

import highlights.archetypes.megacontributor.MarginalContributionAlgorithm;
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
 * Defines the top-k archetype property: which members contribute the most measure mass along a breakdown
 * dimension? Tested by {@link TopKContributionAlgorithm} over an additive measure. The generalization of
 * {@link MegaContributorArchetype}: same marginal contribution, but a rank-then-cut selection rather than a
 * dominance threshold.
 */
public final class TopKContributorsArchetype {

    private TopKContributorsArchetype() {}

    public static ArchetypeProperty create() {
        ElementaryHighlightRole topContributorRole = new ElementaryHighlightRole(
                "TopContributor",
                Collections.singletonList(new CharacterRole("Contributor")),
                new MeasureRole("Contribution"),
                Collections.<ScoreType>singletonList(MarginalContributionAlgorithm.CONTRIBUTION_SHARE));

        Algorithm algorithm = new TopKContributionAlgorithm(topContributorRole);
        return new ArchetypeProperty(
                "TopKContributors",
                new MainMeasureRole("Mass", MeasureConstraint.ADDITIVE),
                Collections.singletonList(new ExplanatorRole("Breakdown", ExplanatorConstraint.ANY)),
                Collections.singletonList(algorithm),
                Arrays.<ScoreType>asList(MarginalContributionAlgorithm.CONTRIBUTION_SHARE, InterestingnessFacet.PECULIARITY),
                Collections.singletonList(topContributorRole));
    }
}
