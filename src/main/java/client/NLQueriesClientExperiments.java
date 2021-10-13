package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import mainengine.IMainEngine;
import mainengine.ResultFileMetadata;

public class NLQueriesClientExperiments {
	
	//Host or IP of Server
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static Registry registry;

	public static void main(String[] args) throws Exception {
		// Search the registry in the specific Host, Port.
		registry = LocateRegistry.getRegistry(HOST, PORT);
		// LookUp for MainEngine on the registry
		IMainEngine service = (IMainEngine) registry.lookup(IMainEngine.class
				.getSimpleName());
		if(service == null) {
			System.out.println("Unable to commence server, exiting");
			System.exit(-100);
		}
		
		
		service.initializeConnection("pkdd99_star", "CinecubesUser",
				"Cinecubes", "pkdd99_star", "loan");
		System.out.println("Completed connection initialization for loan");
		
		
		//CleanUp client Cache
		File resultFolder = new File("ClientCache");
		deleteAllFilesOfFolder(resultFolder);
		
		String nlqueriesForLoan[] = {"Describe the max of loan amount per account_dim.district_name and status_dim.status for date_dim.month is '1998-01' as LoanQuery1",
				"Describe the sum of loan amount per account_dim.district_name and date_dim.year for account_dim.region is 'south Moravia' and status_dim.status is 'Running Contract/OK' as LoanQuery2",
				"Describe the sum of loan amount per account_dim.district_name and date_dim.year for account_dim.region is 'north Moravia' and status_dim.status is 'Running Contract/OK' and date_dim.year is '1997' as LoanQuery3",
		};
		
		String queryNamesForLoan[] = {"LoanQuery1", "LoanQuery2", "LoanQuery3"};
		
		String nlqueriesForOrders[] = {"Describe the avg of orders amount per account_dim.region and reason_dim.reason for account_dim.district_name is 'Benesov' as OrdersQuery1",
				"Describe the avg of orders amount per district_name and reason for account_dim.region is 'south Moravia' and reason_dim.reason is 'Leasing' as OrdersQuery2"
		};
		String queryNamesForOrders[] = {"OrdersQuery1", "OrdersQuery2"};
		
		String nlqueriesForAdult[] = {"Describe the min of adult hours_per_week per occupation_dim.lvl0 and education_dim.lvl2 for occupation_dim.lvl1 is 'Blue-collar' as AdultNLQuery1",
				"Describe the avg of adult hours_per_week per occupation_dim.lvl0 and marital_dim.lvl0 for occupation_dim.lvl1 is 'Other' and marital_dim.lvl1 is 'Partner-absent' as AdultNLQuery2",
				"Describe the avg of adult hours_per_week per education_dim.lvl2 and work_dim.lvl1 for education_dim.lvl3 is 'Post-Secondary' and work_dim.lvl2 is 'With-Pay' and age_dim.lvl3 is '37-56' as AdultNLQuery3",
				"Describe the avg of adult hours_per_week per native_country_dim.lvl0 and work_dim.lvl1 for native_country_dim.lvl1 is 'USA' and education_dim.lvl3 is 'Post-Secondary' and work_dim.lvl2 is 'With-Pay' and occupation_dim.lvl1 is 'Blue-collar' as AdultNLQuery4"
		};
		
		String queryNamesForAdult[] = {"AdultNLQuery1", "AdultNLQuery2", "AdultNLQuery3", "AdultNLQuery4"};
		
		
		//1st Experiment: using loan cube queries.
		//1,2,3,4 atomic filters in each nlquery respectively.
		for(int i=0; i<nlqueriesForLoan.length; i++) {
			ResultFileMetadata errors = service.prepareCubeQuery(nlqueriesForLoan[i]);
			String path = bringErrorDataToTheClient(service, "ErrorChecking", errors);
			String contents = getContents(path);
			String[] splitContents = contents.split(";\n"); 
			if(splitContents[1].equals("true")) {
				continue;
			}
			
			ResultFileMetadata resMetadata = service.answerCubeQueryFromNLStringWithMetadata(nlqueriesForLoan[i]);
			String queryName = queryNamesForLoan[i];
			bringDataToTheClient(service, queryName, resMetadata);
		}

		
		service.initializeConnection("pkdd99_star", "CinecubesUser",
				"Cinecubes", "pkdd99_star", "orders");
		System.out.println("Completed connection initialization for orders");
		
		for(int i=0; i<nlqueriesForOrders.length; i++) {
			ResultFileMetadata errors = service.prepareCubeQuery(nlqueriesForOrders[i]);
			String path = bringErrorDataToTheClient(service, "ErrorChecking", errors);
			String contents = getContents(path);
			String[] splitContents = contents.split(";\n"); 
			if(splitContents[1].equals("true")) {
				continue;
			}
			
			ResultFileMetadata resMetadata = service.answerCubeQueryFromNLStringWithMetadata(nlqueriesForOrders[i]);
			String queryName = queryNamesForOrders[i];
			bringDataToTheClient(service, queryName, resMetadata);
		}
		
		
		service.initializeConnection("adult", "CinecubesUser",
				"Cinecubes", "adult", "adult");
		System.out.println("Completed connection initialization for adult");
		
		for(int i=0; i<nlqueriesForAdult.length; i++) {
			ResultFileMetadata errors = service.prepareCubeQuery(nlqueriesForAdult[i]);
			String path = bringErrorDataToTheClient(service, "ErrorChecking", errors);
			String contents = getContents(path);
			String[] splitContents = contents.split(";\n"); 
			if(splitContents[1].equals("true")) {
				continue;
			}
			
			ResultFileMetadata resMetadata = service.answerCubeQueryFromNLStringWithMetadata(nlqueriesForAdult[i]);
			String queryName = queryNamesForAdult[i];
			bringDataToTheClient(service, queryName, resMetadata);
		}
		
				
	}
	
