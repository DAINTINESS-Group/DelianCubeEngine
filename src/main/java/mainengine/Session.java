package mainengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
					String creationName = parserManager.creationName;
					String datasourceTable = parserManager.datasourceTable;
					ArrayList<String> originalLevelList = parserManager.originalLevelList;
					ArrayList<String> customLevelList = parserManager.customLevelList;
					String dimensionType = parserManager.dimensionType;
					ArrayList<String> hierarchyList = parserManager.hierarchyList;
					HashMap<String, String> levelID = parserManager.levelID;
					HashMap<String, String> levelDescription = parserManager.levelDescription;
					HashMap<String, ArrayList<String>> levelAttributes = parserManager.levelAttributes;
					HashMap<String, String> attributeTypes = parserManager.attributeTypes;
					HashMap<String, String> attributeDatasource = parserManager.attributeDatasource;
					
					this.cubeManager.insertDimension(
							creationName, datasourceTable,
							originalLevelList, customLevelList, dimensionType,
							hierarchyList, levelID, levelDescription,
							levelAttributes, attributeTypes, attributeDatasource);
				} else if (parserManager.mode.equals("Cube")) {
					String creationName = parserManager.creationName;
					String datasourceTable = parserManager.datasourceTable;
					ArrayList<String> dimensionList = parserManager.dimensionList;
					ArrayList<String> dimensionsAtCubeDataSource = parserManager.dimensionsAtCubeDataSource;
					ArrayList<String> measureList = parserManager.measureList;
					ArrayList<String> measureFields = parserManager.measureFields;
					
					this.cubeManager.insertCube(creationName,
							datasourceTable, dimensionList,
							dimensionsAtCubeDataSource, measureList,
							measureFields);
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
