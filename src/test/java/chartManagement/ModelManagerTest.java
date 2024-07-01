package chartManagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import chartManagement.models.ChartModel;
import chartManagement.models.ContributorModel;
import chartManagement.models.DominanceModel;
import chartManagement.models.ModalityModel;
import chartManagement.models.ModelManager;
import chartManagement.models.RegressionModel;
import chartManagement.models.AbsoluteTrendModel;

public class ModelManagerTest {
	
	
	@Test
	public void testReturnModelList() {
		
		//Arrange
		IChartQueryNModelGenerator barchartType = new BarchartQueryGenerator();
		ModelManager modelManager = new ModelManager();
		modelManager.setQueryNModelGenerator(barchartType);
		
		//Act
		List<ChartModel> models = modelManager.returnModelList();
		
		//Assert
		assertEquals(5,models.size());
		assertEquals("Kendall's based Trend", models.get(0).getModelName());
		assertEquals("Absolute Trend", models.get(1).getModelName());
		
		
	}
	
	@Test
	public void testfindDominanceInArray() {
		
		//Arrange
		String [][] query = new String[2][3];
		query[0] = new String[]{"Default_row_col_1","Default_row_col_1","Default_row_col_1"};
		query[1] = new String[]{"Default_row_col_2","Default_row_col_2","Default_row_col_2"};
		String [][] insertedArrayWithOneCategory =  {{"1998-01", "Greece","300"}, {"1998-02", "Greece","500"}, {"1998-03", "Greece","700"}, {"1998-04", "Greece","800"}, {"1998-05", "Greece","800"}};
		String [][] insertedArrayWithMultipleCategories =  {{"1998-01", "Greece","300"}, {"1998-01", "Germany","350"}
														,{"1998-02", "Greece","500"}, {"1998-02", "Germany","550"}, 
														{"1998-03", "Greece","700"}, {"1998-03", "Germany","750"},
														{"1998-04", "Greece","800"}, {"1998-04", "Germany","850"}};
		String [][] queryWithNoDominance = new DominanceModel().addMatrix(query, insertedArrayWithOneCategory);
		String [][] queryWithDominance = new DominanceModel().addMatrix(query, insertedArrayWithMultipleCategories);
		
		//Act 
		String result = new DominanceModel().findDominanceInArray(queryWithNoDominance);
		String result2 = new DominanceModel().findDominanceInArray(queryWithDominance);
		
		//Assert
		assertEquals(result, "No Dominance, The query result has only one Category (" +  insertedArrayWithOneCategory[2][1] + ") for Series.");
		assertEquals(result2, "Dominator Value for query: " + "Germany");
		
	}
	
