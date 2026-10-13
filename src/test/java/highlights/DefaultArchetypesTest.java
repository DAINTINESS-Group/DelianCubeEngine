package highlights;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import intentional.model.ArchetypeProperty;
import intentional.model.archetypes.DefaultArchetypes;

/** The generic archetype set profiles start from. */
public class DefaultArchetypesTest {

    private static List<String> names(List<ArchetypeProperty> archetypes) {
        return archetypes.stream().map(a -> a.name).collect(Collectors.toList());
    }

    @Test
    public void allReturnsTheGenericArchetypes() {
        assertEquals(
                java.util.Arrays.asList("MegaContributor", "TopKContributors", "Outlier", "Modality", "LabelPredominance"),
                names(DefaultArchetypes.all()));
    }

    @Test
    public void allReturnsAFreshMutableListSoOperatorsCanAppend() {
        List<ArchetypeProperty> first = DefaultArchetypes.all();
        int base = first.size();
        first.add(DefaultArchetypes.all().get(0)); // append is allowed
        assertEquals(base + 1, first.size());
        assertEquals("a second call is unaffected by the first", base, DefaultArchetypes.all().size());
    }
}
