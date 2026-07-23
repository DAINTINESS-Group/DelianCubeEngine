package scratch;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import analyze.AnalyzeOperatorByIakovidis;
import analyze.AnalyzeOperatorMaxMultiQueryOptimizer;
import analyze.AnalyzeOperatorMidMultiQueryOptimizer;
import analyze.AnalyzeOperatorMinMultiQueryOptimizer;
import analyze.AnalyzeTranslationManager;
import assess.AssessOperator;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import describe.DescribeOperator;
import mainengine.Session;
import mainengine.managers.IntentionalPipeline;
import mainengine.managers.IntentionalProfile;
import result.Result;
import result.ResultFileMetadata;

/**
 * Disposable interactive REPL to fire queries at the DelianCube engine by hand.
 * Auto-detects DESCRIBE / ANALYZE / ASSESS / plain-cube queries from the input.
 *
 * Launch with ./run-query.sh   (see that script for the classpath wiring)
 */
public class QueryRunner {

    private static final String CONN = "RDBMS";

    private CubeManager cubeManager;
    private String schemaName;
    private String analyzeStrategy = "iakovidis"; // iakovidis | min | max | mid

    public static void main(String[] args) throws Exception {
        QueryRunner runner = new QueryRunner();
        // default dataset
        runner.use("pkdd99_star", "loan", "pkdd99_star");
        if (args.length > 0) {
            runner.dispatch(String.join(" ", args).trim());   // one-shot: run the given query and exit
        } else {
            runner.repl();
        }
    }

    /** (re)initialise the engine against a dataset. */
    private void use(String schema, String cube, String inputFolder) throws Exception {
        HashMap<String, String> in = new HashMap<>();
        in.put("schemaName", schema);
        in.put("username", "CinecubesUser");
        in.put("password", "Cinecubes");
        in.put("cubeName", cube);
        in.put("inputFolder", inputFolder);
        this.cubeManager = new CubeManager(CONN, in);
        Session session = new Session(this.cubeManager);
        session.initialize(CONN, in);
        this.schemaName = schema;
        System.out.println("[ok] connected to '" + schema + "' (cube=" + cube + ")");
    }

