package cubemanager.queryoptimizer.statistics;


import cubemanager.relationalstarschema.Database;

import java.util.Random;

/**
 * A reservoir sampling algorithm. Implementations draw a simple random sample of
 * whole rows from the result of a query without knowing the population size beforehand
 */
public interface IReservoirSampler {

	/**
	 * Draws a sample of rows from the result of the query
	 * @param sql the query whose rows are sampled
	 * @param reservoirSize number of rows to keep
	 * @param db
	 * @param random
	 * @return the sampled rows, trimmed to the actual row count if the query returned fewer than reservoirSize rows
	 * Returns an empty array if the query failed
	 */
	String[][] sample(String sql, int reservoirSize, Database db, Random random);
}
