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
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
//import java.sql.ResultSet;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
//import exctractionmethod.SqlQuery;
/*
import filecreation.FileMgr;
import filecreation.PptxFile;
import filecreation.WordFile;
*/
import parsermgr.ParserManager;
//import storymgr.FinResult;
//import storymgr.StoryMgr;
import result.Result;


/**
 * @author pvassil
 *
 * USed to be MainEngine in Cinecubes. Refactored to address the needs of SimpleOLAP Engine.
 * The class is the main server component of answering _cube_ queries.
 * 
 * Use {@link #answerCubeQueriesFromFile(File file)} to answer the queries contained in a file 
 */
@SuppressWarnings("serial")
public class SimpleQueryProcessorEngine extends UnicastRemoteObject implements IMainEngine {

	private CubeManager cubeManager;
	//private StoryMgr storMgr;
	private ParserManager prsMng;
//	private Options optMgr;
//	private String msrname;
	
	
/**
 * Simple constructor for the class
 * @throws RemoteException
 */
	public SimpleQueryProcessorEngine() throws RemoteException {
		super();
		//storMgr = new StoryMgr();
		//optMgr = new Options();
	}
	
	
	/* (non-Javadoc)
	 * @see mainengine.IMainEngine#initializeConnection(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void initializeConnection(String schemaName, String login,
			String passwd, String inputFolder, String cubeName)
			throws RemoteException {
		//createDefaultFolders();
		initializeCubeMgr(inputFolder);
		cubeManager.CreateCubeBase(schemaName, login, passwd);
		constructDimension(inputFolder, cubeName);
		cubeManager.setCubeQueryTranslator();
		System.out.println("DONE WITH INIT");
	}
	
	
	private void initializeCubeMgr(String lookupFolder) throws RemoteException {
		cubeManager = new CubeManager(lookupFolder);
	}

	private void constructDimension(String inputlookup, String cubeName)
			throws RemoteException {
		try {
			this.parseFile(new File("InputFiles/" + inputlookup + "/"
					+ cubeName + ".ini"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void parseFile(File file) throws FileNotFoundException {
		if (file != null) {
			prsMng = new ParserManager();
			@SuppressWarnings("resource")
			Scanner sc = (new Scanner(file)).useDelimiter(";");
			while (sc.hasNext()) {
				prsMng.parse(sc.next() + ";");
				if (prsMng.mode == 2) {
					this.cubeManager.InsertionDimensionLvl(
							prsMng.name_creation, prsMng.sqltable,
							prsMng.originallvllst, prsMng.customlvllst,
							prsMng.dimensionlst);
				} else if (prsMng.mode == 1) {
					this.cubeManager.InsertionCube(prsMng.name_creation,
							prsMng.sqltable, prsMng.dimensionlst,
							prsMng.originallvllst, prsMng.measurelst,
							prsMng.measurefields);
				}
			}
		}
	}

//	public void optionsChoice(boolean audio, boolean word)
//			throws RemoteException {
//		optMgr.setAudio(audio);
//		optMgr.setWord(word);
//	}
	/* (non-Javadoc)
	 * @see mainengine.IMainEngine#answerCubeQueriesFromFile(java.io.File)
	 */
	@Override
	public ArrayList<String> answerCubeQueriesFromFile(File file) throws RemoteException {
		ArrayList<String> fileLocations = new ArrayList<String>();
		Scanner sc;
		try {
			Scanner scanner = new Scanner(file);
			sc = scanner.useDelimiter("@");
			while (sc.hasNext()) {
				String filename;
				//long startTime = System.nanoTime();
				String queryString = sc.next();
				filename = answerCubeQueryFromString(queryString);
				fileLocations.add(filename);

				
			}
			
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileLocations;
	}	
	

	/** 
	 * Gets the query from a string, executes it and produces the output of a query
	 * <p>
	 * The main idea is:
	 * (1) construct the query via <code>createCubeQueryFromString</code> of <code>CubeManager</code>, see {@link CubeManager}
	 * (2) execute the query again via {@link CubeManager} and obtain a {@link Result}
	 * (3b) produce a file in the directory <code>OutputFiles</code> with the name of the query
	 * (3a) btw., the results are also output to the console
	 * 
	 * @author pvassil
	 * @since v.0.1
	 * @see mainengine.IMainEngine#answerCubeQueryFromString(java.lang.String)
	 * @see cubemanager.CubeManager#executeQuery(CubeQuery)
	 */
	@Override
	public String answerCubeQueryFromString(String queryRawString) throws RemoteException{
		//Use a hashmap to get any useful data (like queryname) from the raw query string
		HashMap<String, String> queryParams = new HashMap<String, String>();
		
		//1. parse query and produce a CubeQuery
		CubeQuery currentCubQuery = cubeManager.createCubeQueryFromString(queryRawString, queryParams);

		//2. execute the query AND populate Result with a 2D string
		//Result res = cubeManager.getCubeBase().executeQuery(currentCubQuery);
		Result res = cubeManager.executeQuery(currentCubQuery);
				
		//3a. print result to screen
		String queryName = queryParams.get("QueryName");
		
		//Replaced all printing of String[][] with printing of Cells which seems to be identical 
		//res.printStringArrayTostream(System.out, res.getResultArray());
		//System.out.println("------- Done with printString, go for printCells  --------------------------"+"\n");
		res.printCellsToStream(System.out);
		System.out.println("------- Done with " + queryName + " --------------------------"+"\n");
				
		//3b. print result to file
		String outputLocation = this.printToTabTextFile(currentCubQuery,  "OutputFiles/");
		
		return outputLocation;
	}
	


	/**
	 * Returns the location of a tab-separated file where the result of a query is stored.
	 * <p>
	 * The goal of this method is to output a file containing the result of a query
	 * The name of the file is the name of the query + extension tab 
	 * 
	 * @param cubequery The query whose result is being outputed
	 * @return  A String with the location of the file holding the results
	 * @author pvassil
	 * @author dgkesouli
	 * @version v.0.1
	 * @since v.0.0 from Cincecubes
	 */
	public String printToTabTextFile(CubeQuery cubequery, String outputFolder) {
		Result res = cubequery.getResult();
		String fileName = outputFolder + cubequery.getName() + ".tab";
		File file=new File(fileName);
		FileOutputStream fileOutputStream=null;
		PrintStream printStream=null;
		try {
			fileOutputStream=new FileOutputStream(file);
			printStream=new PrintStream(fileOutputStream);
			
			//res.printStringArrayTostream(printStream, res.getResultArray());
			res.printCellsToStream(printStream);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(fileOutputStream!=null){
					fileOutputStream.close();
				}
				if(printStream!=null){
					printStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}//end finally try
		}//end finally
		
		return fileName;
	}//end method
	
	
}//end class
