package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import highlights.archetypes.DefaultArchetypes;
import highlights.metamodel.ArchetypeProperty;
import intentionaloperator.IntentionalOperator;

/** The default archetype set every operator inherits, and the append-your-own path ASSESS uses. */
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

    @Test
    public void operatorInheritsTheDefaultsUnlessItOverrides() {
        IntentionalOperator bareOperator = () -> null; // only toOperatorResult is abstract
        assertTrue(names(bareOperator.registeredArchetypes()).contains("Modality"));
        assertEquals(DefaultArchetypes.all().size(), bareOperator.registeredArchetypes().size());
    }
}
