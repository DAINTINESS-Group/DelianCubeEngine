package client;

import intentional.assess.AssessOperator;
import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.metamodel.ArchetypeProperty;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import org.antlr.runtime.RecognitionException;
import result.ResultFileMetadata;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AssessOperatorClient {

    public static void main(String[] args) throws RecognitionException, IOException {
        CubeManager cubeManager = initCubeMangerB();
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR year = '1997'\n" +
                "BY region, year, status\n" +
                "ASSESS sum(amount)\n" +
                "AGAINST PAST 2\n" +
                "USING ratio(absolute(amount, benchmark.amount))\n" +
                "LABELS {[0.001, 0.05]: low, (0.05, 0.1]: high, (0.1, +inf): ultra}\n" +
                "SAVE AS PastBenchmarkDemo";

        CubeSchemaResolver schemaResolver = CubeSchemaResolver.from(cubeManager);

        // Stage 1: the operator produces its result. Stage 2: highlight extraction runs on top of it.
        long start = System.nanoTime();
        LabeledResult result = operator.execute(query).get(0);
        List<ArchetypeProperty> registeredArchetypes = IntentionalProfile.ASSESS.archetypes();

        HighlightSet highlights = new HighlightExtractor()
                .extract(result, registeredArchetypes, schemaResolver);
        long ms = (System.nanoTime() - start) / 1_000_000;

        System.out.println("Execution + extraction: " + ms + " ms");
        ResultFileMetadata report = IntentionalProfile.ASSESS.writer().write(query,
                Collections.singletonList(result), Collections.singletonList(highlights));
        ReportPrinter.print(report);
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
