package test.nlqueries;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import mainengine.nlq.NLTranslator;
import mainengine.IMainEngine;
import mainengine.SimpleQueryProcessorEngine;


public class SimplifiedNLQueriesTest {
	
	private static IMainEngine testedQPEngine;
	NLTranslator translator = new NLTranslator();
	
	/**
	 * Setup before all: Initialize connection
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testedQPEngine = new SimpleQueryProcessorEngine(); 
		
		testedQPEngine.initializeConnection("pkdd99_star", "CinecubesUser",
				"Cinecubes", "pkdd99_star", "loan");
				
		//TODO: currently, the result goes to the DelianCubeEngine/OutputFiles, i.e., it is mixed with the output of the regular execution. can we isolate the output of the tests, within the test folder?
		//TODO:  Basically needs to invoke the answerQueriesFromFile to get an OutputFolder parameter.
	}

	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#answerCubeQueryFromNLString(java.io.File)}.
	 * @throws IOException 
	 */
	@Test
	public final void testanswerCubeQueryFromNLString() throws IOException {
		//fail("Not yet implemented");
		// can try failures by modifying filenames and/or paths. See answerCQFromFILES for comments 

		//GIVE STH DIFFERENT
		String testQuery1 = "Describe the min of loan amount per district_name and month for region is 'Prague' as LoanQuery1_FailTheTest";

		String path =testedQPEngine.answerCubeQueryFromNLString(testQuery1);   /**/
		System.out.println(path);
		
		File fileProduced1 = new File("OutputFiles/LoanQuery1_FailTheTest.tab");
		File fileReference1 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery2.tab");
        boolean comparison1 = FileUtils.contentEquals(fileProduced1, fileReference1);
		
        assertEquals(comparison1 , false);
        
        
        //GIVE RIGHT QUERY
		String testQuery2 = "Describe the min of loan amount per district_name and month for region is 'Prague' and year is '1998' as LoanQuery1";
		testedQPEngine.answerCubeQueryFromNLString(testQuery2);
		File fileProduced2 = new File("OutputFiles/LoanQuery1.tab");
		File fileReference2 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1.tab");
        boolean comparison2 = FileUtils.contentEquals(fileProduced2, fileReference2);
        assertEquals(comparison2 , true);
	}//end testanswerCubeQueryFromString
	
	
	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#answerCubeQueriesFromNLStringWithMetadata(String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testanswerCubeQueryFromNLStringWithMetadata() throws IOException{
		String testQueryString ="Describe the avg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString);   /**/
		
