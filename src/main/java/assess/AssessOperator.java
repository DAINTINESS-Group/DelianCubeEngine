package assess;

import assess.syntax.AssessQueryLexer;
import assess.syntax.AssessQueryParser;
import assess.utils.LabeledCell;
import cubemanager.CubeManager;
import mainengine.ResultFileMetadata;
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

    static class AssessResults {
        long parseTime;
        long comparisonTime;
        long labelingTime;
        String query;
        AssessQuery parsedQuery;
        List<LabeledCell> labeledCells;
    }

    public AssessOperator(CubeManager cubeManager) {
        this.cubeManager = cubeManager;
    }

    private String outputFileName;

    public ResultFileMetadata execute(String assessQuery, String name) {
        ResultFileMetadata results = new ResultFileMetadata();
        results.setComponentResultFiles(null);
        results.setComponentResultInfoFiles(null);
        results.setResultInfoFile(name);
        try {
            execute(assessQuery);
            results.setResultFile(outputFileName);
        }
        catch (RecognitionException | RuntimeException e) {
            results.setErrorCheckingStatus(e.toString());
        }
        return results;
    }

    /* Executes the provided query. Returns ?
     * @param assessQuery The user-provided query for assessment reasons
     * @return
     * @throws RecognitionException If the query does not follow the defined syntax
     */
    public AssessResults execute(String assessQuery) throws RecognitionException {
        AssessResults assessResults = new AssessResults();
        assessResults.query = assessQuery;
        // Parse the Query
        Instant start = Instant.now();
        AssessQuery parsedQuery = parseQuery(assessQuery);
        outputFileName = parsedQuery.outputName;
        Instant end = Instant.now();
        assessResults.parseTime = Duration.between(start, end).toMillis();
        assessResults.parsedQuery = parsedQuery;

        // Execute Comparisons
        start = Instant.now();
        HashMap<Cell, Double> comparisonResults = executeComparison(parsedQuery);
        end = Instant.now();
        assessResults.comparisonTime = Duration.between(start, end).toMillis();

        // Label Comparison Results
        start = Instant.now();
        List<LabeledCell> results = labelResults(parsedQuery, comparisonResults);
        end = Instant.now();
        assessResults.labelingTime = Duration.between(start, end).toNanos();
        assessResults.labeledCells = results;

        exportToMD(assessResults);
        return assessResults;
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

    private void exportToMD(AssessResults assessResults) {
        String outputName = assessResults.parsedQuery.outputName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("OutputFiles/assessments/" + outputName + ".md"))){
            writer.append("## Query\n");
            writer.append(assessResults.query);
            writer.append("\n## Results");
            for (LabeledCell cell : assessResults.labeledCells) {
                writer.append(cell.toString());
            }
            writer.append("\n## Performance Results\n");
            writer.append("Parsing time: ").append(String.valueOf(assessResults.parseTime)).append(" ms\n");
            writer.append("Comparison time: ").append(String.valueOf(assessResults.comparisonTime)).append(" ms\n");
            writer.append("Labeling time: ").append(String.valueOf(assessResults.labelingTime)).append(" ns\n");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to export to MarkDown");
        }
    }
}