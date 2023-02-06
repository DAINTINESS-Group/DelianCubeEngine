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
															 HashMap<String,String> sigmaExpressionsValues,
															 ArrayList<String> gammaExpressions,
															 HashMap<String,String> dimensions,
															 HashMap<String,String> childrenLevels,
															 HashMap<String,String> parentLevels,
															 HashMap<String,String> tableName,
															 HashMap<String,String> currentLevelDescriptions);
}
