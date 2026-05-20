package cubemanager.usability;

import cubemanager.cubebase.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CubeQueryUsabilityChecker}, covering all six Theorem 9.1 conditions (pass and fail for each)
 *
 * <p>
 * The loan cube schema used as reference is taken from loanQueries_Usability.txt:
 *
 * <pre>
 *   account_dim hierarchy (position):
 *     detail_account(0) → district_name(1) → region(2) → all_accounts(3)
 *
 *   date_dim hierarchy (position):
 *     day(0) → month(1) → year(2) → all_dates(3)
 * </pre>
 *
 * The two usable queries from the file:
 *
 * <pre>
 *   q^b (LoanQuery21_S2_CG-Cmmn):
 *       Gamma: account_dim.district_name, date_dim.month
 *       Sigma: account_dim.region='Prague', date_dim.year='1998'
 *       Measure: Min(amount)
 *
 *   q^n (LoanQuery21_S2_CG-Cmmn2):
 *       Gamma: account_dim.district_name, date_dim.year
 *       Sigma: account_dim.district_name='Hl.m. Praha', date_dim.year='1998'
 *       Measure: Min(amount)
 * </pre>
 */
public class CubeQueryUsabilityCheckerTest {

    private CubeQueryUsabilityChecker checker;

    //same underlying data-set–"loan"
    private BasicStoredCube loanCube;


    @Before
    public void setUp() {
        // Reset singleton so each test starts clean
        CubeQueryUsabilityChecker.resetInstance();
        checker = CubeQueryUsabilityChecker.getInstance();
        loanCube = buildLoanCube("loan");
    }

    // TEST Condition 1 – Same underlying data-set(cube name)
    // -------------------------------------------------------

    /** Both queries reference the same cube -> condition 1 passes. */
    @Test
    public void testCondition1_Pass_SameDS() {
        CubeQuery qb = buildBaseQuery(loanCube);
        CubeQuery qn = buildNewQuery(loanCube);
        setQueries(checker, qn, qb);

        assertTrue("Condition 1 should pass when both queries use the same cube",
                checker.checkCondition1SameDS());
    }

    /** q^n references a different cube -> condition 1 fails. */
    @Test
    public void testCondition1_Fail_DifferentDS() {
        BasicStoredCube otherCube = buildLoanCube("orders"); // different name
        CubeQuery qb = buildBaseQuery(loanCube);
        CubeQuery qn = buildNewQuery(otherCube);
        setQueries(checker, qn, qb);

        assertFalse("Condition 1 should fail when queries use different cubes",
                checker.checkCondition1SameDS());
    }

    // TEST Condition 2 – Same dimensions AND same distributive measures
    // -----------------------------------------------------------------

    /** Same dimensions and same distributive measure -> condition 2 passes. */
    @Test
    public void testCondition2_Pass_SameDimensionsAndMeasures() {
        CubeQuery qb = buildBaseQuery(loanCube);
        CubeQuery qn = buildNewQuery(loanCube);
        setQueries(checker, qn, qb);

        assertTrue("Condition 2 should pass for matching dimensions and measures",
                checker.checkCondition2SameDimensionsAndMeasures());
    }

    /** q^n has an extra dimension --> condition 2 fails. */
    @Test
    public void testCondition2_Fail_DifferentDimensionSize() {
        CubeQuery qb = buildBaseQuery(loanCube);

        // q^n with an extra dimension not present in q^b
        CubeQuery qn = new CubeQuery("NewQuery_ExtraDim");
        qn.setBasicStoredCube(loanCube);
        qn.addGammaExpression("account_dim", "district_name");
        qn.addGammaExpression("date_dim", "year");
        qn.addGammaExpression("extra_dim", "level1"); // extra dimension
        qn.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qn.addSigmaExpression("date_dim.year", "=", "'1998'");
        qn.addSigmaExpression("extra_dim.level1", "=", "'x'");
        qn.addMeasure("amount", "Min");
        setQueries(checker, qn, qb);

        assertFalse("Condition 2 should fail when dimension count differs",
                checker.checkCondition2SameDimensionsAndMeasures());
    }

