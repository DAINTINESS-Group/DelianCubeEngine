package client;

import assess.AssessOperator;
import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;

import java.rmi.RemoteException;
import java.util.HashMap;

public class AssessOperatorClient {

    public static void main(String [] args) throws RecognitionException {
        CubeManager cubeManager = initCubeManger();
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR region = 'North Moravia'\n" +
                "BY region, status, month\n" +
                "ASSESS max(amount)\n" +
                "AGAINST region = 'Prague'\n" +
                "USING ratio(amount, benchmark.amount)\n" +
                "LABELS {[0.01, 0.5]: low, (0.5, 1]: high, (1, +inf): ultra}\n" +
                "SAVE AS NMoraviaVSPrague";
        operator.execute(query);
    }

    private static CubeManager initCubeManger() {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star_100K");
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
}
