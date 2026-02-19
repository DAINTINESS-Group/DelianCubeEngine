package client;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import mainengine.IMainEngine;

public class DescribePerformanceExperiment {

    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 2020;
    private static final int NUM_ITERATIONS = 10;

    private static final String CSV_FILE = "experiment_results.csv";

    public static void main(String[] args) {
        try {
            System.out.println("--- Connecting to Server (" + SERVER_IP + ":" + SERVER_PORT + ") ---");
            Registry registry = LocateRegistry.getRegistry(SERVER_IP, SERVER_PORT);
            IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());
            
            String typeOfConnection = "RDBMS";
            HashMap<String, String> userInputList = new HashMap<>();
            userInputList.put("schemaName", "pkdd99_star");
            userInputList.put("username", "CinecubesUser");
            userInputList.put("password", "Cinecubes");
            userInputList.put("cubeName", "loan");
            userInputList.put("inputFolder", "pkdd99_star"); 
            service.initializeConnection(typeOfConnection, userInputList);

            Map<String, String> testSuite = new LinkedHashMap<>();
            testSuite.put("Q1_Simple", "WITH loan DESCRIBE amount FOR year >= '1997'");
            testSuite.put("Q2_Selection", "WITH loan DESCRIBE amount FOR region IN {'Prague', 'north Bohemia'}");
            testSuite.put("Q3_Grouping", "WITH loan DESCRIBE SUM(amount) AS TotalAmount FOR region = 'Prague' GROUP BY district_name, month, status");
            testSuite.put("Q4_Complex_Model", "WITH loan DESCRIBE (SUM(amount) - SUM(payments)) AS Test FOR region IN {'Prague', 'north Bohemia'} AND year >= '1997' GROUP BY district_name, month USING KPIMedianBased");

            System.out.println("=== WARM-UP PHASE (1 Run - Ignored) ===");
            for (Map.Entry<String, String> entry : testSuite.entrySet()) {
                 try { service.describe(entry.getValue()); } catch (Exception e) {}
            }

            System.out.println("\n=== PERFORMANCE EXPERIMENT (" + NUM_ITERATIONS + " Iterations) ===");
            
            try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
                pw.println("Query ID,Avg Time (ms),Query String");
                
                System.out.printf("%-20s | %-15s | %s\n", "Query ID", "Avg Time (ms)", "Query String");
                System.out.println("------------------------------------------------------------------------------------------------");

                for (Map.Entry<String, String> entry : testSuite.entrySet()) {
                    String id = entry.getKey();
                    String query = entry.getValue();
                    
                    long totalTimeNs = 0;
                    boolean success = true;

                    for (int i = 0; i < NUM_ITERATIONS; i++) {
                        long start = System.nanoTime();
                        try {
                            service.describe(query);
                        } catch (Exception e) {
                            success = false; 
                            break; 
                        }
                        long end = System.nanoTime();
                        totalTimeNs += (end - start);
                    }

                    if (success) {
                        double avgTimeMs = (totalTimeNs / (double) NUM_ITERATIONS) / 1_000_000.0;
                        
                        String displayQuery = query.length() > 50 ? query.substring(0, 47) + "..." : query;
                        System.out.printf("%-20s | %-15.3f | %s\n", id, avgTimeMs, displayQuery);
                        pw.println(id + "," + String.format("%.3f", avgTimeMs).replace(",", ".") + ",\"" + query + "\"");
                    }
                }
            }
            
            System.out.println("\nExperiment Completed. Results saved to: " + CSV_FILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}