    /** q^n has a different dimension --> condition 2 fails. */
    @Test
    public void testCondition2_Fail_DifferentDimensions() {
        CubeQuery qb = buildBaseQuery(loanCube);

        // q^n with an extra dimension not present in q^b
        CubeQuery qn = new CubeQuery("NewQuery_ExtraDim");
        qn.setBasicStoredCube(loanCube);
        qn.addGammaExpression("account_dim", "district_name");
        qn.addGammaExpression("status_dim", "status");
        qn.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qn.addSigmaExpression("status_dim.status", "=", "'Other status'");
        qn.addMeasure("amount", "Min");
        setQueries(checker, qn, qb);

        assertFalse("Condition 2 should fail when dimension sets differ",
                checker.checkCondition2SameDimensionsAndMeasures());
    }

    /** q^n uses a non-distributive aggregate function (AVG) -> condition 2 fails. */
    @Test
    public void testCondition2_Fail_NonDistributiveMeasure() {
        CubeQuery qb = buildBaseQuery(loanCube);

        CubeQuery qn = new CubeQuery("NewQuery_AVG");
        qn.setBasicStoredCube(loanCube);
        qn.addGammaExpression("account_dim", "district_name");
        qn.addGammaExpression("date_dim", "year");
        qn.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qn.addSigmaExpression("date_dim.year", "=", "'1998'");
        qn.addMeasure("amount", "Avg"); // non-distributive
        setQueries(checker, qn, qb);

        assertFalse("Condition 2 should fail when aggregate function is non-distributive",
                checker.checkCondition2SameDimensionsAndMeasures());
    }

    // TEST Condition 3 – Exactly one sigma atom per dimension
    // -------------------------------------------------------

    /** Each dimension has exactly one sigma atom --> condition 3 passes for q^n */
    @Test
    public void testCondition3_Pass_OneAtomPerDimension() {
        CubeQuery qn = buildNewQuery(loanCube);
        setQueries(checker, qn, buildBaseQuery(loanCube));

        assertTrue("Condition 3 should pass when each dimension has exactly one sigma atom",
                checker.checkCondition3OneAtomPerDimension(qn));
    }

    /** account_dim appears twice in sigma -> condition 3 fails. */
    @Test
    public void testCondition3_Fail_TwoAtomsForOneDimension() {
        CubeQuery qn = new CubeQuery("NewQuery_TwoAtoms");
        qn.setBasicStoredCube(loanCube);
        qn.addGammaExpression("account_dim", "district_name");
        qn.addGammaExpression("date_dim", "year");
        // Two sigma atoms for the same dimension
        qn.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qn.addSigmaExpression("account_dim.region", "=", "'Prague'"); // duplicate!

        qn.addSigmaExpression("date_dim.year", "=", "'1998'");
        qn.addMeasure("amount", "Min");
        setQueries(checker, qn, buildBaseQuery(loanCube));

        assertFalse("Condition 3 should fail when a dimension has more than one sigma atom",
                checker.checkCondition3OneAtomPerDimension(qn));
    }

    // TEST Condition 4 – Perfectly rollable (sigma level is ancestor of grouper level)
    // ------------------------------------------------------------------------------

    /**
     * q^b: sigma at region(2), gamma at district_name(1) -> 1 <= 2 --->OK
     * q^n: sigma at district_name(1), gamma at district_name(1) ||| sigma at year(2), gamma at year(2) --->OK
     */
    @Test
    public void testCondition4_Pass_PerfectlyRollable() {
        CubeQuery qb = buildBaseQuery(loanCube);
        CubeQuery qn = buildNewQuery(loanCube);
        setQueries(checker, qn, qb);

        assertTrue("Condition 4 should pass for q^b (sigma level is ancestor of grouper level)",
                checker.checkCondition4PerfectlyRollable(qb));
        assertTrue("Condition 4 should pass for q^n (sigma level is ancestor of grouper level)",
                checker.checkCondition4PerfectlyRollable(qn));
    }

