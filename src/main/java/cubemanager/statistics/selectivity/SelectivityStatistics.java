package cubemanager.statistics.selectivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.relationalstarschema.Database;
import result.Result;

/**
 * A class that: (i) samples the cube, (ii) produces a HashMap that contains the number of tuples 
 * corresponding to a single selection condition atom.
 * @author mariosjkb
 *
 */
public class SelectivityStatistics {
	
	private String inputFolder;
	
	private String cubeName;
	
	private int storedFactTableSize;
	
	private int sampleSize;
	
	private HashMap<CustomKey,Integer> sample = new HashMap<CustomKey,Integer>();
	
	public SelectivityStatistics (String inputFolder, String cubeName) {
		this.inputFolder = inputFolder;
		this.cubeName = cubeName;
	}
	
	
	public int getStoredFactTableSize() {
		return storedFactTableSize;
	}

	public int getSampleSize() {
		return sampleSize;
	}
	
	public HashMap<CustomKey,Integer> getSample(){
		return sample;
	}
	
	/** 
	 * Auxiliary method that computes the fact table size.
	 * @param factTable
	 * @param cubeBase
	 * @return The number of tuples of the fact table.
	 */
	private int computeFactTableSize(String factTable, CubeBase cubeBase) {
		String sql = "SELECT COUNT(*) FROM " + factTable;
		Result result = new Result();
		cubeBase.executeQueryToProduceResult(sql, result);
		String[][] resultArray = result.getResultArray();

		if(resultArray == null || resultArray.length < 3 || resultArray[2][0] == null) return -1;

		try {
			return Integer.parseInt(resultArray[2][0]);
		} catch (NumberFormatException e) {
			return -1;
		}
	}
	
	private String[] buildReservoir(int factTableSize, int reservoirSize, Random random) {
		long startTime = System.nanoTime();
		String[] reservoirSample = new String[reservoirSize];
		
		for(int i = 1;i<factTableSize+1;i++) {
			String id = Integer.toString(i);
			if(i <= reservoirSize-1) {
				reservoirSample[i] = id;
			}else{
				int rand = random.nextInt(i);
				if (rand < reservoirSize) {
					reservoirSample[rand] = id;
				}
			}
		}
		long endTime = System.nanoTime();
		return reservoirSample;
	}
	
