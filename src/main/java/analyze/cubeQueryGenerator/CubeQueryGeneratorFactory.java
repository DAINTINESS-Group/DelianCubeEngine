package analyze.cubeQueryGenerator;

/**
 * A factory class to create CubeQueryGenerator objects
 * @author mariosjkb
 */
import cubemanager.CubeManager;

public class CubeQueryGeneratorFactory {

	public CubeQueryGeneratorFactory() {}
	
	public CubeQueryGenerator getCubeQueryGenerator(String typeOfGenerator, CubeManager cubeManager) {
		if(typeOfGenerator.equals("Base")) {
			return new BaseQueryGenerator(cubeManager);
		}else if(typeOfGenerator.equals("Siblings")) {
			return new SiblingsQueryGenerator(cubeManager);
		}else if(typeOfGenerator.equals("Drill-Downs")) {
			return new DrillDownsQueryGenerator(cubeManager);
		}
		return null;
	}
}
