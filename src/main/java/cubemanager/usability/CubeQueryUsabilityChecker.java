package cubemanager.usability;

import cubemanager.cubebase.*;
import describe.QueryMeasure;
import result.Result;

import java.util.*;

public class CubeQueryUsabilityChecker {

    private static CubeQueryUsabilityChecker cubeQueryUsabilityChecker;

    public static CubeQueryUsabilityChecker getInstance() {
        if (cubeQueryUsabilityChecker == null) {
            cubeQueryUsabilityChecker = new CubeQueryUsabilityChecker();
        }
        return cubeQueryUsabilityChecker;
    }

    //to be used for tests!
    static void resetInstance() {
        cubeQueryUsabilityChecker = null;
    }

    //two queries for comparison
    /** q^n – the current (New) query to be answered*/
    private CubeQuery newQuery;

    /** q^b – the previous (Base) query whose result is already computed */
    private CubeQuery baseQuery;
    private CubeBase cubeBase;

    private final CubeQueryUsabilityExecutor executor = new CubeQueryUsabilityExecutor(this);

    public CubeQuery getNewQuery() {
        return newQuery;
    }

    public CubeQuery getBaseQuery() {
        return baseQuery;
    }

    public CubeBase getCubeBase() {
        return cubeBase;
    }

    /**
     * Sets the pair of queries to be compared head-to-head
     * @param  newQuery the current q^n query (not yet executed)
     * @param baseQuery the candidate base query q^b (that has a result)
     * @param cubeBase the cube base used
     */
    public void setQueries(CubeQuery newQuery, CubeQuery baseQuery, CubeBase cubeBase) {
        this.newQuery = newQuery;
        this.baseQuery = baseQuery; //candidate will come here as base
        this.cubeBase = cubeBase;
    }

    // base result (q^b)
    public Result getComputedResult() {
        return executor.getComputedResult();
    }

    /**
     * Checks all six conditions of Theorem 9.1 (Cube Usability) for the currently set pair (q^n, q^b)
     * <p>
     * Both newQuery and baseQuery must be set via {@link #setQueries} before calling this method.
     * {@link #executeCubeQueryWithUsability(CubeQuery)} uses the right cached
     * result
     *
     * @return true if a usable q^b was found in the history
     */
    public boolean checkUsability() {
        if (newQuery == null || baseQuery == null || baseQuery.getResult() == null) {
            return false;
        }

        return checkCondition1SameDS()
                && checkCondition2SameDimensionsAndMeasures()
                && checkCondition3OneAtomPerDimension(newQuery)
                && checkCondition3OneAtomPerDimension(baseQuery)
                && checkCondition4PerfectlyRollable(newQuery)
                && checkCondition4PerfectlyRollable(baseQuery)
                && checkCondition5NewLevelsAreAncestors()
                && checkCondition6SignatureSubset();
    }

    public boolean executeCubeQueryWithUsability(CubeQuery currentCubeQuery) {
       return executor.executeCubeQueryWithUsability(currentCubeQuery);
    }

    //condition checkers

    /**
     * Condition 1: both queries have the same underlying detailed cube DS
     */
    boolean checkCondition1SameDS() {
        if (newQuery.getReferCube() == null || baseQuery.getReferCube() == null) {
            return false;
        }
        // dataset is identified by the cube's name - assuming uniqueness in cube names
        String dsN = newQuery.getReferCube().getName();
        String dsB = baseQuery.getReferCube().getName();
        boolean result = dsN != null && dsN.equals(dsB);
        if (!result) {
            System.out.println("[Usability] Condition 1 FAILED - different DS ("
                    + dsN + " vs " + dsB + ")");
        }
        return result;
    }


