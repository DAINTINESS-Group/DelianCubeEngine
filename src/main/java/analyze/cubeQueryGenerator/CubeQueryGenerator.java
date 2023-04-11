package analyze.cubeQueryGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import analyze.AnalyzeQuery;

/**
 * An interface that implements the generateCubeQueries method
 * @author mariosjkb
 *
 */
public interface CubeQueryGenerator {
	public abstract ArrayList<AnalyzeQuery> generateCubeQueries(String aggrFunc,
															 String measure,
															 String cubeName,
															 ArrayList<String> sigmaExpressions,
															 HashMap<String,String> sigmaExpressionsToValues,
															 ArrayList<String> gammaExpressions,
															 String queryAlias,
															 HashMap<String,String> dimensions,
															 HashMap<String,String> childToLevelById,
															 HashMap<String,String> childToLevelByName,
															 HashMap<String,String> parentToLevelById,
															 HashMap<String,String> parentToLevelByName,
															 HashMap<String,String> expressionToTableName,
															 HashMap<String,String> currentLevelToDescriptions,
															 String schemaName,
															 String connectionType);
}
