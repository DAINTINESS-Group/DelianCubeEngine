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

public class AnalyzeMQOQueryCharacteristicsExperiment {
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
		
		String selectivityEffectWorkload[] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_state = 'NY' AND store_state = 'TN' GROUP BY customer_state,store_state AS 10%",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_state = 'TN' GROUP BY customer_state,store_state AS 50%",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' GROUP BY customer_state,store_state AS 90&"
		};
		
		String grouperLevelEffectWorkload[] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND store_country = 'United States' GROUP BY month,store_county AS Low-Low",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND store_country = 'United States' GROUP BY quarter,store_state AS Mid-Mid",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND store_country = 'United States' GROUP BY year,store_country AS High-High"
		};
		
		String numberOfFiltersEffectWorkload[] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND store_country = 'United States' GROUP BY quarter,store_state AS Two",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' GROUP BY quarter,store_state AS Three",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' AND time_of_day = 'morning' GROUP BY quarter,store_state AS Four",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' AND time_of_day = 'morning' AND category = 'Women' GROUP BY quarter,store_state AS Five"
		};
		
		
		// EXPERIMENT 1 - QUERY CHARACTERISTICS EFFECT ON QUERY EXEC TIME
		for(int i = 0;i<selectivityEffectWorkload.length;i++) {
			String incomingExpression = selectivityEffectWorkload[i];
			for(int j = 0;j < 5;j++) {
				ResultFileMetadata operatorResult = service.analyzeWithMaxMQO(incomingExpression);
				String localFolder = operatorResult.getLocalFolder();
				String resultFile = operatorResult.getResultFile();
				fetchData(localFolder,resultFile);
			}
		}
		
		
		for(int i = 0;i<grouperLevelEffectWorkload.length;i++) {
			String incomingExpression = grouperLevelEffectWorkload[i];
			for(int j = 0;j < 5;j++) {
				ResultFileMetadata operatorResult = service.analyzeWithMaxMQO(incomingExpression);
				String localFolder = operatorResult.getLocalFolder();
				String resultFile = operatorResult.getResultFile();
				fetchData(localFolder,resultFile);
			}
		}
		
		
		for(int i = 0;i<numberOfFiltersEffectWorkload.length;i++) {
			String incomingExpression = numberOfFiltersEffectWorkload[i];
			for(int j = 0;j < 5;j++) {
				ResultFileMetadata operatorResult = service.analyzeWithMaxMQO(incomingExpression);
				String localFolder = operatorResult.getLocalFolder();
				String resultFile = operatorResult.getResultFile();
				fetchData(localFolder,resultFile);
			}
		}
		
	}
	
}
