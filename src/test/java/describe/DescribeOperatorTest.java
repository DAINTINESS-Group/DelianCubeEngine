package describe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.antlr.runtime.RecognitionException;
import org.junit.Before;
import org.junit.Test;

import describe.syntax.DescribeParserManager;

public class DescribeOperatorTest {
	
	private DescribeParserManager manager;

	@Before
	public void setUp() {
		manager = new DescribeParserManager();
	}
	
	@Test
	public void testComplexQueryWithMathSetsAndGrouping() throws RecognitionException{
		String query = "WITH SalesCube " +
		                "DESCRIBE SUM(Profit), Revenue - Cost " + 
		                "FOR Year > 2000 AND Region IN {Europe, Asia} " + 
		                "GROUP BY Country, Product";
		
		int syntaxErrors = manager.parse(query);
		DescribeQuery result = manager.getQuery();
				
		assertEquals("Parsing failed with syntax errors", 0, syntaxErrors);
		
		assertEquals("Cube name mismatch", "SalesCube", result.getCubeName());
		
		assertEquals("Should find 2 measures", 2, result.getMeasureList().size());
        assertTrue("Missing SUM(Profit)", result.getMeasureList().contains("SUM(Profit)"));
        assertTrue("Missing Revenue - Cost", result.getMeasureList().contains("Revenue - Cost"));
        
        assertTrue("Missing year filter", result.getSigmaExpressions().contains("Year>2000"));
        assertTrue("Missing Region filter", result.getSigmaExpressions().contains("Region IN {Europe, Asia}"));
        
        assertEquals("Should find 2 groupers", 2, result.getGammaExpressions().size());
        assertTrue("Missing Country grouper", result.getGammaExpressions().contains("Country"));
        assertTrue("Missing Product grouper", result.getGammaExpressions().contains("Product"));
		
	}
	
	@Test
	public void TestNestedFilter() throws RecognitionException{
		String query = "WITH Loan " +
		                "DESCRIBE AVG(Amount) " +
		                "FOR Customer WITH Income > 50000"; 

		int syntaxErrors = manager.parse(query);
		DescribeQuery result = manager.getQuery();
		
		assertEquals("Parsing failed", 0, syntaxErrors);
        assertEquals("Loan", result.getCubeName());
        
        String expectedFilter = "Customer WITH Income > 50000";
        assertTrue("Should contain nested filter", result.getSigmaExpressions().contains(expectedFilter));      
	}
	
	@Test
    public void testJoinQuery() throws RecognitionException {
        String query = "WITH Sales " + 
                       "DESCRIBE Revenue " + 
                       "JOIN " + 
                       "WITH Marketing " + 
                       "DESCRIBE Cost " + 
                       "ON ProductID";

        int syntaxErrors = manager.parse(query);
    	DescribeQuery result = manager.getQuery();
        
        assertEquals("Parsing failed", 0, syntaxErrors);
        
        assertNotNull("Join Type should not be null", result.getJoinType());
        assertEquals("Join Type mismatch", "JOIN", result.getJoinType());
        assertEquals("Join Condition mismatch", "ProductID", result.getJoinCondition());
        
        assertEquals("Sales JOIN Marketing", result.getCubeName());
        
        assertEquals("Should have 2 measures", 2, result.getMeasureList().size());
        assertTrue("Should contain Revenue", result.getMeasureList().contains("Revenue"));
        assertTrue("Should contain Cost", result.getMeasureList().contains("Cost"));
	}
	
	@Test 
	public void testUlimateQuery() throws RecognitionException {
		String ultimateQuery = 
				"WITH SalesCube " +
	            "DESCRIBE SUM(Profit), Revenue - (Cost * 1.2) " +  // 1. Complex Measures
	            "FOR Year > 2022 " +                               // 2. Simple Filter
	            "AND Country IN {USA, Canada} " +                  // 3. Set Filter
	            "AND Customer WITH Status = 'Gold' " +             // 4. Nested Filter
	            "GROUP BY Region, Category " +                     // 5. Grouping
	            "ORDER BY Profit DESC " +                          // 6. Ordering
	            "USING Outliers, SkyLine " +                       // 7. Models
	            "AS Q1 " +                                         // 8. Alias
	            "JOIN " +                                          // 9. Join Operation
	            "WITH BudgetCube " +
	            "DESCRIBE Target " +
	            "ON Region";
		
		int syntaxErrors = manager.parse(ultimateQuery);
		DescribeQuery result = manager.getQuery();
		
		assertEquals("Parsing failed", 0, syntaxErrors);
		
		assertEquals("SalesCube JOIN BudgetCube", result.getCubeName());
		
		assertEquals("Should have 3 measures", 3, result.getMeasureList().size());
        assertTrue(result.getMeasureList().contains("SUM(Profit)"));
		
        assertTrue("Missing derived measure", result.getMeasureList().stream().anyMatch(m -> m.contains("Revenue") && m.contains("Cost")));
        assertTrue("Missing second cube measure", result.getMeasureList().contains("Target"));
		
        assertEquals("Should have 3 filters", 3, result.getSigmaExpressions().size());
        assertTrue("Missing Year filter", result.getSigmaExpressions().contains("Year>2022"));
        assertTrue("Missing Set filter", result.getSigmaExpressions().contains("Country IN {USA, Canada}"));
        assertTrue("Missing Nested filter", result.getSigmaExpressions().stream().anyMatch(f -> f.startsWith("Customer WITH Status")));
        
        assertEquals(2, result.getGammaExpressions().size());
        assertTrue(result.getGammaExpressions().contains("Region"));
        assertTrue(result.getGammaExpressions().contains("Category"));
        
        assertEquals(1, result.getOrderExpressions().size());
        assertTrue(result.getOrderExpressions().contains("Profit DESC"));
        
        assertEquals(2, result.getModelList().size());
        assertTrue(result.getModelList().contains("Outliers"));
        assertTrue(result.getModelList().contains("SkyLine"));
        
        assertEquals("Q1", result.getQueryAlias());
        
        assertEquals("JOIN", result.getJoinType());
        assertEquals("Region", result.getJoinCondition());  
	}
}
