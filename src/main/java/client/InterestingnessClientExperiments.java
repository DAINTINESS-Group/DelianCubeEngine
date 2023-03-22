package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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

public class InterestingnessClientExperiments {
		// Host or IP of Server
		private static final String HOST = "localhost";
		private static final int PORT = 2020;
		private static Registry registry;
		
		//Clears history folders before running next set of experiments
		static void clearOldHistory() throws IOException {
			Files.walk(Paths.get("InputFiles/ServerRegisteredInfo/Interestingness/History/Queries"))
	        .filter(Files::isRegularFile)
	        .map(Path::toFile)
	        .forEach(File::delete);
			Files.walk(Paths.get("InputFiles/ServerRegisteredInfo/Interestingness/History/Results"))
	        .filter(Files::isRegularFile)
	        .map(Path::toFile)
	        .forEach(File::delete);
		}
		
		//Clears the experiment folder
		static void clearExperimentsOutput() throws IOException {
			Files.walk(Paths.get("OutputFiles\\Interestingness\\Experiments"))
	        .filter(Files::isRegularFile)
	        .map(Path::toFile)
	        .forEach(File::delete);
		}

		
		static void createGitignoreFiles() {
			try(FileWriter fw = new FileWriter("InputFiles/ServerRegisteredInfo/Interestingness/History/Queries/.gitignore", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw)){
				    	out.println("");
					} catch (IOException e) {}
			
			try(FileWriter fw = new FileWriter("InputFiles/ServerRegisteredInfo/Interestingness/History/Results/.gitignore", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw)){
				    	out.println("");
					} catch (IOException e) {}
		}

		//Creates history based on historySize parameter (1, 5 or 10)
		private static void createHistory(IMainEngine service, int historySize) throws RemoteException {
			// ###### Create History ######
			List<String> measures = new ArrayList<String>();
			// History size = 1
			
			switch (historySize) {
				case 0:
					break;
				case 1:
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery11_S1_CG-Prtl\n" + 
							"AggrFunc:Avg\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='north Moravia'", measures);
					break;
				case 2:
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery11_S1_CG-Prtl\n" + 
							"AggrFunc:Avg\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='north Moravia'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery12_S1_CG-Dsjnt\n" + 
							"AggrFunc:Max\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name, status_dim.status\n" + 
							"Sigma:date_dim.month='1998-01'", measures);
					break;
				case 5:
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery11_S1_CG-Prtl\n" + 
							"AggrFunc:Avg\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='north Moravia'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery12_S1_CG-Dsjnt\n" + 
							"AggrFunc:Max\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name, status_dim.status\n" + 
							"Sigma:date_dim.month='1998-01'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery21_S2_CG-Cmmn\n" + 
							"AggrFunc:Min\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='Prague',date_dim.year='1998'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery22_S2_CG-Prtl\n" + 
							"AggrFunc:Sum\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='south Moravia',status_dim.status='Running Contract/OK'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery31_S3_CG-Prtl\n" + 
							"AggrFunc:Sum\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='west Bohemia',status_dim.status='Contract Finished/No Problems', date_dim.year='1996'", measures);
					break;
				case 10:
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery11_S1_CG-Prtl\n" + 
							"AggrFunc:Avg\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='north Moravia'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery12_S1_CG-Dsjnt\n" + 
							"AggrFunc:Max\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name, status_dim.status\n" + 
							"Sigma:date_dim.month='1998-01'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery21_S2_CG-Cmmn\n" + 
							"AggrFunc:Min\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='Prague',date_dim.year='1998'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery22_S2_CG-Prtl\n" + 
							"AggrFunc:Sum\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='south Moravia',status_dim.status='Running Contract/OK'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery31_S3_CG-Prtl\n" + 
							"AggrFunc:Sum\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='west Bohemia',status_dim.status='Contract Finished/No Problems', date_dim.year='1996'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery41_S4_CG-Prtl\n" + 
							"AggrFunc:Min\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='west Bohemia',status_dim.status='Running Contract/Client in Debt'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery51_S5_CG-Dsjnt\n" + 
							"AggrFunc:Max\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name, status_dim.status\n" + 
							"Sigma:date_dim.month='1998-02'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery61_S6_CG-Prtl\n" + 
							"AggrFunc:Avg\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + 
							"Sigma:account_dim.region='south Bohemia'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery71_S7_CG-Prtl\n" + 
							"AggrFunc:Sum\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='Prague',status_dim.status='Contract Finished/Loan not Payed'", measures);
					service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
							"Name: LoanQuery81_S8_CG-Prtl\n" + 
							"AggrFunc:Min\n" + 
							"Measure:amount\n" + 
							"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
							"Sigma:account_dim.region='west Bohemia'", measures);
					break;

			}
