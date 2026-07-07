package result.highlights.archetypes.topk;

import result.highlights.archetypes.megacontributor.MarginalContributionAlgorithm;
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

import java.util.Arrays;
import java.util.Collections;


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
