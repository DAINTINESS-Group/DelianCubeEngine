package cubemanager.usability;

import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.QueryHistoryManager;
import result.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages query history and drives the cube-usability optimisation.
 * <p>
 * <ol>
 * <li>Iterates through the query history (most-recent first) to find the
 * closest cached result that is usable for a new query q^n.</li>
 * <li>Delegates the actual head-to-head usability check to
 * {@link CubeQueryUsabilityChecker} for each candidate pair.</li>
 * <li>When a usable base is found it extracts the result via
 * {@link CubeQueryUsabilityChecker#executeCubeQueryWithUsability}</li>
 * </ol>
 */
public class UsabilityOptimizer {

    private static UsabilityOptimizer instance;
    private final CubeQueryUsabilityChecker checker = CubeQueryUsabilityChecker.getInstance();

    private UsabilityOptimizer() {
    }

    public static UsabilityOptimizer getInstance() {
        if (instance == null) {
            instance = new UsabilityOptimizer();
        }
        return instance;
    }

    /**
     * Searches the query history for the most-recent cached query that is
     * usable as a base for the newQuery q^n and, if found, calculates the resul from that cached base
     *
     * @param newQuery       q^n – the query to be answered
     * @param historyManager the session's query history
     * @param cubeBase       the cube base
     * @return true if a usable base was found and
     *         {@link #getComputedResult()} is ready
     */
    public boolean tryExecuteWithUsability(CubeQuery newQuery,
                                           QueryHistoryManager historyManager,
                                           CubeBase cubeBase) {

        CubeQuery usableBase = findUsableBase(newQuery, historyManager, cubeBase);
        if (usableBase == null) {
            return false;
        }
        //if it returned a candidate, then we execute the query with usability
        return checker.executeCubeQueryWithUsability(newQuery);
    }

    /**
     * Returns the {@link Result} produced by the most recent successful call to
     * {@link #tryExecuteWithUsability}
     *
     * @return the computed result, or null if usability execution has not been performed yet
     */
    public Result getComputedResult() {
        return checker.getComputedResult();
    }


    /**
     * Iterates through the history from most-recent to oldest and returns the
     * first query whose cached result satisfies all six usability conditions
     *
     * @return the usable base query or null if none was found
     */
    //finds the first usable qb
    public CubeQuery findUsableBase(CubeQuery newQuery,
                                    QueryHistoryManager historyManager,
                                    CubeBase cubeBase) {

        if (newQuery == null || historyManager == null) {
            return null;
        }

        List<CubeQuery> candidates = getCandidatesFromHistory(historyManager);
        for (CubeQuery candidate : candidates) {
            if (candidate.getResult() == null) {
                continue;
            }
            checker.setQueries(newQuery, candidate, cubeBase);
            if (checker.checkUsability()) {
                System.out.println("[UsabilityOptimizer] Found usable base query: "
                        + candidate.getName());
                return candidate;
            }
        }

        System.out.println("[UsabilityOptimizer] No usable base query found for: "
                + newQuery.getName());
        return null;
    }


    /**
     * Returns all queries stored in the history, ordered from most-recent to
     * oldest, so that the closest usable cached result is preferred
     */
    private List<CubeQuery> getCandidatesFromHistory(QueryHistoryManager historyManager) {
        List<CubeQuery> history = historyManager.getQueryHistory();
        List<CubeQuery> reversed = new ArrayList<>(history);
        Collections.reverse(reversed);
        return reversed;
    }
}
