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
 * We benchmark the overhead produced whenever we change one of the following
 * in the <b>PKDD99_star_size</b> database
 * <ol>
 *     <li>Data Set Size (100k, 1M, 10M, 100M)</li>
 *     <li>Benchmark Type Used</li>
 *     <li>Groupers (1, 2, 3)</li>
 *     <li>result size (Number of cells 10, 100, 1000)</li>
 * </ol>
 *
 * In these measurements we consider the following default values
 * and we change only one of the parameters above at a time:
 * <ul>
 *     <li>Data Set Size 100k</li>
 *     <li>Benchmark Type Sibling</li>
 *     <li>Groupers 2</li>
 *     <li>Result size (Number of cells) 10</li>
 * </ul>
 */
@BenchmarkMode(Mode.AverageTime) // Default will be 5 times
@Warmup(iterations = 2) // How many warm up runs, discarding results
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Print time in milliseconds
@Fork(1)
public class AssessOperatorDefaultMeasuring {
    @State(Scope.Benchmark)
    public static class CubeManagers {
        CubeManager pkdd99CubeManager = initPKDD99CubeManager();
        CubeManager pkdd99CubeManager100K = initPKDD99CubeManager100K();
        CubeManager pkdd99CubeManager1M = initPKDD99CubeManager1M();
        CubeManager pkdd99CubeManager10M = initPKDD99CubeManager10M();
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

    public static CubeManager initPKDD99CubeManager100K() {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star_100K");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "loan");
        userInputList.put("inputFolder", "pkdd99_star_100K");
        CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
        Session session = new Session(cubeManager);
        try {
            session.initialize(typeOfConnection, userInputList);
        } catch (RemoteException re) {
            System.exit(0);
        }
        return cubeManager;
    }

    public static CubeManager initPKDD99CubeManager1M() {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star_1M");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "loan");
        userInputList.put("inputFolder", "pkdd99_star_1M");
        CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
        Session session = new Session(cubeManager);
        try {
            session.initialize(typeOfConnection, userInputList);
        } catch (RemoteException re) {
            System.exit(0);
        }
        return cubeManager;
    }

    public static CubeManager initPKDD99CubeManager10M() {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star_10M");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "loan");
        userInputList.put("inputFolder", "pkdd99_star_10M");
        CubeManager cubeManager = new CubeManager(typeOfConnection, userInputList);
        Session session = new Session(cubeManager);
        try {
            session.initialize(typeOfConnection, userInputList);
        } catch (RemoteException re) {
            System.exit(0);
        }
        return cubeManager;
    }

    @Benchmark
    public void defaultBenchmark(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month, region\n" +
                "ASSESS max(amount)\n" +
                "AGAINST month = '12/1994'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS defaultMeasuring";
        operator.execute(query);
    }

    // Database Size Effects
    @Benchmark
    public void siblingBenchmark_1MRecords_TwoGroupers(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager1M);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month, region\n" +
                "ASSESS max(amount)\n" +
                "AGAINST 5000\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS sibling1MTwoGroupers";
        operator.execute(query);
    }

    @Benchmark
    public void siblingBenchmark_10MRecords_TwoGroupers(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager10M);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month, region\n" +
                "ASSESS max(amount)\n" +
                "AGAINST 5000\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS sibling10MTwoGroupers";
        operator.execute(query);
    }

    // Benchmark Type Effects
    @Benchmark
    public void constantBenchmark_100KRecords_TwoGroupers(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month, region\n" +
                "ASSESS max(amount)\n" +
                "AGAINST 5000\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS constant100KTwoGroupers";
        operator.execute(query);
    }


    @Benchmark
    public void pastBenchmark_100KRecords_TwoGroupers(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month\n" +
                "ASSESS max(amount)\n" +
                "AGAINST PAST 10\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS past100KTwoGroupers";
        operator.execute(query);
    }

    // Groupers
    @Benchmark
    public void siblingBenchmark_100KRecords_OneGrouper(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month\n" +
                "ASSESS max(amount)\n" +
                "AGAINST month = '12/1994'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS sibling100KOneGrouper";
        operator.execute(query);
    }

    @Benchmark
    public void siblingBenchmark_100KRecords_ThreeGroupers(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR month = '01/1995'\n" +
                "BY month, region, status\n" +
                "ASSESS max(amount)\n" +
                "AGAINST month = '12/1994'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS sibling100KThreeGroupers";
        operator.execute(query);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AssessOperatorDefaultMeasuring.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
