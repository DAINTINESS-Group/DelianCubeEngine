package intentional.assess.deltas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;

import intentional.assess.benchmarks.AssessBenchmark;
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

    private static DeltaScheme scheme(String... methods) {
        return new DeltaScheme(new ArrayList<>(Arrays.asList(methods)));
    }

    private static double[] compare(DeltaScheme scheme, List<Cell> targetCells, AssessBenchmark benchmark) {
        Map<Cell, Double> comparison =
                scheme.compareTargetToBenchmark(targetCells, benchmark, new ArrayList<ComparedCell>());
        double[] values = new double[targetCells.size()];
        for (int i = 0; i < targetCells.size(); i++) {
            values[i] = comparison.get(targetCells.get(i));
        }
        return values;
    }

    @Test
    public void percOfTotalScalesTheDifferenceAgainstTheTargetTotal() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = compare(scheme("percOfTotal", "difference"), targetCells, benchmark);

        assertEquals(-50.0 / 220, values[0], EPSILON);
        assertEquals(-20.0 / 220, values[1], EPSILON);
        assertEquals(10.0 / 220, values[2], EPSILON);
    }

    @Test
    public void minMaxNormRescalesTheDifferenceColumnToUnitRange() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = compare(scheme("minMaxNorm", "difference"), targetCells, benchmark);

        assertEquals(0.0, values[0], EPSILON);
        assertEquals(0.5, values[1], EPSILON);
        assertEquals(1.0, values[2], EPSILON);
    }

    @Test
    public void zscoreCentersTheDifferenceColumn() {
        List<Cell> targetCells = targets(100, 90, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = compare(scheme("zscore", "difference"), targetCells, benchmark);

        assertEquals(-1.0, values[0], EPSILON);
        assertEquals(0.0, values[1], EPSILON);
        assertEquals(1.0, values[2], EPSILON);
    }

    @Test
    public void rankOrdersAscendingAndAveragesTies() {
        List<Cell> targetCells = targets(100, 120, 30);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150, 110, 20);

        double[] values = compare(scheme("rank", "difference"), targetCells, benchmark);

        assertEquals(1.0, values[0], EPSILON);
        assertEquals(2.5, values[1], EPSILON);
        assertEquals(2.5, values[2], EPSILON);
    }

    @Test
    public void flatColumnsMapToZeroInsteadOfDividingByZero() {
        List<Cell> targetCells = targets(50, 50);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 40, 40);

        double[] minMax = compare(scheme("minMaxNorm", "difference"), targetCells, benchmark);
        double[] zscores = compare(scheme("zscore", "difference"), targetCells, benchmark);

        assertEquals(0.0, minMax[0], EPSILON);
        assertEquals(0.0, minMax[1], EPSILON);
        assertEquals(0.0, zscores[0], EPSILON);
        assertEquals(0.0, zscores[1], EPSILON);
    }

    @Test
    public void cellWiseChainStillRechainsTheRightOperand() {
        List<Cell> targetCells = targets(100);
        AssessBenchmark benchmark = benchmarkOf(targetCells, 150);

        double[] values = compare(scheme("ratio", "absolute"), targetCells, benchmark);

        assertEquals(50.0 / 150, values[0], EPSILON);
    }

    @Test
    public void holisticTransformsRunWithoutABenchmark() {
        DeltaScheme scheme = new DeltaScheme(new ArrayList<>(Arrays.asList("minMaxNorm", "ratio")),
                DeltaScheme.Operand.TARGET, DeltaScheme.Operand.constant(50));
        List<Cell> targetCells = targets(100, 50);

        double[] values = compare(scheme, targetCells, null);

        assertEquals(1.0, values[0], EPSILON);
        assertEquals(0.0, values[1], EPSILON);
    }

    @Test
    public void unknownFunctionNamesFailAtConstruction() {
        try {
            scheme("nonesuch", "difference");
            fail("expected the unknown function to be rejected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("nonesuch"));
        }
    }

    @Test
    public void holisticTransformCannotTakeTheOperandPair() {
        try {
            scheme("minMaxNorm");
            fail("expected the innermost holistic transform to be rejected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("cell-wise"));
        }
    }
}
