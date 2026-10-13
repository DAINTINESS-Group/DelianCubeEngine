package cubemanager.queryoptimizer.statistics;


import java.util.Random;

/**
 * A reservoir sampling algorithm. Implementations draw a simple random sample of
 * surrogate keys from the range 1..populationSize without accessing the database.
 * Only the selected rows are fetched afterward, by key
 */
public interface IReservoirSampler {

	/**
	 * Draws a simple random sample of keys from 1...populationSize
	 *
	 * @param populationSize the number of rows of the fact table
	 * @param reservoirSize  the number of keys to keep
	 * @param random
	 * @return the sampled keys, trimmed if population is smaller than the requested sample
	 */
	int[] sample(int populationSize, int reservoirSize, Random random);
}
