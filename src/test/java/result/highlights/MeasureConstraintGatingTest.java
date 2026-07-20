package result.highlights;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import cubemanager.CubeSchemaResolver;
import cubemanager.cubebase.CubeQuery;
import result.Cell;
import result.Result;
import result.highlights.archetypes.megacontributor.MegaContributorArchetype;
import result.highlights.metamodel.ArchetypeProperty;

/**
 * The additive main-measure constraint is honored via the typed aggregation function: an {@code ADDITIVE}
 * archetype (MegaContributor) is evaluated over a {@code sum} measure and skipped over a non-additive
 * {@code avg} one.
 */
public class MeasureConstraintGatingTest {

    private static Result twoRegionResult() {
        Result data = new Result();
        data.getCells().add(new Cell(new String[]{"north", "100", "1"}, 1));
        data.getCells().add(new Cell(new String[]{"south", "200", "1"}, 1));
        return data;
    }

    private static int megaContributorHolistics(String function) {
        CubeQuery query = new CubeQuery("gatingTest");
        query.setGammaExpressions(new ArrayList<String[]>());
        query.addQueryMeasure(function, "amount", "amount");

        OperatorResult operatorResult = new OperatorResult(query, twoRegionResult(), null);
        CubeSchemaResolver schema = new CubeSchemaResolver(new ArrayList<>(), new ArrayList<>());
        List<ArchetypeProperty> candidates = Collections.singletonList(MegaContributorArchetype.create());

        return new HighlightExtractor().extract(operatorResult, candidates, schema).size();
    }

    @Test
    public void additiveMeasureIsEvaluated() {
        assertEquals("MegaContributor runs over a sum measure", 1, megaContributorHolistics("sum"));
    }

    @Test
    public void nonAdditiveMeasureIsSkipped() {
        assertEquals("MegaContributor is gated out over an avg measure", 0, megaContributorHolistics("avg"));
    }
}
