package describe;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import cubemanager.CubeManager;
import cubemanager.CubeSchemaResolver;
import describe.models.KPIMedianLabelingModel;
import highlights.HighlightExtractor;
import highlights.HighlightSet;
import highlights.instance.ElementaryHighlight;
import highlights.instance.Highlight;
import highlights.instance.HolisticHighlight;
import intentional.result.LabeledResult;
import mainengine.Session;
import mainengine.managers.IntentionalProfile;

/**
 * Not an assertion test — a Stage-0 "story preview": runs a real DESCRIBE query against the
 * loan cube and prints every holistic as a claim-anchored node (the claim, its verdict and
 * algorithm scores, then its elementaries as internal evidence). Interestingness is NOT
 * computed here: the query-level interestingnessengine is intentionally not wired to the
 * highlights path (see docs/highlights-interestingness.md for the highlight-level plan).
 */
public class HighlightsPreviewTest {

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
    public final void previewDescribeHighlights() throws Exception {
        String query = "WITH loan DESCRIBE SUM(amount) AS Total "
                + "FOR region='south Bohemia' AND year <= '1998' "
                + "GROUP BY district_name, region USING " + KPIMedianLabelingModel.NAME;

        DescribeOperator operator = new DescribeOperator(testCubeManager);
        LabeledResult result = operator.execute(query).get(0);

        HighlightSet highlights = new HighlightExtractor().extract(
                result, IntentionalProfile.DESCRIBE.archetypes(), CubeSchemaResolver.from(testCubeManager));

        StringBuilder out = new StringBuilder();
        out.append("\n============ STORY PREVIEW — Stage 0, claim-anchored ============\n");
        out.append("QUERY: ").append(query).append("\n\n");

        int claim = 0;
        for (Highlight h : highlights.highlights()) {
            if (!(h instanceof HolisticHighlight)) continue;
            HolisticHighlight hh = (HolisticHighlight) h;
            boolean holds = hh.execution.result.verdict();
            out.append("CLAIM ").append(++claim)
               .append(" [").append(hh.archetype.name).append("]  verdict=")
               .append(holds ? "HOLDS" : "FAILS").append('\n');
            out.append("   ").append(hh.toText()).append('\n');
            for (ElementaryHighlight e : hh.elementary()) {
                out.append("      evidence <").append(e.role).append(">: ")
                   .append(e.toText()).append('\n');
            }
            out.append('\n');
        }
        out.append("Total claims: ").append(claim).append('\n');
        out.append("=================================================================\n");
        System.out.println(out);
    }
}
