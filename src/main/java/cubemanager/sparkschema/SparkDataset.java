package cubemanager.sparkschema;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FilenameUtils;

import connection.DataSourceDescription;
import cubemanager.relationalstarschema.Table;
import result.Result;

/**
 * 
 * Spark Connection. Creates the cube and a SparkManager object.
 * <br>The SparkManager is the main class that creates DataSets and runs the queries in Spark.
 * 
 * @author kitos14 (Konstantinos Kadoglou)
 * <br>Email : kostas.kadoglou@gmail.com
 * 
 */

public class SparkDataset extends DataSourceDescription {
	
	private String schemaName;
	private String inputFolder;
	private String cubeName;
	private SparkManager sparkManager;
	
	public SparkDataset() {
		Tbl = new ArrayList<Table>();
	}

	@Override
	protected void generateTableList() {
		String path = "InputFiles/" + inputFolder + "/" + cubeName;
//		String path = PathFolder.getPathOfProject() + File.separator + "InputFiles" + File.separator + inputFolder + File.separator + cubeName;

		System.out.println(path);
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		
		if (!folder.exists()) {
			System.err.println("Error : The inputFolder = \"" + inputFolder + "\" doesn't exist. Please check if inputFolder is set correct.");
		} else if (listOfFiles.length == 0) {
			System.err.println("Error : The inputFolder = \"" + inputFolder + "\" is empty. Please check if inputFolder is set correct.");
		}
		
		System.out.println("The path for the csv files is : " + path);
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    System.out.println("\nReading file : " + FilenameUtils.removeExtension(listOfFiles[i].getName()));
			String attrs = FilenameUtils.removeExtension(listOfFiles[i].getName());
			String name =  attrs.substring(0, attrs.length() - 6);
			System.out.println("Table : " + name);
			Table tmp = new Table(name);
			tmp.setAttribute(attrs, path);
			System.out.println();
			this.Tbl.add(tmp);
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}
		System.out.println();
	}

	@Override
	public void registerCubeBase(HashMap<String, String> userInputList) {
		schemaName = userInputList.get("schemaName");
		inputFolder = userInputList.get("inputFolder");
		cubeName = userInputList.get("cubeName");
		registerSpark();
		generateTableList();
	}

	@Override
	public Result executeQueryToProduceResult(String queryString, Result result) {
		Result finalResults =  sparkManager.executeQueryToSpark(queryString, result);
		setQueryTime();
		return finalResults;
	}
	
	public void registerSpark() {
		sparkManager = new SparkManager(schemaName, inputFolder, cubeName);
	}
	
	public void setQueryTime() {
		queryTimeString = sparkManager.getQueryTime();
	}

}
