package intentional.describe;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static highlights.HighlightTestSupport.holisticFor;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import highlights.HighlightTestSupport;
import highlights.HighlightSet;
import highlights.instance.HolisticHighlight;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;

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
    public final void testDescribeProducesMeasureHighlights() throws Exception {
        String incomingExpression = "WITH loan DESCRIBE SUM(amount) AS Total "
                + "FOR region='south Bohemia' AND year <= '1998' "
                + "GROUP BY district_name, region";

        DescribeOperator operator = new DescribeOperator(testCubeManager);
        LabeledResult operatorResult = operator.execute(incomingExpression).get(0);
        assertNotNull("DESCRIBE should produce an operator result", operatorResult);
        assertTrue("DESCRIBE carries no models", operatorResult.models().isEmpty());

        HighlightSet highlights = HighlightTestSupport.highlights(
                operatorResult, IntentionalProfile.DESCRIBE.archetypes(), testCubeManager);
        assertFalse("DESCRIBE should produce highlights", highlights.isEmpty());

        HolisticHighlight outlier = holisticFor(highlights, "Outlier");
        HolisticHighlight topK = holisticFor(highlights, "TopKContributors");
        HolisticHighlight megaContributor = holisticFor(highlights, "MegaContributor");
        assertNotNull("expected an Outlier highlight", outlier);
        assertNotNull("expected a Top-k highlight", topK);
        assertNotNull("expected a Mega Contributor highlight", megaContributor);
    }
}
