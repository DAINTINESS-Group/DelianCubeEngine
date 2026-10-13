package intentional.labeling.schemes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;

import org.junit.Test;

public class MedianDistanceSchemeTest {

    /** Median 10: OK from 5 to 15. */
    @Test
    public void aPositiveMedianLabelsFromHalfToOneAndAHalfTimesIt() {
        MedianDistanceScheme scheme = new MedianDistanceScheme();
        scheme.fit(Arrays.asList(4.0, 10.0, 20.0));

        assertEquals("Low", scheme.applyLabels(4.0));
        assertEquals("OK", scheme.applyLabels(5.0));
        assertEquals("OK", scheme.applyLabels(10.0));
        assertEquals("OK", scheme.applyLabels(15.0));
        assertEquals("High", scheme.applyLabels(20.0));
    }

    /** Median -10: OK from -15 to -5, and the median itself is OK. */
    @Test
    public void aNegativeMedianKeepsLowBelowAndHighAbove() {
        MedianDistanceScheme scheme = new MedianDistanceScheme();
        scheme.fit(Arrays.asList(-20.0, -10.0, 0.0));

        assertEquals("Low", scheme.applyLabels(-20.0));
        assertEquals("OK", scheme.applyLabels(-15.0));
        assertEquals("OK", scheme.applyLabels(-10.0));
        assertEquals("OK", scheme.applyLabels(-5.0));
        assertEquals("High", scheme.applyLabels(0.0));
    }

    @Test
    public void aZeroMedianIsAnError() {
        assertThrows(IllegalStateException.class,
                () -> new MedianDistanceScheme().fit(Arrays.asList(-3.0, 0.0, 3.0)));
    }

    @Test
    public void labelingBeforeFittingIsAnError() {
        assertThrows(IllegalStateException.class, () -> new MedianDistanceScheme().applyLabels(1.0));
    }
}
