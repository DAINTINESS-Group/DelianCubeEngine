package mainengine;

import java.util.Arrays;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeQuery;

public class DrillDownOperator {

	private CubeQuery oldCubeQuery;
	
	public DrillDownOperator(CubeQuery oldCubeQuery) {
		this.oldCubeQuery = oldCubeQuery;
	}
	
	public ResultFileMetadata validateDrillDown(String dimensionName, String targetLevelName) {
		Integer oldLevelHierarchyPosition = null;
		Integer targetLevelHierarchyPosition = null;
		Boolean dimensionFound = false;
		Boolean targetLevelFound = false;
		ResultFileMetadata resMetadata = new ResultFileMetadata();
		
		BasicStoredCube referCube = oldCubeQuery.getReferCube();
		
		
		for(int i=0; i<referCube.getDimensionsList().size(); i++) {
			//find if dimension exists
			if(referCube.getDimensionsList().get(i).getName().equals(dimensionName)){
				dimensionFound = true;
				
				//find if target level exists in hierarchy
				for(int j=0; j<referCube.getDimensionsList().get(i).getHierarchy().get(0).getLevels().size(); j++) {
					if(referCube.getDimensionsList().get(i).getHierarchy().get(0).getLevels().get(j).getName().equals(targetLevelName)) {
						targetLevelFound = true;
						//find target level's position in hierarchy
						targetLevelHierarchyPosition = referCube.getDimensionsList().get(i).getHierarchy().get(0).getLevels().get(j).getPositionInHierarchy();						
					}
					//find old level's position in hierarchy
					for(int k=0; k<oldCubeQuery.getGammaExpressions().size(); k++) {
						if(oldCubeQuery.getGammaExpressions().get(k)[0].equals(dimensionName)) {
							if(referCube.getDimensionsList().get(i).getHierarchy().get(0).getLevels().get(j).getName().equals(oldCubeQuery.getGammaExpressions().get(k)[1])) {
								oldLevelHierarchyPosition = referCube.getDimensionsList().get(i).getHierarchy().get(0).getLevels().get(j).getPositionInHierarchy();
							}
						}
					}
				}
				if(oldLevelHierarchyPosition==0) {
					System.out.println("Already at the bottom of the dimension hierarchy. Cannot perform Drill-Down");
					resMetadata.setErrorCheckingStatus("Already at the bottom of the dimension hierarchy.");
					return resMetadata;
				}
			}
		}
		if(dimensionFound==false) {
			System.out.println("Dimension was not found. Cannot perform Drill-Down");
			resMetadata.setErrorCheckingStatus("Dimension not found");
			return resMetadata;
		}
		else if(targetLevelFound==false) {
			System.out.println("Target level was not found. Cannot perform Drill-Down");
			resMetadata.setErrorCheckingStatus("Level not found");
			return resMetadata;
		}
		else if(targetLevelHierarchyPosition>oldLevelHierarchyPosition) {
			System.out.println("Target level is not lower than the current level in the dimension hierarchy. Cannot perfom Drill-Down");
			resMetadata.setErrorCheckingStatus("Target level is not lower than the current level in the dimension hierarchy.");
			return resMetadata;
		}
		else {
			resMetadata.setErrorCheckingStatus("");
		}
		return resMetadata;
	}

	
	public CubeQuery executeDrillDown(String oldQueryName, String newQueryName, String dimensionName, String targetLevelName) {
		CubeQuery cubeQuery = new CubeQuery(oldCubeQuery);
		cubeQuery.setName(newQueryName);
		for(int i=0; i<cubeQuery.getGammaExpressions().size(); i++) {
			if(cubeQuery.getGammaExpressions().get(i)[0].equals(dimensionName)) {
				
				System.out.println("Before the Drill-down: "+Arrays.toString(cubeQuery.getGammaExpressions().get(i)));				
				//1. Perform the Drill-Down
				cubeQuery.getGammaExpressions().get(i)[1] = targetLevelName;
				System.out.println("After the Drill-down: "+Arrays.toString(cubeQuery.getGammaExpressions().get(i)));
			}
		}
		return cubeQuery;
	}
}
