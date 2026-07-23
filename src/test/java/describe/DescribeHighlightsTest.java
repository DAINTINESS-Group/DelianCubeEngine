package describe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static highlights.HighlightTestSupport.holisticFor;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import describe.models.KPIMedianLabelingModel;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.instance.HolisticHighlight;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;
import intentionaloperator.OperatorResult;

public class DescribeHighlightsTest {

    private static CubeManager testCubeManager;
    private static Session testSession;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String typeOfConnection = "RDBMS";
        HashMap<String, String> userInputList = new HashMap<>();
        userInputList.put("schemaName", "pkdd99_star");
        userInputList.put("username", "CinecubesUser");
        userInputList.put("password", "Cinecubes");
        userInputList.put("cubeName", "loan");
        userInputList.put("inputFolder", "pkdd99_star");

        testCubeManager = new CubeManager(typeOfConnection, userInputList);
        testSession = new Session(testCubeManager);
        testSession.initialize(typeOfConnection, userInputList);
    }

    @Test
    public final void testDescribeWithKpiProducesHighlights() throws Exception {
        String incomingExpression = "WITH loan DESCRIBE SUM(amount) AS Total "
                + "FOR region='south Bohemia' AND year <= '1998' "
                + "GROUP BY district_name, region USING " + KPIMedianLabelingModel.NAME;

        DescribeOperator operator = new DescribeOperator(testCubeManager);
        OperatorResult operatorResult = operator.execute(incomingExpression).get(0);
        assertNotNull("DESCRIBE should produce an operator result", operatorResult);

        HighlightSet highlights = new HighlightExtractor().extract(
                operatorResult, IntentionalProfile.DESCRIBE.archetypes(), CubeSchemaResolver.from(testCubeManager));
        assertFalse("DESCRIBE should produce highlights", highlights.isEmpty());

        HolisticHighlight outlier = holisticFor(highlights, "Outlier");
        HolisticHighlight topK = holisticFor(highlights, "TopKContributors");
        HolisticHighlight megaContributor = holisticFor(highlights, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlier);
        assertNotNull("expected a Top-k highlight", topK);
        assertNotNull("expected a Mega Contributor highlight", megaContributor);

        HolisticHighlight labelPredominance = holisticFor(highlights, "LabelPredominance");
        assertNotNull("KPI labeling should yield a LabelPredominance highlight", labelPredominance);
        assertTrue("LabelPredominance should report a Low/OK/High dominant label",
                labelPredominance.getScores().stream().anyMatch(s ->
                        "Low".equals(s.label) || "OK".equals(s.label) || "High".equals(s.label)));
    }
}
