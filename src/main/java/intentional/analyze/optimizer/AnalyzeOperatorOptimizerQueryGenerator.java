package intentional.analyze.optimizer;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;

import cubemanager.CubeManager;
import intentional.analyze.AnalyzeQuery;
import intentional.analyze.cubeQueryGenerator.CubeQueryGenerator;
import intentional.analyze.cubeQueryGenerator.CubeQueryGeneratorFactory;
import intentional.analyze.cubeQueryGenerator.CubeQueryGeneratorFactory.GeneratorType;
import intentional.analyze.syntax.AnalyzeParserManager;

/**
 * A simplyfied version of AnalyzeTranslationManager to construct only the facilitator
 * queries necessary for the Analyze Optimizer Decision Rule.
 * @author mariosjkb
 *
 */
public class AnalyzeOperatorOptimizerQueryGenerator {
	// The expression of the analyze query
		private String incomingExpression;
		
		// Manager object to manage the cube
		private CubeManager cubeManager;
		
		// The name of the dataset loaded on Delian
		private String schemaName;
		
		// Spark or RDBMS connection
		private String connectionType;
		
		// An object that manages the parsing of the incoming expression 
		private AnalyzeParserManager analyzeParserManager;
		
		// Aggregate Function of the analyze query
		private String aggrFunc;
		
		// Measure of the analyze query
		private String measure;
		
		// Cube that the analyze query uses
		private String cubeName;
		
		// Alias to be used for naming the Cube Queries
		private String queryAlias;
		
		// Sigma expressions of the analyze query
		private ArrayList<String> sigmaExpressions;
		
		// The values of the sigma expressions
		private HashMap<String,String> sigmaExpressionsToValues;
		
		// Gamma expressions of the analyze query
		private ArrayList<String> gammaExpressions;
		
		// A hashmap with the dimension each sigma and gamma expression belongs
		private HashMap<String,String> dimensions;

		// A hashmap that maps each sigma and gamma level to its child level
		private HashMap<String,String> childToLevelById;
		
		private HashMap<String,String> childToLevelByName;
		
		// A hashmap that maps each sigma and gamma level to its parent level 
		private HashMap<String,String> parentToLevelById;
		
		private HashMap<String,String> parentToLevelByName;

		// A hashmap that maps each sigma and gamma level to its table name in the relational schema
		private HashMap<String,String> expressionToTableName;
		
		// A hashmap that maps each sigma and gamma level to its description
		private HashMap<String,String> currentLevelToDescriptions;

		//A hashmap that maps each sigma and gamma level to its position in hierarchy
		private HashMap<String,Integer> expressionToPositionInHierarchy;
		
		// Boolean variable that checks if there was errors during the Cube Queries translation
		private boolean cubeQueryGenerationStatus = true;
		
		/**
		 * Constructor method that initializes the AnalyzeTranslationManager fields
		 * @param incomingExpression
		 * @param cubeManager
		 */
		public AnalyzeOperatorOptimizerQueryGenerator(String incomingExpression, CubeManager cubeManager, String schemaName, String connectionType) {
			this.incomingExpression = incomingExpression;
			this.cubeManager = cubeManager;
			this.schemaName = schemaName;
			this.connectionType = connectionType;
			this.analyzeParserManager = new AnalyzeParserManager();
			this.dimensions = new HashMap<String,String>();
			this.childToLevelById = new HashMap<String,String>();
			this.childToLevelByName = new HashMap<String,String>();
			this.parentToLevelById = new HashMap<String,String>();
			this.parentToLevelByName = new HashMap<String,String>();
			this.expressionToTableName = new HashMap<String,String>();
			this.currentLevelToDescriptions = new HashMap<String,String>();
			this.expressionToPositionInHierarchy = new HashMap<String,Integer>();
		}
		
		/**
		 * Method that receives the information about the analyze query after the incoming expression parsing
		 */
		private void getAnalyzeQueryInfo() {
			this.aggrFunc = analyzeParserManager.getAggrFunc();
			this.measure = analyzeParserManager.getMeasure();
			this.cubeName = analyzeParserManager.getCubeName();
			this.sigmaExpressions = analyzeParserManager.getSigmaExpressions();
			this.sigmaExpressionsToValues = analyzeParserManager.getSigmaExpressionsValues();
			System.out.println(this.incomingExpression);
			this.gammaExpressions = analyzeParserManager.getGammaExpressions();
			this.queryAlias = analyzeParserManager.getQueryAlias();
		}
		
