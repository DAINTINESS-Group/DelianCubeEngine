package test.nlqueries;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import mainengine.nlq.NLTranslator;
import mainengine.IMainEngine;
import mainengine.ResultFileMetadata;
import mainengine.SimpleQueryProcessorEngine;


public class SimplifiedNLQueriesTest {
	
	private static IMainEngine testedQPEngine;
	private static IMainEngine testedQPEngine2;
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
		
		testedQPEngine2 = new SimpleQueryProcessorEngine(); 
		
		testedQPEngine2.initializeConnection("adult", "CinecubesUser",
				"Cinecubes", "adult", "adult");
				
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
		
		String producedContents = getContents("OutputFiles/LoanQuery1_info.txt");
		String referenceContents = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_info.txt");
		assertEquals(producedContents, referenceContents);
        
        
		
		String testQueryString2 ="Describe the max of loan amount per district_name and status for month is '1998-01' as LoanQuery2"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString2);   /**/
		
		String producedContents2 = getContents("OutputFiles/LoanQuery2_info.txt");
		String referenceContents2 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery2_info.txt");
		assertEquals(producedContents2, referenceContents2);
        
		
        
        String testQueryString3 ="Describe the min of loan amount per district_name and month for region is 'Prague' and year is '1998' as LoanQuery3"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString3);   /**/	
		
		String producedContents3 = getContents("OutputFiles/LoanQuery3_info.txt");
		String referenceContents3 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery3_info.txt");
		assertEquals(producedContents3, referenceContents3);
		
		
		String testQueryString4 ="Describe the sum of loan amount per district_name and year for region is 'south Moravia' and status is 'Running Contract/OK' as LoanQuery4"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString4);   /**/	
		
		String producedContents4 = getContents("OutputFiles/LoanQuery4_info.txt");
		String referenceContents4 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery4_info.txt");
		assertEquals(producedContents4, referenceContents4);	
		
		
		
		String testQueryString5 ="Describe the sum of loan amount per district_name and year for region is 'west Bohemia' and status is 'Contract Finished/No Problems' and year is '1996' as LoanQuery5"; 
		testedQPEngine.answerCubeQueryFromNLStringWithMetadata(testQueryString5);   /**/	
		
		String producedContents5 = getContents("OutputFiles/LoanQuery5_info.txt");
		String referenceContents5 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery5_info.txt");
		assertEquals(producedContents5, referenceContents5);
        
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
		
		String producedContents = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_ErrorChecking.txt");
		assertEquals(producedContents, referenceContents);	
		
        
        //Error: Wrong cube name
		String testQueryString2 ="Describe the avg of lon amount per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString2);   /**/
		
		String producedContents2 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents2 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongCubeName.txt");
		assertEquals(producedContents2, referenceContents2);
		
        
        //Error: Wrong agreggate function
        String testQueryString3 ="Describe the avgg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString3);   /**/	
		
		String producedContents3 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents3 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongAggrFunc.txt");
		assertEquals(producedContents3, referenceContents3);
		
		
		//Error: Wrong measure name
		String testQueryString4 ="Describe the avg of loan amountt per district_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString4);   /**/	
		
		String producedContents4 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents4 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongMeasureName.txt");
		assertEquals(producedContents4, referenceContents4);
		
		
		//Error: Wrong gamma
		String testQueryString5 ="Describe the avg of loan amount per districtt_name and month for region is 'north Moravia' as LoanQuery1"; 
		testedQPEngine.prepareCubeQuery(testQueryString5);   /**/	
		
		String producedContents5 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents5 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongGamma.txt");
		assertEquals(producedContents5, referenceContents5);
		
        
        //Error: Wrong sigma
        String testQueryString6 ="Describe the avg of loan amount per district_name and month for rregion is 'north Moravia' as LoanQuery1"; 
      	testedQPEngine.prepareCubeQuery(testQueryString6);   /**/	
      	
      	String producedContents6 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents6 = getContents("src/test/OutputFiles/pkdd99_star/Reference_LoanQuery1_WrongSigma.txt");
		assertEquals(producedContents6, referenceContents6);
		
		
		//Error: Many Same Levels
		String testQueryString7 ="Describe the min of adult hours_per_week per lvl0 and lvl2 for lvl1 is 'Blue-collar' as AdultNLQuery0";
		testedQPEngine2.prepareCubeQuery(testQueryString7);   /**/	
      	
      	String producedContents7 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents7 = getContents("src/test/OutputFiles/adult/Reference_AdultNLQuery0_SameLevelNames.txt");
		assertEquals(producedContents7, referenceContents7);

		
		
		//Correct Query for adult database
		String testQueryString8 ="Describe the min of adult hours_per_week per occupation_dim.lvl0 and education_dim.lvl2 for occupation_dim.lvl1 is 'Blue-collar' as AdultNLQuery0";
		testedQPEngine2.prepareCubeQuery(testQueryString8);   /**/	
      	
      	String producedContents8 = getContents("OutputFiles/ErrorChecking.txt");
		String referenceContents8 = getContents("src/test/OutputFiles/adult/Reference_CorrectAdultNLQuery0.txt");
		assertEquals(producedContents8, referenceContents8);
		
	}//end method testanswerCubeQueryFromStringWithMetadata

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
//Some nlqueries
/*
"Describe the avg of loan amount per district_name and month for region is 'north Moravia' as LoanQuery11_S1_CG-Prtl"
"Describe the max of loan amount per district_name and status for month is '1998-01' as LoanQuery12_S1_CG-Dsjnt"
"Describe the min of loan amount per district_name and month for region is 'Prague' and year is '1998' as LoanQuery21_S2_CG-Cmmn"
"Describe the sum of loan amount per district_name and year for region is 'south Moravia' and status is 'Running Contract/OK' as LoanQuery22_S2_CG-Prtl"
"Describe the sum of loan amount per district_name and year for region is 'west Bohemia' and status is 'Contract Finished/No Problems' and year is '1996' as LoanQuery31_S3_CG-Prtl"
*/

}
