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
package test;




import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

//import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import mainengine.IMainEngine;
import mainengine.SimpleQueryProcessorEngine;


/**
 * Test class for SimpleQueryProcessorEngine AND for the ENTIRE ENGINE
 * Main test to be checked: AnswerCubeQueriesFromFile()
 *
 */
public class SimpleQueryProcessorEngineTest {
	
	private static IMainEngine testedQPEngine;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testedQPEngine = new SimpleQueryProcessorEngine(); 
		
		testedQPEngine.initializeConnection("pkdd99", "CinecubesUser",
				"Cinecubes", "pkdd99", "loan");
				
		
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
		
		
		/**
		 * Try some easy small queries first
		 */
		File f = new File("src/test/InputFiles/pkdd99/_cubeQueriesloan.ini");
		//System.out.println(f.getPath() + "\n"+ f.getAbsolutePath());
		testedQPEngine.answerCubeQueriesFromFile(f);/**/
		
		
		File fileProduced01 = new File("OutputFiles/CubeQueryLoan1.tab");
		File fileReference01 = new File("src/test/OutputFiles/pkdd99/Reference_CubeQueryLoan1.tab");
        boolean comparison01 = FileUtils.contentEquals(fileProduced01, fileReference01);
		
//        System.out.println("\n PRODUCED: "+ fileProduced1.getAbsolutePath());
//        System.out.println("\n REFERENCE: "+ fileReference1.getAbsolutePath());
        
		File fileProduced02 = new File("OutputFiles/CubeQueryLoan2.tab");
		File fileReference02 = new File("src/test/OutputFiles/pkdd99/Reference_CubeQueryLoan2.tab");
        boolean comparison02 = FileUtils.contentEquals(fileProduced02, fileReference02);

		
		File fileProduced03 = new File("OutputFiles/CubeQueryLoan3.tab");
		File fileReference03 = new File("src/test/OutputFiles/pkdd99/Reference_CubeQueryLoan3.tab");
        boolean comparison03 = FileUtils.contentEquals(fileProduced03, fileReference03);
        assertEquals((comparison01 && comparison02 && comparison03), true);


        /**
         * Try some more principled querying. The names stand for
         * S<k>: k stands for how many atoms the sigma selection condition has
         * CG-<xxx>: whether the group-by dimensions and the sigma dimensions have a partial coverage, are common, or are disjoint  
         */
        File inputFile = new File("src/test/InputFiles/pkdd99/_loanQueriesPrincipled.txt");
        testedQPEngine.answerCubeQueriesFromFile(inputFile);

		File fileProduced11 = new File("OutputFiles/LoanQuery11_S1_CG-Prtl.tab");
		File fileReference11 = new File("src/test/OutputFiles/pkdd99/Reference_LoanQuery11_S1_CG-Prtl.tsv");
        boolean comparison11 = FileUtils.contentEquals(fileProduced11, fileReference11);
        assertEquals(comparison11, true);
        
        File fileProduced12 = new File("OutputFiles/LoanQuery12_S1_CG-Dsjnt.tab");
		File fileReference12 = new File("src/test/OutputFiles/pkdd99/Reference_LoanQuery12_S1_CG-Dsjnt.tsv");
		boolean comparison12 = FileUtils.contentEquals(fileProduced12, fileReference12);
        assertEquals(comparison12, true);
        
		File fileProduced21 = new File("OutputFiles/LoanQuery21_S2_CG-Cmmn.tab");
		File fileReference21 = new File("src/test/OutputFiles/pkdd99/Reference_LoanQuery21_S2_CG-Cmmn.tsv");
		boolean comparison21 = FileUtils.contentEquals(fileProduced21, fileReference21);
        assertEquals(comparison21, true);
        
		File fileProduced22 = new File("OutputFiles/LoanQuery22_S2_CG-Prtl.tab");
		File fileReference22 = new File("src/test/OutputFiles/pkdd99/Reference_LoanQuery22_S2_CG-Prtl.tsv");
		boolean comparison22 = FileUtils.contentEquals(fileProduced22, fileReference22);
        assertEquals(comparison22, true);
        
		File fileProduced31 = new File("OutputFiles/LoanQuery31_S3_CG-Prtl.tab");
		File fileReference31 = new File("src/test/OutputFiles/pkdd99/Reference_LoanQuery31_S3_CG-Prtl.tsv");
		boolean comparison31 = FileUtils.contentEquals(fileProduced31, fileReference31);
        assertEquals(comparison31, true);
        
        
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
						"Sigma:account_dim.lvl1='Liberec'";
		testedQPEngine.answerCubeQueryFromString(testQueryString1);   /**/
		File fileProduced1 = new File("OutputFiles/CubeQueryLoan1_FailTheTest.tab");
		File fileReference1 = new File("src/test/OutputFiles/pkdd99/Reference_CubeQueryLoan1.tab");
        boolean comparison1 = FileUtils.contentEquals(fileProduced1, fileReference1);
		
        assertEquals(comparison1 , false);				

		//GIVE THE EXACT SAME QUERY
		String testQueryString2 = 
				"CubeName:loan" + " \n" +
				"Name:CubeQueryLoan2_Copy" + " \n" +
				"AggrFunc:Avg" + " \n" +
				"Measure:amount" + " \n" +
				"Gamma:account_dim.lvl2,date_dim.lvl2" + " \n" +
				"Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'";

		testedQPEngine.answerCubeQueryFromString(testQueryString2);   /**/
		

		File fileProduced2 = new File("OutputFiles/CubeQueryLoan2_Copy.tab");
		File fileReference2 = new File("src/test/OutputFiles/pkdd99/Reference_CubeQueryLoan2.tab");
        boolean comparison2 = FileUtils.contentEquals(fileProduced2, fileReference2);
		
        assertEquals(comparison2 , true);/**/
        
	}

}
