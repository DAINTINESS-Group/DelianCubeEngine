package analyze.cubeQueryGenerator;

/**
 * A factory class to create CubeQueryGenerator objects
 * @author mariosjkb
 */
import cubemanager.CubeManager;

public class CubeQueryGeneratorFactory {
	public enum GeneratorType {
		Base{
			@Override
			public BaseQueryGenerator getInstance(CubeManager cubeManager) {
				return new BaseQueryGenerator(cubeManager);
			}
		},
		Siblings{
			@Override
			public SiblingsQueryGenerator getInstance(CubeManager cubeManager) {
				return new SiblingsQueryGenerator(cubeManager);
			}
		},
		Drill_Downs{
			@Override
			public DrillDownsQueryGenerator getInstance(CubeManager cubeManager) {
				return new DrillDownsQueryGenerator(cubeManager);
			}
		};
		public abstract CubeQueryGenerator getInstance(CubeManager cubeManager);
	}
	public CubeQueryGeneratorFactory() {}
	
	public CubeQueryGenerator getCubeQueryGenerator(GeneratorType type,CubeManager cubeManager) {
		return type.getInstance(cubeManager);
	}
}