package client.analyzemqoexperiments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mainengine.IMainEngine;
import result.ResultFileMetadata;

public class AnalyzeMQOPerformanceExperiment {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static Registry registry;
		
	// Method that reads the report file and prints it to the console line by line
	public static void fetchData(String localFolder,String resultFile) throws IOException {
		String str;
		File reportFile = new File(localFolder + resultFile);
		
		BufferedReader br = new BufferedReader(new FileReader(reportFile));
		while((str = br.readLine()) != null) {
			System.out.println(str);
		}
		br.close();
	}
	
	public static void main(String[] args) throws Exception {
		registry = LocateRegistry.getRegistry(HOST,PORT);
		
		// connection to server
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());
		if(service == null) {
			System.err.println("Server not found.Exiting...");
			System.exit(-100);
		}
		
		// connection to datasets
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "tpc_ds_cube_100M");
		userInputList.put("username", "CinecubesUser"); 
		userInputList.put("password", "Cinecubes"); 
		userInputList.put("cubeName", "store_sales");
		userInputList.put("inputFolder", "tpc_ds_cube_100M");
		
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Connection is successful.");
		
		String queryWorkload[] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' AND time_of_day = 'morning' AND category = 'Women' GROUP BY customer_state,store_state AS Q1",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' GROUP BY customer_country,store_country AS Q2",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' GROUP BY customer_state,store_state AS Q3",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR store_country = 'United States' AND year = '1999' GROUP BY store_country,year AS Q4",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR store_country = 'United States' AND year = '1999' GROUP BY store_state,quarter AS Q5",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR store_country = 'United States' AND year = '1999' GROUP BY store_state,month AS Q6",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR store_country = 'United States' AND year = '1999' GROUP BY store_county,month AS Q7",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_state = 'TN' GROUP BY customer_country,store_country AS Q8",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_state = 'TN' GROUP BY customer_state,store_state AS Q9",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_state = 'TN' GROUP BY customer_state,store_county AS Q10",
		};
		
		// EXPERIMENT 2 - QUERY WORKLOAD PERFORMANCE EVALUATION */
		for(int i = 0;i<queryWorkload.length;i++) {
			String incomingExpression = queryWorkload[i];
			for(int j = 0;j < 5;j++) {
				ResultFileMetadata operatorResult = service.analyzeWithMidMQO(incomingExpression);
				String localFolder = operatorResult.getLocalFolder();
				String resultFile = operatorResult.getResultFile();
				fetchData(localFolder,resultFile);
			}
		}
	}
	
}
