package highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cubemanager.cubebase.CubeQuery;
import intentional.model.ModelResult;
import intentional.model.archetypes.DefaultArchetypes;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * Modality reads the mass distribution's shape: uniform mass yields near-zero concentration and does not
 * hold; mass spiked into one cell yields high concentration and holds. Holistic-only.
 */
public class ModalityTest {

    private static ModelResult modalityOf(double[] values) {
        Result data = new Result();
        for (int i = 0; i < values.length; i++) {
            data.getCells().add(new Cell(new String[]{"m" + i, Double.toString(values[i]), "1"}, 1));
        }
        CubeQuery query = new CubeQuery("modalityTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, data, null);
        List<ModelResult> results = HighlightTestSupport.models(operatorResult, DefaultArchetypes.subset("Modality"));
        assertEquals(1, results.size());
        return results.get(0);
    }

    @Test
    public void uniformMassIsNotConcentrated() {
        ModelResult m = modalityOf(new double[]{100, 100, 100, 100});
        assertFalse("uniform does not hold", m.verdict());
        assertEquals("uniform concentration ~ 0", 0.0, m.holisticMagnitude(), 1e-9);
        assertNull("holistic-only", m.labelling());
    }

    @Test
    public void spikedMassIsConcentrated() {
        ModelResult m = modalityOf(new double[]{970, 10, 10, 10});
        assertTrue("spiked holds", m.verdict());
        assertTrue("spiked concentration > 0.5", m.holisticMagnitude() > 0.5);
        assertNull("holistic-only", m.labelling());
    }
}
