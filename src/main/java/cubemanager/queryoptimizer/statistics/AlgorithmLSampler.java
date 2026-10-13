package cubemanager.queryoptimizer.statistics;

import java.util.Random;

/**
 * Algorithm L. Draws a geometric skip distance instead of a random number per key
 */
public class AlgorithmLSampler implements IReservoirSampler {

	@Override
	public int[] sample(int populationSize, int reservoirSize, Random random) {
		if (populationSize <= reservoirSize) {
			int[] all = new int[populationSize];
			for (int i = 0; i < populationSize; i++){
				all[i] = i + 1;
			}
			return all;
		}

		int[] reservoir = new int[reservoirSize];

		for (int i =0; i < reservoirSize; i++) {
			reservoir[i] = i + 1;
		}

		double w = Math.exp(Math.log(random.nextDouble()) / reservoirSize);
		int key = reservoirSize;

		while (true) {
			int skip = (int) (Math.log(random.nextDouble()) / Math.log(1 - w));

			key += skip + 1;
			if (key > populationSize) {
				break;
			}

			reservoir[random.nextInt(reservoirSize)] = key;
			w = w * Math.exp(Math.log(random.nextDouble()) / reservoirSize);
		}

		return reservoir;
	}
}
