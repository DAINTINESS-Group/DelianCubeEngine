package cubemanager.cubebase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** The typed aggregation vocabulary: classification is case/format tolerant, additivity is a fixed property. */
public class AggregationFunctionTest {

    @Test
    public void classifiesTokensTolerantly() {
        assertEquals(AggregationFunction.SUM, AggregationFunction.parse("SUM"));
        assertEquals(AggregationFunction.SUM, AggregationFunction.parse("sum"));
        assertEquals(AggregationFunction.SUM, AggregationFunction.parse("Sum(amount)"));
        assertEquals(AggregationFunction.AVG, AggregationFunction.parse("avg"));
        assertEquals(AggregationFunction.DERIVED, AggregationFunction.parse(""));
        assertEquals(AggregationFunction.DERIVED, AggregationFunction.parse(null));
        assertEquals(AggregationFunction.UNKNOWN, AggregationFunction.parse("median"));
    }

    @Test
    public void additivityMatchesTheOldHeuristic() {
        assertTrue(AggregationFunction.SUM.additive);
        assertTrue(AggregationFunction.COUNT.additive);
        assertFalse(AggregationFunction.AVG.additive);
        assertFalse(AggregationFunction.MIN.additive);
        assertFalse(AggregationFunction.MAX.additive);
        assertFalse(AggregationFunction.DERIVED.additive);
        assertFalse(AggregationFunction.UNKNOWN.additive);
    }
}
