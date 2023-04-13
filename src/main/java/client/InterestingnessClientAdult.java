package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mainengine.IMainEngine;

 
public class InterestingnessClientAdult {
	// Host or IP of Server
	private static final String HOST = "localhost";
	private static final int PORT = 2020;
	private static Registry registry;
	
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
		
		//Clear history files
		clearOldHistory();
		
		// Cube ADULT and queries
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "adult_no_dublic");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "adult");
		userInputList.put("inputFolder", "adult");
		service.initializeConnectionWithIntrMng(typeOfConnection, userInputList,
				"InputFiles/ServerRegisteredInfo/Interestingness/History", "InputFiles/UserProfile/ExpectedValues/adultPredictions", 
				"InputFiles/UserProfile/ExpectedValues/adultPredictions","InputFiles/UserProfile/ExpectedValues/adultPredictionsWithProbabilities", 1);
		System.out.println("Completed connection initialization");
		
		
		
		//create history to be able to compute every measure
		List<String> measures = new ArrayList<String>();
		
		//compute all measures		
		measures = new ArrayList<String>(Arrays.asList( "Partial Detailed Extensional Novelty",
				"Partial Detailed Extensional Belief Based Novelty", "Partial Detailed Extensional Relevance",
				"Partial Same Level Extensional Relevance", "Partial Detailed Extensional Jaccard Based Peculiarity",
				"Partial Syntactic Average Peculiarity", "Partial Extensional Value Based Surprise"
				));
		

		// New query with predictions available
	/*	//
		String[] answers0 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery0\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl0 , education_dim.lvl2\n" + 
				"Sigma:occupation_dim.lvl1='Blue-collar',education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers0.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers0[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		String[] answers1 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery1\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl0 , marital_dim.lvl0\n" + 
				"Sigma:occupation_dim.lvl1='Other',marital_dim.lvl1='Partner-absent'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers1.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers1[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
*/
		
		
	/*	String[] answers2 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery2\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl0 , work_dim.lvl1\n" + 
				"Sigma:native_country_dim.lvl2='Europe',work_dim.lvl2='With-Pay'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers2.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers2[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");	*/
		
		String[] answers1 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery0\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2, work_dim.lvl2\n" + 
				"Sigma:occupation_dim.lvl1='Blue-collar',education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers1.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers1[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] answers2 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery2\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl3 , work_dim.lvl1\n" + 
				"Sigma:native_country_dim.lvl2='Europe',work_dim.lvl2='With-Pay'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers2.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers2[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	
	
		String[] answers3 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery3\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,work_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary',work_dim.lvl2='With-Pay',occupation_dim.lvl1='Blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers3.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers3[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			
		
		
		//syntactic Peculiarity wins the value Peculiarity
		String[] answers4 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery4\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,work_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary',work_dim.lvl2='With-Pay'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers4.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers4[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	
	
		String[] answers5 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery5\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,work_dim.lvl1\n" + 
				"Sigma:native_country_dim.lvl1='USA',education_dim.lvl3='Post-Secondary',work_dim.lvl2='With-Pay',age_dim.lvl3='37-56'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers5.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers5[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
				
		
		String[] answers6 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:CubeQuery6\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,work_dim.lvl1\n" + 
				"Sigma:occupation_dim.lvl1='Blue-collar',work_dim.lvl2='With-Pay',education_dim.lvl3='Post-Secondary',native_country_dim.lvl1='USA',marital_dim.lvl2='Married'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < answers6.length; i++) {
			System.out.println(measures.get(i) + ":    " + answers6[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
			
		//Delete history files created
		clearOldHistory();
		//Create .gitignore files
		createGitignoreFiles();
	}
}
