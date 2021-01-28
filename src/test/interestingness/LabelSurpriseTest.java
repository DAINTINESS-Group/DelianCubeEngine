package test.interestingness;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import mainengine.SimpleQueryProcessorEngine;

public class LabelSurpriseTest {

	private static SimpleQueryProcessorEngine queryEngine;
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
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		clearOldHistory();
		queryEngine = new SimpleQueryProcessorEngine(); 
		
		queryEngine.initializeConnectionWithIntrMng("pkdd99", "CinecubesUser",
				"Cinecubes", "pkdd99","", "", "InputFiles/UserProfile/ExpectedValues/predictions1", -1, "loan");
		measures.add("Label Surprise");
	}

	@Test
	public void test() throws RemoteException {
		String[] answer = queryEngine.answerCubeQueryWithInterestMeasures("CubeName:loan\n" + 
				"Name: LoanQuery21_S2_CG-Cmmn\n" + 
				"AggrFunc:Min\n" + 
				"Measure:amount\n" + 
				"Gamma:account_dim.lvl1,date_dim.lvl2\n" + 
				"Sigma:account_dim.lvl2='Prague', date_dim.lvl3 = '1998'", measures);
		assertEquals("0.25", answer[0]);
	}

}
