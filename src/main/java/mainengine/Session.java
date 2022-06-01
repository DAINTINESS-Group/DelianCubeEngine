package mainengine;

import java.io.File;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import parsermgr.ParserManager;

public class Session {

	private String id;
	private CubeManager cubeManager;
	private ParserManager parserManager;
	
	public Session(CubeManager cubeManager) {
		id = new Timestamp(System.currentTimeMillis()).toInstant().toString();
		this.cubeManager = cubeManager;
	}
	
	public String initialize(String typeOfConnection, HashMap<String, String> userInputList) throws RemoteException {		
		cubeManager.createCubeBase(userInputList);
		constructDimension(userInputList.get("inputFolder"), userInputList.get("cubeName"));
		cubeManager.setCubeQueryTranslator();
		
		return this.id;
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
				if (parserManager.getMode().equals("Dimension")) {
					String creationName = parserManager.getCreationName();
					String datasourceTable = parserManager.getDatasourceTable();
					ArrayList<String> originalLevelList = parserManager.getOriginalLevelList();
					ArrayList<String> customLevelList = parserManager.getCustomLevelList();
					String dimensionType = parserManager.getDimensionType();
					ArrayList<String> hierarchyList = parserManager.getHierarchyList();
					HashMap<String, String> levelID = parserManager.getLevelID();
					HashMap<String, String> levelDescription = parserManager.getLevelDescription();
					HashMap<String, ArrayList<String>> levelAttributes = parserManager.getLevelAttributes();
					HashMap<String, String> attributeTypes = parserManager.getAttributeTypes();
					HashMap<String, String> attributeDatasource = parserManager.getAttributeDatasource();
					
					this.cubeManager.insertDimension(
							creationName, datasourceTable,
							originalLevelList, customLevelList, dimensionType,
							hierarchyList, levelID, levelDescription,
							levelAttributes, attributeTypes, attributeDatasource);
				} else if (parserManager.getMode().equals("Cube")) {
					String creationName = parserManager.getCreationName();
					String datasourceTable = parserManager.getDatasourceTable();
					ArrayList<String> dimensionList = parserManager.getDimensionList();
					ArrayList<String> dimensionsAtCubeDataSource = parserManager.getDimensionsAtCubeDataSource();
					ArrayList<String> measureList = parserManager.getMeasureList();
					ArrayList<String> measureFields = parserManager.getMeasureFields();
					
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

	public String getSessionId() {
		return id;
	}
	
	
}
