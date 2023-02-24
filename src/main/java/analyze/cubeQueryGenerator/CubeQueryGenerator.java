package analyze.cubeQueryGenerator;

import java.util.ArrayList;
import java.util.HashMap;

import cubemanager.cubebase.CubeQuery;

/**
 * An interface that implements the generateCubeQueries method
 * @author mariosjkb
 *
 */
public interface CubeQueryGenerator {
	public abstract ArrayList<CubeQuery> generateCubeQueries(String aggrFunc,
															 String measure,
															 String cubeName,
															 ArrayList<String> sigmaExpressions,
															 HashMap<String,String> sigmaExpressionsToValues,
															 ArrayList<String> gammaExpressions,
															 String queryAlias,
															 HashMap<String,String> dimensions,
															 HashMap<String,String> childToLevel,
															 HashMap<String,String> parentToLevel,
															 HashMap<String,String> expressionToTableName,
															 HashMap<String,String> currentLevelToDescriptions,
															 String schemaName);
}
