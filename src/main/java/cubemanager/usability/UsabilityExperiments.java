package cubemanager.usability;

import mainengine.IMainEngine;

import java.io.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class UsabilityExperiments {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/%s?autoReconnect=true&useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "CinecubesUser";
    private static final String DB_PASSWORD = "Cinecubes";

    private static final String DB_HOST = "127.0.0.1";

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 2020;
    private static Registry registry;

    private static IMainEngine getService() throws Exception {
        registry = LocateRegistry.getRegistry(SERVER_IP, SERVER_PORT);
        IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());
        if (service == null ) {
            System.err.println("Server not found. Exiting...");
            System.exit(-100);
        }
        return service;
    }

    /** EXP-2 Scalability query file for the baseline pkdd99_star dataset (20 queries). */
    //static File EXP2_QUERY_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpScalability/scalabilityQueries.txt");
    static File EXP2_QUERY_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpScalability/scalabilityQueries10pctCover.txt");
    //static File EXP2_QUERY_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpScalability/scalabilityQueries15pctCover.txt");


    /** EXP-3 files (20 queries, pkdd99_star_1M). */
    static File EXP3_SESSION_1_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession1.txt");
    static File EXP3_SESSION_2_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession2.txt");
    static File EXP3_SESSION_3_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession3.txt");
    static File EXP3_SESSION_4_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession4.txt");
    static File EXP3_SESSION_5_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession5.txt");
    static File EXP3_SESSION_6_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession6.txt");
    static File EXP3_SESSION_7_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession7.txt");
    static File EXP3_SESSION_8_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession8.txt");
    static File EXP3_SESSION_9_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession9.txt");
    static File EXP3_SESSION_10_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpTimeBreakdown/timeBreakdownSession10.txt");

    /**
     * EXP-4 Session files for usability coverage (20 queries, pkdd99_star_1M).
     */
    static File EXP4_SESSION_10PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession1.txt");
    static File EXP4_SESSION_20PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession2.txt");
    static File EXP4_SESSION_30PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession3.txt");
    static File EXP4_SESSION_40PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession4.txt");
    static File EXP4_SESSION_50PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession5.txt");
    static File EXP4_SESSION_60PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession6.txt");
    static File EXP4_SESSION_70PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession7.txt");
    static File EXP4_SESSION_80PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession8.txt");
    static File EXP4_SESSION_90PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession9.txt");
    static File EXP4_SESSION_100PCT_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpCoverage/coverageSession10.txt");

    /**
     * EXP-5 Session files for query history size.
     */
    static File EXP5_SESSION_HIST10_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistorySize/historySizeSession1.txt");
    static File EXP5_SESSION_HIST20_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistorySize/historySizeSession2.txt");
    static File EXP5_SESSION_HIST30_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistorySize/historySizeSession3.txt");
    static File EXP5_SESSION_HIST40_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistorySize/historySizeSession4.txt");
    static File EXP5_SESSION_HIST50_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistorySize/historySizeSession5.txt");

    /** EXP-6 Session file where the usable query is at certain history position */
    static File EXP6_SESSION_POS2_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistoryPosition/histPositionSession1.txt");
    static File EXP6_SESSION_POS10_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistoryPosition/histPositionSession2.txt");
    static File EXP6_SESSION_POS20_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistoryPosition/histPositionSession3.txt");
    static File EXP6_SESSION_POS30_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistoryPosition/histPositionSession4.txt");
    static File EXP6_SESSION_POS40_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistoryPosition/histPositionSession5.txt");
    static File EXP6_SESSION_POS50_FILE = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/ExpHistoryPosition/histPositionSession6.txt");


    public enum DatasetScale {
        BASELINE("pkdd99_star", "pkdd99_star", "pkdd99_star"),
        SCALE_100K("pkdd99_star_100K", "pkdd99_star_100k", "pkdd99_star_100K"),
        SCALE_1M("pkdd99_star_1M", "pkdd99_star_1m", "pkdd99_star_1M"),
        SCALE_10M("pkdd99_star_10M", "pkdd99_star_10m", "pkdd99_star_10M"),
        SCALE_100M("pkdd99_star_100M", "pkdd99_star_100m", "pkdd99_star_100M");

        public final String tag;
        public final String schemaName;
        public final String inputFolder;

        DatasetScale(String tag, String schemaName, String inputFolder) {
            this.tag = tag;
            this.schemaName = schemaName;
            this.inputFolder = inputFolder;
        }

        public ConnectionConfig toLoanConfig() {
            return new ConnectionConfig(schemaName, DB_USER, DB_PASSWORD, "loan", inputFolder);
        }
    }

    public static class ConnectionConfig {
        public final String schemaName;
        public final String username;
        public final String password;
        public final String cubeName;
        public final String inputFolder;

        public ConnectionConfig(String schemaName, String username, String password,
                                String cubeName, String inputFolder) {
            this.schemaName = schemaName;
            this.username = username;
            this.password = password;
            this.cubeName = cubeName;
            this.inputFolder = inputFolder;
        }

        HashMap<String, String> toMap() {
            HashMap<String, String> m = new HashMap<>();
            m.put("schemaName", schemaName);
            m.put("username", username);
            m.put("password", password);
            m.put("cubeName", cubeName);
            m.put("inputFolder", inputFolder);
            m.put("dbHost", DB_HOST);
            return m;
        }
    }

    /**
     * Holds all timing and usability-path data for a single query execution.
     */
    public static class QueryResult {
        public String queryName;
        public double directTimeMs = -1.0;
        public double usabilityTimeMs = -1.0;
        public boolean answeredViaUsability = false;
        public String outputFile;

        public double usabilityCheckTimeMs = -1.0;
        public double dbExecTimeMs = -1.0;
        public double usabilityAnswerTimeMs = -1.0;

        public QueryResult(String queryName) {
            this.queryName = queryName;
        }

        public double speedUp() {
            if (directTimeMs <= 0 || usabilityTimeMs <= 0)
                return Double.NaN;
            return (double) directTimeMs / usabilityTimeMs;
        }
    }

    /**
     * Aggregate report for a single experiment session (one query file, one
     * execution mode).
     */
    public static class ExperimentReport {

        public String label;
        public String queryFilePath;
        public List<QueryResult> queryResults = new ArrayList<>();
        public int totalQueries;
        public int usabilityHits;
        public double usabilityHitPct;
        public double totalDirectTimeMs;
        public double totalUsabilityTimeMs;
        public double avgDirectTimeMs;
        public double avgUsabilityTimeMs;
        /**
         * Speed-up = totalDirectTime / totalUsabilityTime
         */
        public double overallSpeedUp;
        public final Map<String, String> metadata = new LinkedHashMap<>();

        public ExperimentReport(String label, String queryFilePath) {
            this.label = label;
            this.queryFilePath = queryFilePath;
        }

        /**
         * Derives all aggregate metrics from {@link #queryResults}.
         * Call this once after all queries have been added.
         */
        public void computeAggregates() {
            totalQueries = queryResults.size();
            usabilityHits = 0;
            totalDirectTimeMs = 0.0;
            totalUsabilityTimeMs = 0.0;

            for (QueryResult qr : queryResults) {
                if (qr.answeredViaUsability)
                    usabilityHits++;
                if (qr.directTimeMs >= 0)
                    totalDirectTimeMs += qr.directTimeMs;
                if (qr.usabilityTimeMs >= 0)
                    totalUsabilityTimeMs += qr.usabilityTimeMs;
            }
            usabilityHitPct = totalQueries > 0 ? 100.0 * usabilityHits / totalQueries : 0.0;
            avgDirectTimeMs = totalQueries > 0 ? totalDirectTimeMs / totalQueries : 0;
            avgUsabilityTimeMs = totalQueries > 0 ? totalUsabilityTimeMs / totalQueries : 0;
            overallSpeedUp = totalUsabilityTimeMs > 0
                    ? (double) totalDirectTimeMs / totalUsabilityTimeMs
                    : Double.NaN;
        }
    }

    public static ExperimentReport runExperiment(String label,
                                                 ConnectionConfig config,
                                                 File queryFile,
                                                 boolean withUsability) throws Exception {
        if (queryFile == null) {
            System.out.println("  [SKIP] " + label + " – query file placeholder is null.");
            return null;
        }

        ExperimentReport report = new ExperimentReport(label, queryFile.getPath());

        // Fresh engine per run so that query history clears between runs
        IMainEngine engine = getService();
        engine.initializeConnection("RDBMS", config.toMap());

        List<String> queryStrings = parseQueryFile(queryFile);
        int position = 0;

        for (String rawQuery : queryStrings) {
            position++;
            String queryName = extractQueryName(rawQuery, position);
            QueryResult qr = new QueryResult(queryName);

            if (withUsability) {
                long t0 = System.nanoTime();
                qr.outputFile = engine.answerCubeQueryFromStringWithUsability(rawQuery);
                qr.usabilityTimeMs = (System.nanoTime() - t0) / 1_000_000.0;
                qr.answeredViaUsability = engine.wasLastQueryUsabilityHit();
            } else {
                long t0 = System.nanoTime();
                qr.outputFile = engine.answerCubeQueryFromString(rawQuery);
                qr.directTimeMs = (System.nanoTime() - t0) / 1_000_000.0;
            }

            report.queryResults.add(qr);
        }

        report.computeAggregates();
        return report;
    }

    /**
     * Runs two passes over queryFile: one direct (no usability) and one
     * via the usability path. Direct-run timings are also copied into the
     * usability report so that per-query speed-up can be computed.
     */
    public static List<ExperimentReport> runComparativeExperiment(String baseLabel,
                                                                  ConnectionConfig config,
                                                                  File queryFile) throws Exception {
        if (queryFile == null) {
            System.out.println("  [SKIP] " + baseLabel + " – query file placeholder is null.");
            return Collections.emptyList();
        }

        System.out.println("  [DIRECT]     " + baseLabel);
        ExperimentReport direct = runExperiment(
                baseLabel + " – DIRECT (no usability)", config, queryFile, false);

        System.out.println("  [USABILITY]  " + baseLabel);
        ExperimentReport usability = runExperiment(
                baseLabel + " – WITH USABILITY", config, queryFile, true);

        // Copy direct-path timings into the usability report for speed-up columns
        if (direct != null && usability != null) {
            int n = Math.min(direct.queryResults.size(), usability.queryResults.size());
            for (int i = 0; i < n; i++) {
                usability.queryResults.get(i).directTimeMs = direct.queryResults.get(i).directTimeMs;
            }
            usability.computeAggregates();
        }

        List<ExperimentReport> pair = new ArrayList<>();
        if (direct != null)
            pair.add(direct);
        if (usability != null)
            pair.add(usability);
        return pair;
    }

    /**
     * Runs the usability path over queryFile and records three separate
     * timing phases for every query:
     * <ol>
     * <li><b>Usability-check time</b> – history traversal + condition checks.</li>
     * <li><b>DB-execution time</b> – SQL round-trip on a cache miss.</li>
     * <li><b>Usability-answer time</b> – result derivation on a cache hit.</li>
     * </ol>
     */
    public static ExperimentReport runTimingBreakdownExperiment(String label,
                                                                ConnectionConfig config,
                                                                File queryFile) throws Exception {
        if (queryFile == null) {
            System.out.println("  [SKIP] " + label + " – query file placeholder is null.");
            return null;
        }

        ExperimentReport report = new ExperimentReport(label, queryFile.getPath());

        IMainEngine engine = getService();
        engine.initializeConnection("RDBMS", config.toMap());

        List<String> queryStrings = parseQueryFile(queryFile);
        int position = 0;

        for (String rawQuery : queryStrings) {
            position++;
            String queryName = extractQueryName(rawQuery, position);
            QueryResult qr = new QueryResult(queryName);

            long t0 = System.nanoTime();
            qr.outputFile = engine.answerCubeQueryFromStringWithUsability(rawQuery);
            double totalMs = (System.nanoTime() - t0) / 1_000_000.0;
            qr.usabilityTimeMs = totalMs;
            qr.usabilityCheckTimeMs = engine.getLastUsabilityCheckTimeMs();
            qr.answeredViaUsability = engine.wasLastQueryUsabilityHit();

            if (qr.answeredViaUsability) {
                qr.usabilityAnswerTimeMs = engine.getLastUsabilityAnswerTimeMs();
                qr.dbExecTimeMs = -1;
            } else {
                double checkMs = qr.usabilityCheckTimeMs >= 0 ? qr.usabilityCheckTimeMs : 0.0;
                qr.dbExecTimeMs = Math.max(0.0, totalMs - checkMs);
                qr.usabilityAnswerTimeMs = -1.0;
            }

            report.queryResults.add(qr);
        }

        report.computeAggregates();
        return report;
    }

    /**
     * EXP-1: Runs loanQueries_Usability.txt with and without
     * usability enabled for each of the five dataset scales (5 sessions total).
     */
    public static List<ExperimentReport> runExp1_LoanUsabilityFile(
            List<ExperimentReport> allReports) throws Exception {

        System.out.println("\n" + repeatChar('=', 80));
        System.out.println("EXP-1: loanQueries_Usability.txt – all five dataset scales");
        System.out.println(repeatChar('=', 80));

        File usabilityFile = new File("InputFiles/pkdd99_star/Queries/UsabilityQueries_Loans/loanQueries_Usability.txt");

        DatasetScale[] scales = {
                DatasetScale.BASELINE,
                DatasetScale.SCALE_100K,
                DatasetScale.SCALE_1M,
                DatasetScale.SCALE_10M,
                DatasetScale.SCALE_100M
        };

        List<ExperimentReport> results = new ArrayList<>();

        for (DatasetScale scale : scales) {
            System.out.println("\n  [EXP-1][" + scale.tag + "][loan] Checking DB availability...");
            if (!isDatabaseAvailable(scale.schemaName)) {
                System.out.println("  [SKIP] Database '" + scale.schemaName + "' not reachable.");
                continue;
            }

            ConnectionConfig config = scale.toLoanConfig();
            String baseLabel = "EXP-1 [" + scale.tag + "][loan]  loanQueries_Usability.txt";
            List<ExperimentReport> pair = runComparativeExperiment(baseLabel, config, usabilityFile);
            results.addAll(pair);
        }

        allReports.addAll(results);
        return results;
    }

    /**
     * EXP-2: Scalability experiment. Executes a 20-query file against each
     * of the five dataset scales, both with and without the usability optimiser.
     */
    public static List<ExperimentReport> runExp2_Scalability(
            List<ExperimentReport> allReports) throws Exception {

        System.out.println("\n" + repeatChar('=', 80));
        System.out.println("EXP-2: Scalability – 5 sessions × 20 queries (one per dataset scale)");
        System.out.println(repeatChar('=', 80));

        DatasetScale[] scales = {
                DatasetScale.BASELINE,
                DatasetScale.SCALE_100K,
                DatasetScale.SCALE_1M,
                DatasetScale.SCALE_10M,
                DatasetScale.SCALE_100M
        };
        File[] queryFiles = {
                EXP2_QUERY_FILE,
                EXP2_QUERY_FILE,
                EXP2_QUERY_FILE,
                EXP2_QUERY_FILE,
                EXP2_QUERY_FILE
        };

        List<ExperimentReport> results = new ArrayList<>();

        for (int i = 0; i < scales.length; i++) {
            DatasetScale scale = scales[i];
            File queryFile = queryFiles[i];

            System.out.println("\n  [EXP-2][" + scale.tag + "][loan]");

            if (!isDatabaseAvailable(scale.schemaName)) {
                System.out.println("  [SKIP] Database '" + scale.schemaName + "' not reachable.");
                continue;
            }
            if (queryFile == null) {
                System.out.println("  [SKIP] EXP2 query file for " + scale.tag
                        + " is not set (placeholder is null).");
                continue;
            }

            ConnectionConfig config = scale.toLoanConfig();
            String baseLabel = "EXP-2 [" + scale.tag + "][loan]  " + queryFile.getName();
            List<ExperimentReport> pair = runComparativeExperiment(baseLabel, config, queryFile);
            results.addAll(pair);
        }

        allReports.addAll(results);
        return results;
    }

    /**
     * EXP-3: Fine-grained timing breakdown for 10 independent sessions, each
     * with its own 20-query file, all targeting the pkdd99_star_1M dataset.
     */
    public static List<ExperimentReport> runExp3_TimeBreakdown(
            List<ExperimentReport> allReports,
            List<List<String>> extraBlocks) throws Exception {

        System.out.println("\n" + repeatChar('=', 80));
        System.out.println("EXP-3: Fine-grained Time Breakdown – 10 sessions (pkdd99_star_1M)");
        System.out.println(repeatChar('=', 80));

        DatasetScale scale = DatasetScale.SCALE_1M;
        if (!isDatabaseAvailable(scale.schemaName)) {
            System.out.println("  [SKIP] Database '" + scale.schemaName + "' not reachable.");
            return Collections.emptyList();
        }
        ConnectionConfig config = scale.toLoanConfig();

        File[] sessionFiles = {
                EXP3_SESSION_1_FILE, EXP3_SESSION_2_FILE, EXP3_SESSION_3_FILE,
                EXP3_SESSION_4_FILE, EXP3_SESSION_5_FILE, EXP3_SESSION_6_FILE,
                EXP3_SESSION_7_FILE, EXP3_SESSION_8_FILE, EXP3_SESSION_9_FILE,
                EXP3_SESSION_10_FILE
        };

        List<ExperimentReport> results = new ArrayList<>();

        for (int i = 0; i < sessionFiles.length; i++) {
            int sessionNum = i + 1;
            File queryFile = sessionFiles[i];

            System.out.println("\n  [EXP-3][" + scale.tag + "][loan] Session-" + sessionNum);

            if (queryFile == null) {
                System.out.println("  [SKIP] EXP-3 Session-" + sessionNum
                        + " – query file placeholder is null.");
                continue;
            }

            String baseLabel = "EXP-3 [" + scale.tag + "][loan]  Session-" + sessionNum;

            System.out.println("  [DIRECT]    " + baseLabel);
            ExperimentReport direct = runExperiment(
                    baseLabel + " – DIRECT (no usability)", config, queryFile, false);
            if (direct != null) {
                results.add(direct);
                allReports.add(direct);
            }

            System.out.println("  [BREAKDOWN] " + baseLabel);
            ExperimentReport breakdown = runTimingBreakdownExperiment(
                    baseLabel + "  Timing breakdown", config, queryFile);
            if (breakdown != null) {
                results.add(breakdown);
                allReports.add(breakdown);
                extraBlocks.add(formatTimingBreakdownReport(breakdown));
            }
        }

        return results;
    }

    /**
     * EXP-4: Usability coverage experiment. Runs 10 sessions of 16 queries
     * each against pkdd99_star_1M, where each session's query file has
     * a different fraction of queries designed to trigger usability hits.
     */
    public static List<ExperimentReport> runExp4_UsabilityCoverage(
            List<ExperimentReport> allReports) throws Exception {

        System.out.println("\n" + repeatChar('=', 80));
        System.out.println("EXP-4: Usability Coverage – 10 sessions × 20 queries (pkdd99_star_1M)");
        System.out.println(repeatChar('=', 80));

        DatasetScale scale = DatasetScale.SCALE_1M;
        if (!isDatabaseAvailable(scale.schemaName)) {
            System.out.println("  [SKIP] Database '" + scale.schemaName + "' not reachable.");
            return Collections.emptyList();
        }
        ConnectionConfig config = scale.toLoanConfig();

        double[] coveragePcts = { 12.5, 18.8, 25, 31.3, 37.5, 43.8, 50, 56.3, 62.5, 68.8 };
        File[] sessionFiles = {
                EXP4_SESSION_10PCT_FILE, EXP4_SESSION_20PCT_FILE,
                EXP4_SESSION_30PCT_FILE, EXP4_SESSION_40PCT_FILE,
                EXP4_SESSION_50PCT_FILE, EXP4_SESSION_60PCT_FILE,
                EXP4_SESSION_70PCT_FILE, EXP4_SESSION_80PCT_FILE,
                EXP4_SESSION_90PCT_FILE, EXP4_SESSION_100PCT_FILE
        };

        List<ExperimentReport> results = new ArrayList<>();

        for (int i = 0; i < sessionFiles.length; i++) {
            double pct = coveragePcts[i];
            File queryFile = sessionFiles[i];
            String pctLabel = String.format("%.0f%%", pct);

            System.out.println("\n  [EXP-4][" + scale.tag + "][loan] Coverage=" + pctLabel);

            if (queryFile == null) {
                System.out.println("  [SKIP] EXP-4 Coverage=" + pctLabel
                        + " – query file placeholder is null.");
                continue;
            }

            String baseLabel = "EXP-4 [" + scale.tag + "][loan]  Coverage=" + pctLabel;
            String metaKey = "Usability Coverage";

            System.out.println("  [DIRECT]    " + baseLabel);
            ExperimentReport direct = runExperiment(
                    baseLabel + " – DIRECT (no usability)", config, queryFile, false);
            if (direct != null) {
                direct.metadata.put(metaKey, pctLabel);
                results.add(direct);
            }

            System.out.println("  [USABILITY] " + baseLabel);
            ExperimentReport usability = runExperiment(
                    baseLabel + " – WITH USABILITY", config, queryFile, true);
            if (usability != null) {
                // Merge direct timings for per-query speed-up
                if (direct != null) {
                    int n = Math.min(direct.queryResults.size(), usability.queryResults.size());
                    for (int j = 0; j < n; j++) {
                        usability.queryResults.get(j).directTimeMs = direct.queryResults.get(j).directTimeMs;
                    }
                    usability.computeAggregates();
                }
                usability.metadata.put(metaKey, pctLabel);
                results.add(usability);
            }
        }

        allReports.addAll(results);
        return results;
    }


    /**
     * EXP-5: Query history size experiment. Runs 5 sessions of 50 queries each
     * against pkdd99_star_1M, holding the usability-hit coverage fixed at
     * 10 % and varying the query-history window size (10, 20, 30, 40, 50) across
     * sessions.
     */
    public static List<ExperimentReport> runExp5_QueryHistorySize(
            List<ExperimentReport> allReports) throws Exception {

        System.out.println("\n" + repeatChar('=', 80));
        System.out.println("EXP-5: Query History Size – 5 sessions × 50 queries (pkdd99_star_1M)");
        System.out.println(repeatChar('=', 80));

        DatasetScale scale = DatasetScale.SCALE_1M;
        if (!isDatabaseAvailable(scale.schemaName)) {
            System.out.println("  [SKIP] Database '" + scale.schemaName + "' not reachable.");
            return Collections.emptyList();
        }
        ConnectionConfig config = scale.toLoanConfig();

        int[] historySizes = { 10, 20, 30, 40, 50 };
        double fixedCoverage = 10.0;
        File[] sessionFiles = {
                EXP5_SESSION_HIST10_FILE, EXP5_SESSION_HIST20_FILE,
                EXP5_SESSION_HIST30_FILE, EXP5_SESSION_HIST40_FILE,
                EXP5_SESSION_HIST50_FILE
        };

        List<ExperimentReport> results = new ArrayList<>();

        for (int i = 0; i < sessionFiles.length; i++) {
            int histSize = historySizes[i];
            File queryFile = sessionFiles[i];

            System.out.println("\n  [EXP-5][" + scale.tag + "][loan] HistorySize=" + histSize);

            if (queryFile == null) {
                System.out.println("  [SKIP] EXP-5 HistorySize=" + histSize
                        + " – query file placeholder is null.");
                continue;
            }

            String baseLabel = String.format("EXP-5 [%s][loan]  HistSize=%d  Coverage=%.0f%%",
                    scale.tag, histSize, fixedCoverage);
            String histMeta = String.valueOf(histSize);
            String covMeta = String.format("%.0f%%", fixedCoverage);

            System.out.println("  [DIRECT]    " + baseLabel);
            ExperimentReport direct = runExperiment(
                    baseLabel + " – DIRECT (no usability)", config, queryFile, false);
            if (direct != null) {
                direct.metadata.put("History Size", histMeta);
                direct.metadata.put("Usability Coverage", covMeta);
                results.add(direct);
            }

            System.out.println("  [USABILITY] " + baseLabel);
            ExperimentReport usability = runExperiment(
                    baseLabel + " – WITH USABILITY", config, queryFile, true);
            if (usability != null) {
                if (direct != null) {
                    int n = Math.min(direct.queryResults.size(), usability.queryResults.size());
                    for (int j = 0; j < n; j++) {
                        usability.queryResults.get(j).directTimeMs = direct.queryResults.get(j).directTimeMs;
                    }
                    usability.computeAggregates();
                }
                usability.metadata.put("History Size", histMeta);
                usability.metadata.put("Usability Coverage", covMeta);
                results.add(usability);
            }
        }

        allReports.addAll(results);
        return results;
    }

    /**
     * EXP-6: Usable-query position experiment. Runs 6 sessions of 50 queries
     * each against pkdd99_star_1M. In each session exactly one query is
     * designed to be answered via the usability cache; that query is placed at a
     * different position in the query sequence across sessions
     * (positions 2, 10, 20, 30, 40, 50).
     */
    public static List<ExperimentReport> runExp6_QueryPosition(
            List<ExperimentReport> allReports) throws Exception {

        System.out.println("\n" + repeatChar('=', 80));
        System.out.println("EXP-6: Position of Usability Query – 6 sessions × 50 queries (pkdd99_star_1M)");
        System.out.println(repeatChar('=', 80));

        DatasetScale scale = DatasetScale.SCALE_1M;
        if (!isDatabaseAvailable(scale.schemaName)) {
            System.out.println("  [SKIP] Database '" + scale.schemaName + "' not reachable.");
            return Collections.emptyList();
        }
        ConnectionConfig config = scale.toLoanConfig();

        int[] positions = { 2, 10, 20, 30, 40, 50 };
        File[] sessionFiles = {
                EXP6_SESSION_POS2_FILE, EXP6_SESSION_POS10_FILE,
                EXP6_SESSION_POS20_FILE, EXP6_SESSION_POS30_FILE,
                EXP6_SESSION_POS40_FILE, EXP6_SESSION_POS50_FILE
        };

        List<ExperimentReport> results = new ArrayList<>();

        for (int i = 0; i < sessionFiles.length; i++) {
            int pos = positions[i];
            File queryFile = sessionFiles[i];

            System.out.println("\n  [EXP-6][" + scale.tag + "][loan] UsableQueryPosition=" + pos);

            if (queryFile == null) {
                System.out.println("  [SKIP] EXP-6 Position=" + pos
                        + " – query file placeholder is null.");
                continue;
            }

            String baseLabel = "EXP-6 [" + scale.tag + "][loan]  Position=" + pos;
            String posMeta = String.valueOf(pos);
            String covMeta = "1 usable query per session";

            System.out.println("  [DIRECT]    " + baseLabel);
            ExperimentReport direct = runExperiment(
                    baseLabel + " – DIRECT (no usability)", config, queryFile, false);
            if (direct != null) {
                direct.metadata.put("Usable Query Position", posMeta);
                direct.metadata.put("Usability Coverage", covMeta);
                results.add(direct);
            }

            System.out.println("  [USABILITY] " + baseLabel);
            ExperimentReport usability = runExperiment(
                    baseLabel + " – WITH USABILITY", config, queryFile, true);
            if (usability != null) {
                if (direct != null) {
                    int n = Math.min(direct.queryResults.size(), usability.queryResults.size());
                    for (int j = 0; j < n; j++) {
                        usability.queryResults.get(j).directTimeMs = direct.queryResults.get(j).directTimeMs;
                    }
                    usability.computeAggregates();
                }
                usability.metadata.put("Usable Query Position", posMeta);
                usability.metadata.put("Usability Coverage", covMeta);
                results.add(usability);
            }
        }

        allReports.addAll(results);
        return results;
    }

    public static List<String> formatReport(ExperimentReport report) {
        List<String> lines = new ArrayList<>();
        String sep = repeatChar('=', 80);
        String dash = repeatChar('-', 80);

        lines.add(sep);
        lines.add("EXPERIMENT: " + report.label);
        lines.add("File      : " + report.queryFilePath);

        for (Map.Entry<String, String> entry : report.metadata.entrySet()) {
            lines.add(String.format("%-26s: %s", entry.getKey(), entry.getValue()));
        }

        lines.add(dash);
        lines.add(String.format("Total queries          : %d", report.totalQueries));
        lines.add(String.format("Usability hits         : %d  (%.1f%%)",
                report.usabilityHits, report.usabilityHitPct));
        lines.add(String.format("Total direct time      : %.2f ms  (avg %.2f ms/query)",
                report.totalDirectTimeMs, report.avgDirectTimeMs));
        lines.add(String.format("Total usability time   : %.2f ms  (avg %.2f ms/query)",
                report.totalUsabilityTimeMs, report.avgUsabilityTimeMs));
        if (!Double.isNaN(report.overallSpeedUp)
                && report.totalDirectTimeMs > 0
                && report.totalUsabilityTimeMs > 0) {
            lines.add(String.format("Overall speed-up       : %.2fx  (direct / usability)",
                    report.overallSpeedUp));
        }
        lines.add(dash);

        boolean hasCoverage = report.metadata.containsKey("Usability Coverage");
        boolean hasHistory = report.metadata.containsKey("History Size");
        boolean hasPosition = report.metadata.containsKey("Usable Query Position");
        boolean hasExtra = hasCoverage || hasHistory || hasPosition;

        if (hasExtra) {
            String extraHeader;
            String extraValue;
            if (hasCoverage) {
                extraHeader = "Coverage%";
                extraValue = report.metadata.get("Usability Coverage");
            } else if (hasHistory) {
                extraHeader = "HistSize";
                extraValue = report.metadata.get("History Size");
            } else {
                extraHeader = "Position";
                extraValue = report.metadata.get("Usable Query Position");
            }

            lines.add(String.format("%-40s %12s %14s %10s %10s %8s",
                    "Query Name", "Direct (ms)", "Usability (ms)", "Via Usab?",
                    extraHeader, "Speed-Up"));
            lines.add(repeatChar('-', 98));

            for (QueryResult qr : report.queryResults) {
                String directStr = qr.directTimeMs >= 0 ? String.format("%.2f", qr.directTimeMs) : "N/A";
                String usabilityStr = qr.usabilityTimeMs >= 0 ? String.format("%.2f", qr.usabilityTimeMs) : "N/A";
                String via = qr.answeredViaUsability ? "YES" : "no";
                String speedUpStr = Double.isNaN(qr.speedUp()) ? "N/A"
                        : String.format("%.2fx", qr.speedUp());

                lines.add(String.format("%-40s %12s %14s %10s %10s %8s",
                        truncate(qr.queryName, 40),
                        directStr, usabilityStr, via, extraValue, speedUpStr));
            }
        } else {
            lines.add(String.format("%-40s %12s %14s %10s %8s",
                    "Query Name", "Direct (ms)", "Usability (ms)", "Via Usab?", "Speed-Up"));
            lines.add(repeatChar('-', 80));

            for (QueryResult qr : report.queryResults) {
                String directStr = qr.directTimeMs >= 0 ? String.format("%.2f", qr.directTimeMs) : "N/A";
                String usabilityStr = qr.usabilityTimeMs >= 0 ? String.format("%.2f", qr.usabilityTimeMs) : "N/A";
                String via = qr.answeredViaUsability ? "YES" : "no";
                String speedUpStr = Double.isNaN(qr.speedUp()) ? "N/A"
                        : String.format("%.2fx", qr.speedUp());

                lines.add(String.format("%-40s %12s %14s %10s %8s",
                        truncate(qr.queryName, 40),
                        directStr, usabilityStr, via, speedUpStr));
            }
        }

        lines.add(sep);
        return lines;
    }

    public static List<String> formatTimingBreakdownReport(ExperimentReport report) {
        List<String> lines = new ArrayList<>();
        String sep = repeatChar('=', 100);
        String dash = repeatChar('-', 100);

        lines.add(sep);
        lines.add("EXPERIMENT 3 – FINE-GRAINED TIMING BREAKDOWN: " + report.label);
        lines.add("File: " + report.queryFilePath);
        lines.add(dash);
        lines.add(String.format("Total queries   : %d", report.totalQueries));
        lines.add(String.format("Usability hits  : %d  (%.1f%%)",
                report.usabilityHits, report.usabilityHitPct));
        lines.add("");

        double sumCheck = 0.0;
        double sumDbExec = 0.0;
        double sumAnswer = 0.0;
        int cntCheck = 0;
        int cntDbExec = 0;
        int cntAnswer = 0;

        for (QueryResult qr : report.queryResults) {
            if (qr.usabilityCheckTimeMs >= 0) {
                sumCheck += qr.usabilityCheckTimeMs;
                cntCheck++;
            }
            if (qr.dbExecTimeMs >= 0) {
                sumDbExec += qr.dbExecTimeMs;
                cntDbExec++;
            }
            if (qr.usabilityAnswerTimeMs >= 0) {
                sumAnswer += qr.usabilityAnswerTimeMs;
                cntAnswer++;
            }
        }

        lines.add("-- Phase averages --");
        lines.add(String.format("Usability check time  (all %4d queries) : avg %6.2f ms  |  total %.2f ms",
                cntCheck,
                cntCheck > 0 ? sumCheck / cntCheck : 0.0,
                sumCheck));
        lines.add(String.format("DB execution time     (miss %4d queries): avg %6.2f ms  |  total %.2f ms",
                cntDbExec,
                cntDbExec > 0 ? sumDbExec / cntDbExec : 0.0,
                sumDbExec));
        lines.add(String.format("Usability answer time (hit  %4d queries): avg %6.2f ms  |  total %.2f ms",
                cntAnswer,
                cntAnswer > 0 ? sumAnswer / cntAnswer : 0.0,
                sumAnswer));
        lines.add("");
        lines.add("-- Interpretation --");

        if (cntDbExec > 0 && cntAnswer > 0) {
            double saving = (double) sumDbExec / cntDbExec - (double) sumAnswer / cntAnswer;
            lines.add(String.format(
                    "Avg saving per hit (DB exec avg − usability answer avg) : %.1f ms", saving));
        }
        if (cntCheck > 0 && cntDbExec > 0) {
            double checkPct = 100.0 * sumCheck / (sumCheck + sumDbExec);
            lines.add(String.format(
                    "Usability check cost as %% of total miss-path cost       : %.1f%%", checkPct));
        }
        lines.add(dash);

        lines.add(String.format("%-40s %6s %12s %12s %12s %8s",
                "Query Name", "Hit?", "Check(ms)", "DBExec(ms)", "UabAns(ms)", "Total(ms)"));
        lines.add(repeatChar('-', 95));

        for (QueryResult qr : report.queryResults) {
            String hitStr = qr.answeredViaUsability ? "YES" : "no";
            String checkStr = qr.usabilityCheckTimeMs >= 0 ? String.format("%.2f", qr.usabilityCheckTimeMs) : "N/A";
            String dbExecStr = qr.dbExecTimeMs >= 0 ? String.format("%.2f", qr.dbExecTimeMs) : "N/A";
            String answerStr = qr.usabilityAnswerTimeMs >= 0 ? String.format("%.2f", qr.usabilityAnswerTimeMs) : "N/A";
            String totalStr = qr.usabilityTimeMs >= 0 ? String.format("%.2f", qr.usabilityTimeMs) : "N/A";

            lines.add(String.format("%-40s %6s %12s %12s %12s %8s",
                    truncate(qr.queryName, 40),
                    hitStr, checkStr, dbExecStr, answerStr, totalStr));
        }

        lines.add(sep);
        return lines;
    }

    public static List<String> parseQueryFile(File queryFile) throws IOException {
        List<String> queries = new ArrayList<>();
        try (Scanner scanner = new Scanner(queryFile).useDelimiter("@")) {
            while (scanner.hasNext()) {
                String q = scanner.next().trim();
                if (!q.isEmpty())
                    queries.add(q);
            }
        }
        return queries;
    }

    public static String extractQueryName(String rawQuery, int position) {
        for (String line : rawQuery.split("\\r?\\n")) {
            String trimmed = line.trim();
            if (trimmed.toLowerCase().startsWith("name:")) {
                String name = trimmed.substring(5).trim();
                if (!name.isEmpty())
                    return name;
            }
        }
        return "Query_" + position;
    }

    public static boolean isDatabaseAvailable(String schemaName, String inputFolder) {
        try {
            IMainEngine engine = getService();
            HashMap<String, String> config = new HashMap<>();
            config.put("schemaName,", schemaName);
            config.put("username", DB_USER);
            config.put("password", DB_PASSWORD);
            config.put("cubeName", "loan");
            config.put("inputFolder", inputFolder);
            config.put("dbHost", DB_HOST);
            engine.initializeConnection("RDBMS", config);
            return true;
        } catch (Exception e) {
            System.out.println("  [WARN] Database '" + schemaName + "' not reachable: " + e.getMessage());
            return false;
        }
    }

    public static void printLines(PrintStream out, List<String> lines) {
        for (String line : lines)
            out.println(line);
    }

    public static String printAndSaveResults(List<ExperimentReport> reports,
                                             List<List<String>> extraLines) throws IOException {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String outputPath = "usability_experiments_new_" + timestamp + ".txt";

        List<String> allLines = new ArrayList<>();
        allLines.add(repeatChar('=', 80));
        allLines.add("NEW USABILITY EXPERIMENTS  –  " + timestamp);
        allLines.add("Datasets : pkdd99_star | pkdd99_star_100K | pkdd99_star_1M | "
                + "pkdd99_star_10M | pkdd99_star_100M");
        allLines.add("Cube     : loan");
        allLines.add(repeatChar('=', 80));
        allLines.add("");

        for (ExperimentReport r : reports) {
            allLines.addAll(formatReport(r));
            allLines.add("");
        }

        for (List<String> block : extraLines) {
            allLines.addAll(block);
            allLines.add("");
        }

        printLines(System.out, allLines);

        try (PrintWriter pw = new PrintWriter(new FileWriter(outputPath))) {
            for (String line : allLines)
                pw.println(line);
        }
        System.out.println("[INFO] Results saved to: " + new File(outputPath).getAbsolutePath());
        return outputPath;
    }

    private static String truncate(String s, int maxLen) {
        if (s == null)
            return "";
        return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "\u2026";
    }

    private static String repeatChar(char ch, int n) {
        char[] arr = new char[n];
        Arrays.fill(arr, ch);
        return new String(arr);
    }

    public static boolean isDatabaseAvailable(String schemaName) {
        String url = "jdbc:mysql://" + DB_HOST + ":3306/" + schemaName
                + "?connectTimeout=3000&socketTimeout=5000"
                + "&autoReconnect=true&useSSL=false&serverTimezone=UTC";
        try (Connection c = DriverManager.getConnection(url, DB_USER, DB_PASSWORD)) {
            return c != null && !c.isClosed();
        } catch (Exception e) {
            System.err.println("  [WARN] Schema '" + schemaName + "' not reachable: " + e.getMessage());
            return false;
        }
    }

    /**
     * Runs all six experiments in sequence and saves the combined
     * output to a timestamped file.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("[INFO] Starting UsabilityExperiments...");

        List<ExperimentReport> allReports = new ArrayList<>();
        List<List<String>> extraBlocks = new ArrayList<>();

        //EXP-1: loanQueries_Usability.txt – all five scales
        runExp1_LoanUsabilityFile(allReports);

        //EXP-2: Scalability – 5 sessions × 20 queries (one per scale)
        runExp2_Scalability(allReports);

        //EXP-3: Time Breakdown – 10 sessions × 20 queries (pkdd99_star_1M)
        runExp3_TimeBreakdown(allReports, extraBlocks);

        //EXP-4: Usability Coverage – 10 sessions × 20 queries (pkdd99_star_1M)
        runExp4_UsabilityCoverage(allReports);

        //EXP-5: Query History Size – 5 sessions × 50 queries (pkdd99_star_1M)
        runExp5_QueryHistorySize(allReports);

        //EXP-6: Position of Usability Query – 6 sessions × 50 queries (pkdd99_star_1M)
        runExp6_QueryPosition(allReports);

        //Persist all results
        String savedPath = printAndSaveResults(allReports, extraBlocks);
        System.out.println("\n[DONE] All experiments complete. Results saved to: " + savedPath);
    }
}

