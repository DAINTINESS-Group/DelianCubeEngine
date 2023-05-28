package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import assess.utils.LabeledCell;
import cubemanager.CubeManager;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import result.Cell;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The top layer class for any assessments done in the intentional model.
 * Given that the CubeManager handles only one cube at a time, instances
 * of this class are created everytime we wish to change cubes.
 */
public class AssessOperator {
    private final CubeManager cubeManager;
    private final PerformanceResults performanceResults;

    static class PerformanceResults {
        long parseTime;
        long comparisonTime;
        long labelingTime;
    }
    public AssessOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
        this.performanceResults = new PerformanceResults();
    }


    /* Executes the provided query. Returns ?
     * @param assessQuery The user-provided query for assessment reasons
     * @return
     * @throws RecognitionException If the query does not follow the defined syntax
     */
    public List<LabeledCell> execute(String assessQuery) throws RecognitionException {
        Instant start = Instant.now();
        AssessQuery parsedQuery = parseQuery(assessQuery);
        Instant end = Instant.now();
        performanceResults.parseTime = Duration.between(start, end).toMillis();

        start = Instant.now();
        HashMap<Cell, Double> comparisonResults = executeComparison(parsedQuery);
        end = Instant.now();
        performanceResults.comparisonTime = Duration.between(start, end).toMillis();

        start = Instant.now();
        List<LabeledCell> results = labelResults(parsedQuery, comparisonResults);
        end = Instant.now();
        performanceResults.labelingTime = Duration.between(start, end).toNanos();

        exportToMD(assessQuery, parsedQuery.outputName, results);
        return results;
    }

    private AssessQuery parseQuery(String assessQuery) throws RecognitionException {
        AssessQueryParser parser = createParser(assessQuery);
        return parser.parse(new AssessQueryBuilder(cubeManager));
    }

    private AssessQueryParser createParser(String incomingExpression) {
        try {
            InputStream stream = new ByteArrayInputStream(incomingExpression.getBytes(StandardCharsets.UTF_8));
            ANTLRInputStream input = new ANTLRInputStream(stream);
            AssessQueryLexer lexer = new AssessQueryLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            return new AssessQueryParser(tokens);
        } catch (IOException e) {
            throw new RuntimeException("There was an error while creating the Assess Query parser");
        }
    }

    private HashMap<Cell, Double> executeComparison(AssessQuery parsedQuery) {
        return parsedQuery.deltaFunction.compareTargetToBenchmark(
                cubeManager.executeQuery(parsedQuery.targetCubeQuery),
                parsedQuery.benchmark);
    }

    private List<LabeledCell> labelResults(AssessQuery parsedQuery, HashMap<Cell, Double> comparisonResults) {
        List<LabeledCell> labeledCells = new ArrayList<>();
        for (Cell cell : comparisonResults.keySet()) {
            String label = parsedQuery.labelingScheme.applyLabels(comparisonResults.get(cell));
            labeledCells.add(new LabeledCell(cell, label));
        }
        return labeledCells;
    }

    private void exportToMD(String assessQuery, String outputName, List<LabeledCell> results) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputFiles/assessments/" + outputName + ".md"));){
            writer.append("## Query\n");
            writer.append(assessQuery);
            writer.append("\n## Results");
            for (LabeledCell cell : results) {
                writer.append(cell.toString());
            }
            writer.append("\n## Performance Results\n");
            writer.append("Parsing time: ").append(String.valueOf(performanceResults.parseTime)).append(" ms\n");
            writer.append("Comparison time: ").append(String.valueOf(performanceResults.comparisonTime)).append(" ms\n");
            writer.append("Labeling time: ").append(String.valueOf(performanceResults.labelingTime)).append(" ns\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to export to MarkDown");
        }
    }
}