		File fileInfoProduced = new File("OutputFiles/LoanQuery1_info.txt");
		File fileInfoReference = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_info.txt");
        boolean comparison = FileUtils.contentEquals(fileInfoProduced, fileInfoReference);
        assertEquals(comparison , true);/**/
        
        
		String testQueryString2 ="Describe the max of loan amount per district_name and status for month is '1998-01' as LoanQuery2"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString2);   /**/
		
		File fileInfoProduced2 = new File("OutputFiles/LoanQuery2_info.txt");
		File fileInfoReference2 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery2_info.txt");
        boolean comparison2 = FileUtils.contentEquals(fileInfoProduced2, fileInfoReference2);
        assertEquals(comparison2 , true);/**/
        
        
        String testQueryString3 ="Describe the min of loan amount per district_name and month for region is 'Prague' and year is '1998' as LoanQuery3"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString3);   /**/	
		
		File fileInfoProduced3 = new File("OutputFiles/LoanQuery3_info.txt");
		File fileInfoReference3 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery3_info.txt");
        boolean comparison3 = FileUtils.contentEquals(fileInfoProduced3, fileInfoReference3);
        assertEquals(comparison3 , true);/**/
		
		
		String testQueryString4 ="Describe the sum of loan amount per district_name and year for region is 'south Moravia' and status is 'Running Contract/OK' as LoanQuery4"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString4);   /**/	
		
		File fileInfoProduced4 = new File("OutputFiles/LoanQuery4_info.txt");
		File fileInfoReference4 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery4_info.txt");
        boolean comparison4 = FileUtils.contentEquals(fileInfoProduced4, fileInfoReference4);
        assertEquals(comparison4 , true);/**/
		
		
		String testQueryString5 ="Describe the sum of loan amount per district_name and year for region is 'west Bohemia' and status is 'Contract Finished/No Problems' and year is '1996' as LoanQuery5"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString5);   /**/	
		
		File fileInfoProduced5 = new File("OutputFiles/LoanQuery5_info.txt");
		File fileInfoReference5 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery5_info.txt");
        boolean comparison5 = FileUtils.contentEquals(fileInfoProduced5, fileInfoReference5);
        assertEquals(comparison5 , true);/**/
        
	}//end method testanswerCubeQueryFromStringWithMetadata
	
	
	
	/**
	 * Test method for {@link mainengine.SimpleQueryProcessorEngine#prepareCubeQuery(String)}.
	 * @throws IOException 
	 */
	@Test
	public final void testprepareCubeQuery() throws IOException{
		//First give right query
		String testQueryString ="Describe the avg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString);   /**/
		
		File fileInfoProduced = new File("OutputFiles/ErrorChecking.txt");
		File fileInfoReference = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_ErrorChecking.txt");
        boolean comparison = FileUtils.contentEquals(fileInfoProduced, fileInfoReference);
        assertEquals(comparison , true);/**/
        
        //Error: Wrong cube name
		String testQueryString2 ="Describe the avg of lon amount per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString2);   /**/
		
		File fileInfoProduced2 = new File("OutputFiles/ErrorChecking.txt");
		File fileInfoReference2 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongCubeName.txt");
        boolean comparison2 = FileUtils.contentEquals(fileInfoProduced2, fileInfoReference2);
        assertEquals(comparison2 , true);/**/
        
        
        //Error: Wrong agreggate function
        String testQueryString3 ="Describe the avgg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString3);   /**/	
		
		File fileInfoProduced3 = new File("OutputFiles/ErrorChecking.txt");
		File fileInfoReference3 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongAggrFunc.txt");
        boolean comparison3 = FileUtils.contentEquals(fileInfoProduced3, fileInfoReference3);
        assertEquals(comparison3 , true);/**/
		
		//Error: Wrong measure name
		String testQueryString4 ="Describe the avg of loan amountt per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString4);   /**/	
		
		File fileInfoProduced4 = new File("OutputFiles/ErrorChecking.txt");
		File fileInfoReference4 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongMeasureName.txt");
        boolean comparison4 = FileUtils.contentEquals(fileInfoProduced4, fileInfoReference4);
        assertEquals(comparison4 , true);/**/
		
		//Error: Wrong gamma
		String testQueryString5 ="Describe the avg of loan amount per districtt_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString5);   /**/	
		
		File fileInfoProduced5 = new File("OutputFiles/ErrorChecking.txt");
		File fileInfoReference5 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongGamma.txt");
        boolean comparison5 = FileUtils.contentEquals(fileInfoProduced5, fileInfoReference5);
        assertEquals(comparison5 , true);/**/
        
        //Error: Wrong sigma
        String testQueryString6 ="Describe the avg of loan amount per district_name and month for rregion is 'north Moravia' as LoanQuery1"; 
      	testedQPEngine.prepareCubeQuery(testQueryString6);   /**/	
      		
      	File fileInfoProduced6 = new File("OutputFiles/ErrorChecking.txt");
  		File fileInfoReference6 = new File("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongSigma.txt");
        boolean comparison6 = FileUtils.contentEquals(fileInfoProduced6, fileInfoReference6);
        assertEquals(comparison6 , true);/**/
	}//end method testanswerCubeQueryFromStringWithMetadata

	
//Some nlqueries
/*
"Describe the avg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery11_S1_CG-Prtl"
"Describe the max of loan amount per district_name and status for month is '1998-01' as LoanQuery12_S1_CG-Dsjnt"
"Describe the min of loan amount per district_name and month for region is 'Prague' and year is '1998' as LoanQuery21_S2_CG-Cmmn"
"Describe the sum of loan amount per district_name and year for region is 'south Moravia' and status is 'Running Contract/OK' as LoanQuery22_S2_CG-Prtl"
"Describe the sum of loan amount per district_name and year for region is 'west Bohemia' and status is 'Contract Finished/No Problems' and year is '1996' as LoanQuery31_S3_CG-Prtl"
*/

}
