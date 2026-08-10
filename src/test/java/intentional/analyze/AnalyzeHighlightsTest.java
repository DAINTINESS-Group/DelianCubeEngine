package intentional.analyze;

import cubemanager.CubeManager;
import highlights.HighlightTestSupport;
import highlights.HighlightSet;
import highlights.instance.HolisticHighlight;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static highlights.HighlightTestSupport.holisticFor;

public class AnalyzeHighlightsTest {
    private static CubeManager testCubeManager;
    private static Session testSession;
    private static String testSchemaName;
    private static String testTypeOfConnection;

    // set up SQP and CubeManager
    @BeforeClass
    public static void setUpBeforeClass() throws Exception{
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "foodmart_reduced");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "sales");
        userInputList.put("inputFolder", "foodmart_reduced");

        testSchemaName = userInputList.get("schemaName");
        testCubeManager = new CubeManager(typeOfConnection, userInputList);
        testSession = new Session(testCubeManager);
        testSession.initialize(typeOfConnection, userInputList);
        testTypeOfConnection = typeOfConnection;
    }

    @Test
    public final void testAnalyzeMinMQOExecution() throws IOException {
        String incomingExpression = "ANALYZE sum(store_sales) " +
                "                    FROM sales " +
                "                    FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' " +
                "                    GROUP BY month, region " +
                "                    AS 3rd_working_example";

        AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression, testCubeManager, testSchemaName, testTypeOfConnection);
        AnalyzeOperatorMinMultiQueryOptimizer testAnalyzeOperator = new AnalyzeOperatorMinMultiQueryOptimizer(testCubeManager, testAnalyzeTranslationManager);

        List<LabeledResult> minMQOQueries = testAnalyzeOperator.execute(incomingExpression);

        HighlightSet highlightsBase = HighlightTestSupport.highlights(minMQOQueries.get(0), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierBase = holisticFor(highlightsBase, "Outlier");
        HolisticHighlight topKBase = holisticFor(highlightsBase, "TopKContributors");
        HolisticHighlight megaContributorBase = holisticFor(highlightsBase, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierBase);
        assertNotNull("expected a Top-k highlight", topKBase);
        assertNotNull("expected a Mega Contributor highlight", megaContributorBase);
        assertTrue("Outlier should hold", outlierBase.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKBase.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorBase.modelResult.verdict());
        assertFalse(megaContributorBase.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",megaContributorBase.modelResult.holisticMagnitude() > 0.5);

        HighlightSet highlightsSib1 = HighlightTestSupport.highlights(minMQOQueries.get(1), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierSib1 = holisticFor(highlightsSib1, "Outlier");
        HolisticHighlight topKSib1 = holisticFor(highlightsSib1, "TopKContributors");
        HolisticHighlight megaContributorSib1 = holisticFor(highlightsSib1, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierSib1);
        assertNotNull("expected a Top-k highlight", topKSib1);
        assertNotNull("expected a Mega Contributor highlight", megaContributorSib1);
        assertTrue("Outlier should hold", outlierSib1.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKSib1.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorSib1.modelResult.verdict());
        assertFalse(megaContributorSib1.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",megaContributorSib1.modelResult.holisticMagnitude() > 0.5);

        HighlightSet highlightsSib2 = HighlightTestSupport.highlights(minMQOQueries.get(2), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierSib2 = holisticFor(highlightsSib2, "Outlier");
        HolisticHighlight topKSib2 = holisticFor(highlightsSib2, "TopKContributors");
        HolisticHighlight megaContributorSib2 = holisticFor(highlightsSib2, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierSib2);
        assertNotNull("expected a Top-k highlight", topKSib2);
        assertNotNull("expected a Mega Contributor highlight", megaContributorSib2);
        assertFalse("Outlier should not hold", outlierSib2.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKSib2.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorSib2.modelResult.verdict());
        assertFalse(megaContributorSib2.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold", megaContributorSib2.modelResult.holisticMagnitude() > 0.5);

        HighlightSet highlightsDD1 = HighlightTestSupport.highlights(minMQOQueries.get(3), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierDD1 = holisticFor(highlightsDD1, "Outlier");
        HolisticHighlight topKDD1 = holisticFor(highlightsDD1, "TopKContributors");
        HolisticHighlight megaContributorDD1 = holisticFor(highlightsDD1, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierDD1);
        assertNotNull("expected a Top-k highlight", topKDD1);
        assertNotNull("expected a Mega Contributor highlight", megaContributorDD1);
        assertTrue("Outlier should hold", outlierDD1.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKDD1.modelResult.verdict());
        assertFalse("MegaContributor should not hold", megaContributorDD1.modelResult.verdict());

        HighlightSet highlightsDD2 = HighlightTestSupport.highlights(minMQOQueries.get(4), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierDD2 = holisticFor(highlightsDD2, "Outlier");
        HolisticHighlight topKDD2 = holisticFor(highlightsDD2, "TopKContributors");
        HolisticHighlight megaContributorDD2 = holisticFor(highlightsDD2, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierDD2);
        assertNotNull("expected a Top-k highlight", topKDD2);
        assertNotNull("expected a Mega Contributor highlight", megaContributorDD2);
        assertTrue("Outlier should hold", outlierDD2.modelResult.verdict());
        assertTrue("Top-k contributor should hold", topKDD2.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorDD2.modelResult.verdict());
        assertFalse(megaContributorDD2.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",megaContributorDD2.modelResult.holisticMagnitude() > 0.5);
    }

    @Test
    public final void testAnalyzeMidMQOExecution() throws IOException {
        String incomingExpression = "ANALYZE sum(store_sales) " +
                "                    FROM sales " +
                "                    FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' " +
                "                    GROUP BY month, region " +
                "                    AS 3rd_working_example";

        AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression, testCubeManager, testSchemaName, testTypeOfConnection);
        AnalyzeOperatorMidMultiQueryOptimizer testAnalyzeOperator = new AnalyzeOperatorMidMultiQueryOptimizer(testCubeManager, testAnalyzeTranslationManager);

        List<LabeledResult> midMQOQueries = testAnalyzeOperator.execute(incomingExpression);

        HighlightSet highlightsMid = HighlightTestSupport.highlights(midMQOQueries.get(0), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierMid = holisticFor(highlightsMid, "Outlier");
        HolisticHighlight topKMid = holisticFor(highlightsMid, "TopKContributors");
        HolisticHighlight megaContributorMid = holisticFor(highlightsMid, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierMid);
        assertNotNull("expected a Top-k highlight", topKMid);
        assertNotNull("expected a Mega Contributor highlight", megaContributorMid);
        assertTrue("Outlier should hold", outlierMid.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKMid.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorMid.modelResult.verdict());
        assertFalse(megaContributorMid.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",megaContributorMid.modelResult.holisticMagnitude() > 0.5);


        HighlightSet highlightsSib1 = HighlightTestSupport.highlights(midMQOQueries.get(1), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierSib1 = holisticFor(highlightsSib1, "Outlier");
        HolisticHighlight topKSib1 = holisticFor(highlightsSib1, "TopKContributors");
        HolisticHighlight megaContributorSib1 = holisticFor(highlightsSib1, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierSib1);
        assertNotNull("expected a Top-k highlight", topKSib1);
        assertNotNull("expected a Mega Contributor highlight", megaContributorSib1);
        assertTrue("Outlier should hold", outlierSib1.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKSib1.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorSib1.modelResult.verdict());
        assertFalse(megaContributorSib1.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",megaContributorSib1.modelResult.holisticMagnitude() > 0.5);

        HighlightSet highlightsSib2 = HighlightTestSupport.highlights(midMQOQueries.get(2), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierSib2 = holisticFor(highlightsSib2, "Outlier");
        HolisticHighlight topKSib2 = holisticFor(highlightsSib2, "TopKContributors");
        HolisticHighlight megaContributorSib2 = holisticFor(highlightsSib2, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierSib2);
        assertNotNull("expected a Top-k highlight", topKSib2);
        assertNotNull("expected a Mega Contributor highlight", megaContributorSib2);
        assertFalse("Outlier should not hold", outlierSib2.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKSib2.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorSib2.modelResult.verdict());
        assertFalse(megaContributorSib2.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold", megaContributorSib2.modelResult.holisticMagnitude() > 0.5);

    }

    @Test
    public final void testAnalyzeMaxMQOExecution() throws IOException {
        String incomingExpression = "ANALYZE sum(store_sales) " +
                "                    FROM sales " +
                "                    FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' " +
                "                    GROUP BY month, region " +
                "                    AS 3rd_working_example";

        AnalyzeTranslationManager testAnalyzeTranslationManager = new AnalyzeTranslationManager(incomingExpression, testCubeManager, testSchemaName, testTypeOfConnection);
        AnalyzeOperatorMaxMultiQueryOptimizer testAnalyzeOperator = new AnalyzeOperatorMaxMultiQueryOptimizer(testCubeManager, testAnalyzeTranslationManager);

        List<LabeledResult> maxMQOQueries = testAnalyzeOperator.execute(incomingExpression);

        HighlightSet highlightsMax = HighlightTestSupport.highlights(maxMQOQueries.get(0), IntentionalProfile.ANALYZE.archetypes(), testCubeManager);
        HolisticHighlight outlierMax = holisticFor(highlightsMax, "Outlier");
        HolisticHighlight topKMax = holisticFor(highlightsMax, "TopKContributors");
        HolisticHighlight megaContributorMax = holisticFor(highlightsMax, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlierMax);
        assertNotNull("expected a Top-k highlight", topKMax);
        assertNotNull("expected a Mega Contributor highlight", megaContributorMax);
        assertTrue("Outlier should hold", outlierMax.modelResult.verdict());
        assertTrue("Top-k contributors should hold", topKMax.modelResult.verdict());
        assertTrue("MegaContributor should hold", megaContributorMax.modelResult.verdict());
        assertFalse(megaContributorMax.elementary().isEmpty());
        assertTrue("dominant share should exceed the threshold",megaContributorMax.modelResult.holisticMagnitude() > 0.5);
    }
}
