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
        String query = "with loan for region = 'North Moravia' " +
                "by region, status, month asseSs max(amount) against region = 'Prague'" +
                "using ratio(amount, benchmark.amount) " +
                "labels {[-inf, 50000.0]: low, (50000.0, +inf]: high}";
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