    /**
     * Sigma level is FINER than grouper level (not rollable):
     * gamma=year(2), sigma at month(1) -> 2 > 1 ---> fails
     */
    @Test
    public void testCondition4_Fail_SigmaFinerThanGrouper() {
        CubeQuery qBad = new CubeQuery("BadQuery_NotRollable");
        qBad.setBasicStoredCube(loanCube);
        qBad.addGammaExpression("account_dim", "district_name");
        qBad.addGammaExpression("date_dim", "year"); // grouper = year (pos 2)
        qBad.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qBad.addSigmaExpression("date_dim.month", "=", "'01-1998'"); // sigma = month (pos 1) < year (pos 2)
        qBad.addMeasure("amount", "Min");
        setQueries(checker, qBad, buildBaseQuery(loanCube));

        assertFalse("Condition 4 should fail when sigma level is finer than grouper level",
                checker.checkCondition4PerfectlyRollable(qBad));
    }

    // TEST Condition 5 – q^n grouper levels are ancestors of q^b grouper levels
    // --------------------------------------------------------------------------

    /**
     * q^n groups date at year(2), q^b groups date at month(1) -> 2 >= 1 --->OK
     * q^n groups account at district_name(1), q^b also at district_name(1) -> 1 >= 1
     * ✓
     */
    @Test
    public void testCondition5_Pass_NewLevelsAreAncestors() {
        CubeQuery qb = buildBaseQuery(loanCube);
        CubeQuery qn = buildNewQuery(loanCube);
        setQueries(checker, qn, qb);

        assertTrue("Condition 5 should pass when q^n grouper levels are at or above q^b grouper levels",
                checker.checkCondition5NewLevelsAreAncestors());
    }

    /**
     * q^n groups date at month(1), q^b already groups at year(2) -> 1 < 2 ---> fails
     */
    @Test
    public void testCondition5_Fail_NewLevelFinerThanBase() {
        // Swap queries: new query groups at month (finer than year)
        CubeQuery qb = buildNewQuery(loanCube); // q^b groups date at year(2)
        CubeQuery qn = buildBaseQuery(loanCube); // q^n groups date at month(1) --> finer!
        setQueries(checker, qn, qb);

        assertFalse("Condition 5 should fail when q^n grouper level is finer than q^b grouper level",
                checker.checkCondition5NewLevelsAreAncestors());
    }

    // TEST Condition 6 – Signature of q^n subset(⊆) grouper domain of q^b
    // ------------------------------------------------------------------

    /**
     * q^n and q^b have the same sigma values, which are within q^b's grouper domain -> condition 6 passes.
     *
     * <p>
     * grouperDomain(q^b) = {'Hl.m. Praha'} and {'1998'}
     * signature(q^n) = {'Hl.m. Praha'} and {'1998'}
     * subset check passes
     */
    @Test
    public void testCondition6_Pass_SigmaLevelEqualsGrouperLevel() {
        CubeQuery qb = new CubeQuery("BaseQuery_SameLevel");
        qb.setBasicStoredCube(loanCube);
        qb.addGammaExpression("account_dim", "district_name");
        qb.addGammaExpression("date_dim", "year");
        qb.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qb.addSigmaExpression("date_dim.year", "=", "'1998'");
        qb.addMeasure("amount", "Min");

        CubeQuery qn = new CubeQuery("NewQuery_SameLevel");
        qn.setBasicStoredCube(loanCube);
        qn.addGammaExpression("account_dim", "district_name");
        qn.addGammaExpression("date_dim", "year");
        qn.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qn.addSigmaExpression("date_dim.year", "=", "'1998'");
        qn.addMeasure("amount", "Min");

        setQueries(checker, qn, qb);
        assertTrue("Condition 6 should pass when q^n sigma values are within q^b's grouper domain",
                checker.checkCondition6SignatureSubset());
    }

    /**
     * q^n sigma value is NOT in q^b's grouper domain:
     * q^b covers account district_name='Hl.m. Praha', but q^n requests 'Tabor' ->
     * fails.
     */
    @Test
    public void testCondition6_Fail_SignatureNotSubset() {
        CubeQuery qb = new CubeQuery("BaseQuery_Prague");
        qb.setBasicStoredCube(loanCube);
        qb.addGammaExpression("account_dim", "district_name");
        qb.addGammaExpression("date_dim", "year");
        qb.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        qb.addSigmaExpression("date_dim.year", "=", "'1998'");
        qb.addMeasure("amount", "Min");

        // q^n requests a different district -> not a subset of q^b's domain
        CubeQuery qn = new CubeQuery("NewQuery_Brno");
        qn.setBasicStoredCube(loanCube);
        qn.addGammaExpression("account_dim", "district_name");
        qn.addGammaExpression("date_dim", "year");
        qn.addSigmaExpression("account_dim.district_name", "=", "'Tabor'"); // not in base domain
        qn.addSigmaExpression("date_dim.year", "=", "'1998'");
        qn.addMeasure("amount", "Min");

        setQueries(checker, qn, qb);
        assertFalse("Condition 6 should fail when q^n sigma values are outside q^b's grouper domain",
                checker.checkCondition6SignatureSubset());
    }


