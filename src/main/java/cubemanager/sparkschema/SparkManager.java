package cubemanager.sparkschema;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import parsermgr.PathFolder;
import result.Cell;
import result.Result;

/**
 * The main class which runs the Spark master.<br>
 * Loads the DataSets from the csv files.<br>
 * And runs the queries.
 * 
 * 
 * @author kitos14 (Konstantinos Kadoglou)
 *
 */

public class SparkManager {
	
	private String schemaName;
	private String inputFolder;
	private String cubeName;
	private SparkSession spark;
	private long queryTimeString;
	
	private HashMap<String, Dataset<Row>> datasets = new HashMap<>();
	private ArrayList<String> sparkIni = new ArrayList<String>();

	@SuppressWarnings("resource")
	public SparkManager(String schemaName, String inputFolder, String cubeName) {
		this.schemaName = schemaName;
		this.inputFolder = inputFolder;
		this.cubeName = cubeName;
		
		readSparkIni();
		
		System.setProperty("hadoop.home.dir", sparkIni.get(0).toString());
		Logger.getLogger("org.apache").setLevel(Level.WARN);
		
		
		if (sparkIni.get(4).toString().equals("YES")) {
			spark = SparkSession.builder().getOrCreate();
		} else {
			spark = SparkSession
					.builder()
					.appName("Spark Session")
					.master(sparkIni.get(1).toString())
					.config("spark.sql.warehouse.dir", sparkIni.get(2).toString())
					.getOrCreate();
		}
		
		createDataSets();
	}
	
