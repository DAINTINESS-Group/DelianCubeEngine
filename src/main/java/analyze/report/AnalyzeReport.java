package analyze.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import analyze.AnalyzeQuery;
import result.Result;
import result.ResultFileMetadata;

/**
 * Writes the ANALYZE results table — the produced cube queries and their tuples — to a Markdown file
 * under {@code OutputFiles}, returning its {@link ResultFileMetadata}. A non-null {@code errorMessage}
 * produces an error report instead of the results.
 */
public final class AnalyzeReport {

    private AnalyzeReport() {}

    public static ResultFileMetadata write(String incomingExpression, String connectionType,
            List<AnalyzeQuery> queries, String errorMessage) {
        String localFolder = "OutputFiles" + File.separator;
        String baseName = queries.isEmpty() ? "Analyze"
                : queries.get(0).getAnalyzeCubeQuery().getName().split("-")[0];
        String reportFile = baseName + "-Analyze_Operator_Report.md";

        try (FileWriter writer = new FileWriter(localFolder + reportFile)) {
            writer.write("## ------------------------------------ANALYZE OPERATOR REPORT-------------------------\n\n");
            writer.write("ANALYZE OPERATOR INTENTIONAL QUERY: \n\n**" + incomingExpression + "**\n\n");
            if (errorMessage != null) {
                writer.write("**ERROR WAS ENCOUNTERED DURING THE OPERATOR'S EXECUTION**\n\n");
                writer.write("AnalyzeExecutionError: " + errorMessage + "\n\n");
            } else {
                writer.write("### -----------------------------------------PRODUCED CUBE QUERIES--------------------------------------\n\n");
                for (AnalyzeQuery aq : queries) {
                    appendQuery(writer, aq, connectionType);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ResultFileMetadata metadata = new ResultFileMetadata();
        metadata.setLocalFolder(localFolder);
        metadata.setResultFile(reportFile);
        if (errorMessage != null) {
            metadata.setErrorCheckingStatus(errorMessage);
        }
        return metadata;
    }

    private static void appendQuery(FileWriter writer, AnalyzeQuery aq, String connectionType) throws IOException {
        Result result = aq.getAnalyzeQueryResult();
        if (result == null) {
            return;
        }
        String[][] resultArray = result.getResultArray();
        String resultString = (resultArray == null)
                ? "The result of the Cube Query is empty!"
                : buildTable(resultArray, connectionType);

        writer.write("#### ANALYZE CUBE QUERY\n\n"
                + aq.getAnalyzeCubeQuery().toString() + "\n\n"
                + "##### ANALYZE CUBE QUERY DETAILS\n\n"
                + "Cube Query Type: **" + aq.getType() + "**\n"
                + "Filter value that is modified compared to the Base Query: **" + aq.getOriginalSigmaValue() + "**\n"
                + "Filter value after modification: **" + aq.getModifiedSigmaValue() + "**\n"
                + "Grouper value that is modified compared to the Base Query: **" + aq.getOriginalGammaValue() + "**\n"
                + "Grouper value after modification: **" + aq.getModifiedGammaValue() + "**\n"
                + "Result of the Cube Query in ascending order:\n" + resultString + "\n"
                + "-----------------------------------------------------------------\n");
    }

    /** Renders the result rows as a Markdown table. RDBMS results carry two header rows; Spark two trailing. */
    private static String buildTable(String[][] resultArray, String connectionType) {
        int first;
        int last;
        if ("Spark".equals(connectionType)) {
            first = 0;
            last = resultArray.length - 2;
        } else {
            first = 2;
            last = resultArray.length;
        }

        StringBuilder table = new StringBuilder();
        for (int i = first; i < last; i++) {
            if (i == first) {
                table.append("|");
                for (int k = 0; k < resultArray[i].length - 1; k++) {
                    table.append(k == resultArray[i].length - 2 ? "Metric|" : "Grouper " + (k + 1) + "|");
                }
                table.append("\n|");
                for (int k = 0; k < resultArray[i].length - 1; k++) {
                    table.append("---|");
                }
                table.append("\n|");
            }
            for (int j = 0; j < resultArray[i].length - 1; j++) {
                table.append(resultArray[i][j]).append("|");
            }
            table.append("\n");
            if (i < last - 1) {
                table.append("|");
            }
        }
        return table.toString();
    }
}