    // Helpers

    /**
     * Builds the base query (q^b) as is in loanQueries_Usability.txt:
     *
     * <pre>
     *   Gamma: account_dim.district_name, date_dim.month
     *   Sigma: account_dim.region='Prague', date_dim.year='1998'
     *   Measure: Min(amount)
     * </pre>
     */
    private CubeQuery buildBaseQuery(BasicStoredCube cube) {
        CubeQuery q = new CubeQuery("LoanQuery21_S2_CG-Cmmn");
        q.setBasicStoredCube(cube);
        q.addGammaExpression("account_dim", "district_name");
        q.addGammaExpression("date_dim", "month");
        q.addSigmaExpression("account_dim.region", "=", "'Prague'");
        q.addSigmaExpression("date_dim.year", "=", "'1998'");
        q.addMeasure("amount", "Min");
        return q;
    }

    /**
     * Builds the new query (q^n) as is in loanQueries_Usability.txt:
     *
     * <pre>
     *   Gamma: account_dim.district_name, date_dim.year
     *   Sigma: account_dim.district_name='Hl.m. Praha', date_dim.year='1998'
     *   Measure: Min(amount)
     * </pre>
     */
    private CubeQuery buildNewQuery(BasicStoredCube cube) {
        CubeQuery q = new CubeQuery("LoanQuery21_S2_CG-Cmmn2");
        q.setBasicStoredCube(cube);
        q.addGammaExpression("account_dim", "district_name");
        q.addGammaExpression("date_dim", "year");
        q.addSigmaExpression("account_dim.district_name", "=", "'Hl.m. Praha'");
        q.addSigmaExpression("date_dim.year", "=", "'1998'");
        q.addMeasure("amount", "Min");
        return q;
    }

    /**
     * Builds an in-memory loan cube with two dimensions:
     *
     * <pre>
     *   account_dim: detail_account(0) --> district_name(1) --> region(2) --> all_accounts(3)
     *   date_dim: day(0) --> month(1) --> year(2) --> all_dates(3)
     * </pre>
     */
    private BasicStoredCube buildLoanCube(String cubeName) {
        BasicStoredCube cube = new BasicStoredCube(cubeName);

        //account_dim
        Dimension accountDim = new Dimension("account_dim");
        LinearHierarchy accountHier = new LinearHierarchy();
        setHierarchyLevels(accountHier,
                makeLevel(0, "detail_account"),
                makeLevel(1, "district_name"),
                makeLevel(2, "region"),
                makeLevel(3, "all_accounts"));
        accountDim.getHierarchy().add(accountHier);
        cube.addDimension(accountDim);

        //date_dim
        Dimension dateDim = new Dimension("date_dim");
        LinearHierarchy dateHier = new LinearHierarchy();
        setHierarchyLevels(dateHier,
                makeLevel(0, "day"),
                makeLevel(1, "month"),
                makeLevel(2, "year"),
                makeLevel(3, "all_dates"));
        dateDim.getHierarchy().add(dateHier);
        cube.addDimension(dateDim);

        return cube;
    }

    /**
     * Sets the levels to {@link LinearHierarchy}
     */
    private void setHierarchyLevels(LinearHierarchy hier, Level... levels) {
        for (Level l : levels) {
            hier.getLevels().add(l);
        }
    }

    /** Creates a {@link Level} with the given position and name */
    private Level makeLevel(int position, String name) {
        return new Level(position, name);
    }

    /**
     * Convenience method: injects q^n and q^b directly into the checker
     */
    private void setQueries(CubeQueryUsabilityChecker c, CubeQuery qn, CubeQuery qb) {
        c.setQueries(qn, qb, null);
    }
}

