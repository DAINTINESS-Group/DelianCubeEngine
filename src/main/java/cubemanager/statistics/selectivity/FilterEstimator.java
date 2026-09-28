package cubemanager.statistics.selectivity;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;

import java.util.*;

/**
 * Estimate the selectivity for each selection atom of a given cube query
 * @author mariosjkb
 *
 */
public class FilterEstimator {

	private int factTableSize;
	private int sampleSize;

	private HashMap<String,HashMap<String,Integer>> valueCardinalityProfile = new HashMap<String,HashMap<String,Integer>>();
	

	public FilterEstimator(CubeManager cubeManager) {
		this.factTableSize = cubeManager.getFactTableSize();
		this.sampleSize = cubeManager.getSampleSize();
		this.valueCardinalityProfile = cubeManager.getValueCardinalityProfile();
	}
	
	/**
	 * Estimate the selectivity for each selection atom of a given cube query
	 * @param query
	 * @return List that contains the estimated selectivities for each query atom of the input query
	 */
	public List<SelectivityResult> estimate(CubeQuery query) {
		Integer numOfDetailedTuples;
		List<SelectivityResult> results = new ArrayList<>();
		BasicStoredCube referCube = query.getReferCube();
		String factTable = referCube.getFactTable().getTableName();
		List<Dimension> dimensions = referCube.getDimensionsList();
		List<String> dimRefFields = referCube.getDimensionRefFieldList();

		for (String[] sigma : query.getSigmaExpressions()) {
			SigmaParser.ParsedSigma parsed = SigmaParser.parse(sigma, dimensions, dimRefFields);
			if (parsed == null || factTableSize < 0) {
				continue;
			}
			if(!valueCardinalityProfile.containsKey(parsed.filterCol)) {
				numOfDetailedTuples = 0;
				results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, sampleSize, numOfDetailedTuples));
			}else {
				HashMap<String,Integer> profile = valueCardinalityProfile.get(parsed.filterCol);
				if(!profile.containsKey(sigma[2].substring(1, sigma[2].length() - 1))) {
					numOfDetailedTuples = 0;
					results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, sampleSize, numOfDetailedTuples));
				}else {
					numOfDetailedTuples = profile.get(sigma[2].substring(1, sigma[2].length() - 1));			
					if(numOfDetailedTuples == null) {
						numOfDetailedTuples = 0;
					}else {
						results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, sampleSize, numOfDetailedTuples));
					}
				}
			}
		}
		return results;
	}
}