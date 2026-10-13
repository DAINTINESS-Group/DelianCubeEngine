package cubemanager.queryoptimizer.statistics;

import cubemanager.relationalstarschema.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Random;

/**
 * Algorithm R. Draws one random number for every record past the first n
 */
public class AlgorithmRSampler implements IReservoirSampler {

	@Override
	public String[][] sample(String sql, int reservoirSize, Database db, Random random) {
		String[][] reservoir = new String[reservoirSize][];
		int count = 0;

		try (Statement statement = db.getConnection().createStatement(
				ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {

			statement.setFetchSize(Integer.MIN_VALUE);
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				int columnCount = resultSet.getMetaData().getColumnCount();

				while(resultSet.next()) {
					String[] row = new String[columnCount];
					for (int col = 0; col < columnCount; col++) {
						String val = resultSet.getString(col + 1);
						if (val == null) {
							row[col] = "";
						} else row[col] = val;
					}

					count++;
					if(count <= reservoirSize) {
						reservoir[count - 1] = row;
					} else {
						int j = random.nextInt(count);
						if (j < reservoirSize) reservoir[j] = row;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0][];
		}

		if (count < reservoirSize) {
			return Arrays.copyOf(reservoir, count);
		}

		return reservoir;
	}
}
