package intentional.assess.deltas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import intentional.assess.benchmarks.AssessBenchmark;
import intentional.assess.syntax.AssessQueryLexer;
import intentional.assess.syntax.AssessQueryParser;
import intentional.assess.utils.ComparedCell;
import result.Cell;

public class DeltaSchemeTest {

    private static final double EPSILON = 1e-9;

    private static Cell cell(String member, double value) {
        return new Cell(new String[]{member, Double.toString(value), "1"});
    }

    private static List<Cell> targets(double... values) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            cells.add(cell("member" + i, values[i]));
        }
        return cells;
    }

    private static AssessBenchmark benchmarkOf(List<Cell> targetCells, double... values) {
        Map<String, Double> byMember = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            byMember.put(targetCells.get(i).getDimensionMembers().get(0), values[i]);
        }
        return targetCell -> {
            Double value = byMember.get(targetCell.getDimensionMembers().get(0));
            return value == null ? Optional.<Cell>empty()
                    : Optional.of(cell(targetCell.getDimensionMembers().get(0), value));
        };
    }

    /** The expression as a USING clause writes it, over the target measure {@code m}. */
    private static DeltaScheme scheme(String expression) {
        try {
            AssessQueryParser parser = new AssessQueryParser(new CommonTokenStream(new AssessQueryLexer(
                    new ANTLRInputStream(new ByteArrayInputStream(expression.getBytes(StandardCharsets.UTF_8))))));
            return new DeltaScheme(parser.comparison_expression(), ref -> {
                if (ref.matches("[0-9.]+")) return DeltaScheme.Operand.constant(Double.parseDouble(ref));
                return ref.startsWith("benchmark.") ? DeltaScheme.Operand.BENCHMARK : DeltaScheme.Operand.TARGET;
            });
        } catch (IOException | RecognitionException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Cell, Double> compare(DeltaScheme scheme, List<Cell> targetCells, AssessBenchmark benchmark) {
        return scheme.compareTargetToBenchmark(targetCells, benchmark, new ArrayList<ComparedCell>());
    }

    private static double[] values(DeltaScheme scheme, List<Cell> targetCells, AssessBenchmark benchmark) {
        Map<Cell, Double> comparison = compare(scheme, targetCells, benchmark);
        double[] values = new double[targetCells.size()];
        for (int i = 0; i < targetCells.size(); i++) {
            values[i] = comparison.get(targetCells.get(i));
        }
        return values;
    }

    private static void assertRejected(String expression, String messagePart) {
        try {
            scheme(expression);
            fail("expected " + expression + " to be rejected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage(), e.getMessage().contains(messagePart));
        }
    }

    @Test
    public void percOfTotalScalesTheDifferenceAgainstTheTargetTotal() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = values(scheme("percOfTotal(difference(m, benchmark.m))"), targetCells, benchmark);

        assertEquals(-50.0 / 220, values[0], EPSILON);
        assertEquals(-20.0 / 220, values[1], EPSILON);
        assertEquals(10.0 / 220, values[2], EPSILON);
    }

    @Test
    public void minMaxNormRescalesTheDifferenceColumnToUnitRange() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = values(scheme("minMaxNorm(difference(m, benchmark.m))"), targetCells, benchmark);

        assertEquals(0.0, values[0], EPSILON);
        assertEquals(0.5, values[1], EPSILON);
        assertEquals(1.0, values[2], EPSILON);
    }

    @Test
    public void zscoreCentersTheDifferenceColumn() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = values(scheme("zscore(difference(m, benchmark.m))"), targetCells, benchmark);

        assertEquals(-1.0, values[0], EPSILON);
        assertEquals(0.0, values[1], EPSILON);
        assertEquals(1.0, values[2], EPSILON);
    }

    @Test
    public void rankOrdersAscendingAndAveragesTies() {
        List<Cell> targetCells = targets(100, 120, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = values(scheme("rank(difference(m, benchmark.m))"), targetCells, benchmark);

        assertEquals(1.0, values[0], EPSILON);
        assertEquals(2.5, values[1], EPSILON);
        assertEquals(2.5, values[2], EPSILON);
    }

    @Test
    public void flatColumnsMapToZeroInsteadOfDividingByZero() {
        List<Cell> targetCells = targets(50, 50);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 40, 40);

        double[] minMax = values(scheme("minMaxNorm(difference(m, benchmark.m))"), targetCells, benchmark);
        double[] zscores = values(scheme("zscore(difference(m, benchmark.m))"), targetCells, benchmark);

        assertEquals(0.0, minMax[0], EPSILON);
        assertEquals(0.0, minMax[1], EPSILON);
        assertEquals(0.0, zscores[0], EPSILON);
        assertEquals(0.0, zscores[1], EPSILON);
    }

    @Test
    public void everyFunctionNamesItsOwnArguments() {
        List<Cell> targetCells = targets(100);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150);

        double overBenchmark = values(scheme("ratio(absolute(m, benchmark.m), benchmark.m)"), targetCells, benchmark)[0];
        double overTarget = values(scheme("ratio(difference(m, benchmark.m), m)"), targetCells, benchmark)[0];

        assertEquals(50.0 / 150, overBenchmark, EPSILON);
        assertEquals(-50.0 / 100, overTarget, EPSILON);
    }

    @Test
    public void holisticTransformsRunWithoutABenchmark() {
        List<Cell> targetCells = targets(100, 50);

        double[] values = values(scheme("minMaxNorm(ratio(m, 50))"), targetCells, null);

        assertEquals(1.0, values[0], EPSILON);
        assertEquals(0.0, values[1], EPSILON);
    }

    @Test
    public void operandZscoresNormalizeEachPopulationSeparately() {
        List<Cell> targetCells = targets(10, 20, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 30, 20, 10);

        double[] values = values(scheme("difference(zscore(m), zscore(benchmark.m))"), targetCells, benchmark);

        assertEquals(-2.0, values[0], EPSILON);
        assertEquals(0.0, values[1], EPSILON);
        assertEquals(2.0, values[2], EPSILON);
    }

    @Test
    public void percOfTotalDividesByTheColumnItIsGiven() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = values(scheme("ratio(percOfTotal(m, m), percOfTotal(benchmark.m, benchmark.m))"),
                targetCells, benchmark);

        assertEquals((100.0 / 220) / (150.0 / 280), values[0], EPSILON);
        assertEquals((90.0 / 220) / (110.0 / 280), values[1], EPSILON);
        assertEquals((30.0 / 220) / (20.0 / 280), values[2], EPSILON);
    }

    @Test
    public void theDefaultComparisonIsTheDifferenceToTheBenchmark() {
        List<Cell> targetCells = targets(100, 90);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 80);

        double[] values = values(DeltaScheme.difference(), targetCells, benchmark);

        assertEquals(-50.0, values[0], EPSILON);
        assertEquals(10.0, values[1], EPSILON);
    }

    @Test
    public void withoutBenchmarkOrUsingEachCellKeepsItsOwnValue() {
        List<Cell> targetCells = targets(100, 90);

        double[] values = values(DeltaScheme.identity(), targetCells, null);

        assertEquals(100.0, values[0], EPSILON);
        assertEquals(90.0, values[1], EPSILON);
    }

    @Test
    public void aRatioByAZeroBenchmarkLeavesTheCellOut() {
        List<Cell> targetCells = targets(100, 0, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 0, 0, 20);

        Map<Cell, Double> comparison = compare(scheme("ratio(m, benchmark.m)"), targetCells, benchmark);

        assertFalse("x / 0 is undefined", comparison.containsKey(targetCells.get(0)));
        assertFalse("0 / 0 is undefined", comparison.containsKey(targetCells.get(1)));
        assertEquals(1.5, comparison.get(targetCells.get(2)), EPSILON);
    }

    @Test
    public void undefinedCellsStayOutOfColumnStatistics() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 0, 110, 20);

        Map<Cell, Double> comparison = compare(scheme("zscore(ratio(m, benchmark.m))"), targetCells, benchmark);

        assertEquals(2, comparison.size());
        assertEquals(-Math.sqrt(0.5), comparison.get(targetCells.get(1)), EPSILON);
        assertEquals(Math.sqrt(0.5), comparison.get(targetCells.get(2)), EPSILON);
    }

    @Test
    public void missingInputValuesLeaveTheCellOut() {
        List<Cell> targetCells = targets(Double.NaN, 90);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 100, Double.NaN);

        assertTrue(compare(scheme("difference(m, benchmark.m)"), targetCells, benchmark).isEmpty());
    }

    @Test
    public void percOfTotalOverAZeroTotalFails() {
        List<Cell> targetCells = targets(50, -50);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 10, 10);

        try {
            compare(scheme("percOfTotal(difference(m, benchmark.m))"), targetCells, benchmark);
            fail("expected the zero total to be rejected");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("zero"));
        }
    }

    @Test
    public void unknownFunctionNamesFailAtConstruction() {
        assertRejected("nonesuch(m, benchmark.m)", "nonesuch");
    }

    @Test
    public void aCellWiseFunctionTakesTwoArguments() {
        assertRejected("ratio(absolute(m, benchmark.m))", "two values");
    }

    @Test
    public void aHolisticFunctionTakesOneArgument() {
        assertRejected("zscore(m, benchmark.m)", "one argument");
        assertRejected("percOfTotal(m, m, m)", "one or two arguments");
    }

    @Test
    public void aConstantCannotBeTransformed() {
        assertRejected("zscore(5)", "constant");
    }
}