    /**
     * Condition 2: both queries have the same dimensions in their schema and the same
     * aggregate measures (implying a 1-to-1 mapping between their measures). All aggregate functions must be distributive.
     * <p>
     * Set of known distributive aggregate functions: {SUM, MIN, MAX, COUNT}
     */
    boolean checkCondition2SameDimensionsAndMeasures() {
        // query dimensions - same set - order doesn't matter
        // query dimensions are contained within the first element of each gamma expression
        ArrayList<String[]> gammaN = newQuery.getGammaExpressions();
        ArrayList<String[]> gammaB = baseQuery.getGammaExpressions();
        if (gammaN.size() != gammaB.size()) {
            System.out.println("[Usability] Condition 2 FAILED - different number of dimensions");
            return false;
        }

        // checking if the dimensions are the same
        Set<String> dimsN = new HashSet<>();
        for (String[] g : gammaN)
            dimsN.add(g[0]); //1st element of the gamma(=dimension)
        Set<String> dimsB = new HashSet<>();
        for (String[] g : gammaB)
            dimsB.add(g[0]);
        if (!dimsN.equals(dimsB)) {
            System.out.println("[Usability] Condition 2 FAILED - different dimension sets");
            return false;
        }

        // measures & aggregate functions
        ArrayList<QueryMeasure> measuresN = newQuery.getQueryMeasures();
        ArrayList<QueryMeasure> measuresB = baseQuery.getQueryMeasures();

        //check size
        if (measuresN.size() != measuresB.size()) {
            System.out.println("[Usability] Condition 2 FAILED - different number of measures");
            return false;
        }

        // Build a map from attribute name -> QueryMeasure for q^b so that
        // the comparison is order-independent (e.g. [("loan","min"),("time","avg")]
        // must match [("time","avg"),("loan","min")])
        // -> ("measure", "aggrFunction")
        Map<String, QueryMeasure> baseMeasureMap = new HashMap<>();
        for (QueryMeasure mb : measuresB) {
            baseMeasureMap.put(mb.getAttribute(), mb); //key is measure, value is the QueryMeasure object
        }
        for (QueryMeasure mn : measuresN) {
            QueryMeasure mb = baseMeasureMap.get(mn.getAttribute()); //search by key to get the object
            if (mb == null) {
                System.out.println("[Usability] Condition 2 FAILED: measure '"
                        + mn.getAttribute() + "' not found in base query q^b");
                return false;
            }
            // Same aggregate function
            if (!mn.getFunction().equalsIgnoreCase(mb.getFunction())) {
                System.out.println("[Usability] Condition 2 FAILED: different aggregate functions for '"
                        + mn.getAttribute() + "' (" + mn.getFunction()
                        + " vs " + mb.getFunction() + ")");
                return false;
            }
            // Must be distributive
            if (!isDistributive(mn.getFunction())) {
                System.out.println("[Usability] Condition 2 FAILED: non-distributive function: "
                        + mn.getFunction());
                return false;
            }
        }
        return true;
    }


    /**
     * Condition 3: the given query has exactly one sigma atom per dimension.
     * The query structure implies that the selection condition is by default conjunction of all sigma atoms, so this condition also means that the selection condition is a conjunction of exactly one atom per dimension.
     * So no need to check for equality (=), membership (IN) or "AND" operator!
     */
    boolean checkCondition3OneAtomPerDimension(CubeQuery query) {
        ArrayList<String[]> gammaExpressions = query.getGammaExpressions();
        ArrayList<String[]> sigmaExpressions = query.getSigmaExpressions();

        // Step 1: check for conjunction
        // no need

        // Step 2: count how many sigma atoms each dimension has
        // computed for each query separately, so that we can check both q^n and q^b
        Map<String, Integer> sigmaCountPerDimension = new HashMap<>();
        for (String[] sigmaAtom : sigmaExpressions) {
            String dimension = sigmaAtom[0].contains(".") ? sigmaAtom[0].split("\\.")[0] : sigmaAtom[0];
            int currentCount = sigmaCountPerDimension.getOrDefault(dimension, 0);
            sigmaCountPerDimension.put(dimension, currentCount + 1);
            //key is dimension name and value is the count
        }

        // Step 3: every dimension in gamma must have exactly one sigma atom
        for (String[] gammaAtom : gammaExpressions) {
            String dimension = gammaAtom[0];
            int count = sigmaCountPerDimension.getOrDefault(dimension, 0);
            if (count != 1) {
                System.out.println("[Usability] Condition 3 FAILED: dimension '" + dimension
                        + "' has " + count + " sigma atom(s) in query " + query.getName());
                return false;
            }
        }

        return true;
    }


