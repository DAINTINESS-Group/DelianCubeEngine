/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/


/**
 * 
 *  @author pvassil
 */
package mainengine;




import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;

import java.io.BufferedReader;

//import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.spark.sql.AnalysisException;
import org.junit.BeforeClass;
import org.junit.Test;

import result.ResultFileMetadata;




/**
 * Test class for SimpleQueryProcessorEngine AND for the ENTIRE ENGINE
 * Main test to be checked: AnswerCubeQueriesFromFile()
 *
 */
public class SessionQueryProcessorEngineTest {
	
	private static IMainEngine testedQPEngine;
	
	/**
	 * Setup before all: Initialize connection
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testedQPEngine = new SessionQueryProcessorEngine(); 
		String typeOfConnection = "RDBMS";
		HashMap<String, String>userInputList = new HashMap<>();
		userInputList.put("schemaName", "pkdd99");
		userInputList.put("username", "CinecubesUser");
		userInputList.put("password", "Cinecubes");
		userInputList.put("cubeName", "loan");
		userInputList.put("inputFolder", "pkdd99");
		
		testedQPEngine.initializeConnection(typeOfConnection, userInputList);
				
		//TODO: currently, the result goes to the DelianCubeEngine/OutputFiles, i.e., it is mixed with the output of the regular execution. can we isolate the output of the tests, within the test folder?
		//TODO:  Basically needs to invoke the answerQueriesFromFile to get an OutputFolder parameter.
	}


	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#answerCubeQueriesFromFile(java.io.File)}.
	 * @throws IOException 
	 */
	@Test
	public final void testAnswerCubeQueriesFromFile() throws IOException {
		//fail("Not yet implemented"); 
		// can try failures by modifying filenames and/or paths. Keep the getAbsolutePath() comments for failure tests
		
		boolean comparison01 = true;
		boolean comparison02 = true;
		boolean comparison03 = true;
		
		/**
		 * Try some easy small queries first
		 */
		File f = new File("src/test/resources/InputFiles/pkdd99/_cubeQueriesloan.ini");
		//System.out.println(f.getPath() + "\n"+ f.getAbsolutePath());
		testedQPEngine.answerCubeQueriesFromFile(f);
		
		
		String fileProduced01 = getContents("OutputFiles/CubeQueryLoan1.tab");
		String fileReference01 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan1.tab");
        comparison01 = fileProduced01.equals(fileReference01);
		
//        System.out.println("\n PRODUCED: "+ fileProduced1.getAbsolutePath());
//        System.out.println("\n REFERENCE: "+ fileReference1.getAbsolutePath());
        
		String fileProduced02 = getContents("OutputFiles/CubeQueryLoan2.tab");
		String fileReference02 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan2.tab");
        comparison02 = fileProduced02.equals(fileReference02);

		
		String fileProduced03 = getContents("OutputFiles/CubeQueryLoan3.tab");
		String fileReference03 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan3.tab");
        comparison03 = fileProduced03.equals(fileReference03);
        assertEquals((comparison01 && comparison02 && comparison03), true);/**/


        /**
         * Try some more principled querying. The names stand for
         * S<k>: k stands for how many atoms the sigma selection condition has
         * CG-<xxx>: whether the group-by dimensions and the sigma dimensions have a partial coverage, are common, or are disjoint  
         */ 
	/*
        File inputFile = new File("src/test/resources/InputFiles/pkdd99/_loanQueriesPrincipled.txt");
        testedQPEngine.answerCubeQueriesFromFile(inputFile);

		File fileProduced11 = new File("OutputFiles/LoanQuery11_S1_CG-Prtl.tab");
		File fileReference11 = new File("src/test/resources/OutputFiles/pkdd99/Reference_LoanQuery11_S1_CG-Prtl.tsv");
        boolean comparison11 = FileUtils.contentEquals(fileProduced11, fileReference11);
        assertEquals(comparison11, true);
        //Can fail because at lines 16, 17, the two districts have exactly the same aggr. measure
        //So sometimes the output has Bruntal first and sometimes it has Brenov first :P
        //diff OutputFiles/LoanQuery11_S1_CG-Prtl.tab src/test/OutputFiles/pkdd99/Reference_LoanQuery11_S1_CG-Prtl.tsv
     
        
        String fileProduced12 = getContents("OutputFiles/LoanQuery12_S1_CG-Dsjnt.tab");
		String fileReference12 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_LoanQuery12_S1_CG-Dsjnt.tsv");
		boolean comparison12 = fileProduced12.equals(fileReference12);
        assertEquals(comparison12, true);
        */
        
        
		String fileProduced21 = getContents("OutputFiles/LoanQuery21_S2_CG-Cmmn.tab");
		String fileReference21 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_LoanQuery21_S2_CG-Cmmn.tsv");
		boolean comparison21 = fileProduced21.equals(fileReference21);
        assertEquals(true, comparison21);
        
		String fileProduced22 = getContents("OutputFiles/LoanQuery22_S2_CG-Prtl.tab");
		String fileReference22 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_LoanQuery22_S2_CG-Prtl.tsv");
		boolean comparison22 = fileProduced22.equals(fileReference22);
        assertEquals(true, comparison22);
        
		String fileProduced31 = getContents("OutputFiles/LoanQuery31_S3_CG-Prtl.tab");
		String fileReference31 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_LoanQuery31_S3_CG-Prtl.tsv");
		boolean comparison31 = fileProduced31.equals(fileReference31);
        assertEquals(true, comparison31);
		
	} //end testAnswerCubeQueriesFromFile
	
	
	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#answerCubeQueryFromString(java.io.File)}.
	 * @throws IOException 
	 */
	@Test
	public final void testanswerCubeQueryFromString() throws IOException {
		//fail("Not yet implemented");
		// can try failures by modifying filenames and/or paths. See answerCQFromFILES for comments 

		//GIVE STH DIFFERENT
		String testQueryString1 = 
				"CubeName:loan" + " \n" +
						"Name:CubeQueryLoan1_FailTheTest" + " \n" +
						"AggrFunc:Avg" + " \n" +
						"Measure:amount" + " \n" +
						"Gamma:account_dim.lvl2,date_dim.lvl2" + " \n" +
						"Sigma:account_dim.lvl1 = 'Liberec'";
		testedQPEngine.answerCubeQueryFromString(testQueryString1);   /**/
		File fileProduced1 = new File("OutputFiles/CubeQueryLoan1_FailTheTest.tab");
		File fileReference1 = new File("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan1.tab");
        boolean comparison1 = FileUtils.contentEquals(fileProduced1, fileReference1);
		
        assertEquals(comparison1 , false);				

		//GIVE THE EXACT SAME QUERY
		String testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl2,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec' ,status_dim.lvl0 = 'Running Contract/OK'";

		testedQPEngine.answerCubeQueryFromString(testQueryString2);   /**/
		

		File fileProduced2 = new File("OutputFiles/CubeQueryLoan2_Copy.tab");
		File fileReference2 = new File("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan2.tab");
        // EOL-agnostic: getContents() reads line-by-line, so CRLF reference fixtures match LF output.
        boolean comparison2 = getContents(fileProduced2.getPath()).equals(getContents(fileReference2.getPath()));

        assertEquals(comparison2 , true);/**/
        
	}//end testanswerCubeQueryFromString

	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#answerCubeQueriesFromStringWithMetadata(String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testanswerCubeQueryFromStringWithMetadata() throws IOException{

