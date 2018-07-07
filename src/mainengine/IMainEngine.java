/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/


package mainengine;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * @author pvassil
 *
 */
public interface IMainEngine extends Remote {

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


	/**
	 * Answers a query as prescribed in a String
	 * 
	 * An exemplary query definition is as follows:
	 * <p>
	 * <code>CubeName:loan</code>
	 * <code>Name:CubeQueryLoan2</code>
	 * <code>AggrFunc:Avg</code>
	 * <code>Measure:amount</code>
	 * <code>Gamma:account_dim.lvl2,date_dim.lvl2</code>
	 * <code>Sigma:account_dim.lvl1='Liberec',status_dim.lvl0='Running Contract/OK'</code>
	 * <p>
	 * Observe:
	 * The query has a name, in the example,CubeQueryLoan2
	 * The query comes with two groupers
	 * The selection condition Sigma, comes as a COMMA-SEPARATED list of atomic expressions of the form Level=value
	 * 
	 * TODO: test with less/more groupers + with alternative selection atoms
	 * 
	 * @param queryRawString The query definition
	 * @return the path of the result file
	 * @throws RemoteException
	 * 
 
	 */
	String answerCubeQueryFromString(String queryRawString) throws RemoteException;
	
	
	/**
	 * This is the method to call for answering queries from a file. 
	 * Inside the file, queries are separated by a '@'
	 * 
	 * @param file	The PATH of the filename containing the queries
	 * @throws RemoteException
	 */
	ArrayList<String> answerCubeQueriesFromFile(File file) throws RemoteException;
	
	
	
	/**
	 * @deprecated	Not to be used in the context of the DelianCubeEngine project.
	 * This is an old-to-be-removed method, from Cinecubes, that turns on/off audio and MS word generation
	 * 
	 * @param audio	To turn on/off audio generation
	 * @param word	To turn on/off MS Word generation
	 * @throws RemoteException
	 */
//	void optionsChoice(boolean audio, boolean word)throws RemoteException;

}