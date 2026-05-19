package cubemanager.usability;

import cubemanager.cubebase.*;
import describe.QueryMeasure;
import result.Cell;
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


    //two queries for comparison
    /** q^n – the current (New) query to be answered*/
    private CubeQuery newQuery;

    /** q^b – the previous (Base) query whose result is already computed */
    private CubeQuery baseQuery;
    private CubeBase cubeBase;

    private Result computedResult;

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
        this.computedResult = null;
    }

    // base result (q^b)
    public Result getComputedResult() {
        return computedResult;
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


    /**
     * Derives the result of currentCubeQuery q^n from the cached
     * cells of q^b by:
     * <ol>
     * <li>Applying φ^n@b (the selection condition of q^n transformed to
     * q^b's level) to q^b's cells.</li>
     * <li>Re-grouping the surviving cells by their ancestor values at the
     * grouper levels of q^n.</li>
     * <li>Aggregating the measures within each group (distributive aggregation)</li>
     * </ol>
     * Returns true if the computation of the result was successful
     */
    public boolean executeCubeQueryWithUsability(CubeQuery currentCubeQuery) {
        if (baseQuery == null || baseQuery.getResult() == null) {
            return false;
        }

        Result baseResult = baseQuery.getResult();
        ArrayList<Cell> baseCells = baseResult.getCells();

        // step 1
        ArrayList<String[]> baseGamma = baseQuery.getGammaExpressions();
        ArrayList<String[]> newGamma = currentCubeQuery.getGammaExpressions();

        // dim -> column index in q^b result cells
        Map<String, Integer> dimToColIndex = new HashMap<>();
        for (int i = 0; i < baseGamma.size(); i++) {
            dimToColIndex.put(baseGamma.get(i)[0], i);
        }

        // dim -> q^n sigma level name (e.g. "date_dim" -> "year")
        Map<String, String> newSigmaLevelNames = new HashMap<>();
        for (String[] sigma : currentCubeQuery.getSigmaExpressions()) {
            if (sigma[0].contains(".")) {
                String[] parts = sigma[0].split("\\.");
                newSigmaLevelNames.put(parts[0], parts[1]);
            }
        }

        // dim -> q^b grouper level name (e.g. "date_dim" -> "month")
        Map<String, String> baseGrouperLevelNames = new HashMap<>();
        for (String[] g : baseGamma) {
            baseGrouperLevelNames.put(g[0], g[1]);
        }

        // step 2: build sigma filter of q^n expanded to q^b's grouper level
        Map<String, Set<String>> sigmaFilterN = buildSigmaFilter(currentCubeQuery);
        for (String dim : new HashSet<>(sigmaFilterN.keySet())) {
            Set<String> rawValues = sigmaFilterN.get(dim);
            if (rawValues.contains("*"))
                continue;

            String sigmaLvlN = newSigmaLevelNames.get(dim);
            String grouperLvlB = baseGrouperLevelNames.get(dim);

            if (sigmaLvlN != null && grouperLvlB != null && !sigmaLvlN.equals(grouperLvlB)) {
                // q^n's sigma is at a coarser level than q^b's grouper level
                // (e.g. year='1998' vs cells at month level) – roll down so the filter operates at the correct granularity
                Set<String> expanded = rollDownValuesToLevel(dim, sigmaLvlN, rawValues, grouperLvlB);
                if (expanded != null && !expanded.isEmpty()) {
                    sigmaFilterN.put(dim, expanded);
                } else {
                    System.out.println("[Usability] executeCubeQueryWithUsability: "
                            + "could not expand sigma values for dim " + dim
                            + " from level '" + sigmaLvlN + "' to '" + grouperLvlB + "'");
                    return false; // cannot reuse
                }
            }
        }

        //step 3: filter base cells
        List<Cell> survivingCells = new ArrayList<>();
        for (Cell cell : baseCells) {
            if (cellPassesFilter(cell, dimToColIndex, sigmaFilterN)) {
                survivingCells.add(cell);
            }
        }

        // step 4: build fine->coarse roll-up lookup tables
        // When q^n's grouper level is coarser than q^b's grouper level
        // (condition 5 allows this), we must roll UP each cell's fine-level
        // member value to the coarser level(e.g. month -> year) used by q^n before building the
        // grouping key. We pre-compute a lookup map per dimension to avoid a DB call inside the inner loop
        //
        // For each dim where the levels differ, collect all distinct fine values
        // from the surviving cells, then query the dimension table once
        Map<String, Map<String, String>> rollUpLookup = new HashMap<>(); // dim -> (fineVal -> coarseVal)
        for (String[] g : newGamma) {
            String dim = g[0];
            String lvlN = g[1];
            String lvlB = baseGrouperLevelNames.get(dim);
            if (lvlB == null || lvlB.equals(lvlN))
                continue; // same level, no roll-up needed

            int posN = getLevelPosition(currentCubeQuery, dim, lvlN);
            int posB = getLevelPosition(baseQuery, dim, lvlB);

            if (posN <= posB)
                continue;

            //collect distinct fine values from surviving cells
            Integer colIdx = dimToColIndex.get(dim);
            if (colIdx == null)
                continue;
            Set<String> fineValues = new HashSet<>();
            for (Cell cell : survivingCells) {
                if (colIdx < cell.getDimensionMembers().size()) {
                    fineValues.add(cell.getDimensionMembers().get(colIdx));
                }
            }
            if (fineValues.isEmpty())
                continue;

            //single DB query: SELECT DISTINCT fineCol, coarseCol FROM dimTable WHERE fineCol IN (...)
            Map<String, String> lookup = buildFineToCoarseLookup(dim, lvlB, fineValues, lvlN);
            if (lookup != null) {
                rollUpLookup.put(dim, lookup);
            }
        }

        // group surviving cells by q^n's gamma levels and aggregate
        ArrayList<QueryMeasure> queryMeasures = currentCubeQuery.getQueryMeasures();
        List<String> measureFunctions = new ArrayList<>();
        for (QueryMeasure qm : queryMeasures) {
            measureFunctions.add(qm.getFunction().trim().toUpperCase());
        }

        Map<String, double[]> groupedMeasures = new HashMap<>();
        Map<String, String[]> groupedMembers = new HashMap<>();
        Map<String, Long> groupedCounts = new HashMap<>(); //(sum of countOfDetailedCells per group)

        for (Cell cell : survivingCells) {
            StringBuilder keyBuilder = new StringBuilder();
            String[] memberValues = new String[newGamma.size()];

            for (int i = 0; i < newGamma.size(); i++) {
                String dim = newGamma.get(i)[0];
                Integer colIdx = dimToColIndex.get(dim);
                String fineVal = (colIdx != null && colIdx < cell.getDimensionMembers().size())
                        ? cell.getDimensionMembers().get(colIdx)
                        : "";

                //Roll up to q^n's grouper level if necessary
                String memberVal = fineVal;
                Map<String, String> lookup = rollUpLookup.get(dim);
                if (lookup != null) {
                    String coarseVal = lookup.get(fineVal);
                    if (coarseVal != null)
                        memberVal = coarseVal;
                }

                memberValues[i] = memberVal;
                if (i > 0)
                    keyBuilder.append("|");
                keyBuilder.append(memberVal);
            }
            String key = keyBuilder.toString();

            ArrayList<String> cellMeasures = cell.getMeasures();
            final int numM = cellMeasures.size();

            // if the key is not already in the map, add it with default values
            if (!groupedMeasures.containsKey(key)) {
                double[] init = new double[numM];
                for (int m = 0; m < numM; m++) {
                    String func = "SUM";
                    if (m < measureFunctions.size()) {
                        func = measureFunctions.get(m);
                    }
                    if (func.equals("MIN")) {
                        init[m] = Double.MAX_VALUE;
                    } else if (func.equals("MAX")) {
                        init[m] = -Double.MAX_VALUE;
                    } else {
                        init[m] = 0.0;
                    }
                }
                groupedMeasures.put(key, init);
            }
            double[] acc = groupedMeasures.get(key);


            for (int m = 0; m < numM; m++) {
                try {
                    double val = Double.parseDouble(cellMeasures.get(m));
                    String func = (m < measureFunctions.size()) ? measureFunctions.get(m) : "SUM";
                    switch (func) {
                        case "MIN":
                            acc[m] = Math.min(acc[m], val);
                            break;
                        case "MAX":
                            acc[m] = Math.max(acc[m], val);
                            break;
                        default:
                            acc[m] += val;
                            break; // SUM and COUNT
                    }
                } catch (NumberFormatException ignored) {
                    }
            }
            groupedMembers.putIfAbsent(key, memberValues);

            Integer cellCount = cell.getCountOfDetailedCells();
            Long currentCount = groupedCounts.get(key);
            if (currentCount == null) {
                currentCount = 0L;
            }

            long valueToAdd;
            if (cellCount != null) {
                valueToAdd = cellCount.longValue();
            } else {
                valueToAdd = 1L;
            }
            groupedCounts.put(key, currentCount + valueToAdd);
        }

        //Step 5: build a new Result and store it
        int numMeasures = currentCubeQuery.getQueryMeasures().size();
        if (numMeasures == 0)
            numMeasures = 1;

        computedResult = new Result();
        computedResult.setNumMeasures(numMeasures);
        for (Map.Entry<String, double[]> entry : groupedMeasures.entrySet()) {
            double[] accMeasures = entry.getValue();
            String[] members = groupedMembers.get(entry.getKey());
            long count = groupedCounts.getOrDefault(entry.getKey(), 0L);

            String[] values = new String[members.length + accMeasures.length + 1];
            System.arraycopy(members, 0, values, 0, members.length);
            for (int m = 0; m < accMeasures.length; m++) {
                //as integer string
                double v = accMeasures[m];
                values[members.length + m] = (v == Math.floor(v) && !Double.isInfinite(v)) //todo: floor?
                        ? String.valueOf((long) v)
                        : String.valueOf(v);
            }
            values[values.length - 1] = String.valueOf(count);
            computedResult.addPair(values);
        }

        //Step 6: populate columnNames, columnLabels and resultArray
        int numDims = newGamma.size();
        int totalCols = numDims + numMeasures + 1; // dims + measures + countOfDetailedCells

        ArrayList<String> colNames = computedResult.getColumnNames();
        ArrayList<String> colLabels = computedResult.getColumnLabels();

        for (String[] g : newGamma) {
            String dimName = g[0];
            String lvlName = g[1];
            String colName = resolveLevelColumn(dimName, lvlName, currentCubeQuery, baseQuery);
            colNames.add(colName != null ? colName : lvlName);
            colLabels.add(lvlName);
        }

        //todo
        // Measure columns: column name = FUNC(attr), label = alias if present
        /*for (QueryMeasure qm : queryMeasures) {
            String func = qm.getFunction();
            String attr = qm.getAttribute();
            String colName = (func != null && !func.isEmpty()) ? func + "(" + attr + ")" : attr;
            String label = (qm.getAlias() != null) ? qm.getAlias() : colName;
            colNames.add(colName);
            colLabels.add(label);
        }*/

        // measure column
        colNames.add("measure");
        colLabels.add("measure");

        // countOfDetailedCells column
        colNames.add("countOfDetailedCells");
        colLabels.add("countOfDetailedCells");

        //Build resultArray: row 0 = column names, row 1 = column labels, rows >=2 -> data
        ArrayList<Cell> resultCells = computedResult.getCells();
        String[][] resultArray = new String[2 + resultCells.size()][totalCols];
        for (int c = 0; c < totalCols; c++) {
            resultArray[0][c] = colNames.get(c);
            resultArray[1][c] = colLabels.get(c);
        }
        for (int r = 0; r < resultCells.size(); r++) {
            Cell cell = resultCells.get(r);
            ArrayList<String> dimMembers = cell.getDimensionMembers();
            for (int c = 0; c < numDims && c < dimMembers.size(); c++) {
                resultArray[r + 2][c] = dimMembers.get(c);
            }
            ArrayList<String> cellMeasureVals = cell.getMeasures();
            for (int m = 0; m < cellMeasureVals.size(); m++) {
                resultArray[r + 2][numDims + m] = cellMeasureVals.get(m);
            }
            Integer cellCount = cell.getCountOfDetailedCells();
            resultArray[r + 2][totalCols - 1] = String.valueOf(cellCount != null ? cellCount : 0);
        }
        computedResult.setResultArray(resultArray);

        return true;
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
    private Set<String> rollDownValuesToLevel(String dimension,
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
    private String resolveLevelColumn(String dimension, String levelName, CubeQuery... queries) {
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
    private Map<String, Set<String>> buildSigmaFilter(CubeQuery query) {
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
    private String stripQuotes(String value) {
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



    /**
     * Returns {@code true} if the cell passes the sigma filter
     */
    private boolean cellPassesFilter(Cell cell,
                                     Map<String, Integer> dimToColIndex,
                                     Map<String, Set<String>> sigmaFilter) {
        for (Map.Entry<String, Set<String>> entry : sigmaFilter.entrySet()) {
            String dim = entry.getKey();
            Set<String> allowed = entry.getValue();
            if (allowed.contains("*"))
                continue;

            Integer colIdx = dimToColIndex.get(dim);
            if (colIdx == null)
                continue;
            if (colIdx >= cell.getDimensionMembers().size())
                return false;

            String cellVal = cell.getDimensionMembers().get(colIdx);
            if (!allowed.contains(cellVal))
                return false;
        }
        return true;
    }

    /**
     * Builds a fine-value → coarse-value lookup map for a single dimension by
     * executing one DB query against the dimension table:
     *
     * <pre>
     * SELECT DISTINCT &lt;fineCol&gt;, &lt;coarseCol&gt;
     * FROM   &lt;dimTable&gt;
     * WHERE  &lt;fineCol&gt; IN ('v1', 'v2', ...)
     * </pre>
     *
     * Used in {@link #executeCubeQueryWithUsability} to roll up cell member
     * values from q^b's grouper level to q^n's (coarser) grouper level
     *
     * @param dimension          the dimension name (e.g. "date_dim")
     * @param fromFinerLevelName the level already present in q^b's cells (e.g. "month")
     * @param fromValues         the distinct fine values to look up
     * @param toCoarserLevelName the level required by q^n's gamma (e.g. "year")
     *
     * @return map fineValue → coarseValue, or null on any error
     */
    private Map<String, String> buildFineToCoarseLookup(String dimension,
                                                        String fromFinerLevelName,
                                                        Set<String> fromValues,
                                                        String toCoarserLevelName) {
        if (cubeBase == null || fromValues == null || fromValues.isEmpty()) {
            return null;
        }

        //dimension table
        String dimTable = null;
        for (Dimension d : cubeBase.getDimensions()) {
            if (d.getName().equals(dimension)) {
                dimTable = d.getTableName();
                break;
            }
        }
        if (dimTable == null || dimTable.isEmpty()) {
            System.err.println("[CubeQueryUsabilityChecker] buildFineToCoarseLookup: no table for dimension " + dimension);
            return null;
        }

        //DB column names for both levels
        String fineCol = resolveLevelColumn(dimension, fromFinerLevelName, baseQuery, newQuery);
        String coarseCol = resolveLevelColumn(dimension, toCoarserLevelName, baseQuery, newQuery);

        if (fineCol == null || coarseCol == null) {
            System.err.println("[CubeQueryUsabilityChecker] buildFineToCoarseLookup: could not resolve columns for "
                    + fromFinerLevelName + " -> " + toCoarserLevelName + " in dimension " + dimension);
            return null;
        }

        //SQL query
        StringBuilder inList = new StringBuilder();
        for (String v : fromValues) {
            if (inList.length() > 0)
                inList.append(", ");
            inList.append("'").append(v.replace("'", "''")).append("'");
        }
        String sql = "SELECT DISTINCT " + fineCol + ", " + coarseCol
                + " FROM " + dimTable
                + " WHERE " + fineCol + " IN (" + inList + ")";

        try {
            Result lookupResult = new Result();
            cubeBase.executeQueryToProduceResult(sql, lookupResult);
            Map<String, String> lookup = new HashMap<>();

            String[][] resultArray = lookupResult.getResultArray();
            if (resultArray != null) {
                for (int row = 2; row < resultArray.length; row++) {
                    if (resultArray[row] != null && resultArray[row].length >= 2
                            && resultArray[row][0] != null && resultArray[row][1] != null) {
                        lookup.put(resultArray[row][0].trim(), resultArray[row][1].trim());
                    }
                }
            }
            return lookup;
        } catch (Exception e) {
            System.err.println("[CubeQueryUsabilityChecker] buildFineToCoarseLookup SQL failed: " + e.getMessage());
            return null;
        }
    }


}

