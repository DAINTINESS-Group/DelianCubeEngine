package assess;

import cubemanager.CubeManager;
import mainengine.Session;
import org.antlr.runtime.RecognitionException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.HashMap;

public class AssessOperatorTest {
    private final CubeManager cubeManager = initializeCubeManger();

    private CubeManager initializeCubeManger() {
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
    public void executeIncompleteQuery() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '5/2019', region = 'south Moravia'\n" +
                "by region, month assess avg(amount) against region = 'north Moravia'";

        operator.execute(query);

        //TODO: This should return an exception that the query is invalid
    }

    @Test
    public void executeQuery() throws RecognitionException {
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "with loan for month = '1998-02', region = 'South Moravia'\n" +
                "by month assess avg(amount) against region = 'North Moravia'\n" +
                "using ratio(absolute(amount, benchmark.amount)\n" +
                "labels {[0.0, 0.3): low_effort, [0.3, 0.6): mid_effort, [0.6, 1]: high}";

        operator.execute(query);
    }
}
