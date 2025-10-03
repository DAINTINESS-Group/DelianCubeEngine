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
		IAKOVIDISSIBLINGS{
			@Override
			public SiblingsQueryGenerator getInstance(CubeManager cubeManager) {
				return new SiblingsQueryGenerator(cubeManager);
			}
		},
		IAKOVIDISDRILLDOWNS{
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
		},
		SINGLEQUERYOPTIMIZER{
			@Override
			public MaxMQOQueryOptimizerGenerator getInstance(CubeManager cubeManager) {
				return new MaxMQOQueryOptimizerGenerator(cubeManager);
			}
		},
		DUOQUERYSIBLINGSOPTIMIZER{
			@Override
			public MidMQOQueryOptimizerSiblingsGenerator getInstance(CubeManager cubeManager) {
				return new MidMQOQueryOptimizerSiblingsGenerator(cubeManager);
			}
		},
		DUOQUERYDRILLDOWNSOPTIMIZER{
			@Override
			public MidMQOQueryOptimizerDrillDownsGenerator getInstance(CubeManager cubeManager) {
				return new MidMQOQueryOptimizerDrillDownsGenerator(cubeManager);
			}
		};
		public abstract CubeQueryGenerator getInstance(CubeManager cubeManager);
	}
	public CubeQueryGeneratorFactory() {}
	
	public CubeQueryGenerator getCubeQueryGenerator(GeneratorType type,CubeManager cubeManager) {
		return type.getInstance(cubeManager);
	}
}