    /**
     * Condition 4: the given query is perfectly rollable – for every dimension
     * D, its sigma level (D.L^φ) is an ancestor of (or equal to) its grouper
     * level (D.L^g). In terms of hierarchy position:
     * position(L^g) <= position(L^φ)
     */
    boolean checkCondition4PerfectlyRollable(CubeQuery query) {
        ArrayList<String[]> gamma = query.getGammaExpressions();
        ArrayList<String[]> sigma = query.getSigmaExpressions();

        // Build map: dimensionName -> sigma level name
        Map<String, String> sigmaLevel = new HashMap<>();
        for (String[] s : sigma) {
            if (s[0].contains(".")) {
                String[] parts = s[0].trim().split("\\.");
                sigmaLevel.put(parts[0], parts[1]); //0: dimension -> key, value [1]-> level
            }
        }

        for (String[] g : gamma) {
            String dim = g[0].trim();
            String gammaLvlName = g[1].trim();
            String sigmaLvlName = sigmaLevel.get(dim);
            if (sigmaLvlName == null)
                continue; // no sigma for this dim

            int gammaPos = getLevelPosition(query, dim, gammaLvlName);
            int sigmaPos = getLevelPosition(query, dim, sigmaLvlName);

            // Grouper level must be at or below sigma level in the hierarchy
            // i.e. gammaPos <= sigmaPos (smaller position = more detailed)
            if (gammaPos > sigmaPos) {
                System.out.println("[Usability] Condition 4 FAILED: dimension " + dim
                        + " – grouper level '" + gammaLvlName + "' (pos " + gammaPos
                        + ") is coarser than sigma level '" + sigmaLvlName + "' (pos "
                        + sigmaPos + ") in query " + query.getName());
                return false;
            }
        }
        return true;
    }


    /**
     * Condition 5: for every dimension D, the grouper level of q^n is an
     * ancestor of (or equal to) the grouper level of q^b
     * i.e. position(L^n) >= position(L^b)
     */
    boolean checkCondition5NewLevelsAreAncestors() {
        ArrayList<String[]> gammaN = newQuery.getGammaExpressions();
        ArrayList<String[]> gammaB = baseQuery.getGammaExpressions();

        // Build map for q^b: dimensionName -> level name
        Map<String, String> baseLevels = new HashMap<>();
        for (String[] g : gammaB)
            baseLevels.put(g[0].trim(), g[1].trim());

        for (String[] g : gammaN) {
            String dim = g[0].trim();
            String lvlN = g[1].trim();
            String lvlB = baseLevels.get(dim);
            if (lvlB == null)
                continue;

            int posN = getLevelPosition(newQuery, dim, lvlN);
            int posB = getLevelPosition(baseQuery, dim, lvlB);

            if (posN < 0 || posB < 0)
                continue;

            if (posN < posB) {
                System.out.println("[Usability] Condition 5 FAILED: dimension " + dim
                        + " – q^n level '" + lvlN + "' (pos " + posN
                        + ") is more detailed than q^b level '" + lvlB + "' (pos " + posB + ")");
                return false;
            }
        }
        return true;
    }


