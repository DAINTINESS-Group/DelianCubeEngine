package client;

import assess.AssessOperator;
import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;

import java.rmi.RemoteException;
import java.util.HashMap;

public class AssessOperatorClient {

    public static void main(String [] args) throws RecognitionException {
        CubeManager cubeManager = initCubeMangerB();
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR district_name = 'Liberec'\n" +
                "BY district_name\n" +
                "ASSESS max(amount)\n" +
                "AGAINST district_name = 'Decin'\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n";
        operator.execute(query);
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
}