	/**
	 * Parses the .ini file and aggregates the sample to calculate the number of occurrences that is stored in a HashMap.
	 * @return A HashMaP that connects a selection atom and its value with the number of occurrences in the sample.
	 */ 
	private HashMap<CustomKey,Integer> calculateSelectivityFromFile() {
		File sampleFile = new File("InputFiles/" + inputFolder + "/" + cubeName + "_samples.ini");
		
		try (BufferedReader reader = new BufferedReader(new FileReader(sampleFile))) {
			reader.readLine();
			String line;
			
			

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("factTableSize = ")) {
					try {
						storedFactTableSize = Integer.parseInt(line.substring("factTableSize = ".length()).trim());
					} catch (NumberFormatException ignore) {}
					continue;
				}else if(line.startsWith("sampleSize = ")) {
					try {
						sampleSize = Integer.parseInt(line.substring("sampleSize = ".length()).trim());
					} catch (NumberFormatException ignore) {}
				}
				
				String[] atomParts = line.split("=");
				String columnName = atomParts[0];
				String columnValue = atomParts[1];

				if(!sample.containsKey(new CustomKey(columnName,columnValue))) {
					sample.put(new CustomKey(columnName,columnValue), 1);
				}else {
					int currentValue = sample.get(new CustomKey(columnName,columnValue));
					sample.put(new CustomKey(columnName, columnValue), currentValue  + 1);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nDONE WITH LOADING THE FACT TABLE SAMPLE\n");
		return sample;
	}

	
	/**
	 * Builds the sample if the .ini file is missing from the dataset's input folder.
	 * It retrieves the tuples that correspond to the surrogate keys of the Reservoir Sample and fetches all
	 * the respective levels and values of each dimension that participates in the fact table tuple. 
	 * The result is written in a .ini file.
	 * @param cubeBase
	 * @param schemaName
	 * @param cubeName
	 * @param samplePercentage
	 * @param forceRebuild
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildSelectivitySample(CubeBase cubeBase, String schemaName, String cubeName, double samplePercentage, boolean forceRebuild, boolean sqrtSample) throws IOException, SQLException {
		File file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_samples.ini");
		if (file.exists() && !forceRebuild) {
			calculateSelectivityFromFile();
			return; 
		}else {
		
			System.out.println("\nSAMPLING THE FACT TABLE...\n");
			Database db = (Database) cubeBase.getDataSourceDescription();
	
			try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
				Random random = new Random();
				
				for (BasicStoredCube cube : cubeBase.getRegisteredCubeList()) {
					String factTable = cube.getFactTable().getTableName();
					List<Dimension> dimensions = cube.getDimensionsList();
					List<String> dimRefFields = cube.getDimensionRefFieldList();
					String dimensionPrimaryKeys = "";
					
					this.storedFactTableSize = computeFactTableSize(factTable, cubeBase);
					writer.println("factTableSize = " + storedFactTableSize);
					
					if(!sqrtSample) {
						this.sampleSize = (int) (samplePercentage * storedFactTableSize);
					}else {
						this.sampleSize = (int) (Math.sqrt(storedFactTableSize));
					}
					writer.println("sampleSize = " + sampleSize);
					
					for (int i = 0; i < dimensions.size(); i++) {
						String factFK = dimRefFields.get(i);
						if(dimensionPrimaryKeys.equals("")) {
							dimensionPrimaryKeys += factFK;
						}else {
							dimensionPrimaryKeys += "," + factFK;
						}
					}
					
					String [] reservoirSample = buildReservoir(storedFactTableSize,sampleSize,random);
					
					
					for(String id: reservoirSample) {
						String sql = "SELECT " + dimensionPrimaryKeys + " FROM " + factTable + " WHERE SK_id = " + id; 
						Result result = new Result();
						System.out.println(sql);
						cubeBase.executeQueryToProduceResult(sql, result);
						String[][] resultArray = result.getResultArray();
	
						for(int i = 0; i<dimensions.size();i++) {
							String dimensionID = resultArray[2][i];
							Dimension dimension = dimensions.get(i);
							String tableName = dimension.getTableName();
							String dimPK = dimension.getHierarchy().get(0).getLevels().get(0).getAttributeName(0);
							int numOfLevels = 0;
	
							
							sql = "SELECT ";
							
							for (Level level : dimension.getHierarchy().get(0).getLevels()) {
								if(level.getAttributeName(0).equals("All")) {
									sql += "`" + level.getAttributeName(0) + "`" + ",";
								}else {
									sql += level.getAttributeName(0) + ",";
								}
								numOfLevels++;
							}
							sql = sql.substring(0, sql.length() - 1);
							sql += " FROM " + tableName + " WHERE " + dimPK + "='" + dimensionID + "'";
							
							Statement stmt = db.getConnection().createStatement();
							ResultSet results = stmt.executeQuery(sql);
							
							
							while (results.next()) {
								writer.println(tableName + "." + dimension.getHierarchy().get(0).getLevels().get(0).getAttributeName(0) + "=" + results.getString(1));
								for(int k = 2;k<numOfLevels+1;k++) {
									writer.println(tableName + "." + dimension.getHierarchy().get(0).getLevels().get(k-1).getAttributeName(0) + "=" + results.getString(k));
									String columnName = tableName + "." + dimension.getHierarchy().get(0).getLevels().get(k-1).getAttributeName(0);
									String columnValue = results.getString(k);

									if(!sample.containsKey(new CustomKey(columnName,columnValue))) {
										sample.put(new CustomKey(columnName,columnValue), 1);
									}else {
										int currentValue = sample.get(new CustomKey(columnName,columnValue));
										sample.put(new CustomKey(columnName, columnValue), currentValue  + 1);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
