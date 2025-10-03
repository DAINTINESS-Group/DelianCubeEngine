package analyze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.antlr.runtime.RecognitionException;

import analyze.cubeQueryGenerator.CubeQueryGenerator;
import analyze.cubeQueryGenerator.CubeQueryGeneratorFactory;
import analyze.cubeQueryGenerator.CubeQueryGeneratorFactory.GeneratorType;
import analyze.syntax.AnalyzeParserManager;
import cubemanager.CubeManager;


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
	
	public HashMap<String, String> getDimensions() {
		return dimensions;
	}

	// A hashmap that maps each sigma and gamma level to its child level
	private HashMap<String,String> childToLevelById;
	
	private HashMap<String,String> childToLevelByName;
	
	// A hashmap that maps each sigma and gamma level to its parent level 
	private HashMap<String,String> parentToLevelById;
	
	private HashMap<String,String> parentToLevelByName;
	
	public HashMap<String, String> getParentToLevelByName() {
		return parentToLevelByName;
	}

	// A hashmap that maps each sigma and gamma level to its table name in the relational schema
	private HashMap<String,String> expressionToTableName;
	
	// A hashmap that maps each sigma and gamma level to its description
	private HashMap<String,String> currentLevelToDescriptions;
	
	public HashMap<String, String> getCurrentLevelToDescriptions() {
		return currentLevelToDescriptions;
	}

	//A hashmap that maps each sigma and gamma level to its position in hierarchy
	private HashMap<String,Integer> expressionToPositionInHierarchy;
	
	// Boolean variable that checks if there was errors during the Cube Queries translation
	private boolean cubeQueryGenerationStatus = true;
	
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
	
	public ArrayList<String> getSigmaExpressions(){
		return this.sigmaExpressions;
	}
	
	public HashMap<String, String> getSigmaExpressionsToValues(){
		return this.sigmaExpressionsToValues;
	}
	
	public String getAggrFunc() {
		return this.aggrFunc;
	}
	
	public HashMap<String,Integer> getExpressionToPositionInHierarchy(){
		return expressionToPositionInHierarchy;
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
			System.out.println("Incoming Expression Parsing Time :" + Double.toString(parsingTime/1000000) + " ms");
		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		if(numOfErrors != 0) {
			return false;
		}
		return true;
	}
	
	public boolean getCubeQueryGenerationStatus() {
		return cubeQueryGenerationStatus;
	}
	
	/**
	 * Method that create cubeQueryGenerator objects via a cubeQueryGeneratorFactory and concatenates the
	 * returned queries into one ArrayList. If any type of query generator returns empty ArrayList then something 
	 * went wrong and the status of the translation process becomes false.
	 * @return An ArrayList&ltCubeQuery&gt object that contains the Base CubeQuery, the Drill-Downs and the Siblings
	 * of the Base CubeQuery.
	 */
	public ArrayList<AnalyzeQuery> translateToCubeQueries(){
		ArrayList<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
		CubeQueryGeneratorFactory cubeQueryGeneratorFactory = new CubeQueryGeneratorFactory();
		
		//set up variables for translation
		setUpTranslation();
		
		//translate to Base Query
		CubeQueryGenerator queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.BASE, cubeManager);
		ArrayList<AnalyzeQuery> baseQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(baseQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(baseQueries);
		
		// translate to Sibling queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.IAKOVIDISSIBLINGS, cubeManager);
		ArrayList<AnalyzeQuery> siblingQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(siblingQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(siblingQueries);
		
		// translate to Drill-Down queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.IAKOVIDISDRILLDOWNS, cubeManager);
		ArrayList<AnalyzeQuery> drillDownQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(drillDownQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(drillDownQueries);
		
		return analyzeQueries;
	}
	
	public ArrayList<AnalyzeQuery> translateToUpdatedCubeQueries(){
		ArrayList<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
		CubeQueryGeneratorFactory cubeQueryGeneratorFactory = new CubeQueryGeneratorFactory();
		
		//set up variables for translation
		setUpTranslation();
		
		//translate to Base Query
		CubeQueryGenerator queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.BASE, cubeManager);
		ArrayList<AnalyzeQuery> baseQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(baseQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(baseQueries);
		
		// translate to Sibling queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.UPDATEDSIBLINGS, cubeManager);
		ArrayList<AnalyzeQuery> siblingQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(siblingQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(siblingQueries);
		
		// translate to Drill-Down queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.TRADITIONALDRILLDOWNS, cubeManager);
		ArrayList<AnalyzeQuery> drillDownQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(drillDownQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(drillDownQueries);
		
		/*
		// translate to Cinecubes Drill-Down queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.CINECUBESDRILLDOWNS, cubeManager);
		ArrayList<AnalyzeQuery> cinecubesDrillDownQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(drillDownQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(cinecubesDrillDownQueries);
		*/
		return analyzeQueries;
	}
	
	public ArrayList<AnalyzeQuery> translateToOptimizedSingleCubeQueries(){
		ArrayList<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
		CubeQueryGeneratorFactory cubeQueryGeneratorFactory = new CubeQueryGeneratorFactory();
		
		//set up variables for translation
		setUpTranslation();
		
		//translate to Multi-Query
		CubeQueryGenerator queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.SINGLEQUERYOPTIMIZER, cubeManager);
		analyzeQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(analyzeQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		
		return analyzeQueries;
	}

	public ArrayList<AnalyzeQuery> translateToOptimizedDuoCubeQueries() {
		ArrayList<AnalyzeQuery> analyzeQueries = new ArrayList<AnalyzeQuery>();
		CubeQueryGeneratorFactory cubeQueryGeneratorFactory = new CubeQueryGeneratorFactory();
		
		//set up variables for translation
		setUpTranslation();
		
		//translate to Multi-Query
		// translate to Base and Drill-Down queries
		CubeQueryGenerator queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.DUOQUERYDRILLDOWNSOPTIMIZER, cubeManager);
		ArrayList<AnalyzeQuery> baseDrillDownQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(baseDrillDownQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(baseDrillDownQueries);
		
		// translate to Siblings queries
		queryGenerator = cubeQueryGeneratorFactory.getCubeQueryGenerator(GeneratorType.UPDATEDSIBLINGS, cubeManager);
		ArrayList<AnalyzeQuery> siblingQueries = queryGenerator.generateCubeQueries(aggrFunc, measure,cubeName,sigmaExpressions,sigmaExpressionsToValues,gammaExpressions,queryAlias,dimensions,childToLevelById,childToLevelByName,parentToLevelById,parentToLevelByName,expressionToTableName,currentLevelToDescriptions,schemaName,connectionType);
		if(siblingQueries.isEmpty()) {
			cubeQueryGenerationStatus = false;
		}
		analyzeQueries.addAll(siblingQueries);

		
		return analyzeQueries;
	}
	
}

