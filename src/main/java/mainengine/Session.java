package mainengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Scanner;

import cubemanager.CubeManager;
import parsermgr.ParserManager;

public class Session {

	private String id;
	private CubeManager cubeManager;
	private ParserManager parserManager;
	
	public Session(CubeManager cubeManager, ParserManager prsMng) {
		id = new Timestamp(System.currentTimeMillis()).toInstant().toString();
		this.cubeManager = cubeManager;
		this.parserManager = prsMng;
	}
	
	public String initialize(String typeOfConnection, HashMap<String, String> userInputList) throws RemoteException {		
		//initializeCubeMgr(typeOfConnection, userInputList);
		cubeManager.createCubeBase(userInputList);
		constructDimension(userInputList.get("inputFolder"), userInputList.get("cubeName"));
		cubeManager.setCubeQueryTranslator();
		
		return this.id;
	}
	
	private void initializeCubeMgr(String typeOfConnection, HashMap<String, String> userInputList) throws RemoteException {
		cubeManager = new CubeManager(typeOfConnection, userInputList);
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
			parserManager = new ParserManager();
			@SuppressWarnings("resource")
			Scanner sc = (new Scanner(file)).useDelimiter(";");
			while (sc.hasNext()) {
				parserManager.parse(sc.next() + ";");
				if (parserManager.mode.equals("Dimension")) {
					System.out.println(parserManager.creationName);
					System.out.println(parserManager.datasourceTable);
					System.out.println(parserManager.originalLevelList);
					System.out.println(parserManager.customLevelList);
					System.out.println(parserManager.dimensionList);
					this.cubeManager.insertDimension(
							parserManager.creationName, parserManager.datasourceTable,
							parserManager.originalLevelList, parserManager.customLevelList,
							parserManager.hierarchyList);
				} else if (parserManager.mode.equals("Cube")) {
					System.out.println(parserManager.creationName);
					System.out.println(parserManager.datasourceTable);
					System.out.println(parserManager.dimensionList);
					System.out.println(parserManager.dimensionsAtCubeDataSource);
					System.out.println(parserManager.measureList);
					System.out.println(parserManager.measureFields);
					this.cubeManager.insertCube(parserManager.creationName,
							parserManager.datasourceTable, parserManager.dimensionList,
							parserManager.dimensionsAtCubeDataSource, parserManager.measureList,
							parserManager.measureFields);
				}
			}
		}
	}

	public CubeManager getCubeManager() {
		return cubeManager;
	}

	public void setCubeManager(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}

	public ParserManager getPrsMng() {
		return parserManager;
	}

	public void setPrsMng(ParserManager prsMng) {
		this.parserManager = prsMng;
	}

	public String getId() {
		return id;
	}
	
}
