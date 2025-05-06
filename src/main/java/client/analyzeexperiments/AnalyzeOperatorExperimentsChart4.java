package client.analyzeexperiments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import mainengine.IMainEngine;
import result.ResultFileMetadata;

public class AnalyzeOperatorExperimentsChart4 {
	// Host or IP of Server
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
		
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "foodmart_reduced");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "sales");
		userInputList.put("inputFolder", "foodmart_reduced");
		
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Connection is successful.");
		
		String foodmart_incomingExpression1 = "ANALYZE sum(store_sales) FROM sales FOR year='1997' AND country='USA' AND product_subcategory='Milk' GROUP BY month, state AS 1st_working_example";
		String foodmart_incomingExpression2 = "ANALYZE sum(store_sales) FROM sales FOR year='1997' AND product_category='Breakfast Foods' AND store_country='USA' GROUP BY quarter, product_subcategory AS 2nd_working_example";
		String foodmart_incomingExpression3 = "ANALYZE sum(store_sales) FROM sales FOR quarter='1997-Q3' AND state='CA' AND media='Daily Paper' GROUP BY month, region AS 3rd_working_example";

		String pkdd99_incomingExpression1 = "ANALYZE sum(amount) FROM loan FOR year='1997' AND region='Prague' AND status='Contract Finished/No Problems' GROUP BY month,district_name AS 1st_loan_query";
		String pkdd99_incomingExpression2 = "ANALYZE sum(amount) FROM loan FOR year='1996' AND region='north Moravia' AND status='Running Contract/OK' GROUP BY month,district_name AS 2nd_loan_query";
		String pkdd99_incomingExpression3 = "ANALYZE sum(amount) FROM loan FOR month='1994-11' AND region='south Bohemia' AND status='Contract Finished/No Problems' GROUP BY day,district_name AS 3rd_loan_query";
		
		//Chart4:
		for(int i=0; i<5; i++) {
			userInputList.put("schemaName", "foodmart_reduced");
			userInputList.put("cubeName", "sales");
			userInputList.put("inputFolder", "foodmart_reduced");
			service.initializeConnection(typeOfConnection, userInputList);
			System.out.println("Connection is successful.");
			
			ResultFileMetadata resultMetadata = service.analyzeUpdated(foodmart_incomingExpression3);
			String localFolder = resultMetadata.getLocalFolder();
			String resultFile = resultMetadata.getResultFile();
			//fetchData(localFolder,resultFile);
			resultMetadata = service.analyzeWithMultiQueryToSingleQueryOptimizer(foodmart_incomingExpression3);
			localFolder = resultMetadata.getLocalFolder();
			resultFile = resultMetadata.getResultFile();
			//fetchData(localFolder,resultFile);
		}
	
		for(int i=0; i<5; i++) {
			typeOfConnection = "Spark";
			userInputList.put("schemaName", "foodmart_reduced");
			userInputList.put("cubeName", "sales");
			userInputList.put("inputFolder", "foodmart_reduced");
			service.initializeConnection(typeOfConnection, userInputList);
			System.out.println("Connection is successful.");
			
			ResultFileMetadata resultMetadata = service.analyzeUpdated(foodmart_incomingExpression3);
			String localFolder = resultMetadata.getLocalFolder();
			String resultFile = resultMetadata.getResultFile();
			//fetchData(localFolder,resultFile);
			resultMetadata = service.analyzeWithMultiQueryToSingleQueryOptimizer(foodmart_incomingExpression3);
			localFolder = resultMetadata.getLocalFolder();
			resultFile = resultMetadata.getResultFile();
			//fetchData(localFolder,resultFile);
		}			
	}
}
