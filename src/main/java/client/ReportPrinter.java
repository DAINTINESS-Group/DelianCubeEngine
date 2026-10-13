package client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import result.ResultFileMetadata;

/**
 * Prints an operator's report to the console, resolved from the {@link ResultFileMetadata} the operator
 * returned. The report file is the local folder plus the result file, or the result file alone when no
 * folder is set.
 */
public final class ReportPrinter {

    private ReportPrinter() {}

    public static void print(ResultFileMetadata report) throws IOException {
        String folder = report.getLocalFolder();
        File file = new File(folder == null ? report.getResultFile() : folder + report.getResultFile());
        if (!file.exists()) {
            System.out.println("Report file not found: " + file.getAbsolutePath());
            return;
        }
        System.out.println("Report: " + file.getPath() + "\n");
        for (String line : Files.readAllLines(file.toPath())) {
            System.out.println(line);
        }
    }
}
