package cubemanager.usability;

import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import describe.QueryMeasure;
import result.Cell;
import result.Result;

import java.util.*;

/**
 * Handles the execution part of cube-query usability.
 */
public class CubeQueryUsabilityExecutor {

    private final CubeQueryUsabilityChecker checker;

    private Result computedResult;

    public CubeQueryUsabilityExecutor(CubeQueryUsabilityChecker checker) {
        this.checker = checker;
    }

    public Result getComputedResult() {
        return computedResult;
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
        CubeQuery baseQuery = checker.getBaseQuery();
        if (baseQuery == null || baseQuery.getResult() == null) {
            return false;
        }

        Result baseResult = baseQuery.getResult();
        ArrayList<Cell> baseCells = baseResult.getCells();

        //step 1
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
                newSigmaLevelNames.put(parts[0].trim(), parts[1].trim());
            }
        }

        // dim -> q^b grouper level name (e.g. "date_dim" -> "month")
        Map<String, String> baseGrouperLevelNames = new HashMap<>();
        for (String[] g : baseGamma) {
            baseGrouperLevelNames.put(g[0].trim(), g[1].trim());
        }

        // step 2: build sigma filter of q^n expanded to q^b's grouper level
        Map<String, Set<String>> sigmaFilterN = checker.buildSigmaFilter(currentCubeQuery);
        for (String dim : new HashSet<>(sigmaFilterN.keySet())) {
            Set<String> rawValues = sigmaFilterN.get(dim);
            if (rawValues.contains("*"))
                continue;

            String sigmaLvlN = newSigmaLevelNames.get(dim);
            String grouperLvlB = baseGrouperLevelNames.get(dim);

            if (sigmaLvlN != null && grouperLvlB != null && !sigmaLvlN.equals(grouperLvlB)) {
                // q^n's sigma is at a coarser level than q^b's grouper level
                // (e.g. year='1998' vs cells at month level) – roll down so the filter operates at the correct granularity
                Set<String> expanded = checker.rollDownValuesToLevel(dim, sigmaLvlN, rawValues, grouperLvlB);
                if (expanded != null && !expanded.isEmpty()) {
                    sigmaFilterN.put(dim, expanded);
                } else {
                 //   System.out.println("[Usability] executeCubeQueryWithUsability: "
                 //           + "could not expand sigma values for dim " + dim
                //            + " from level '" + sigmaLvlN + "' to '" + grouperLvlB + "'");
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
            groupedCounts.merge(key, (cellCount != null ? cellCount.longValue() : 1L), Long::sum);
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

            String[] vals = new String[members.length + accMeasures.length + 1];
            System.arraycopy(members, 0, vals, 0, members.length);
            for (int m = 0; m < accMeasures.length; m++) {
                //as integer string
                double v = accMeasures[m];
                vals[members.length + m] = (v == Math.floor(v) && !Double.isInfinite(v)) //todo: floor?
                        ? String.valueOf((long) v)
                        : String.valueOf(v);
            }
            vals[vals.length - 1] = String.valueOf(count);
            computedResult.addPair(vals);
        }

        //Step 6: populate columnNames, columnLabels and resultArray
        int numDims = newGamma.size();
        int totalCols = numDims + numMeasures + 1; // dims + measures + countOfDetailedCells

        ArrayList<String> colNames = computedResult.getColumnNames();
        ArrayList<String> colLabels = computedResult.getColumnLabels();

        // Dimension columns: column name = DB attribute, label = level name
        for (String[] g : newGamma) {
            String dimName = g[0];
            String lvlName = g[1];
            String colName = checker.resolveLevelColumn(dimName, lvlName, currentCubeQuery, baseQuery);
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
        if (checker.getCubeBase() == null || fromValues == null || fromValues.isEmpty()) {
            return null;
        }

        //dimension table
        String dimTable = null;
        for (Dimension d : checker.getCubeBase().getDimensions()) {
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
        String fineCol = checker.resolveLevelColumn(dimension, fromFinerLevelName, checker.getBaseQuery(), checker.getNewQuery());
        String coarseCol = checker.resolveLevelColumn(dimension, toCoarserLevelName, checker.getBaseQuery(), checker.getNewQuery());

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
            checker.getCubeBase().executeQueryToProduceResult(sql, lookupResult);
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
