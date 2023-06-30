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
 * in the <b>PKDD99_star_size</b> database, using the Sibling Benchmark
 * <ol>
 *     <li>Data Set Size (100k, 1M, 10M, 100M)</li>
 *     <li>Groupers (1, 2, 3)</li>
 *     <li>result size (Number of cells 10, 100, 1000)</li>
 * </ol>
 *
 * In these measurements we consider the following default values
 * and we change only one of the parameters above at a time:
 * <ul>
 *     <li>Data Set Size 100k</li>
 *     <li>Groupers 2</li>
 *     <li>Result size (Number of cells) 100</li>
 * </ul>
 */
@BenchmarkMode(Mode.AverageTime) // Default will be 5 times
@Warmup(iterations = 2) // How many warm up runs, discarding results
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Print time in milliseconds
@Fork(1)
public class AssessOperatorSiblingBenchmarkMeasuring {

    @State(Scope.Benchmark)
    public static class CubeManagers {
        CubeManager pkdd99CubeManager100K = initPKDD99CubeManager100K();
        CubeManager pkdd99CubeManager1M = initPKDD99CubeManager1M();
        CubeManager pkdd99CubeManager10M = initPKDD99CubeManager10M();
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
    public void runFor_100KRecords_TwoGroupers_10Cells(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR district_name = 'Liberec'\n" +
                "BY district_name, status\n" +
                "ASSESS max(amount)\n" +
                "AGAINST district_name = 'Decin'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS Sibling100KRecordsTwoGroupers10Cells";
        operator.execute(query);
    }

    @Benchmark
    public void runFor_100KRecords_TwoGroupers_100Cells(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR region = 'south Moravia'\n" +
                "BY region, month\n" +
                "ASSESS max(amount)\n" +
                "AGAINST region = 'north Moravia'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS Sibling100KRecordsTwoGroupers100Cells";
        operator.execute(query);
    }

    @Benchmark
    public void runFor_1MRecords_TwoGroupers_100Cells(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager1M);
        String query = "WITH loan\n" +
                "FOR region = 'south Moravia'\n" +
                "BY region, month\n" +
                "ASSESS max(amount)\n" +
                "AGAINST region = 'north Moravia'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS Sibling1MRecordsTwoGroupers100Cells";
        operator.execute(query);
    }

    @Benchmark
    public void runFor_10MRecords_TwoGroupers_100Cells(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager10M);
        String query = "WITH loan\n" +
                "FOR region = 'south Moravia'\n" +
                "BY region, month\n" +
                "ASSESS max(amount)\n" +
                "AGAINST region = 'north Moravia'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS Sibling10MRecordsTwoGroupers100Cells";
        operator.execute(query);
    }

    @Benchmark
    public void runFor_100KRecords_OneGrouper(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR region = 'south Moravia'\n" +
                "BY region\n" +
                "ASSESS max(amount)\n" +
                "AGAINST region = 'north Moravia'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS Sibling100KRecordsOneGrouper";
        operator.execute(query);
    }

    @Benchmark
    public void runFor_100KRecords_ThreeGroupers(CubeManagers cubeManagers) throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManagers.pkdd99CubeManager100K);
        String query = "WITH loan\n" +
                "FOR region = 'south Moravia'\n" +
                "BY region, status, day\n" +
                "ASSESS max(amount)\n" +
                "AGAINST region = 'north Moravia'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS Sibling100KRecordsThreeGroupers";
        operator.execute(query);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AssessOperatorSiblingBenchmarkMeasuring.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
