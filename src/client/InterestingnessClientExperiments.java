package client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mainengine.IMainEngine;

public class InterestingnessClientExperiments {
	// Host or IP of Server
		private static final String HOST = "localhost";
		private static final int PORT = 2020;
		private static Registry registry;

		public static void main(String[] args) throws Exception {
			// Search the registry in the specific Host, Port.
			registry = LocateRegistry.getRegistry(HOST, PORT);
			// LookUp for MainEngine on the registry
			IMainEngine service = (IMainEngine)registry.lookup(IMainEngine.class
					.getSimpleName());
			if(service == null) {
				System.out.println("Unable to commence server, exiting");
				System.exit(-100);
			}
						
			// Cube LOAN and queries
			service.initializeConnectionWithIntrMng("pkdd99_star", "CinecubesUser",
					"Cinecubes", "pkdd99_star","InputFiles/ServerRegisteredInfo/Interestingness/History", "InputFiles/UserProfile/ExpectedValues/predictions400", "InputFiles/UserProfile/ExpectedValues/predictions400" ,1,"loan");
			System.out.println("Completed connection initialization");
			
			//#### Create experiment results file ####
			try(FileWriter fw = new FileWriter("OutputFiles/Interestingness/Experiments/experiments200T.txt", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
				{
				    out.println("########################################\n"+
				    			"EXPERIMENT PARAMETERS\n"+
				    			"BASE_SIZE\t 200 THOUSAND ENTRIES\n"+
				    			"HISTORY_SIZE\t 1 QUERY\n"+
				    			"EXPECTED VALUES/LABELS\t 400 CELLS\n"+
				    			"########################################\n\n\n");
				} catch (IOException e) {}
			
			// ###### Create History ######
			List<String> measures = new ArrayList<String>();
			// History size = 1
			service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery11_S1_CG-Prtl\n" + 
					"AggrFunc:Avg\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + 
					"Sigma:account_dim.region='north Moravia'", measures);
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
			measures = new ArrayList<String>(Arrays.asList("Direct Novelty", 
					"Indirect Novelty", "Relevance with DAI", "Value Peculiarity", "Label Surprise", 
					"Label Surprise Strict", "Value Surprise"));
			
			String[] answers = service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
					"Name: LoanQuery91_S8_CG-Prtl\n" + 
					"AggrFunc:Min\n" + 
					"Measure:amount\n" + 
					"Gamma:account_dim.district_name,date_dim.month\n" + 
					"Sigma:account_dim.region='north Moravia'" , measures);
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			for(int i = 0; i < answers.length; i++) {
				System.out.println(measures.get(i) + ":    " + answers[i]);
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		}
}
