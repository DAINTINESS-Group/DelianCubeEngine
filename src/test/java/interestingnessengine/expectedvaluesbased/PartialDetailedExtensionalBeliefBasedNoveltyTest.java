package interestingnessengine.expectedvaluesbased;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import mainengine.SessionQueryProcessorEngine;
public class PartialDetailedExtensionalBeliefBasedNoveltyTest {
	private static SessionQueryProcessorEngine queryEngine;
	private static SessionQueryProcessorEngine queryEngine2;
	private static List<String> measures = new ArrayList<String>();
	
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
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		clearOldHistory();
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99");
		queryEngine = new SessionQueryProcessorEngine(); 
		queryEngine2 = new SessionQueryProcessorEngine();
		
		queryEngine.initializeConnectionWithIntrMng(typeOfConnection, userInputList,
				"", "", "", "InputFiles/UserProfile/ExpectedValues/predictionsWithProbabilitiesAvg", -1);
		
		HashMap<String, String>userInputList2 = new HashMap<>();
		userInputList2.put("schemaName", "adult_no_dublic");
		userInputList2.put("username", "CinecubesUser");
		userInputList2.put("password", "Cinecubes");
		userInputList2.put("cubeName", "adult");
		userInputList2.put("inputFolder", "adult");
		queryEngine2.initializeConnectionWithIntrMng(typeOfConnection, userInputList2,
				"", "", "", "InputFiles/UserProfile/ExpectedValues/adultPredictionsWithProbabilities", -1);
		//measures.add("Direct Novelty");
		/*
		queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1997'", measures);
		queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery31_S3_CG-Prtl\n" + 
				"AggrFunc:Sum\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl3\n" + 
				"Sigma:account_dim.lvl2='west Bohemia',status_dim.lvl0='Contract Finished/No Problems', date_dim.lvl3 = '1996'", measures);
	*/
		measures.clear();
		measures.add("Partial Detailed Extensional Belief Based Novelty");

	}
	@Test
	public void testBeliefBasedNovelty() throws Exception {
		
		String[] answer = queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague'", measures);
			
		clearOldHistory();
		createGitignoreFiles();
		assertEquals("0.47619047619047616", answer[0]);	
	}
	
	@Test
	public void testBeliefBasedNovelty2() throws Exception {
	
		String[] answer2 = queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name: AdultCubeQuery\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:work_dim.lvl1,age_dim.lvl2\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl2='Post-grad'", measures);
		
		
		assertEquals("0.497002997002997", answer2[0]);
	}

 
}

