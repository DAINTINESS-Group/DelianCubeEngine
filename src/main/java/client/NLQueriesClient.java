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
 * A simple client used for issuing natural language queries against a specific cubebase and a specific cube
 * 
 * @author DimosGkitsakis
 *
 */
public class NLQueriesClient {
	// Host or IP of Server
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
		
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99_star");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99_star");
		service.initializeConnection(typeOfConnection, userInputList);
		System.out.println("Completed connection initialization");

		//CleanUp client Cache
		File resultFolder = new File("ClientCache");
		deleteAllFilesOfFolder(resultFolder);
		
		String nlqueries[] = {"Describe the avg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery11_S1_CG-Prtl",
				"Describe the max of loan amount per district_name and status for month is '1998-01' as LoanQuery12_S1_CG-Dsjnt",
				"Describe the min of loan amount per district_name and month for region is 'Prague' and year is '1998' as LoanQuery21_S2_CG-Cmmn",
				"Describe the sum of loan amount per district_name and year for region is 'south Moravia' and status is 'Running Contract/OK' as LoanQuery22_S2_CG-Prtl",
				"Describe the sum of loan amount per district_name and year for region is 'west Bohemia' and status is 'Contract Finished/No Problems' and year is '1996' as LoanQuery31_S3_CG-Prtl"
		};
		
		String queryNames[] = {"LoanQuery11_S1_CG-Prtl", "LoanQuery12_S1_CG-Dsjnt", 
		"LoanQuery21_S2_CG-Cmmn", "LoanQuery22_S2_CG-Prtl", "LoanQuery31_S3_CG-Prtl"};
		
		for(int i=0; i<nlqueries.length; i++) {
			ResultFileMetadata errors = service.prepareCubeQuery(nlqueries[i]);
			String path = bringErrorDataToTheClient(service, "ErrorChecking", errors);
			String contents = getContents(path);
			String[] splitContents = contents.split(";\n"); 
			if(splitContents[1].equals("true")) {
				continue;
			}
			
			ResultFileMetadata resMetadata = service.answerCubeQueryFromNLStringWithMetadata(nlqueries[i]);
			String queryName = queryNames[i];
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
		String remoteErrorFile = resMetadata.getErrorCheckingStatus();
		
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
