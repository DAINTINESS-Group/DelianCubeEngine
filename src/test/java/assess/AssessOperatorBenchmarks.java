package assess;

import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Problem Parameters
 * <ol>
 *     <li>Data Set Size</li>
 *     <li>Grouper dimension</li>
 *     <li>result size</li>
 *     <li>type of Benchmark</li>
 * </ol>
 */
@BenchmarkMode(Mode.AverageTime) // Default will be 5 times
@Warmup(iterations = 2) // How many warm up runs, discarding results
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Print time in milliseconds
public class AssessOperatorBenchmarks {
    // How does changing a single one of the following affect performance:
    // Data set size (100K, 1M, 10M, 100M)
    // Grouper Dimension (1, 2, 3, 4, 5) I don't know how many groupers we have
    // Result size (Number of cells 10, 100, 1000)
    // Type of Benchmark (Constant, Sibling, Past)

    @State(Scope.Benchmark)
    public static class CubeManagers {
        CubeManager pkdd99CubeManager = initPKDD99CubeManager();
        CubeManager adultCubeManager;
    }

    public static CubeManager initPKDD99CubeManager() {
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

    // Prototype: <benchmark><data-set-size><result-size><grouper-dimensions>

    @Benchmark
    public void pastBenchmarkSingleGrouper(CubeManagers cubeManagerSample) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagerSample.pkdd99CubeManager);
        String query = "with loan for month = '12/1997', region = 'north Moravia' by month, " +
                "status asseSs max(amount) against PaST 20\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, 1]: high, (1, +inf): ULTRA} " +
                "SAVE aS PastBenchmarkStressing";
        operator.execute(query);
    }

    @Benchmark
    public void constantBenchmarkTwoGroupers(CubeManagers sampleManager) throws RecognitionException {
        AssessOperator operator = new AssessOperator(sampleManager.pkdd99CubeManager);
        String query = "with loan for region = 'central Bohemia' " +
                "by month, region AssEsS max(amount) against 1000\n" +
                "using ratio(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.5]: low, (0.5, +inf]: high} " +
                "save as constantBenchmark";
         operator.execute(query);
    }
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AssessOperatorBenchmarks.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
