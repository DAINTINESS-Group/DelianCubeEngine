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

public class PartialExtensionalValueBasedSurpriseTest {

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
					"", "InputFiles/UserProfile/ExpectedValues/predictions1", "", -1);
		measures.add("Partial Extensional Value Based Surprise");

	}
	
	@Test
	public void testBasicValueSurprise() throws Exception {

		String[] answer = queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1998'", measures);
		clearOldHistory();
		createGitignoreFiles();
		assertEquals("0.12502928257686677", answer[0]);

	}

}
