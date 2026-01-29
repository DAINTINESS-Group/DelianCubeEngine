package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import mainengine.IMainEngine;
import result.ResultFileMetadata;

/**
 * Class that implements a simple client that executes Describe intentional queries
 * @author Nik-Pt
 *
 */
public class DescribeOperatorClient {

	private static final String serverIP = "localhost";
	private static final int serverPort = 2020;
	private static Registry registry;
	
	public static void main(String[] args) throws Exception{
		registry = LocateRegistry.getRegistry(serverIP, serverPort);
		
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class.getSimpleName());
		if(service == null) {
			System.err.println("Server not found. Exiting...");
			System.exit(-100);
		}
		
		String typeOfConnection = "RDBMS";
		HashMap<String, String> userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star");
		
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Connection is successful.");
		
		String describeExpression = "WITH loan DESCRIBE (SUM(amount) - SUM(payments)) AS Test FOR region IN {'Prague', 'Brno'} AND year >= '1997' GROUP BY district_name, month USING KPIMedianBased";
		System.out.println("Sending Query: " + describeExpression);
		
		ResultFileMetadata resultMetadata = service.describe(describeExpression);
		if (resultMetadata != null) {
			String localFolder = resultMetadata.getLocalFolder();
			String resultFile = resultMetadata.getResultFile();
			System.out.println("\n--- Result ---");
			fetchData(localFolder, resultFile);
		}else {
			System.err.println("Execution failed or returned null metadata.");
		}
	}	
	
	public static void fetchData(String localFolder, String resultFile) throws IOException {
		String str;
		File reportFile = new File(localFolder + resultFile);
		
		if (reportFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(reportFile));
			while((str = br.readLine()) != null) {
				System.out.println(str);
			}
			br.close();
		} else {
			System.out.println("File not found: " + reportFile.getAbsolutePath());
		}
	}

}
