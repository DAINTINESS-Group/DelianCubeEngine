package client.analyzemqoexperiments;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;

import mainengine.IMainEngine;
import result.ResultFileMetadata;

public class AnalyzeMQOptimizerExperiments {
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static Registry registry;
	
	private static String[] loadStressTestQueries()
	{
		String queryWorkload [] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_state = 'NY' AND store_state = 'TN' GROUP BY customer_state,store_state AS 10%",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_state = 'TN' GROUP BY customer_state,store_state AS 50%",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' GROUP BY customer_state,store_state AS 90&"
		};
		return queryWorkload;
	}
	
	
	private static String[] loadPkddQueries() {
		String queryWorkload [] = {
				"ANALYZE sum(amount) FROM loan FOR district_name = 'Hl.m. Praha' AND day = '1995-01-05' GROUP BY district_name,day AS Q1",
				"ANALYZE sum(amount) FROM loan FOR district_name = 'Jihlava' AND month = '1994-06' GROUP BY district_name,month AS Q2",
				"ANALYZE sum(amount) FROM loan FOR district_name = 'Hl.m. Praha' AND month = '1997-05' GROUP BY district_name,month AS Q3",
				"ANALYZE sum(amount) FROM loan FOR district_name = 'Karvina' AND year = '1994' GROUP BY district_name,year AS Q4",
				"ANALYZE sum(amount) FROM loan FOR district_name = 'Hl.m. Praha' AND year = '1998' GROUP BY district_name,year AS Q5",
				"ANALYZE sum(amount) FROM loan FOR region = 'north Bohemia' AND year = '1994' GROUP BY district_name,year AS Q6",
				"ANALYZE sum(amount) FROM loan FOR region = 'south Bohemia' AND year = '1997' GROUP BY district_name,year AS Q7",
				"ANALYZE sum(amount) FROM loan FOR region = 'south Moravia' AND year = '1996' GROUP BY region,year AS Q8",
				"ANALYZE sum(amount) FROM loan FOR region = 'north Moravia' AND year = '1993' GROUP BY region,year AS Q9",
				"ANALYZE sum(amount) FROM loan FOR region = 'north Moravia' AND year = '1997' GROUP BY region,year AS Q10"
		};
		return queryWorkload;
	}
	
	private static String[] loadTPCTimeQueries() {
		String queryWorkload [] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '1999Q2' AND time_of_day = 'afternoon' GROUP BY quarter,time_of_day AS Q1",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '1999Q2' AND time_of_day = 'afternoon' GROUP BY quarter,time_of_day AS Q2",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR month = '12-2001' AND time_of_day = 'afternoon' GROUP BY month,time_of_day AS Q3",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '1998Q3' AND time_of_day = 'afternoon' GROUP BY quarter,time_of_day AS Q4",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '1999Q4' AND time_of_day = 'afternoon' GROUP BY quarter,time_of_day AS Q5",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '1998Q4' AND time_of_day = 'afternoon' GROUP BY quarter,time_of_day AS Q6",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '2001' AND time_of_day = 'morning' GROUP BY year,time_of_day AS Q7",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND time_of_day = 'morning' GROUP BY year,time_of_day AS Q8",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND time_of_day = 'afternoon' GROUP BY year,time_of_day AS Q9",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '2000' AND time_of_day = 'afternoon' GROUP BY year,time_of_day AS Q10"
		};
		return queryWorkload;
	}
	
	private static String[] loadTPCItemQueries() {
		String queryWorkload [] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR month = '12-1999' AND product_name = 'eingeingn stcally' GROUP BY month,product_name AS Q1",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '2000Q4' AND product_name = 'eseoughtablecallyought' GROUP BY quarter,product_name AS Q2",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1998' AND product_name = 'callybarcallyought' GROUP BY year,product_name AS Q3",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR month = '8-2002' AND category = 'Men' GROUP BY month,category AS Q4",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR month = '12-1999' AND category = 'Music' GROUP BY month,category AS Q5",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '2001Q3' AND category = 'Jewelry' GROUP BY quarter,category AS Q6",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '2000Q4' AND category = 'Children' GROUP BY quarter,category AS Q7",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR quarter = '2000Q4' AND category = 'Shoes' GROUP BY quarter,category AS Q8",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '2002' AND category = 'Jewelry' GROUP BY year,category AS Q9",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1998' AND category = 'Music' GROUP BY year,category AS Q10"
		};
		return queryWorkload;
	}
	
	private static String[] loadNumOfGroupersWorkload() {
		String queryWorkload[] = {
				"ANALYZE sum(ss_quantity) FROM store_sales FOR year = '1999' AND store_country = 'United States' GROUP BY quarter,store_state AS Two",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' GROUP BY customer_state,store_state,quarter AS Three",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' AND time_of_day = 'morning' GROUP BY customer_state,store_state,quarter,hour AS Four",
				"ANALYZE sum(ss_quantity) FROM store_sales FOR customer_country = 'United States' AND store_country = 'United States' AND year = '1999' AND time_of_day = 'morning' AND category = 'Women' GROUP BY customer_state,store_state,quarter,hour,item AS Five"
		};
		return queryWorkload;
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
		userInputList.put("schemaName", "tpc_ds_cube_100m");
		userInputList.put("username", "CinecubesUser"); 
		userInputList.put("password", "Cinecubes"); 
		userInputList.put("cubeName", "store_sales");
		userInputList.put("inputFolder", "tpc_ds_100m");
		
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Connection is successful.");
		
		String queryWorkload [] = loadStressTestQueries();
		
		for(int i = 0;i<queryWorkload.length;i++) {
			String incomingExpression = queryWorkload[i];
			for(int j = 0;j < 1;j++) {
				ResultFileMetadata operatorResult = service.analyzeWithMidMQO(incomingExpression);
			}
		}
		
		System.out.println("Experiment has been concluded!");
	}

}
