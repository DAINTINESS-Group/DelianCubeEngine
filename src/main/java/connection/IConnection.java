package connection;

import java.rmi.RemoteException;

public interface IConnection {

	/**
	 * Use initializeConnection as the first step between a CLIENT and any implementation of the IMainEngine.
	 * 
	 * @param schemaName  	The name of the database 
	 * @param login  		The user login
	 * @param passwd  		The user passwd
	 * @param inputFolder	The folder where the cube description lies (includes a dbc.ini for the connection and cubde descriptions too)
	 * @param cubeName		The name of the cube + the name of the file where the cube is described = <cubename>.ini
	 * @throws RemoteException
	 */
	void initializeConnection(String schemaName, String login, String passwd,
			 String inputFolder, String cubeName) throws RemoteException;
}
