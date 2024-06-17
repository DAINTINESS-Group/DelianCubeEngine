package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import mainengine.IMainEngine;
import mainengine.ResultFileMetadata;

/**
 * Class that implements a simple client that executes Analyze intentional queries
 * @author mariosjkb
 *
 */
public class AnalyzeOperatorClient {
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
		
		// connection to dataset
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "foodmart_reduced");	//		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser"); //		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes"); //		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "sales");     //		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "foodmart_reduced");//		userInputList.put("inputFolder", "pkdd99_star");
		
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Connection is successful.");
		
		// Analyze intentional query execution
		String analyzeIntentionalExpression2 = "ANALYZE min(store_sales) FROM sales FOR year ='1997'  AND  store_country ='Mexico' GROUP BY year_quarter,store_country AS first_query";                          
		String analyzeIntentionalExpression = "ANALYZE min(amount) FROM loan FOR region ='Prague' AND year='1998' GROUP BY district_name,month AS first_query";
		for(int i = 0;i < 5;i++) {
			ResultFileMetadata resultMetadata = service.analyze(analyzeIntentionalExpression2);
			String localFolder = resultMetadata.getLocalFolder();
			String resultFile = resultMetadata.getResultFile();
			fetchData(localFolder,resultFile);
		}
	}

}