    /**
     * Condition 6: for every sigma atom of q^n, after expanding its
     * values down to q^b's grouper level (the signature of q^n), the
     * resulting set must be a SUBSET of the grouper domain of q^b
     * <p>
     * The condition passes if signature(q^n, D) ⊆ grouperDomain(q^b, D)
     * for every dimension D
     */
    boolean checkCondition6SignatureSubset() {
        ArrayList<String[]> baseGamma = baseQuery.getGammaExpressions();

        //build map: dim -> q^b's grouper level name (eg "date_dim" -> "month")
        Map<String, String> baseGrouperLevelNames = new HashMap<>();
        for (String[] g : baseGamma) {
            baseGrouperLevelNames.put(g[0], g[1]);
        }

        // build map: dim -> q^b's sigma level name
        Map<String, String> baseSigmaLevelNames = new HashMap<>();
        for (String[] sigma : baseQuery.getSigmaExpressions()) {
            if (sigma[0].contains(".")) {
                String[] parts = sigma[0].split("\\.");
                baseSigmaLevelNames.put(parts[0], parts[1]);
            }
        }

        // build map: dim -> q^n's sigma level name
        Map<String, String> newSigmaLevelNames = new HashMap<>();
        for (String[] sigma : newQuery.getSigmaExpressions()) {
            if (sigma[0].contains(".")) {
                String[] parts = sigma[0].split("\\.");
                newSigmaLevelNames.put(parts[0], parts[1]);
            }
        }

        //Build the grouper domain of q^b per dimension
        //for each dimension-> roll q^b's sigma values DOWN to q^b's grouper level
        Map<String, Set<String>> grouperDomainB = new HashMap<>();
        Map<String, Set<String>> sigmaFilterB = buildSigmaFilter(baseQuery);

        for (String[] g : baseGamma) {
            String dim = g[0];
            String grouperLvlB = g[1];
            Set<String> baseSigmaValues = sigmaFilterB.get(dim);

            if (baseSigmaValues != null && !baseSigmaValues.contains("*")) {
                String sigmaLvlB = baseSigmaLevelNames.get(dim);
                if (sigmaLvlB != null && !sigmaLvlB.equals(grouperLvlB)) {
                    // sigma level is greater than grouper level – roll down via DB query
                    Set<String> rolled = rollDownValuesToLevel(dim, sigmaLvlB, baseSigmaValues, grouperLvlB);
                    if (rolled != null && !rolled.isEmpty()) {
                        grouperDomainB.put(dim, rolled);
                        continue;
                    }
                } else {
                    // sigma level equals grouper level – use sigma values directly
                    grouperDomainB.put(dim, new HashSet<>(baseSigmaValues));
                    continue;
                }
            }

            System.out.println("[Usability] Condition 6 FAILED: could not build grouper domain"
                    + " for dimension " + dim + " in q^b");
            return false;
        }

        //Build the signature of q^n per dimension and check subset
        Map<String, Set<String>> sigmaFilterN = buildSigmaFilter(newQuery);

        for (Map.Entry<String, Set<String>> entry : sigmaFilterN.entrySet()) {
            String dim = entry.getKey();
            Set<String> allowedByN = entry.getValue();
            Set<String> domainB = grouperDomainB.get(dim);

            if (domainB == null) {
                System.out.println("[Usability] Condition 6 FAILED: no grouper domain for dimension "
                        + dim + " in q^b");
                return false;
            }

            //wildcard sigma covers everything – no subset check needed
            if (allowedByN.contains("*")) {
                continue;
            }

            String sigmaLvlN = newSigmaLevelNames.get(dim);
            String grouperLvlB = baseGrouperLevelNames.get(dim);

            // build signature: expand q^n's sigma values to q^b's grouper level
            Set<String> signature;
            if (sigmaLvlN != null && grouperLvlB != null && !sigmaLvlN.equals(grouperLvlB)) {
                // q^n's sigma level differs from q^b's grouper level – roll down via DB query
                Set<String> rolledDown = rollDownValuesToLevel(dim, sigmaLvlN, allowedByN, grouperLvlB);
                if (rolledDown == null || rolledDown.isEmpty()) {
                    System.out.println("[Usability] Condition 6 FAILED: could not expand sigma"
                            + " values " + allowedByN + " from level '" + sigmaLvlN
                            + "' to level '" + grouperLvlB + "' for dimension " + dim);
                    return false;
                }
                signature = rolledDown;
            } else {
                // Same level – use sigma values directly as the signature
                signature = new HashSet<>(allowedByN);
            }

            // Subset check: signature(q^n) ⊆ grouperDomain(q^b)
            if (!domainB.containsAll(signature)) {
                Set<String> outside = new HashSet<>(signature);
                outside.removeAll(domainB);
                System.out.println("[Usability] Condition 6 FAILED: signature for dimension "
                        + dim + " contains values " + outside
                        + " not present in q^b's grouper domain " + domainB);
                return false;
            }
        }
        return true;
    }


    // -------
    // Helpers
    // -------

