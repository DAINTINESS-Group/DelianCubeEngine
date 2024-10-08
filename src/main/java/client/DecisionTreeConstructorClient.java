package client;

import java.io.File;
import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mainengine.IMainEngine;
import model.decisiontree.labeling.LabelingSystemConstants;
import model.decisiontree.labeling.Rule;
import model.decisiontree.labeling.RuleSet;
import result.ResultFileMetadata;

public class DecisionTreeConstructorClient {

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
			
			//Decision Trees for queries on pkdd99_star database
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
			
			
			List<Rule> labelingRulesForPkdd99_star =
		                new ArrayList<>(
		                    Arrays.asList(
		                        new Rule("measure", LabelingSystemConstants.LEQ, 40000, "low"),
		                        new Rule("measure", LabelingSystemConstants.LEQ, 60000, "rel_low"),
		                        new Rule("measure", LabelingSystemConstants.LEQ, 100000, "medium"),
		                        new Rule("measure", LabelingSystemConstants.LEQ, 200000, "high"),
		                        new Rule("measure", LabelingSystemConstants.GT, 200000, "super_high")));
	        RuleSet ruleSet = new RuleSet("measure", labelingRulesForPkdd99_star);
	        
	        ResultFileMetadata resMetadata = service.answerCubeQueryFromStringWithMetadata("CubeName:loan \n" + 
					"Name:LoanQuery1\n" + 
					"AggrFunc:Avg\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name, date_dim.month\n" + 
					"Sigma:account_dim.region='north Moravia'");
	        
	        String queryName = "LoanQuery1";
	        bringDataToTheClient(service, queryName, resMetadata);
	        String path = "ClientCache" + File.separator + queryName + ".tab";
	        service.produceDecisionTree(queryName, path, ruleSet);
	        
	        
	        resMetadata = service.answerCubeQueryFromStringWithMetadata("CubeName:loan \n" + 
					"Name:LoanQuery2\n" + 
					"AggrFunc:Avg\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name, status_dim.status\n" + 
					"Sigma:date_dim.month='1998-01'");
	        
	        queryName = "LoanQuery2";
	        bringDataToTheClient(service, queryName, resMetadata);
	        path = "ClientCache" + File.separator + queryName + ".tab";
	        service.produceDecisionTree(queryName, path, ruleSet);
	        
		        
		      
	        //Decision Trees for queries on Adult database
	        userInputList.put("schemaName", "adult_no_dublic");
			userInputList.put("username", "CinecubesUser");
			userInputList.put("password", "Cinecubes");
			userInputList.put("cubeName", "adult");
			userInputList.put("inputFolder", "adult");
			service.initializeConnection(typeOfConnection, userInputList);
			System.out.println("Completed connection initialization");
			
	        List<Rule> labelingRulesForAdult =
	                new ArrayList<>(
	                    Arrays.asList(
	                        new Rule("measure", LabelingSystemConstants.LEQ, 25, "low"),
	                        new Rule("measure", LabelingSystemConstants.LEQ, 30, "rel_low"),
	                        new Rule("measure", LabelingSystemConstants.LEQ, 40, "medium"),
	                        new Rule("measure", LabelingSystemConstants.LEQ, 50, "high"),
	                        new Rule("measure", LabelingSystemConstants.GT, 60, "super_high")));
	        ruleSet = new RuleSet("measure", labelingRulesForAdult);
	        
	        resMetadata = service.answerCubeQueryFromStringWithMetadata("CubeName:adult\n" + 
					"Name:AdultCubeQuery1\n" + 
					"AggrFunc:Avg\n" + 
					"Measure:hours_per_week\n" + 
					"Gamma:native_country_dim.lvl2,race_dim.lvl1\n" + 
					"Sigma:work_dim.lvl2='With-Pay', education_dim.lvl3='Without-Post-Secondary'");
	        
	        queryName = "AdultCubeQuery1";
	        bringDataToTheClient(service, queryName, resMetadata);
	        path = "ClientCache" + File.separator + queryName + ".tab";
	        service.produceDecisionTree(queryName, path, ruleSet);
	        
	        resMetadata = service.answerCubeQueryFromStringWithMetadata("CubeName:adult\n" + 
					"Name:AdultCubeQuery2\n" + 
					"AggrFunc:Avg\n" + 
					"Measure:hours_per_week\n" + 
					"Gamma:occupation_dim.lvl1,age_dim.lvl1\n" + 
					"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl2='Some-college'");
	       
	        queryName = "AdultCubeQuery2";
	        bringDataToTheClient(service, queryName, resMetadata);
	        path = "ClientCache" + File.separator + queryName + ".tab";
	        service.produceDecisionTree(queryName, path, ruleSet);
	        
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
		}//end method

}
