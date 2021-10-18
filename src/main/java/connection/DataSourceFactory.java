package connection;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

import cubemanager.relationalstarschema.Database;
import cubemanager.sparkschema.SparkDataset;

/**
 * 
 * A Factory Pattern for the Connection.<br>
 * If "typeOfConnection" is "RDBMS" it will return a RDBMSConnection,<br>
 * else if "typeOfConnection" is "Spark" it will return a SparkConnection.
 * 
 * @author kitos14 (Konstantinos Kadoglou)
 * <br>Email : kostas.kadoglou@gmail.com
 * 
 */

public class DataSourceFactory {

	/**
	 * 
	 * @param typeOfConnection : Informs which connection the user chose. "RDBMS" or "Spark"
	 * @param userInputList : A HashMap with values for the connection. For more information read at : {@link mainengine.IMainEngine#initializeConnection(String,HashMap)}
	 * 
	 * @return RDBMSConnection or SparkConnection
	 *
	 */
	public DataSourceDescription createConnection(String typeOfConnection, HashMap<String, String> userInputList) {
		
		DataSourceDescription dataSource = null;
		
		if (typeOfConnection.equals("RDBMS")) {
			// userInputList are the values pulled from GUI
			try {
				String line;
				Scanner scanner = new Scanner(new FileReader(System.getProperty("user.dir") + "/InputFiles/" + userInputList.get("schemaName")
						+ "/dbc.ini"));
				while (scanner.hasNextLine()) {
					line = scanner.nextLine();
					String results[] = line.split(";");
					System.out.println("ok - " + results[1] + " : " + results[3]);
					return new Database(results[1], results[3]);
				}
				scanner.close();
			} catch (FileNotFoundException e1) {
				System.err.println("Unable to work correctly with dbc.ini for the setup of the Cubebase");
				e1.printStackTrace();
				return null;
			}
		}
		else if (typeOfConnection.equals("Spark")) {
			return new SparkDataset();
		}
		else {
			throw new IllegalArgumentException("\n\nError : You specified a wrong type of Connection.\n"
					+ "Connection type must be set to \"RDBMS\" or \"Spark\".\n");
		}
		return null;
	}
}
