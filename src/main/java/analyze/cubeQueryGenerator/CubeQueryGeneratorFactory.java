package analyze.cubeQueryGenerator;

/**
 * A factory class to create CubeQueryGenerator objects
 * @author mariosjkb
 */
import cubemanager.CubeManager;

public class CubeQueryGeneratorFactory {
	public enum GeneratorType {
		BASE{
			@Override
			public BaseQueryGenerator getInstance(CubeManager cubeManager) {
				return new BaseQueryGenerator(cubeManager);
			}
		},
		SIBLINGSOLD{
			@Override
			public SiblingsQueryGenerator getInstance(CubeManager cubeManager) {
				return new SiblingsQueryGenerator(cubeManager);
			}
		},
		DRILLDOWNS{
			@Override
			public DrillDownsQueryGenerator getInstance(CubeManager cubeManager) {
				return new DrillDownsQueryGenerator(cubeManager);
			}
		},
		UPDATEDSIBLINGS{
			@Override
			public UpdatedSiblingsQueryGenerator getInstance(CubeManager cubeManager) {
				return new UpdatedSiblingsQueryGenerator(cubeManager);
			}
		},
		CINECUBESDRILLDOWNS{
			@Override
			public CinecubesDrillDownsQueryGenerator getInstance(CubeManager cubeManager) {
				return new CinecubesDrillDownsQueryGenerator(cubeManager);
			}
		},
		TRADITIONALDRILLDOWNS{
			@Override
			public TraditionalDrillDownsQueryGenerator getInstance(CubeManager cubeManager) {
				return new TraditionalDrillDownsQueryGenerator(cubeManager);
			}
		};
		public abstract CubeQueryGenerator getInstance(CubeManager cubeManager);
	}
	public CubeQueryGeneratorFactory() {}
	
	public CubeQueryGenerator getCubeQueryGenerator(GeneratorType type,CubeManager cubeManager) {
		return type.getInstance(cubeManager);
	}
}