		/**
		 * Method that obtains all the necessary information about the analyze query sigma and gamma levels from
		 * the cube with only one parse of the cube's dimensions.  
		 */
		private void getExpressionInfoFromCube() {
			ArrayList<String> expressions = new ArrayList<String>();
			String dimension = null;
			String childById;
			String childByName;
			String parentById;
			String parentByName;
			String currentLevelDescription;
			
			// collect all the sigma and gamma expressions into one collection
			expressions.addAll(sigmaExpressions);
			expressions.addAll(gammaExpressions);
		
			// for each expression parse the cube and get its dimension, currentLevelDescription, child, parent and expressionToTableName
			for(int i = 0;i < expressions.size();i++) {
				for(int j = 0;j < cubeManager.getDimensions().size();j++) {
					for(int k = 0;k < cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().size();k++) {
						String table = cubeManager.getDimensions().get(j).getTableName();
						String hierarchy_level = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getName();
						int positionInHierarchy = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getPositionInHierarchy();
						if(hierarchy_level.equals(expressions.get(i))) {
							dimension = cubeManager.getDimensions().get(j).getName();
							currentLevelDescription = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k).getLevelDescriptionAttribute();
							
							if(positionInHierarchy != 0) {
								childById =  cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k-1).getLevelDescriptionAttribute();
								childByName = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k-1).getName();
							}else {
								childById = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(0).getLevelDescriptionAttribute();
								childByName = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(0).getName();
							}
							
							if(positionInHierarchy < cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().size() - 1) {
								parentById = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k+1).getLevelDescriptionAttribute();
								parentByName = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k+1).getName();
								if(parentById.equals("All")) {
									parentById = "All_" + table;
									parentByName = parentById;
								}
							}else {
								parentById = expressions.get(i);
								parentByName = parentById;
							}
							dimensions.put(expressions.get(i), dimension);
							childToLevelById.put(expressions.get(i), childById);
							childToLevelByName.put(expressions.get(i), childByName);
							parentToLevelById.put(expressions.get(i), parentById);
							parentToLevelByName.put(expressions.get(i), parentByName);
							expressionToTableName.put(expressions.get(i), table);
							currentLevelToDescriptions.put(expressions.get(i), currentLevelDescription);
							expressionToPositionInHierarchy.put(expressions.get(i), positionInHierarchy);
						}
					}
				}
			}
		}	
		
		
		public HashMap<String, String> getDimensions() {
			return dimensions;
		}

		public HashMap<String, String> getCurrentLevelToDescriptions() {
			return currentLevelToDescriptions;
		}
		
		public HashMap<String, String> getParentToLevelByName() {
			return parentToLevelByName;
		}
		
		public ArrayList<String> getSigmaExpressions(){
			return this.sigmaExpressions;
		}
		
		public HashMap<String, String> getSigmaExpressionsToValues(){
			return this.sigmaExpressionsToValues;
		}
		

		public void setUpTranslation() {
			getAnalyzeQueryInfo();
			getExpressionInfoFromCube();
		}
		
		/**
		 * Method that parses the incoming expression and checks if it contains syntax errors.
		 * @return true if no errors found, else it returns false 
		 */
		public boolean validateIncomingExpression() {
			int numOfErrors = 0;
			
			// parse the incoming expression and find the number of syntax errors
			try {
				long startTime = System.nanoTime();
				numOfErrors = analyzeParserManager.parse(incomingExpression);
				long endTime = System.nanoTime();
				double parsingTime = endTime - startTime;
				System.out.println("$$ Incoming Expression Parsing Time \t\t\t" + Double.toString(parsingTime/1000000));// + " ms");
			} catch (RecognitionException e) {
				e.printStackTrace();
			}
			if(numOfErrors != 0) {
				return false;
			}
			return true;
		}
		
		/**
		 * Produce the Siblings and the all-encompassing facilitator queries
		 * @return
		 */
		public ArrayList<AnalyzeQuery> translateToAnalyzeQueries(){
			long startTime = System.nanoTime();
			ArrayList<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
			CubeQueryGeneratorFactory cubeQueryGeneratorFactory = new CubeQueryGeneratorFactory();
			
			
			//set up variables for translation
			setUpTranslation();
			
			CubeQueryGenerator queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.UPDATEDSIBLINGS, cubeManager);
			ArrayList<AnalyzeQuery> siblingQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
			if(siblingQueries.isEmpty()) {
				cubeQueryGenerationStatus = false;
			}
			analyzeQueries.addAll(siblingQueries);
			
			queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.SINGLEQUERYOPTIMIZER, cubeManager);
			analyzeQueries.addAll(queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType));
			if(analyzeQueries.isEmpty()) {
				cubeQueryGenerationStatus = false;
			}
			long endTime = System.nanoTime();
			double totalTimeInMs = (double)(endTime - startTime)/1000000;
			System.out.println("$Facililator Query Generation: " + totalTimeInMs);
			return analyzeQueries;
		}
}
