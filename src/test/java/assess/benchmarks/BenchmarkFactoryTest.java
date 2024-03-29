package assess.benchmarks;

import assess.CubeManagerAdapter;
import cubemanager.CubeManager;
import mainengine.Session;
import org.junit.Test;
import result.Cell;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BenchmarkFactoryTest {

    // Create a query containing the correct gammas for sibling
    // 1 that does not
    // The same for past benchmark
    private CubeManager initializeCubeManager() {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "loan");
        userInputList.put("inputFolder", "pkdd99_star");
        CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
        Session session = new Session(cubeManager);
        try {
            session.initialize(typeOfConnection, userInputList);
        } catch (RemoteException re) {
            System.exit(0);
        }
        return cubeManager;
    }

    @Test
    public void createConstantBenchmark() {
        BenchmarkFactory benchmarkFactory = new BenchmarkFactory(
                new CubeManagerAdapter(initializeCubeManager())
        );
        double expected = 100.0;

        AssessBenchmark constantBenchmark = benchmarkFactory.createBenchmark(
                Arrays.asList("Constant", "100"));

        assertEquals(expected, constantBenchmark.matchCell(null).get().toDouble(), 0.001);
    }

    @Test
    public void createSiblingBenchmark() {
        CubeManagerAdapter cubeManagerAdapter = new CubeManagerAdapter(initializeCubeManager());
        cubeManagerAdapter.setTargetCubeName("loan");
        cubeManagerAdapter.setMeasurement("amount");
        cubeManagerAdapter.setAggregationFunction("Avg");
        cubeManagerAdapter.setGroupBySet(
                Stream.of("region", "month")
                        .collect(Collectors.toCollection(HashSet::new))
        );
        cubeManagerAdapter.setSelectionPredicates(
                Stream.of(new String[][]{{"region", "south Moravia"}})
                        .collect(Collectors.toMap(predicate -> predicate[0],
                                predicate -> predicate[1]))
        );

        BenchmarkFactory benchmarkFactory = new BenchmarkFactory(cubeManagerAdapter);

        AssessBenchmark benchmark = benchmarkFactory.createBenchmark(
                Arrays.asList("Sibling", "region", "north Moravia"));

        Cell targetCell = new Cell(new String[] {"1994-10", "north Moravia", "10000.0", "1"});
        assertEquals(benchmark.matchCell(targetCell).get().toDouble(), 17508.0000, 0.0001);
    }

    @Test
    public void createSiblingWhenSigmaNotDefined() {
        CubeManagerAdapter cubeManagerAdapter = new CubeManagerAdapter(initializeCubeManager());
        cubeManagerAdapter.setTargetCubeName("loan");
        cubeManagerAdapter.setMeasurement("amount");
        cubeManagerAdapter.setAggregationFunction("Avg");
        cubeManagerAdapter.setGroupBySet(
                Stream.of("district_name", "month")
                        .collect(Collectors.toCollection(HashSet::new))
        );
        cubeManagerAdapter.setSelectionPredicates(
                Stream.of(new String[][]{{"region", "south Moravia"}})
                        .collect(Collectors.toMap(predicate -> predicate[0],
                                predicate -> predicate[1]))
        );
        BenchmarkFactory benchmarkFactory = new BenchmarkFactory(cubeManagerAdapter);
        String expectedMessage = "company was not defined in original predicates";

        RuntimeException actualException =
                assertThrows(RuntimeException.class, () ->
                        benchmarkFactory.createBenchmark(
                                Arrays.asList("Sibling", "company", "Toyota")
                        )
                );
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void createPastBenchmarkWhenDateIsNotDefined() {
        CubeManagerAdapter cubeManagerAdapter = new CubeManagerAdapter(initializeCubeManager());
        cubeManagerAdapter.setTargetCubeName("loan");
        cubeManagerAdapter.setMeasurement("amount");
        cubeManagerAdapter.setAggregationFunction("Avg");
        cubeManagerAdapter.setGroupBySet(
                Stream.of("district_name", "month")
                        .collect(Collectors.toCollection(HashSet::new))
        );
        cubeManagerAdapter.setSelectionPredicates(
                Stream.of(new String[][]{{"region", "south Moravia"}})
                        .collect(Collectors.toMap(predicate -> predicate[0],
                                predicate -> predicate[1]))
        );
        BenchmarkFactory benchmarkFactory = new BenchmarkFactory(cubeManagerAdapter);
        String expectedMessage = "A date was not defined in the selection predicates";

        RuntimeException actualException =
                assertThrows(RuntimeException.class, () ->
                        benchmarkFactory.createBenchmark(
                                Arrays.asList("Past", "10")
                        )
                );
        assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    public void createPastBenchmark() {
        CubeManagerAdapter cubeManagerAdapter = new CubeManagerAdapter(initializeCubeManager());
        cubeManagerAdapter.setTargetCubeName("loan");
        cubeManagerAdapter.setMeasurement("amount");
        cubeManagerAdapter.setAggregationFunction("Avg");

        cubeManagerAdapter.setSelectionPredicates(
                Stream.of(new String[][]{{"month", "10/1998"}})
                        .collect(Collectors.toMap(predicate -> predicate[0],
                                predicate -> predicate[1]))
        );

        cubeManagerAdapter.setGroupBySet(
                Stream.of("region", "month", "status")
                        .collect(Collectors.toCollection(HashSet::new))
        );
        BenchmarkFactory benchmarkFactory = new BenchmarkFactory(cubeManagerAdapter);
        AssessBenchmark benchmark = benchmarkFactory.createBenchmark(Arrays.asList("Past", "4"));
        Cell targetCell = new Cell(new String[] {"1998-10", "south Bohemia", "Running Contract/OK", "10000.0", "1"});
        Cell matchedCell = benchmark.matchCell(targetCell).get();
        assertEquals(175660.0, matchedCell.toDouble(), 0.0001);
    }
}
