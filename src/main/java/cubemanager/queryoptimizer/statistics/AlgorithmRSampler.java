package cubemanager.queryoptimizer.statistics;

import java.util.Arrays;
import java.util.Random;

/**
 * Algorithm R. The first reservoirSize keys fill the reservoir, and for every key after those,
 * one random number is drawn to decide whether it replaces a held key or not.
 * The cost is one draw per key of the population
 */
public class AlgorithmRSampler implements IReservoirSampler {

	@Override
	public int[] sample(int populationSize, int reservoirSize, Random random) {
		int[] reservoir = new int[reservoirSize];
		int count = 0;

		for (int key = 1; key <= populationSize; key++) {
			count++;
			if (count <= reservoirSize) {
				reservoir[count - 1] = key;
			} else {
				int j = random.nextInt(count);
				if (j < reservoirSize) {
					reservoir[j] = key;
				}
			}
		}

		if (count < reservoirSize) {
			return Arrays.copyOf(reservoir, count);
		}
		return reservoir;
	}
}
