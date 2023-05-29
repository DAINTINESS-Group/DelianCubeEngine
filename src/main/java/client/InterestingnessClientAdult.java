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
				"InputFiles/ServerRegisteredInfo/Interestingness/History", "InputFiles/UserProfile/ExpectedValues/adultPredictions23", 
				"InputFiles/UserProfile/ExpectedValues/adultPredictions23","InputFiles/UserProfile/ExpectedValues/adultPredictionsWithProbabilities", 1);
		System.out.println("Completed connection initialization");
		
		
		
		//create history to be able to compute every measure
		List<String> measures = new ArrayList<String>();
		
		//compute all measures		
		measures = new ArrayList<String>(Arrays.asList( "Partial Detailed Extensional Novelty",
				/*"Partial Detailed Extensional Belief Based Novelty",*/ "Partial Detailed Extensional Relevance",
				/*"Partial Same Level Extensional Relevance",*/ "Partial Detailed Extensional Jaccard Based Peculiarity",
				/*"Partial Syntactic Average Peculiarity",*/ "Partial Extensional Value Based Surprise"
				));
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////
		//version 1
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

		
		
		String[] answers2 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
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
		
		/*
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
		*/
		
		/*
		///////////////////////////////////////////////////////////////////////////////////////
		//version 2
		String[] initial1 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery1\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='Greece'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial1.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial1[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] initial2 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery2\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='France'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial2.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial2[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//1st Dilemma
		String[] cube11 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube11\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl3 = 'Post-Secondary',native_country_dim.lvl0='France'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube11.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube11[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube12 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube12\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl3 = 'Without-Post-Secondary',native_country_dim.lvl0='France'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube12.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube12[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//2nd Dilemma
		String[] cube21 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube21\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl3 = 'Post-Secondary',occupation_dim.lvl1='Blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube21.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube21[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube22 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube22\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl3 = 'Without-Post-Secondary',occupation_dim.lvl1='Blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube22.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube22[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//3rd Dilemma
		String[] cube31 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube31\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl3 = 'Post-Secondary',occupation_dim.lvl1='White-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube31.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube31[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube32 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube32\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl3 = 'Without-Post-Secondary',occupation_dim.lvl1='White-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube32.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube32[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//4th Dilemma
		String[] cube41 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube41\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='France',occupation_dim.lvl1='White-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube41.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube41[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube42 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube42\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='France',occupation_dim.lvl1='Blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube42.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube42[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//5th Dilemma
		String[] cube51 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube51\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='Greece',occupation_dim.lvl1='White-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube51.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube51[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube52 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube52\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='Greece',occupation_dim.lvl1='Blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube52.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube52[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//6th Dilemma
		String[] cube61 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube61\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='Greece',education_dim.lvl2='Post-grad'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube61.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube61[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube62 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube62\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1,work_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',native_country_dim.lvl0='Greece',education_dim.lvl2='University'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube62.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube62[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		*/
		
		//////////////////////////////////////////////////////////////////////////////////////////
		//version 3-4
		//Session1
		String[] initial1 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery1\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial1.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial1[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		
		String[] initial2 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery2\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl1,occupation_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay', education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial2.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial2[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] initial3 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery3\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl1,age_dim.lvl2\n" + 
				"Sigma:work_dim.lvl2='With-Pay', education_dim.lvl2='Post-grad'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial3.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial3[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] initial4 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery4\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,age_dim.lvl3\n" + 
				"Sigma:work_dim.lvl2='With-Pay', occupation_dim.lvl1='white-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial4.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial4[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		//First slide

		String[] cube11 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube11\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Without-Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube11.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube11[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		String[] cube12 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube12\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl3,occupation_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',age_dim.lvl3='37-56'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube12.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube12[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		String[] cube13 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube13\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl1,occupation_dim.lvl0\n" + 
				"Sigma:education_dim.lvl2='Post-grad',work_dim.lvl2='With-pay',occupation_dim.lvl1='Other'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube13.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube13[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube14 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube14\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='Without-Pay'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube14.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube14[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

		String[] cube15 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube15\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Without-Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube15.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube15[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	
		//Second slide
		
		String[] cube21 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube21\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary', age_dim.lvl3='37-56'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube21.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube21[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube22 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube22\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl2,age_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary', age_dim.lvl2='37-46'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube22.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube22[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube23 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube23\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl2,age_dim.lvl3\n" + 
				"Sigma:occupation_dim.lvl1='white-collar', education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube23.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube23[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube24 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube24\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:work_dim.lvl1='Self-emp',education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube24.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube24[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		//Third slide
		
		String[] cube31 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube31\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,age_dim.lvl3\n" + 
				"Sigma:occupation_dim.lvl1='white-collar', education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube31.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube31[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube32 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube32\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl3,age_dim.lvl3\n" + 
				"Sigma:occupation_dim.lvl1='blue-collar', work_dim.lvl2='With-Pay'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube32.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube32[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube33 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube33\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,age_dim.lvl2\n" + 
				"Sigma:age_dim.lvl3='37-56', education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube33.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube33[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube34 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube34\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,occupation_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Without-Post-Secondary',age_dim.lvl3='37-56'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube34.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube34[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
/*		
		//Session 2
		String[] initial1 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery1\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,native_country_dim.lvl2\n" + 
				"Sigma:occupation_dim.lvl1='white-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial1.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial1[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] initial2 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery2\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,race_dim.lvl1\n" + 
				"Sigma:native_country_dim.lvl1='USA'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial2.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial2[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] initial3 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery3\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl0,race_dim.lvl0\n" + 
				"Sigma:race_dim.lvl1='Colored', education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial3.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial3[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		String[] initial4 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:InitialCubeQuery4\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl2,race_dim.lvl1\n" + 
				"Sigma:occupation_dim.lvl1='white-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < initial4.length; i++) {
			System.out.println(measures.get(i) + ":    " + initial4[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		
		//First slide
		String[] cube11 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube11\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,native_country_dim.lvl2\n" + 
				"Sigma:occupation_dim.lvl1='blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube11.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube11[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube12 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube12\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,native_country_dim.lvl1\n" + 
				"Sigma:native_country_dim.lvl2='North-America'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube12.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube12[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube13 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube13\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl1,native_country_dim.lvl2\n" + 
				"Sigma:education_dim.lvl2='Post-grad', occupation_dim.lvl1='white-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube13.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube13[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube14 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube14\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:education_dim.lvl2,native_country_dim.lvl2\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary',race_dim.lvl1='Colored'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube14.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube14[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//Second slide
		String[] cube21 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube21\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,race_dim.lvl0\n" + 
				"Sigma:race_dim.lvl1='White',education_dim.lvl3='Post-Secondary'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube21.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube21[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube22 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube22\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,race_dim.lvl0\n" + 
				"Sigma:native_country_dim.lvl2='North-America',race_dim.lvl1='Colored'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube22.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube22[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube23 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube23\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl0,race_dim.lvl1\n" + 
				"Sigma:occupation_dim.lvl1='white-collar',education_dim.lvl2='Post-grad'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube23.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube23[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube24 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube24\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl0,race_dim.lvl0\n" + 
				"Sigma:native_country_dim.lvl2='North-America',occupation_dim.lvl1='white-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube24.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube24[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		//Third Slide
		
		String[] cube31 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube31\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl2,race_dim.lvl1\n" + 
				"Sigma:occupation_dim.lvl1='blue-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube31.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube31[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube32 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube32\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl2,race_dim.lvl0\n" + 
				"Sigma:occupation_dim.lvl1='white-collar', race_dim.lvl1='Colored'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube32.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube32[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube33 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube33\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl0,race_dim.lvl1\n" + 
				"Sigma:native_country_dim.lvl2='North-America', education_dim.lvl2='Post-grad'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube33.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube33[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		
		String[] cube34 = service.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:Cube34\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl2,race_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary', occupation_dim.lvl1='white-collar'", measures );
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for(int i = 0; i < cube34.length; i++) {
			System.out.println(measures.get(i) + ":    " + cube34[i]);
		}
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		*/
		//Delete history files created
		clearOldHistory();
		//Create .gitignore files
		createGitignoreFiles();
	}
}
