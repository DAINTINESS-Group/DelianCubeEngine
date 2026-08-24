package intentional.analyze.optimizer.selectivityestimation;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import java.util.*;


public class ReservoirSamplingEstimator {

	private int factTableSize;
	private int sampleSize;

	private HashMap<CustomKey,Integer> selectivities = new HashMap<CustomKey,Integer>();
	

	public ReservoirSamplingEstimator(CubeManager cubeManager) {
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
			System.out.println(selectivities);
			System.out.println(parsed.filterCol);
			System.out.println(sigma[2].substring(1, sigma[2].length() - 1));
			int matchingInSample = selectivities.get(new CustomKey(parsed.filterCol, sigma[2].substring(1, sigma[2].length() - 1)));			
			
			results.add(new SelectivityResult(sigma, factTable, parsed.filterCol, sampleSize, matchingInSample));
		}

		return results;
	}
}