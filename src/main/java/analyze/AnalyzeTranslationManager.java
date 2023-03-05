package analyze;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;

import analyze.cubeQueryGenerator.CubeQueryGenerator;
import analyze.cubeQueryGenerator.CubeQueryGeneratorFactory;
import analyze.syntax.AnalyzeParserManager;
import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;

/**
 * A class that manages the translation process of an analyze query to its respective CubeQueries
 * @author mariosjkb
 */

public class AnalyzeTranslationManager {
	
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
	private HashMap<String,String> childToLevel;
	
	// A hashmap that maps each sigma and gamma level to its parent level 
	private HashMap<String,String> parentToLevel;
	
	// A hashmap that maps each sigma and gamma level to its table name in the relational schema
	private HashMap<String,String> expressionToTableName;
	
	// A hashmap that maps each sigma and gamma level to its description
	private HashMap<String,String> currentLevelToDescriptions;
	
	/**
	 * Constructor method that initializes the AnalyzeTranslationManager fields
	 * @param incomingExpression
	 * @param cubeManager
	 */
	public AnalyzeTranslationManager(String incomingExpression,CubeManager cubeManager, String schemaName, String connectionType) {
		this.incomingExpression = incomingExpression;
		this.cubeManager = cubeManager;
		this.schemaName = schemaName;
		this.connectionType = connectionType;
		this.analyzeParserManager = new AnalyzeParserManager();
		this.dimensions = new HashMap<String,String>();
		this.childToLevel = new HashMap<String,String>();
		this.parentToLevel = new HashMap<String,String>();
		this.expressionToTableName = new HashMap<String,String>();
		this.currentLevelToDescriptions = new HashMap<String,String>();
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
		String child;
		String parent;
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
							child =  cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k-1).getLevelDescriptionAttribute();
						}else {
							child = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(0).getLevelDescriptionAttribute();
						}
						
						if(positionInHierarchy < cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().size() - 1) {
							parent = cubeManager.getDimensions().get(j).getHierarchy().get(0).getLevels().get(k+1).getLevelDescriptionAttribute();
							if(parent.equals("All")) {
								parent = "All_" + table;
							}
						}else {
							parent = expressions.get(i);
						}
						dimensions.put(expressions.get(i), dimension);
						childToLevel.put(expressions.get(i), child);
						parentToLevel.put(expressions.get(i), parent);
						expressionToTableName.put(expressions.get(i), table);
						currentLevelToDescriptions.put(expressions.get(i), currentLevelDescription);
					}
				}
			}
		}
	}	

	
	/**
	 * Method that parses the incoming expression and checks if it contains syntax errors.
	 * @return 0 if no errors found, else it returns -1 
	 */
	public int validateIncomingExpression() {
		int numOfErrors = 0;
		
		// parse the incoming expression and find the number of syntax errors
		try {
			numOfErrors = analyzeParserManager.parse(incomingExpression);
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		if(numOfErrors != 0) {
			return -1;
		}
		return 0;
	}
	
	/**
	 * Method that create cubeQueryGenerator objects via a cubeQueryGeneratorFactory and concats the
	 * returned queries into one ArrayList.
	 * @return An ArrayList&ltCubeQuery&gt object that contains the Base CubeQuery, the Drill-Downs and the Siblings
	 * of the Base CubeQuery.
	 */
	public ArrayList<CubeQuery> translateToCubeQueries(){
		ArrayList<CubeQuery> analyzeCubeQueries = new ArrayList<CubeQuery>();
		CubeQueryGeneratorFactory cubeQueryGeneratorFactory = new CubeQueryGeneratorFactory();
		
		//set up variables for translation
		getAnalyzeQueryInfo();
		getExpressionInfoFromCube();
		
		//translate to Base Query
		CubeQueryGenerator queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator("Base", cubeManager);
		ArrayList<CubeQuery> baseQueries = queryGenerator.generateCubeQueries(   aggrFunc,
																				 measure,
																				 cubeName,
																				 sigmaExpressions,
																				 sigmaExpressionsToValues,
																				 gammaExpressions,
																				 queryAlias,
																				 dimensions,
																				 childToLevel,
																				 parentToLevel,
																				 expressionToTableName,
																				 currentLevelToDescriptions,
																				 schemaName,
																				 connectionType);
		
		analyzeCubeQueries.addAll(baseQueries);
		
		// translate to Sibling queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator("Siblings", cubeManager);
		ArrayList<CubeQuery> siblingQueries = queryGenerator.generateCubeQueries(aggrFunc,
																				 measure, 
																				 cubeName, 
																				 sigmaExpressions, 
																				 sigmaExpressionsToValues, 
																				 gammaExpressions,
																				 queryAlias,
																				 dimensions,
																				 childToLevel, 
																				 parentToLevel, 
																				 expressionToTableName,
																				 currentLevelToDescriptions,
																				 schemaName,
																				 connectionType);
		analyzeCubeQueries.addAll(siblingQueries);
		
		// translate to Drill-Down queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator("Drill-Downs", cubeManager);
		ArrayList<CubeQuery> drillDownQueries = queryGenerator.generateCubeQueries(aggrFunc,
																				 measure, 
																				 cubeName, 
																				 sigmaExpressions, 
																				 sigmaExpressionsToValues, 
																				 gammaExpressions,
																				 queryAlias,
																				 dimensions,
																				 childToLevel, 
																				 parentToLevel, 
																				 expressionToTableName,
																				 currentLevelToDescriptions,
																				 schemaName,
																				 connectionType);
		// concatenate the results to one ArrayList
		analyzeCubeQueries.addAll(drillDownQueries);
		return analyzeCubeQueries;
		
		
	}
	
}
