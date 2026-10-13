package cubemanager.queryoptimizer.statistics;

/**
 * A factory that creates reservoir sampling algorithms by name
 */
public class ReservoirSamplerFactory {

	/**
	 * Creates the sampler for the given algorithm name
	 * @param algorithm For now, one of {@code R} or {@code L}
	 * @return the sampler for that algorithm
	 */
	public static IReservoirSampler create(String algorithm) {
		switch (algorithm.toUpperCase()) {
			case "R" :
				return new AlgorithmRSampler();
			case "L" :
				return new AlgorithmLSampler();
			default:
				throw new IllegalArgumentException("Unknown sampling algorithm: " + algorithm);
		}
	}
}
