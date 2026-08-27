package intentional.assess.fetch;

import intentional.assess.CubeManagerAdapter;
import cubemanager.CubeManager;
import mainengine.Session;
import org.junit.Test;
import result.Cell;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class SerialFetcherTest {

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

    private CubeManagerAdapter loanAdapter(String[][] predicates, String... groupBy) {
        CubeManagerAdapter adapter = new CubeManagerAdapter(initializeCubeManager());
        adapter.setTargetCubeName("loan");
        adapter.setMeasurement("amount");
        adapter.setAggregationFunction("Avg");
        adapter.setGroupBySet(Stream.of(groupBy).collect(Collectors.toCollection(HashSet::new)));
        adapter.setSelectionPredicates(Stream.of(predicates)
                .collect(Collectors.toMap(predicate -> predicate[0], predicate -> predicate[1])));
        return adapter;
    }

    protected AssessFetcher fetcher() {
        return new SerialFetcher();
    }

    private FetchedCubes fetch(CubeManagerAdapter adapter, List<String> details) {
        return fetcher().fetch(adapter, Collections.singletonList(details));
    }

    @Test
    public void fetchesAConstantBenchmark() {
        CubeManagerAdapter adapter = loanAdapter(
                new String[][]{{"region", "south Moravia"}}, "region", "month");

        FetchedCubes cubes = fetch(adapter, Arrays.asList("Constant", "100"));

        assertEquals(100.0, cubes.benchmarks.get(0).matchCell(null).get().toDouble(), 0.001);
    }

    @Test
    public void fetchesASiblingBenchmark() {
        CubeManagerAdapter adapter = loanAdapter(
                new String[][]{{"region", "south Moravia"}}, "region", "month");

        FetchedCubes cubes = fetch(adapter, Arrays.asList("Sibling", "region", "north Moravia"));

        Cell targetCell = new Cell(new String[]{"1994-10", "north Moravia", "10000.0", "1"});
        assertEquals(17508.0, cubes.benchmarks.get(0).matchCell(targetCell).get().toDouble(), 0.0001);
    }

    @Test
    public void rejectsASiblingWhoseLevelIsNotSliced() {
        CubeManagerAdapter adapter = loanAdapter(
                new String[][]{{"region", "south Moravia"}}, "district_name", "month");

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> fetch(adapter, Arrays.asList("Sibling", "company", "Toyota")));
        assertEquals("company was not defined in original predicates", error.getMessage());
    }

    @Test
    public void rejectsAPastBenchmarkWithoutADatePredicate() {
        CubeManagerAdapter adapter = loanAdapter(
                new String[][]{{"region", "south Moravia"}}, "district_name", "month");

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> fetch(adapter, Arrays.asList("Past", "10")));
        assertEquals("A date was not defined in the selection predicates", error.getMessage());
    }

    @Test
    public void fetchesAPastBenchmark() {
        CubeManagerAdapter adapter = loanAdapter(
                new String[][]{{"month", "10/1998"}}, "region", "month", "status");

        FetchedCubes cubes = fetch(adapter, Arrays.asList("Past", "4"));

        Cell targetCell = new Cell(
                new String[]{"1998-10", "south Bohemia", "Running Contract/OK", "10000.0", "1"});
        assertEquals(175660.0, cubes.benchmarks.get(0).matchCell(targetCell).get().toDouble(), 0.0001);
    }
}
