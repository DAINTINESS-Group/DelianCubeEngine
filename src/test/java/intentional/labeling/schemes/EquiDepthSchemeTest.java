package intentional.labeling.schemes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class EquiDepthSchemeTest {

    /** Five values, three buckets: the two extremes are set aside and land back in the end buckets. */
    @Test
    public void bucketsEqualCountsAndFoldsExtremesIntoEndBuckets() {
        EquiDepthScheme scheme = new EquiDepthScheme();
        scheme.fit(Arrays.asList(-8.0, -3.0, 0.0, 2.0, 7.0));

        assertEquals("Low", scheme.applyLabels(-8.0));
        assertEquals("Low", scheme.applyLabels(-3.0));
        assertEquals("OK", scheme.applyLabels(0.0));
        assertEquals("High", scheme.applyLabels(2.0));
        assertEquals("High", scheme.applyLabels(7.0));
    }

    /** Six values cut evenly into three buckets of two. */
    @Test
    public void evenCountNeedsNoSettingAside() {
        EquiDepthScheme scheme = new EquiDepthScheme();
        scheme.fit(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0));

        assertEquals("Low", scheme.applyLabels(1.0));
        assertEquals("Low", scheme.applyLabels(2.0));
        assertEquals("OK", scheme.applyLabels(3.0));
        assertEquals("OK", scheme.applyLabels(4.0));
        assertEquals("High", scheme.applyLabels(5.0));
        assertEquals("High", scheme.applyLabels(6.0));
    }

    @Test
    public void labelingBeforeFittingIsAnError() {
        assertThrows(IllegalStateException.class, () -> new EquiDepthScheme().applyLabels(1.0));
    }

    @Test
    public void domainIsTheOrderedBucketLabels() {
        EquiDepthScheme scheme = new EquiDepthScheme(Arrays.asList("BAD", "OK", "GOOD"));
        assertTrue(scheme.domain().ordered());
        assertEquals(Arrays.asList("BAD", "OK", "GOOD"), scheme.domain().labels());
    }

    @Test
    public void needsAtLeastTwoLabels() {
        assertThrows(IllegalArgumentException.class,
                () -> new EquiDepthScheme(Collections.singletonList("only")));
    }
}
