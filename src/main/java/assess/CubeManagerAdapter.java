package assess;

import cubemanager.CubeManager;
import cubemanager.cubebase.Cube;
import cubemanager.cubebase.CubeQuery;
import result.Result;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

// Author Note: I hate myself for the amount of mutators on this class

/**
 * This class collects the various data needed to define a QueryString for the
 * CubeManager and issues it.
 */
public class CubeManagerAdapter {
	private final CubeManager cubeManager;
	private String targetCubeName;
	private Map<String, String> selectionPredicates;

	private String aggregationFunction;
	private String measurement;
	private Set<String> groupBySet;

	public CubeManagerAdapter(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}

	public void setTargetCubeName(String targetCubeName) {
		this.targetCubeName = targetCubeName;
	}

	public void setSelectionPredicates(Map<String, String> selectionPredicates) {
		this.selectionPredicates = selectionPredicates;
	}

	public Map<String, String> getSelectionPredicates() {
		return selectionPredicates;
	}

	public void updateSelectionPredicate(String key, String value) {
		selectionPredicates.put(key, value);
	}

	public void setAggregationFunction(String aggregationFunction) {
		this.aggregationFunction = aggregationFunction;
	}

	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}

	public void setGroupBySet(Set<String> groupBySet) {
		this.groupBySet = groupBySet;
	}

	public CubeQuery translateToCubeQuery() {
		try {
			return cubeManager.createCubeQueryFromString(generateQuery(), new HashMap<>());
		} catch (RemoteException re) {
			throw new RuntimeException(
					"Failed to issue AssessQuery to the CubeManager\n" + re);
		}
	}

	public Result executeCubeQuery(CubeQuery cb) {
		return cubeManager.executeQuery(cb);
	}

	/**
	 * Creates the query for the CubeManager
	 * @return the formatted query
	 */
	private String generateQuery() {
        return new StringJoiner("\n")
				.add("CubeName:" + targetCubeName)
				.add("Name:" + targetCubeName + "_" + measurement)
				.add("AggrFunc:" + aggregationFunction)
				.add("Measure:" + measurement)
				.add("Gamma:" + collectGammaLevels(targetCubeName + "_cube"))
				.add("Sigma:" + collectSigmaLevels(targetCubeName + "_cube"))
				.toString();
	}

	private String collectSigmaLevels(String targetCubeName) {
		Cube targetCube = cubeManager.getCubeByName(targetCubeName);
		StringJoiner stringJoiner = new StringJoiner(",");
		selectionPredicates.forEach((key, value) -> {
			String result = targetCube.findLevelByName(key);
			result += "='" + value + "'";
			stringJoiner.add(result);
		});
		return stringJoiner.toString();
	}

	private String collectGammaLevels(String targetCubeName) {
		Cube targetCube = cubeManager.getCubeByName(targetCubeName);
		StringJoiner stringJoiner = new StringJoiner(",");
		groupBySet.forEach(attribute ->
				stringJoiner.add(targetCube.findLevelByName(attribute)));
		return stringJoiner.toString();
	}
}
