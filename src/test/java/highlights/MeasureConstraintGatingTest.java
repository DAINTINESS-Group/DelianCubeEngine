package highlights;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import cubemanager.cubebase.CubeQuery;
import intentional.model.archetypes.DefaultArchetypes;
import intentional.result.LabeledResult;
import result.Cell;
import result.Result;

/**
 * The additive constraint is honored via the typed aggregation function: mega-contributor runs over a
 * {@code sum} measure and is skipped over a non-additive {@code avg} one.
 */
public class MeasureConstraintGatingTest {

    private static Result twoRegionResult() {
        Result data = new Result();
        data.getCells().add(new Cell(new String[]{"north", "100", "1"}, 1));
        data.getCells().add(new Cell(new String[]{"south", "200", "1"}, 1));
        return data;
    }

    private static int megaContributorResults(String function) {
        CubeQuery query = new CubeQuery("gatingTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure(function, "amount", "amount");

        LabeledResult operatorResult = new LabeledResult(query, twoRegionResult(), null);
        return HighlightTestSupport.models(operatorResult, DefaultArchetypes.subset("MegaContributor")).size();
    }

    @Test
    public void additiveMeasureIsEvaluated() {
        assertEquals("MegaContributor runs over a sum measure", 1, megaContributorResults("sum"));
    }

    @Test
    public void nonAdditiveMeasureIsSkipped() {
        assertEquals("MegaContributor is gated out over an avg measure", 0, megaContributorResults("avg"));
    }
}
