package cubemanager.statistics.selectivity;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;

import java.util.*;


public class FilterEstimator {

	private int factTableSize;
	private int sampleSize;

	private HashMap<SelectivityCustomKey,Integer> selectivities = new HashMap<SelectivityCustomKey,Integer>();
	

	public FilterEstimator(CubeManager cubeManager) {
		this.factTableSize = cubeManager.getFactTableSize();
		this.sampleSize = cubeManager.getSampleSize();
		this.selectivities = cubeManager.getSelectivity();
	}

	public List<SelectivityResult> estimate(CubeQuery query) {
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
			Integer numOfDetailedTuples = selectivities.get(new SelectivityCustomKey(parsed.filterCol, sigma[2].substring(1, sigma[2].length() - 1)));			
			if(numOfDetailedTuples == null) {
				numOfDetailedTuples = 0;
			}
			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, sampleSize, numOfDetailedTuples));
		}
		return results;
	}
}