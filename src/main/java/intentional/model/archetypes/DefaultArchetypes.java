package intentional.model.archetypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import intentional.model.ArchetypeProperty;
import intentional.model.Model;
import intentional.model.archetypes.labelpredominance.ElectionSpec;
import intentional.model.archetypes.labelpredominance.LabelDistributionAlgorithm;
import intentional.model.archetypes.megacontributor.MarginalContributionAlgorithm;
import intentional.model.archetypes.modality.NormalizedEntropyModalityAlgorithm;
import intentional.model.archetypes.outlier.ZScoreOutlierAlgorithm;
import intentional.model.archetypes.topk.TopKContributionAlgorithm;

/**
 * The generic archetype properties tested by default.
 */
public final class DefaultArchetypes {

    private DefaultArchetypes() {}

    public static List<ArchetypeProperty> all() {
        return all(ElectionSpec.DEFAULT);
    }

    public static List<ArchetypeProperty> all(ElectionSpec election) {
        List<ArchetypeProperty> archetypes = new ArrayList<>();
        archetypes.add(new ArchetypeProperty("MegaContributor",
                Collections.<Model>singletonList(new MarginalContributionAlgorithm())));
        archetypes.add(new ArchetypeProperty("TopKContributors",
                Collections.<Model>singletonList(new TopKContributionAlgorithm())));
        archetypes.add(new ArchetypeProperty("Outlier",
                Collections.<Model>singletonList(new ZScoreOutlierAlgorithm())));
        archetypes.add(new ArchetypeProperty("Modality",
                Collections.<Model>singletonList(new NormalizedEntropyModalityAlgorithm())));
        archetypes.add(new ArchetypeProperty("LabelPredominance",
                Collections.<Model>singletonList(new LabelDistributionAlgorithm(election))));
        return archetypes;
    }

    public static List<ArchetypeProperty> subset(String... names) {
        Set<String> keep = new HashSet<>(Arrays.asList(names));
        List<ArchetypeProperty> out = new ArrayList<>();
        for (ArchetypeProperty archetype : all()) {
            if (keep.contains(archetype.name)) out.add(archetype);
        }
        return out;
    }
}