    /**
     * Rolls down a set of values from a coarser level
     * fromCoarserLevelName to a finer level toFinerLevelName
     * for the given dimension by querying the dimension table in the DB
     * <p>
     * Runs:
     * <pre>
     * SELECT DISTINCT &lt;fineCol&gt; FROM &lt;dimTable&gt; WHERE &lt;coarseCol&gt; IN ('v1','v2',...)
     * </pre>
     *
     * <p>
     * For example, rolling down year='1998' to the month level
     * returns {'01-1998', '02-1998', ..., '12-1998'}
     * @return the set of finer-level values, or null on any error
     */
    Set<String> rollDownValuesToLevel(String dimension,
                                              String fromCoarserLevelName,
                                              Set<String> fromValues,
                                              String toFinerLevelName) {
        if (cubeBase == null || fromValues == null || fromValues.isEmpty()) {
            return null;
        }

        // get dimension table name from CubeBase's dimension list
        String dimTable = null;
        for (Dimension d : cubeBase.getDimensions()) {
            if (d.getName().equals(dimension)) {
                dimTable = d.getTableName();
                break;
            }
        }
        if (dimTable == null || dimTable.isEmpty()) {
            System.err.println("[CubeQueryUsabilityChecker] rollDownValuesToLevel: no table for dimension " + dimension);
            return null;
        }

        // get DB column names for both levels
        //search newQuery's referCube first, then baseQuery's as a fallback
        String fromCol = resolveLevelColumn(dimension, fromCoarserLevelName, newQuery, baseQuery);
        String toCol = resolveLevelColumn(dimension, toFinerLevelName, newQuery, baseQuery);

        if (fromCol == null || toCol == null) {
            System.err.println("[CubeQueryUsabilityChecker] rollDownValuesToLevel: could not resolve columns for "
                    + fromCoarserLevelName + " -> " + toFinerLevelName + " in dimension " + dimension);
            return null;
        }

        // build IN-list SQL
        StringBuilder inList = new StringBuilder();
        for (String v : fromValues) {
            if (inList.length() > 0)
                inList.append(", ");
            inList.append("'").append(v.replace("'", "''")).append("'");
        }
        String sql = "SELECT DISTINCT " + toCol
                + " FROM " + dimTable
                + " WHERE " + fromCol + " IN (" + inList + ")";

        try {
            Result rollDownResult = new Result();
            cubeBase.executeQueryToProduceResult(sql, rollDownResult);
            Set<String> fineValues = new HashSet<>();
            // single-column queries populate resultArray only (rows 0-1 are headers - data starts at row 2)
            String[][] resultArray = rollDownResult.getResultArray();
            if (resultArray != null) {
                for (int row = 2; row < resultArray.length; row++) {
                    if (resultArray[row][0] != null) {
                        fineValues.add(resultArray[row][0].trim());
                    }
                }
            }
            return fineValues;
        } catch (Exception e) {
            System.err.println("[CubeQueryUsabilityChecker] rollDownValuesToLevel SQL failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Finds the DB column name ({@link Level#getLevelIDAttribute()}) for a
     * level inside a dimension, searching through the provided
     * queries' referCubes in order
     *
     * @param dimension the dimension name to search in
     * @param levelName the level name whose column we need
     * @param queries   one or more queries to search (first match wins)
     * @return the DB column name, or {@code null} if not found in any query
     */
    String resolveLevelColumn(String dimension, String levelName, CubeQuery... queries) {
        for (CubeQuery q : queries) {
            if (q == null || q.getReferCube() == null)
                continue;
            for (Dimension dim : q.getReferCube().getDimensionsList()) {
                if (!dim.getName().equals(dimension))
                    continue;
                for (Hierarchy hier : dim.getHierarchy()) {
                    for (Level lvl : hier.getLevels()) {
                        if (lvl.getName().equals(levelName)) {
                            return lvl.getLevelDescriptionAttribute();
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Builds a map from dimension name to the set of allowed sigma values for
     * the given query. Values are taken from SigmaExpressions[2].
     * For equality (=), the set contains one value.
     * For IN, the values are split on comma/parentheses.
     */
    Map<String, Set<String>> buildSigmaFilter(CubeQuery query) {
        Map<String, Set<String>> filter = new HashMap<>();
        for (String[] sigma : query.getSigmaExpressions()) {
            String dim = sigma[0].contains(".") ? sigma[0].split("\\.")[0] : sigma[0];
            String op = sigma[1].trim().toUpperCase();
            String val = sigma[2].trim();
            Set<String> allowed = new HashSet<>();

            if (op.equals("=")) {
                // Strip surrounding quotes if present
                allowed.add(stripQuotes(val));
            } else if (op.equals("IN")) {
                // Parse "(v1, v2, ...)" or "v1, v2, ..."
                val = val.replaceAll("^\\(|\\)$", "");
                for (String v : val.split(",")) {
                    allowed.add(stripQuotes(v.trim()));
                }
            } else {
                // Unsupported operator: allow everything (wildcard)
                allowed.add("*");
            }
            filter.put(dim, allowed);
        }
        return filter;
    }

    /** removes surrounding single or double quotes from a string. */
    String stripQuotes(String value) {
        if (value == null)
            return "";
        if ((value.startsWith("'") && value.endsWith("'"))
                || (value.startsWith("\"") && value.endsWith("\""))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    /**
     * Returns true if function is a distributive aggregate function {SUM, COUNT, MIN, MAX}
     */
    private boolean isDistributive(String function) {
        if (function == null)
            return false;
        switch (function.trim().toUpperCase()) {
            case "SUM":
            case "COUNT":
            case "MIN":
            case "MAX":
            //case "AVG":
                return true;
            default:
                return false;
        }
    }

    /**
     * Returns the position in the hierarchy of the named level for the given
     * dimension in the given query's referCube. Returns -1 if not found
     */
    private int getLevelPosition(CubeQuery query, String dimensionName, String levelName) {
        if (query.getReferCube() == null)
            return -1;
        List<Dimension> dims = query.getReferCube().getDimensionsList();
        for (Dimension dim : dims) {
            if (dim.getName().equals(dimensionName)) {
                for (Hierarchy hier : dim.getHierarchy()) {
                    for (Level lvl : hier.getLevels()) {
                        if (lvl.getName().equals(levelName)) {
                            return lvl.getPositionInHierarchy();
                        }
                    }
                }
            }
        }
        return -1;
    }

}