		String testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name: CubeQueryLoan22_Copy" + " \n" +
				"AggrFunc:Sum" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl1,date_dim.lvl3" + " \n" +
				"Sigma:account_dim.lvl2='south Moravia',status_dim.lvl0='Running Contract/OK'";

		testedQPEngine.answerCubeQueryFromStringWithMetadata(testQueryString2);   /**/
		
		
		String fileInfoProduced2 = getContents("OutputFiles/CubeQueryLoan22_Copy_Info.txt");
		String fileInfoReference2 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan22_Info.txt");
        //boolean comparison2 = FileUtils.contentEquals(fileInfoProduced2, fileInfoReference2);
        assertEquals(fileInfoProduced2 , fileInfoReference2);/**/
	}//end method testanswerCubeQueryFromStringWithMetadata

	
	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#rollUp(String,String,String,String)}.
	 * @throws RemoteException
	 */
	@Test
	public final void testrollUp() throws RemoteException {
		
		String testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl2,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'";
		
		testedQPEngine.answerCubeQueryFromString(testQueryString2);
		ResultFileMetadata resMetadata = testedQPEngine.rollUp("CubeQueryLoan2_Copy", "CubeQueryLoan2_RollUp", "account_dim", "lvl3");
		
		
		String fileInfoProduced2 = getContents("OutputFiles/CubeQueryLoan2_RollUp_Info.txt");
		String fileInfoReference2 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan2_RollUp_Info.txt");
	
		if(resMetadata.getErrorCheckingStatus()==null) {
			assertEquals(fileInfoProduced2, fileInfoReference2);
		}
		/*
		else {
			System.out.println(resMetadata.getErrorCheckingStatus());
			assertEquals(resMetadata.getErrorCheckingStatus(), null);
		}*/
		
		
		
		resMetadata = testedQPEngine.rollUp("CubeQueryLoan2_Copy", "CubeQueryLoan2_RollUp", "accunt_dim", "lvl3");
		assertEquals("Dimension not found", resMetadata.getErrorCheckingStatus());
		
		
		resMetadata = testedQPEngine.rollUp("CubeQueryLoan2_Copy", "CubeQueryLoan2_RollUp", "account_dim", "lvl1");
		assertEquals("Target level is not higher than the current level in the dimension hierarchy.", resMetadata.getErrorCheckingStatus());
		
		
		resMetadata = testedQPEngine.rollUp("CubeQueryLoan2_Copy", "CubeQueryLoan2_RollUp", "account_dim", "lvl");
		assertEquals("Level not found", resMetadata.getErrorCheckingStatus());
		
		
		testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy2" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl3,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'";
		testedQPEngine.answerCubeQueryFromString(testQueryString2);
		resMetadata = testedQPEngine.rollUp("CubeQueryLoan2_Copy2", "CubeQueryLoan2_RollUp", "account_dim", "lvl3");
		assertEquals("Already at the top of the dimension hierarchy.", resMetadata.getErrorCheckingStatus());
	}

	
	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#drillDown(String,String,String,String)}.
	 * @throws RemoteException
	 */
	@Test
	public final void testdrillDown() throws RemoteException {
		
		String testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl2,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'";
		
		testedQPEngine.answerCubeQueryFromString(testQueryString2);
		ResultFileMetadata resMetadata = testedQPEngine.drillDown("CubeQueryLoan2_Copy", "CubeQueryLoan2_DrillDown", "account_dim", "lvl0");
		
		
		String fileInfoProduced2 = getContents("OutputFiles/CubeQueryLoan2_DrillDown_Info.txt");
		String fileInfoReference2 = getContents("src/test/resources/OutputFiles/pkdd99/Reference_CubeQueryLoan2_DrillDown_Info.txt");
		System.out.println(resMetadata.getErrorCheckingStatus());
		if(resMetadata.getErrorCheckingStatus()==null) {
			assertEquals(fileInfoProduced2, fileInfoReference2);
		}
		else {
			System.out.println(resMetadata.getErrorCheckingStatus());
			assertEquals(resMetadata.getErrorCheckingStatus(), null);
		}
		
		
		
		resMetadata = testedQPEngine.drillDown("CubeQueryLoan2_Copy", "CubeQueryLoan2_DrillDown", "accunt_dim", "lvl3");
		assertEquals("Dimension not found", resMetadata.getErrorCheckingStatus());
		
		
		resMetadata = testedQPEngine.drillDown("CubeQueryLoan2_Copy", "CubeQueryLoan2_DrillDown", "account_dim", "lvl3");
		assertEquals("Target level is not lower than the current level in the dimension hierarchy.", resMetadata.getErrorCheckingStatus());
		
		
		resMetadata = testedQPEngine.drillDown("CubeQueryLoan2_Copy", "CubeQueryLoan2_DrillDown", "account_dim", "lvl");
		assertEquals("Level not found", resMetadata.getErrorCheckingStatus());
		
		
		testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy3" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl0,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'";
		testedQPEngine.answerCubeQueryFromString(testQueryString2);
		resMetadata = testedQPEngine.drillDown("CubeQueryLoan2_Copy3", "CubeQueryLoan2_DrillDown", "account_dim", "lvl0");
		assertEquals("Already at the bottom of the dimension hierarchy.", resMetadata.getErrorCheckingStatus());
	}
	
	
	
	private String getContents(String fileName) {
		String contents = "";
		File file = new File(fileName);
		if(file.exists() && !file.isDirectory()) { 
			BufferedReader reader = null;
			try {
			    reader = new BufferedReader(new FileReader(file));

			    String line;
			    while ((line = reader.readLine()) != null) {
			        contents = contents + line + "\n";
			    }

			} catch (IOException e) {
			    e.printStackTrace();
			} finally {
			    try {
			        reader.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}//end finally
		}//end master if
		return contents;
	}//end method
	
}//end class