    private void repl() throws Exception {
        printHelp();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.print("\nquery> ");
        while ((line = br.readLine()) != null) {
            String q = line.trim();
            try {
                if (q.isEmpty()) {
                    // skip
                } else if (q.equals(":quit") || q.equals(":q") || q.equals(":exit")) {
                    System.out.println("bye.");
                    return;
                } else if (q.equals(":help") || q.equals(":h")) {
                    printHelp();
                } else if (q.equals(":examples") || q.equals(":ex")) {
                    printExamples();
                } else if (q.startsWith(":use")) {
                    handleUse(q);
                } else if (q.startsWith(":strategy")) {
                    handleStrategy(q);
                } else if (q.startsWith(":cube")) {
                    runCube(readMultiline(br));
                } else {
                    dispatch(q);
                }
            } catch (Exception e) {
                System.out.println("[error] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.print("\nquery> ");
        }
    }

    /** Route a single-line DSL statement to the right operator by keyword. */
    private void dispatch(String q) throws Exception {
        String lower = q.toLowerCase();
        if (lower.contains(" assess ") || lower.startsWith("assess ")) {
            runAssess(q);
        } else if (lower.contains("describe ")) {
            runDescribe(q);
        } else if (lower.startsWith("analyze ")) {
            runAnalyze(q);
        } else if (q.startsWith("CubeName:")) {
            runCube(q);
        } else {
            System.out.println("[?] Could not detect operator. Use DESCRIBE/ANALYZE/ASSESS, "
                    + "or ':cube' for a raw cube query. ':help' for examples.");
        }
    }

    private void runDescribe(String q) throws Exception {
        ResultFileMetadata md = IntentionalPipeline.run(new DescribeOperator(cubeManager), q,
                IntentionalProfile.DESCRIBE, cubeManager);
        showReport("DESCRIBE", md);
    }

    private void runAssess(String q) throws Exception {
        new File("OutputFiles/assessments").mkdirs();
        ResultFileMetadata md = IntentionalPipeline.run(new AssessOperator(cubeManager), q,
                IntentionalProfile.ASSESS, cubeManager);
        if (md != null && md.getErrorCheckingStatus() != null) {
            System.out.println("[ASSESS error] " + md.getErrorCheckingStatus());
            return;
        }
        String path = (md != null) ? md.getResultFile() : null;
        File f = (path != null) ? new File(path) : null;
        if (f == null || !f.exists()) f = newestReport();
        showAssessReport(f);
    }

    /**
     * Prints an ASSESS report focused on the highlights: the per-cell "Comparisons Made" and
     * "Labeling Results" bodies are collapsed to their count headers; every other section
     * (Query, Highlights, Performance) is printed in full.
     */
    private void showAssessReport(File f) throws Exception {
        if (f == null || !f.exists()) {
            System.out.println("[ASSESS] executed, but no report file was found.");
            return;
        }
        System.out.println("---- ASSESS report: " + f.getPath() + " ----");
        boolean collapsed = false;
        for (String line : Files.readAllLines(f.toPath())) {
            if (line.startsWith("## Comparisons Made") || line.startsWith("## Labeling Results")) {
                System.out.println(line);   // keep the "(N in total)" header, drop the body
                collapsed = true;
            } else if (line.startsWith("## ")) {
                collapsed = false;
                System.out.println(line);
            } else if (!collapsed) {
                System.out.println(line);
            }
        }
    }

    private void runAnalyze(String q) throws Exception {
        ResultFileMetadata md;
        switch (analyzeStrategy) {
            case "min": {
                AnalyzeTranslationManager tm = new AnalyzeTranslationManager(q, cubeManager, schemaName, CONN);
                md = IntentionalPipeline.run(new AnalyzeOperatorMinMultiQueryOptimizer(cubeManager, tm), q,
                        IntentionalProfile.ANALYZE, cubeManager);
                break;
            }
            case "max": {
                AnalyzeTranslationManager tm = new AnalyzeTranslationManager(q, cubeManager, schemaName, CONN);
                md = new AnalyzeOperatorMaxMultiQueryOptimizer(q, cubeManager, CONN, tm).executeAnalyzeWithMaxMQO();
                break;
            }
            case "mid": {
                AnalyzeTranslationManager tm = new AnalyzeTranslationManager(q, cubeManager, schemaName, CONN);
                md = new AnalyzeOperatorMidMultiQueryOptimizer(q, cubeManager, CONN, tm).executeAnalyzeWithMidMQO();
                break;
            }
            default:
                md = new AnalyzeOperatorByIakovidis(q, cubeManager, schemaName, CONN).execute();
        }
        showReport("ANALYZE[" + analyzeStrategy + "]", md);
    }

    private void runCube(String text) throws Exception {
        if (text == null || text.trim().isEmpty()) return;
        CubeQuery cq = cubeManager.createCubeQueryFromString(text, new HashMap<String, String>());
        Result r = cubeManager.executeQuery(cq);
        System.out.println("---- result ----");
        r.printCellsToStream(System.out);
    }

    // ---- helpers ----

    private void showReport(String tag, ResultFileMetadata md) throws Exception {
        String path = (md != null) ? md.getResultFile() : null;
        File f = (path != null) ? new File(path) : null;
        if (f == null || !f.exists()) {
            f = newestReport(); // fall back to the most recently written .md
        }
        showReportFile(tag, f);
    }

    private void showReportFile(String tag, File f) throws Exception {
        if (f == null || !f.exists()) {
            System.out.println("[" + tag + "] executed, but no report file was found.");
            return;
        }
        System.out.println("---- " + tag + " report: " + f.getPath() + " ----");
        List<String> lines = Files.readAllLines(f.toPath());
        int cap = 120;
        for (int i = 0; i < Math.min(cap, lines.size()); i++) {
            System.out.println(lines.get(i));
        }
        if (lines.size() > cap) {
            System.out.println("... (" + (lines.size() - cap) + " more lines; full report: " + f.getPath() + ")");
        }
    }

    private File newestReport() throws Exception {
        Path dir = new File("OutputFiles").toPath();
        if (!Files.isDirectory(dir)) return null;
        try (Stream<Path> walk = Files.walk(dir)) {
            return walk.filter(p -> p.toString().endsWith(".md"))
                    .map(Path::toFile)
                    .max(Comparator.comparingLong(File::lastModified))
                    .orElse(null);
        }
    }

    private String readMultiline(BufferedReader br) throws Exception {
        System.out.println("(enter raw cube query; finish with an empty line)");
        StringBuilder sb = new StringBuilder();
        String l;
        while ((l = br.readLine()) != null && !l.trim().isEmpty()) {
            sb.append(l).append("\n");
        }
        return sb.toString();
    }

    private void handleUse(String q) throws Exception {
        String[] p = q.split("\\s+");
        if (p.length == 2 && p[1].equalsIgnoreCase("pkdd")) { use("pkdd99_star", "loan", "pkdd99_star"); return; }
        if (p.length == 2 && p[1].equalsIgnoreCase("foodmart")) { use("foodmart_reduced", "sales", "foodmart_reduced"); return; }
        if (p.length >= 4) { use(p[1], p[2], p[3]); return; }
        System.out.println("usage: :use pkdd | :use foodmart | :use <schema> <cube> <inputFolder>");
    }

    private void handleStrategy(String q) {
        String[] p = q.split("\\s+");
        if (p.length == 2 && p[1].matches("iakovidis|min|max|mid")) {
            analyzeStrategy = p[1];
            System.out.println("[ok] ANALYZE strategy = " + analyzeStrategy);
        } else {
            System.out.println("usage: :strategy iakovidis|min|max|mid   (current: " + analyzeStrategy + ")");
        }
    }

    private void printHelp() {
        System.out.println("==================================================================");
        System.out.println(" DelianCube interactive query runner   (schema: " + schemaName + ")");
        System.out.println("------------------------------------------------------------------");
        System.out.println(" Type a query on ONE line; the operator (DESCRIBE/ANALYZE/ASSESS) is auto-detected.");
        System.out.println(" Commands:");
        System.out.println("   :examples  ready-to-run example queries (copy a whole line)");
        System.out.println("   :use pkdd | :use foodmart | :use <schema> <cube> <folder>");
        System.out.println("   :strategy iakovidis|min|max|mid     (ANALYZE optimizer; default iakovidis)");
        System.out.println("   :cube      enter a multi-line raw cube query (CubeName:/Gamma:/Sigma:...)");
        System.out.println("   :help   :quit");
        System.out.println("==================================================================");
    }

    private void printExamples() {
        System.out.println("------------------------------------------------------------------");
        System.out.println(" Example queries (copy a whole line and paste at the prompt):");
        System.out.println();
        System.out.println(" # ASSESS  sum(amount) -> BenchmarkTendency + MegaContributor (sum is additive)");
        System.out.println(" with loan for year='1997' by region, year, status assess sum(amount) against past 2 using ratio(absolute(amount, benchmark.amount)) labels {[0.001,0.05]: low, (0.05,0.1]: high, (0.1,+inf): ultra} save as demo_sum");
        System.out.println();
        System.out.println(" # ASSESS  max(amount) -> BenchmarkTendency only (max is not additive)");
        System.out.println(" with loan for region='central Bohemia' by month, region assess max(amount) against 10000 using ratio(amount, benchmark.amount) labels {[0.0,0.5]: low, (0.5,+inf]: high} save as demo_max");
        System.out.println();
        System.out.println(" # DESCRIBE");
        System.out.println(" WITH loan DESCRIBE SUM(amount) FOR year > 1997 GROUP BY region");
        System.out.println();
        System.out.println(" # ANALYZE  (choose optimizer with :strategy)");
        System.out.println(" ANALYZE max(amount) FROM loan FOR region='Prague' AND year='1998' GROUP BY district_name, month AS q1");
        System.out.println("------------------------------------------------------------------");
    }
}