	/**
	 * Brings the data from the server to the client at the folder ClientCache
	 * 
	 * @param service   The IMainEngine where the server is
	 * @param queryName  The String with the query name that was executed
	 * @param resMetadata  The metadata of the executed query, containing the file names of the queries
	 * 
	 * @throws IOException
	 */
	private static void bringDataToTheClient(IMainEngine service, String queryName, ResultFileMetadata resMetadata)
			throws IOException {
		String remoteResultsFile = resMetadata.getResultFile();
		String remoteInfoFile = resMetadata.getResultInfoFile();
		
		System.out.println("\nRES\t" + remoteResultsFile + "\nINFO\t" + remoteInfoFile);
		
		String localFolder = "ClientCache" + File.separator;
		File remoteRes = new File(remoteResultsFile);
		ClientRMITransferer.download(service, remoteRes, new File( localFolder + queryName + ".tab"));
		File remoteIRes = new File(remoteInfoFile);
		ClientRMITransferer.download(service, remoteIRes, new File(localFolder + queryName + "_Info.txt"));
	}
	
	/**
	 * Brings the data of error checking process from the server to the client at the folder ClientCache
	 * 
	 * @param service   The IMainEngine where the server is
	 * @param queryName  The String with the query name that was executed
	 * @param resMetadata  The metadata of the executed query, containing the file names of the queries
	 * 
	 * @throws IOException
	 */
	private static String bringErrorDataToTheClient(IMainEngine service, String name, ResultFileMetadata resMetadata)
			throws IOException {
		String remoteErrorFile = resMetadata.getErrorCheckingFile();
		
		System.out.println("\nError RES\t" + remoteErrorFile);
		
		String localFolder = "ClientCache" + File.separator;
		File remoteRes = new File(remoteErrorFile);
		ClientRMITransferer.download(service, remoteRes, new File( localFolder + name + ".txt"));
		return remoteRes.getPath();

	}
	
	private static String getContents(String fileName) {
		String contents = "";
		File file = new File(fileName);
		if(file.exists() && !file.isDirectory()) { 
			BufferedReader reader = null;
			try {
			    reader = new BufferedReader(new FileReader(file));

			    String line;
			    while ((line = reader.readLine()) != null) {
			        contents = contents + line + "\n";
			    }

			} catch (IOException e) {
			    e.printStackTrace();
			} finally {
			    try {
			        reader.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}//end finally
		}//end master if
		return contents;
	}//end method
	
	/**
	 * Deletes all files from a folder
	 * @param dir the folder to be cleaned up
	 * @return the number of files removed
	 */
	public static int deleteAllFilesOfFolder(File dir) {
		if(!dir.isDirectory())
			return -1;
		int i = 0;
		for(File file: dir.listFiles()) { 
		    if (!file.isDirectory() && !file.getName().equals("README.txt")) {
		        file.delete();
		        i++;
		    }
		}
		return i;
	}

	
}