/*	// History size = 2
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery12_S1_CG-Dsjnt\n" + 
					"AggrFunc:Max\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name, status_dim.status\n" + 
					"Sigma:date_dim.month='1998-01'", measures);
			// History size = 3
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery21_S2_CG-Cmmn\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + 
					"Sigma:account_dim.region='Prague',date_dim.year='1998'", measures);
			// History size = 4
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery22_S2_CG-Prtl\n" + 
					"AggrFunc:Sum\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
					"Sigma:account_dim.region='south Moravia',status_dim.status='Running Contract/OK'", measures);
			// History size = 5
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery31_S3_CG-Prtl\n" + 
					"AggrFunc:Sum\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
					"Sigma:account_dim.region='west Bohemia',status_dim.status='Contract Finished/No Problems', date_dim.year='1996'", measures);
					// History size = 6
				service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery41_S4_CG-Prtl\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
					"Sigma:account_dim.region='west Bohemia',status_dim.status='Running Contract/Client in Debt'", measures);
			// History size = 7
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery51_S5_CG-Dsjnt\n" + 
					"AggrFunc:Max\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name, status_dim.status\n" + 
					"Sigma:date_dim.month='1998-02'", measures);
			// History size = 8
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery61_S6_CG-Prtl\n" + 
					"AggrFunc:Avg\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + 
					"Sigma:account_dim.region='south Bohemia'", measures);
			// History size = 9
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery71_S7_CG-Prtl\n" + 
					"AggrFunc:Sum\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
					"Sigma:account_dim.region='Prague',status_dim.status='Contract Finished/Loan not Payed'", measures);
			// History size = 10
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery81_S8_CG-Prtl\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + //"Gamma:account_dim.district_name,date_dim.year\n" + 
					"Sigma:account_dim.region='west Bohemia'", measures); */
			// ###### end of History creation ######
		}

		//Initializes the connection to the db
		private static IMainEngine initializeConnection(String schemaName, String username, String password, String cubeName, String inputFolder)
				throws RemoteException, NotBoundException, AccessException, IOException {
			registry = LocateRegistry.getRegistry(HOST, PORT);
			// LookUp for MainEngine on the registry
			IMainEngine service = (IMainEngine)registry.lookup(IMainEngine.class
					.getSimpleName());
			if(service == null) {
				System.out.println("Unable to commence server, exiting");
				System.exit(-100);
			}
			
			//Clear history files
			clearOldHistory();
			
			//Clear old experiments files
			//clearExperimentsOutput();
			
			// Cube LOAN and queries
			String typeOfConnection = "RDBMS";
			HashMap<String, String>userInputList = new HashMap<>();
			userInputList.put("schemaName", schemaName);
			userInputList.put("username", username);
			userInputList.put("password", password);
			userInputList.put("cubeName", cubeName);
			userInputList.put("inputFolder", inputFolder);
			service.initializeConnectionWithIntrMng(typeOfConnection, userInputList,
					"InputFiles/ServerRegisteredInfo/Interestingness/History", "InputFiles/UserProfile/ExpectedValues/predictions400", 
					"InputFiles/UserProfile/ExpectedValues/predictions400","InputFiles/UserProfile/ExpectedValues/predictionsWithProbabilities400", 1);
			System.out.println("Completed connection initialization");
			return service;
		}

		//Runs all experiments, using the service connection. experimentText is a String to write on the experiments200T.txt to identify each experiment
		private static void runExperiment(IMainEngine service, String experimentText)
				throws RemoteException, FileNotFoundException, IOException {
			
			//#### Create experiment results file #### OR keep writing for next experiments
			try(FileWriter fw = new FileWriter("OutputFiles/Interestingness/Experiments/experiments200T.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
				out.println(experimentText);
			} catch (IOException e) {}
			
			List<String> measures;
			String q1 = "CubeName:loan\n" + 
					"Name: LoanQuery91_S8_CG-Prtl\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + 
					"Sigma:account_dim.region='north Moravia',date_dim.year='1998'";
			
			//~10 tuples query for 1M
			String q2 = "CubeName:loan\n" +
					"Name: LoanQuery31_S3_CG-Prtl\n" +
					"AggrFunc:Sum\n" +
					"Measure:amount\n" +
					"Gamma:account_dim.district_name,date_dim.year\n" +
					"Sigma:account_dim.region='west Bohemia',status_dim.status='Contract Finished/No Problems', date_dim.year = '1996'";
			
			//~80 tuples query for 1M
			String q3 = "CubeName:loan\n" +
					"Name: LoanQuery22_S2_CG-Prtl\n" +
					"AggrFunc:Sum\n" +
					"Measure:amount\n" +
					"Gamma:account_dim.district_name,date_dim.year\n" +
					"Sigma:account_dim.region='south Moravia',status_dim.status='Running Contract/OK'";
			
			//~800 tuples query for 1M
			String q4= "CubeName:loan\n" +
					"Name: LoanQuery11_S1_CG-Prtl\n" +
					"AggrFunc:Avg\n" +
					"Measure:amount\n" +
					"Gamma:account_dim.district_name,date_dim.month\n" +
					"Sigma:account_dim.region='north Moravia'";
			
			measures = new ArrayList<String>(Arrays.asList(/*"Direct Novelty", 
					"Indirect Novelty", "Partial Detailed Extensional Relevance", "Value Peculiarity", "Label Surprise", 
					"Label Surprise Strict", "Partial Detailed Extensional Jaccard Based Peculiarity","Partial Detailed Extensional Novelty", "Partial Syntactic Average Peculiarity",
					*/"Partial Detailed Extensional Belief Based Novelty"/*, "Partial Same Level Extensional Relevance", "Partial Extensional Value Based Surprise"*/));

			//theloyme result size experiments gia tous goal based kai value surprise
			
			String[] answers = service.answerCubeQueryWithInterestMeasures(q1 , measures);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			for(int i = 0; i < answers.length; i++) {
				System.out.println(measures.get(i) + ":    " + answers[i]);
			}
			
			//Experiment for FamilyBasedRelevance bellow
			/*String q2 = "CubeName:loan\n" + 
					"Name: LoanQuery91_S8_CG-Prtl\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + 
					"Sigma:account_dim.region='central Bohemia'";*/
			/*String q3 = "CubeName:loan\n" + 
					"Name: LoanQuery21_S2_CG-Cmmn\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:date_dim.month,account_dim.district_name\n" + 
					"Sigma:account_dim.region='Prague',date_dim.year = '1997'";*/

			//measures.clear();
			//measures.add("FamilyBasedRelevance");
			//answers = service.answerCubeQueryWithInterestMeasures(q1, q2 , measures);
			//System.out.println(measures.get(0) + ":    " + answers[0]);
			
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			//Delete history files created to be ready for next set of experiments
			clearOldHistory();
			
		}
			
		public static void main(String[] args) throws Exception {
			clearExperimentsOutput();
			//Change history size to 1, 5 or 10 for each experiment
			int historySize = 0;
			//Change String database to each db name
			String database = "";
			//A counter to help identify the experiments in the txt file
			int experimentCounter = 0;
			
			/*
			 * 
			 * Experiments for 100K db
			 * 
			 */
			/*
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 1;
				database = "pkdd99_star_100K";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 5;
				database = "pkdd99_star_100K";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize=10;
				database = "pkdd99_star_100K";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			*/
			/*
			 * 
			 * Experiments for 1M db
			 * 
			 */
			/*
			//the below for loop is for the result size experiment
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 0;
				database = "pkdd99_star_1M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			*/
			
			experimentCounter++;
			/*
			for(int i=0; i<5; i++) {
				historySize = 1;
				database = "pkdd99_star_1M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			for(int i=0; i<5; i++) {
				historySize = 2;
				database = "pkdd99_star_1M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 5;
				database = "pkdd99_star_1M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}*/
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize=10;
				database = "pkdd99_star_1M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			

			/*
			 * 
			 * Experiments for 10M db
			 * 
			 */
		
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 1;
				database = "pkdd99_star_10M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 5;
				database = "pkdd99_star_10M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize=10;
				database = "pkdd99_star_10M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
		
			
			/*
			 * 
			 * Experiments for 100M db
			 * 
			 */
			/*
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 1;
				database = "pkdd99_star_100M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize = 5;
				database = "pkdd99_star_100M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			experimentCounter++;
			for(int i=0; i<5; i++) {
				historySize=10;
				database = "pkdd99_star_100M";
				IMainEngine service = initializeConnection(database,"CinecubesUser","Cinecubes","loan",database);
				createHistory(service,historySize);
				runExperiment(service,"\n\n########################################\n"+
	    				"EXPERIMENT:\t"+experimentCounter+"\n"+
						"LOOP NUMBER:\t " + i +" \n"+
		    			"EXPERIMENT PARAMETERS\n"+
		    			"DATABASE:\t" + database + "\n"+
		    			"HISTORY_SIZE:\t"+historySize+"\n"+
		    			"########################################\n\n\n");
			}
			
			 */
			//Create .gitignore files
			createGitignoreFiles();
			
		}
	
}
