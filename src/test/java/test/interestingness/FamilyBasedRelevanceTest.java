package test.interestingness;

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
public class FamilyBasedRelevanceTest {
	private static SessionQueryProcessorEngine queryEngine;
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
		
		queryEngine.initializeConnectionWithIntrMng(typeOfConnection, userInputList,
				"InputFiles/ServerRegisteredInfo/Interestingness/History", "", "", "", -1);
		measures.clear();
		measures.add("FamilyBasedRelevance");

	}
	@Test
	public void testFamilyBasedRelevance() throws Exception {
		String q1 = "CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:date_dim.lvl2,account_dim.lvl1\n" + 
				"Sigma:account_dim.lvl2='Prague',date_dim.lvl3 = '1997'";
		String q2 = "CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:date_dim.lvl3 = '1998',account_dim.lvl2='Prague'";
		String q3 = "CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:date_dim.lvl3 = '1998',account_dim.lvl2='Prague'";
		String q4 = "CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:date_dim.lvl3 = '1998',account_dim.lvl1='Hl.m. Praha'";
		String q5 = "CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:date_dim.lvl3 = '1998',account_dim.lvl1='Pisek'";
		String q6 = "CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:date_dim.lvl3 = '1997',account_dim.lvl1='Hl.m. Praha'";

		String[] answer = queryEngine.answerCubeQueryWithInterestMeasures(q1,q2,measures);
		String[] answer1 = queryEngine.answerCubeQueryWithInterestMeasures(q3,q4,measures);
		String[] answer2 = queryEngine.answerCubeQueryWithInterestMeasures(q3,q5,measures);
		String[] answer3 = queryEngine.answerCubeQueryWithInterestMeasures(q5,q6,measures);

		clearOldHistory();
		createGitignoreFiles();
		assertEquals("0.0", answer[0]);	
		assertEquals("0.0", answer1[0]);	
		assertEquals("1.0", answer2[0]);	
		assertEquals("1.0", answer3[0]);	

	}

 
}

