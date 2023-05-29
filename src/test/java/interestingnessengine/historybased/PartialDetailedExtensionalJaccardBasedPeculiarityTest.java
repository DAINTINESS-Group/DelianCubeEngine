package interestingnessengine.historybased;

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

public class PartialDetailedExtensionalJaccardBasedPeculiarityTest {
	
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

		queryEngine = new SessionQueryProcessorEngine(); 
		queryEngine2 = new SessionQueryProcessorEngine();
		
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99");
		
		queryEngine.initializeConnectionWithIntrMng(typeOfConnection, userInputList,
				"InputFiles/ServerRegisteredInfo/Interestingness/History", "", "", "", 1);
		
		HashMap<String, String>userInputList2 = new HashMap<>();
		userInputList2.put("schemaName", "adult_no_dublic");
		userInputList2.put("username", "CinecubesUser");
		userInputList2.put("password", "Cinecubes");
		userInputList2.put("cubeName", "adult");
		userInputList2.put("inputFolder", "adult");
		
		queryEngine2.initializeConnectionWithIntrMng(typeOfConnection, userInputList2,
				"InputFiles/ServerRegisteredInfo/Interestingness/History", "", "", "", 1);
		
		
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
		
		queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery1\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl2,race_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay', education_dim.lvl3='Without-Post-Secondary'", measures );
		
		queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery2\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,age_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl2='Some-college'", measures );
		
		queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery21\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,age_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='With-Pay',education_dim.lvl2='Post-grad'", measures );
		
		measures.clear();
		measures.add("Partial Detailed Extensional Jaccard Based Peculiarity");

	}

	@Test
	public void test() throws IOException {
		String[] answer = queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1997'", measures);
		assertEquals("0.0", answer[0]);
		
		String[] newAnswer = queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague'", measures);
		//clearOldHistory();
		//createGitignoreFiles();
		assertEquals("0.7857142857142857", newAnswer[0]);
	}
	
	@Test
	public void test2() throws IOException {
		
		String[] answer3 = queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery3\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:native_country_dim.lvl2,race_dim.lvl1\n" + 
				"Sigma:work_dim.lvl2='Without-Pay'", measures );
		
		String[] answer4 = queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery4\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,age_dim.lvl1\n" + 
				"Sigma:education_dim.lvl1='Doctorate'", measures );
		
		String[] answer5 = queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery5\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,age_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary'", measures );	
		
		String[] answer6 = queryEngine2.answerCubeQueryWithInterestMeasures("CubeName:adult\n" + 
				"Name:AdultCubeQuery6\n" + 
				"AggrFunc:Avg\n" + 
				"Measure:hours_per_week\n" + 
				"Gamma:occupation_dim.lvl1,age_dim.lvl1\n" + 
				"Sigma:education_dim.lvl3='Post-Secondary', work_dim.lvl2='Without-Pay'", measures );
		
		
		
		clearOldHistory();
		createGitignoreFiles();
		
		assertEquals("1.0", answer3[0]);
		assertEquals("0.761977186311787", answer4[0]);
		assertEquals("0.4785975781470009", answer5[0]);
		assertEquals("0.6923076923076923", answer6[0]);
		
	}

}
