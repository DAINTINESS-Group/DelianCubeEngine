package client;

import intentional.assess.AssessOperator;
import cubemanager.CubeManager;
import highlights.HighlightExtractor;
import highlights.HighlightRecipes;
import highlights.HighlightSet;
import intentional.model.ModelExtraction;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import org.antlr.runtime.RecognitionException;
import result.ResultFileMetadata;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;

public class AssessOperatorClient {

    public static void main(String[] args) throws RecognitionException, IOException {
        CubeManager cubeManager = initCubeMangerB();
        AssessOperator operator = new AssessOperator(cubeManager);
        String query = "WITH loan\n" +
                "FOR year = '1997', region = 'south Bohemia'\n" +
                "BY district_name, year\n" +
                "ASSESS sum(amount) AS total\n" +
                "AGAINST year = '1996' USING difference(zscore(total), zscore(benchmark.total)),\n" +
                "        PAST 2 USING ratio(total, benchmark.total),\n" +
                "        500000\n" +
                "LABELS EquiDepth(low, mid, high)\n" +
                "SAVE AS MultiBenchmarkDemo";

        long start = System.nanoTime();
        LabeledResult result = operator.execute(query).get(0);
        new ModelExtraction().run(result, IntentionalProfile.ASSESS.archetypes());
        HighlightSet highlights = new HighlightExtractor()
                .extract(result, HighlightRecipes.defaults(), cubeManager);
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
