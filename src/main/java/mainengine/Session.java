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
				if (parserManager.mode == 2) {
					this.cubeManager.insertDimension(
							parserManager.name_creation, parserManager.sqlTable,
							parserManager.originalLevelList, parserManager.customLevelList,
							parserManager.dimensionList);
				} else if (parserManager.mode == 1) {
					this.cubeManager.insertCube(parserManager.name_creation,
							parserManager.sqlTable, parserManager.dimensionList,
							parserManager.originalLevelList, parserManager.measureList,
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
