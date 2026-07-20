package result.highlights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import result.Cell;
import result.Result;
import result.highlights.archetypes.modality.NormalizedEntropyModalityAlgorithm;
import result.highlights.instance.HolisticHighlight;
import result.highlights.instance.Score;
import result.highlights.metamodel.ArchetypeProperty;

/**
 * Modality reads the mass distribution's shape: uniform mass yields near-zero concentration and does not
 * hold; mass spiked into one cell yields high concentration and holds. Either way the archetype is
 * holistic-only (no elementary highlights).
 */
public class ModalityTest {

    private static HolisticHighlight modalityOf(double[] values) {
        Result data = new Result();
        for (int i = 0; i < values.length; i++) {
            data.getCells().add(new Cell(new String[]{"m" + i, Double.toString(values[i]), "1"}, 1));
        }
        CubeQuery query = new CubeQuery("modalityTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure("sum", "amount", "amount");

        OperatorResult operatorResult = new OperatorResult(query, data, null);
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(
                result.highlights.archetypes.modality.ModalityArchetype.create());

        HighlightSet highlights = new HighlightExtractor().extract(operatorResult, candidates, schema);
        assertEquals(1, highlights.size());
        return (HolisticHighlight) highlights.highlights().get(0);
    }

    private static double concentrationOf(HolisticHighlight h) {
        for (Score s : h.getScores()) {
            if (s.type == NormalizedEntropyModalityAlgorithm.CONCENTRATION) return s.value;
        }
        throw new AssertionError("no concentration score");
    }

    @Test
    public void uniformMassIsNotConcentrated() {
        HolisticHighlight h = modalityOf(new double[]{100, 100, 100, 100});
        assertFalse("uniform does not hold", h.execution.result.verdict());
        assertEquals("uniform concentration ~ 0", 0.0, concentrationOf(h), 1e-9);
        assertTrue("holistic-only", h.elementary().isEmpty());
    }

    @Test
    public void spikedMassIsConcentrated() {
        HolisticHighlight h = modalityOf(new double[]{970, 10, 10, 10});
        assertTrue("spiked holds", h.execution.result.verdict());
        assertTrue("spiked concentration > 0.5", concentrationOf(h) > 0.5);
        assertTrue("holistic-only", h.elementary().isEmpty());
    }
}
