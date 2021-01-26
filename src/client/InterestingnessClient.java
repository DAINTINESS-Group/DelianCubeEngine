package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mainengine.IMainEngine;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class InterestingnessClient {
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
		service.initializeConnectionWithIntrMng("pkdd99", "CinecubesUser",
				"Cinecubes", "pkdd99","InputFiles/ServerRegisteredInfo/Interestingness/History", "InputFiles/UserProfile/ExpectedValues/predictions1", "InputFiles/UserProfile/ExpectedValues/predictions1" ,1,"loan");
		System.out.println("Completed connection initialization");
		
		//create history to be able to compute every measure
		List<String> measures = new ArrayList<String>();
		
		service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery11_S1_CG-Prtl\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='north Moravia'", measures);
		service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery31_S3_CG-Prtl\n" + 
				"AggrFunc:Sum\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl3\n" + 
				"Sigma:account_dim.lvl2='west Bohemia',status_dim.lvl0='Contract Finished/No Problems', date_dim.lvl3 = '1996'", measures);
		
		
		//compute all measures
		
		measures = new ArrayList<String>(Arrays.asList("Direct Novelty", 
				"Indirect Novelty", "Relevance with DAI", "Value Peculiarity", "Label Surprise", 
				"Label Surprise Strict", "Value Surprise"));
		// New query with predictions available
		String[] answers = service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1998'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// Partly new query
		String[] answers2 = service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl3\n" + 
				"Sigma:account_dim.lvl2='Prague'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers2.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers2[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		// Old query
		String[] answers3 = service.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery31_S3_CG-Prtl\n" + 
				"AggrFunc:Sum\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl3\n" + 
				"Sigma:account_dim.lvl2='west Bohemia',status_dim.lvl0='Contract Finished/No Problems', date_dim.lvl3='1996'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers3.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers3[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}