	@Test
	public void testfindContributionInArray() {
		
		//Arrange
		String [][] query = new String[2][3];
		query[0] = new String[]{"Default_row_col_1","Default_row_col_1","Default_row_col_1"};
		query[1] = new String[]{"Default_row_col_2","Default_row_col_2","Default_row_col_2"};
		String [][] insertedArrayWithOneCategory =  {{"1998-01", "Greece","300"}, {"1998-02", "Greece","500"}, {"1998-03", "Greece","700"}, {"1998-04", "Greece","800"}, {"1998-05", "Greece","800"}};
		String [][] insertedArrayWithMultipleCategories =  {{"1998-01", "Greece","200"}, {"1998-01", "Germany","800"}
														,{"1998-02", "Greece","500"}, {"1998-02", "Germany","500"}, 
														{"1998-03", "Greece","700"}, {"1998-03", "Germany","300"},
														{"1998-04", "Greece","750"}, {"1998-04", "Germany","250"}};
		String [][] queryWith100Contribution = new ContributorModel().addMatrix(query, insertedArrayWithOneCategory);
		String [][] queryWithPartialContribution = new ContributorModel().addMatrix(query, insertedArrayWithMultipleCategories);
		
		//Act
		String result = new ContributorModel().findContributionInArray(queryWith100Contribution);
		String result2 = new ContributorModel().findContributionInArray(queryWithPartialContribution);
		String expectedResult2 = "Series that contributes the most for each date: 1998-01: Germany, 1998-02: Germany, 1998-03: Greece, 1998-04: Greece,";
		
		//Assert 
		assertEquals(result, "The query result has only one series (Greece. No contribution!");
		assertEquals(result2.trim().replaceAll("\\s+", " "), expectedResult2);
	}
	
	
	@Test 
	public void testfindModalityInArray() {
		//Arrange
		String [][] query = new String[2][3];
		query[0] = new String[]{"Default_row_col_1","Default_row_col_1","Default_row_col_1"};
		query[1] = new String[]{"Default_row_col_2","Default_row_col_2","Default_row_col_2"};
		String [][] insertedArrayUnimodality =  {{"1998-01", "Greece","300"}, {"1998-02", "Greece","500"}, {"1998-03", "Greece","700"}, {"1998-04", "Greece","600"}, {"1998-05", "Greece","500"}};
		String [][] insertedArrayBimodality =  {{"1998-01", "Greece","300"}, {"1998-02", "Greece","500"}, {"1998-03", "Greece","400"}, {"1998-04", "Greece","600"}, {"1998-05", "Greece","500"}};
		String [][] insertedArrayWithNoModality =  {{"1998-01", "Greece","300"}, {"1998-02", "Greece","500"}, {"1998-03", "Greece","600"}, {"1998-04", "Greece","600"}, {"1998-05", "Greece","500"}};
		
		String [][] queryWithUnimodality = new ModalityModel().addMatrix(query, insertedArrayUnimodality);
		String [][] queryWithBimodality = new ModalityModel().addMatrix(query, insertedArrayBimodality);
		
		//Act 
		String result = new ModalityModel().findModalityInArray(queryWithUnimodality);
		String result2 = new ModalityModel().findModalityInArray(queryWithBimodality);
		String result3 = new ModalityModel().findModalityInArray(insertedArrayWithNoModality);
		
		//Assert
		assertEquals(result, "Greece has Unimodality.");
		assertEquals(result2, "Greece has Bimodality.");
		assertEquals(result3, "Greece has no clear Modality.");
	}
	
	@Test
	public void testfindRegressionInArray() {
		//Arrange
		String [][] query = new String[2][3];
		query[0] = new String[]{"Default_row_col_1","Default_row_col_1","Default_row_col_1"};
		query[1] = new String[]{"Default_row_col_2","Default_row_col_2","Default_row_col_2"};
		String [][] insertedArrayForRegression =  {{"1998-01", "Greece","2"}, {"1998-02", "Greece","3"}, {"1998-03", "Greece","5"}, {"1998-04", "Greece","4"}, {"1998-05", "Greece","6"}};
		
		//Act
		String result = new RegressionModel().findRegressionInArray(insertedArrayForRegression);
		
		
		//Assert
		assertEquals(result, "Linear regression for category (" + insertedArrayForRegression[2][1] + ") with intercept: " + "3.0"  + " and slope: " + "0.5");
	}
	
	@Test
	public void testfindTrendModelInArray() {
		//Arrange
		String [][] query = new String[2][3];
		query[0] = new String[]{"Default_row_col_1","Default_row_col_1","Default_row_col_1"};
		query[1] = new String[]{"Default_row_col_2","Default_row_col_2","Default_row_col_2"};
		String [][] insertedArrayForUptrend =  {{"1998-01", "Greece","2"}, {"1998-02", "Greece","3"}, {"1998-03", "Greece","5"}, {"1998-04", "Greece","6"}, {"1998-05", "Greece","7"}};
		String [][] insertedArrayForDowntrend =  {{"1998-01", "Greece","10"}, {"1998-02", "Greece","8"}, {"1998-03", "Greece","7"}, {"1998-04", "Greece","6"}, {"1998-05", "Greece","5"}};
		String [][] insertedArrayNoTrend =  {{"1998-01", "Greece","10"}, {"1998-02", "Greece","8"}, {"1998-03", "Greece","7"}, {"1998-04", "Greece","7.5"}, {"1998-05", "Greece", "5"}};
		
		//Act
		String result = new AbsoluteTrendModel().findTrendInArray(insertedArrayForUptrend);
		String result2 = new AbsoluteTrendModel().findTrendInArray(insertedArrayForDowntrend);
		String result3 = new AbsoluteTrendModel().findTrendInArray(insertedArrayNoTrend);
		
		//Assert
		assertEquals(result, "Greece has an uptrend.");
		assertEquals(result2, "Greece has a downtrend.");
		assertEquals(result3, "Greece has no clear trend.");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
