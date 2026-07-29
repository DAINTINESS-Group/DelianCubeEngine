package intentional.labeling.schemes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class EquiWidthSchemeTest {

    /** Range -8..7 in three intervals of width 5, upper-inclusive: (-8,-3], (-3,2], (2,7]. */
    @Test
    public void splitsTheRangeIntoUpperInclusiveIntervals() {
        EquiWidthScheme scheme = new EquiWidthScheme();
        scheme.fit(Arrays.asList(-8.0, -3.0, 0.0, 2.0, 7.0));

        assertEquals("Low", scheme.applyLabels(-8.0));
        assertEquals("Low", scheme.applyLabels(-3.0));
        assertEquals("OK", scheme.applyLabels(0.0));
        assertEquals("OK", scheme.applyLabels(2.0));
        assertEquals("High", scheme.applyLabels(7.0));
    }

    /** A distribution with no spread puts everything in the first interval. */
    @Test
    public void zeroWidthRangeIsASingleBucket() {
        EquiWidthScheme scheme = new EquiWidthScheme();
        scheme.fit(Collections.nCopies(4, 5.0));
        assertEquals("Low", scheme.applyLabels(5.0));
    }

    @Test
    public void labelingBeforeFittingIsAnError() {
        assertThrows(IllegalStateException.class, () -> new EquiWidthScheme().applyLabels(1.0));
    }

    @Test
    public void domainIsTheOrderedIntervalLabels() {
        EquiWidthScheme scheme = new EquiWidthScheme(Arrays.asList("BAD", "OK", "GOOD"));
        assertTrue(scheme.domain().ordered());
        assertEquals(Arrays.asList("BAD", "OK", "GOOD"), scheme.domain().labels());
    }
}
