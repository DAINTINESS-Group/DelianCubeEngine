package client;

import assess.AssessOperator;
import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;

import java.rmi.RemoteException;
import java.util.HashMap;

public class AssessOperatorClient {

    public static void main(String[] args) throws RecognitionException {
        CubeManager cubeManager = initCubeMangerB();
        AssessOperator operator = new AssessOperator(cubeManager);
        long parsingTime = 0;
        long comparisonTime = 0;
        long labelingTime = 0;
        long executionTime = 0;
        String query = "WITH loan\n" +
                "FOR region = 'South Moravia', day = '11/10/1997'\n" +
                "BY region, day, status\n" +
                "ASSESS max(amount)\n" +
                "AGAINST PAST 3\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS PastBenchmark";

        for (int i = 0; i < 5; i++) {
            AssessOperator.AssessResults results = operator.execute(query);
            parsingTime += results.parseTime;
            comparisonTime += results.comparisonTime;
            labelingTime += results.labelingTime;
            executionTime += results.executionTime;;
        }

        System.out.println("Parsing Time Avg in ms: " + parsingTime/5);
        System.out.println("Comparison Time Avg in ms: " + comparisonTime/5 * 0.000001);
        System.out.println("Labeling Time Avg in ms: " + labelingTime/5 * 0.000001);
        System.out.println("Execution Time Avg in ms: " + executionTime/5);
    }

    private static CubeManager initCubeMangerA() {
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

    private static CubeManager initCubeMangerB() {
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

    private static CubeManager initCubeMangerC() {
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

}
