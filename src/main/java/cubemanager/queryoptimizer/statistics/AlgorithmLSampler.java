package cubemanager.queryoptimizer.statistics;

import cubemanager.relationalstarschema.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

/**
 * Algorithm L. Draws a geometric skip distance instead of a random number per record
 */
public class AlgorithmLSampler implements IReservoirSampler {

	@Override
	public String[][] sample(String sql, int reservoirSize, Database db, Random random) {
		String[][] reservoir = new String[reservoirSize][];
		int filled = 0;

		try (Statement statement = db.getConnection().createStatement(
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

			statement.setFetchSize(Integer.MIN_VALUE);
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				int columnCount = resultSet.getMetaData().getColumnCount();

				while (filled < reservoirSize && resultSet.next()) {
					reservoir[filled] = readRow(resultSet, columnCount);
					filled++;
				}
				if (filled < reservoirSize) {
					return Arrays.copyOf(reservoir, filled);
				}

				double w = Math.exp(Math.log(random.nextDouble()) / reservoirSize);

				while (true) {
					int skip = (int) (Math.log(random.nextDouble()) / Math.log(1 - w));

					boolean exhausted = false;
					for (int i = 0; i < skip; i++) {
						if (!resultSet.next()) {
							exhausted = true;
							break;
						}
					}
					if (exhausted || !resultSet.next()) {
						break;
					}

					reservoir[random.nextInt(reservoirSize)] = readRow(resultSet, columnCount);
					w = w * Math.exp(Math.log(random.nextDouble()) / reservoirSize);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0][];
		}

		return reservoir;
	}

	private String[] readRow(ResultSet resultSet, int columnCount) throws SQLException {
		String[] row = new String[columnCount];
		for (int col = 0; col < columnCount; col++) {
			String val = resultSet.getString(col + 1);
			if (val == null) {
				row[col] = "";
			} else row[col] = val;
		}
		return row;
	}
}