	/**
	 * Reads the spark.ini to register information about spark initialization.
	 * <br><br> Saves data in ArrayList sparkIni :
	 * <br><br> 1st value : Hadoop home directory.
	 * <br> 2nd value : Number of nodes spark to run.
	 * <br> 3rd value : Spark SQL Warehouse Directory.
	 * <br> 4th value : Seperator for csv files.
	 * @throws IOException 
	 * 
	 */
	public void readSparkIni() {
		File file = new File("InputFiles" + "/" + schemaName + "/" + "spark.ini"); 

		//File file = new File(PathFolder.getPathOfProject() + File.separator + "InputFiles" + File.separator + schemaName + File.separator + "spark.ini"); 
		
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			String st;
			try {
				int counter = 0;
				while ((st = br.readLine()) != null) {
					String[] words = st.split(" ");
					if (!words[words.length-1].isEmpty()) {
						if(words[words.length-1].equals("C:\\ProgramFiles\\Hadoop")) {
							words[words.length-1] = "C:\\Program Files\\Hadoop";
						}
						sparkIni.add(words[words.length-1]);
					}
				}
			} catch (IOException e) {
				System.out.println("Something went wrong while reading the spark.ini file.\n");
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.err.println("\nError : Can't find the spark.ini file.\n"
					+ "- Check if schemaName is set correct.\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads csv files into DataSets.
	 */
	public void createDataSets() {
		System.out.println("\nCreating the Datasets. This might take some time...");
		Instant startTime = Instant.now();
		
		String path = "InputFiles" + "/" + schemaName + "/" + "Data";

		//String path = PathFolder.getPathOfProject() + File.separator + "InputFiles" + File.separator + schemaName + File.separator + "Data";
		File[] files = new File(path).listFiles();
		for (File file : files) {
			if(file.isFile()) {
				try {
				datasets.put(FilenameUtils.removeExtension(file.getName()),spark.read()
								.format("csv")
								.option("sep", sparkIni.get(3).toString())
								.option("inferSchema", "true")
								.option("header", "true")
								.load(file.getPath()));
				} catch (Exception e) {
					System.err.println("Error : Couldn't load the csv file to Dataset.\n"
							+ "- Check if all files are csv format inside the Data folder.\n"
							+ "- Check if all csv files have header.\n"
							+ "- Check if seperator is set correct.");
				}
			}
		}
		Instant finalTime = Instant.now();
		long estimatedTime = Duration.between(startTime, finalTime).toMillis();
		System.out.println("\nok - Time Loading the Datasets : " + estimatedTime / 1000F + " secs.");
		
		System.out.println("\nok - Schema of cube " + cubeName + " Dataset : \n");
		try {
			datasets.get(cubeName).printSchema();
		} catch (Exception e) {
			System.err.println("\nError : cubeName = " + cubeName + " does not exist.\n"
					+ "Please check if the cubeName is spelled wrong.\n");
			e.printStackTrace();
		}
	}
	
	/**
	 * Executes a query using Spark SQL. Query is generated by the code and passed as paremeter.<br>
	 * At the end it returns a result.
	 * 
	 * @param query : The query that has been generated.
	 * @param result : Gets a result file and returns the modified result file.
	 * 
	 * @return result : Returns the modified result file.
	 */
	public Result executeQueryToSpark(String query, Result result) {
		
		// Create temporary views of the Datasets
		for (Map.Entry<String, Dataset<Row>> entry : datasets.entrySet()) {
			String key = entry.getKey();
			Dataset<Row> value = entry.getValue();
			value.createOrReplaceTempView(key);
		}
		Instant startTime = Instant.now();
		System.out.println("\nrunning - Running query. Please wait...");
		
		// Shows the Schema in Table format.
		Dataset<Row> queryDS = spark.sql(query);

		// Here we can make transformations and actions using the RDD methods.
		// Not used yet but we have an example.
		
		//		Dataset<Row> temp = queryDS.filter(row -> row.getAs("subcategory").equals("subcategory1"));
		//		temp.show();
		List<Row> queryList = queryDS.collectAsList();
		Instant finalTime = Instant.now();
	    queryTimeString = Duration.between(startTime, finalTime).toMillis();
		System.out.println("\nok - Query executed! :)");
		
//		System.out.println("\nShowing the 20 first results of the query : ");
//		queryDS.show();
		System.out.println("\nPrinting the schema of the results table :\n");
		queryDS.printSchema();
		System.out.println("\n\nrunning - Populating results. Please wait...");
		result = generateResult(queryList ,queryDS, result);
		System.out.println("\n"+ "ok - Result produced! :)"+"\n");
		return result;
	}
	
	public long getQueryTime() {
		return queryTimeString;
	}
	
	public Result generateResult(List<Row> queryList, Dataset<Row> queryDS, Result result) {
		int columnCount = queryDS.columns().length;
		int rowCount = queryList.size();
		boolean ret_value=false;
		String resultArray [][] = new String[rowCount+2][columnCount];

		for(int i=0;i<columnCount;i++) {
			resultArray[0][i]= queryDS.columns()[i];
			result.getColumnNames().add(queryDS.columns()[i]);
			resultArray[1][i]= queryDS.columns()[i];
			result.getColumnLabels().add(queryDS.columns()[i]);
		}
		
		for (int i = 0; i < queryList.size(); i++) {
			String[] values = new String[queryList.get(i).size()];
			for (int j = 0; j < columnCount; j++) {
				String value;
				if(StringUtils.isNumeric(queryList.get(i).get(j).toString().replace(",", "")) || StringUtils.isNumeric(queryList.get(i).get(j).toString().replace(".", ""))) {
					if (!StringUtils.isNumeric(queryList.get(i).get(j).toString())) {
						DecimalFormat decim = new DecimalFormat("0.0000");
						value = decim.format(Float.parseFloat(queryList.get(i).get(j).toString()))+"";
						value = value.replace(",", ".");
					} else {
						value = queryList.get(i).get(j).toString();
					}
				} else {
					value = queryList.get(i).get(j).toString();
				}
				resultArray[i][j]=value.toString().trim().replaceAll("\n ", "");
				values[j] = value.toString().trim().replaceAll("\n ", "");
			}
			/*
			 * VERY IMPORTANT: here is where cells are created!
			 * We need them to be in a List, so that they are ordered!
			 */
			result.getCells().add(new Cell(values));

			result.setResultArray(resultArray);
		}
//		String[] columns = queryDS.columns();
//		for (String column : columns) {
//			System.out.println(column);
//		}
//		System.out.println(queryList);
		return result;
	}
}