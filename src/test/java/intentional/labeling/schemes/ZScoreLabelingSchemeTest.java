package intentional.labeling.schemes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class ZScoreLabelingSchemeTest {

    /** Over {-2,-1,0,1,2} the sample deviation is ~1.58, so bands at 0.5 and 1.0 hit all five labels. */
    @Test
    public void labelsEveryBandOfTheSpectrum() {
        ZScoreLabelingScheme scheme = new ZScoreLabelingScheme(0.5, 1.0);
        scheme.fit(Arrays.asList(-2.0, -1.0, 0.0, 1.0, 2.0));

        assertEquals(ZScoreLabelingScheme.FAR_BELOW, scheme.applyLabels(-2.0));
        assertEquals(ZScoreLabelingScheme.BELOW, scheme.applyLabels(-1.0));
        assertEquals(ZScoreLabelingScheme.TYPICAL, scheme.applyLabels(0.0));
        assertEquals(ZScoreLabelingScheme.ABOVE, scheme.applyLabels(1.0));
        assertEquals(ZScoreLabelingScheme.FAR_ABOVE, scheme.applyLabels(2.0));
    }

    /** With the default bands, a lone spike beyond 2.2 deviations is far while the mass stays typical. */
    @Test
    public void defaultBandsSeparateASpikeFromTheMass() {
        ZScoreLabelingScheme scheme = new ZScoreLabelingScheme();
        List<Double> values = Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 11.0);
        scheme.fit(values);

        assertEquals(ZScoreLabelingScheme.FAR_ABOVE, scheme.applyLabels(11.0));
        assertEquals(ZScoreLabelingScheme.TYPICAL, scheme.applyLabels(0.0));
    }

    /** A distribution with no spread z-scores everything to zero. */
    @Test
    public void constantValuesAreAllTypical() {
        ZScoreLabelingScheme scheme = new ZScoreLabelingScheme();
        scheme.fit(Collections.nCopies(4, 7.0));
        assertEquals(ZScoreLabelingScheme.TYPICAL, scheme.applyLabels(7.0));
    }

    @Test
    public void labelingBeforeFittingIsAnError() {
        assertThrows(IllegalStateException.class, () -> new ZScoreLabelingScheme().applyLabels(1.0));
    }

    @Test
    public void domainIsOrderedAlongTheSpectrum() {
        ZScoreLabelingScheme scheme = new ZScoreLabelingScheme();
        assertTrue(scheme.domain().ordered());
        assertEquals(Arrays.asList(
                ZScoreLabelingScheme.FAR_BELOW, ZScoreLabelingScheme.BELOW, ZScoreLabelingScheme.TYPICAL,
                ZScoreLabelingScheme.ABOVE, ZScoreLabelingScheme.FAR_ABOVE),
                scheme.domain().labels());
    }
}
