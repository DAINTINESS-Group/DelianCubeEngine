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
import java.util.Map.Entry;
import java.util.Random;

import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.CubeBase;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import cubemanager.relationalstarschema.Database;
import result.Result;

/**
 * A class that: (i) valueCardinalityProfiles the cube, (ii) produces a HashMap that contains the number of tuples 
 * corresponding to a single selection condition atom.
 * @author mariosjkb
 *
 */
public class SelectivityStatistics {
	
	private String inputFolder;
	
	private String cubeName;
	
	private int storedFactTableSize;
	
	private int sampleSize;
	
	private HashMap<String,HashMap<String,Integer>> valueCardinalityProfile = new  HashMap<String,HashMap<String,Integer>>();

	private double valueCardinalityProfilePercentage;
	
	private int BATCH_SIZE=1000;

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
	
	public  HashMap<String,HashMap<String,Integer>> getValueCardinalityProfile(){
		return valueCardinalityProfile;
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
		return reservoirSample;
	}
	
	/**
	 * Parses the .ini file and aggregates the valueCardinalityProfile to calculate the number of occurrences that is stored in a HashMap.
	 * @return A HashMaP that connects a selection atom and its value with the number of occurrences in the valueCardinalityProfile.
	 */ 
	private HashMap<String,HashMap<String,Integer>> calculateSelectivityFromFile(boolean sqrtSample) {
		File valueCardinalityProfileFile;
		if(sqrtSample) {
			valueCardinalityProfileFile = new File("InputFiles/" + inputFolder + "/" + cubeName + "_sqrt_valueCardinalityProfiles.ini");
		}else {
			valueCardinalityProfileFile = new File("InputFiles/" + inputFolder + "/" + cubeName + "_" + valueCardinalityProfilePercentage + "_valueCardinalityProfiles.ini");
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(valueCardinalityProfileFile))) {
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

				if(!valueCardinalityProfile.containsKey(columnName)) {
					HashMap<String,Integer> profile = new HashMap<String,Integer>();
					profile.put(columnValue, 1);
					valueCardinalityProfile.put(columnName, profile);
				}else {
					HashMap<String,Integer> profile = valueCardinalityProfile.get(columnName);
					if(!profile.containsKey(columnValue)) {
						profile.put(columnValue, 1);
						valueCardinalityProfile.put(columnName,profile);
					}else {
						int currentValue = profile.get(columnValue);
						profile.put(columnValue, currentValue + 1);
						valueCardinalityProfile.put(columnName,profile);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nDONE WITH LOADING THE FACT TABLE SAMPLE\n");
		return valueCardinalityProfile;
	}
	
	private HashMap<String,HashMap<String,Integer>> calculateSelectivityFromFileWithBatches(boolean sqrtSample) {
		File valueCardinalityProfileFile;
		if(sqrtSample) {
			valueCardinalityProfileFile = new File("InputFiles/" + inputFolder + "/" + cubeName + "_sqrt_valueCardinalityProfile.csv");
		}else {
			valueCardinalityProfileFile = new File("InputFiles/" + inputFolder + "/" + cubeName + "_" + valueCardinalityProfilePercentage + "_valueCardinalityProfile.csv");
		}
		
		try (BufferedReader reader = new BufferedReader(new FileReader(valueCardinalityProfileFile))) {
			reader.readLine();
			String line;
			
			

			while ((line = reader.readLine()) != null) {
				if (line.startsWith("factTableSize")) {
					try {
						storedFactTableSize = Integer.parseInt(line.split(",")[2]);
					} catch (NumberFormatException ignore) {}
					continue;
				}else if(line.startsWith("sampleSize")) {
					try {
						sampleSize =  Integer.parseInt(line.split(",")[2]);
					} catch (NumberFormatException ignore) {}
				}
				
				String[] atomParts = line.split(",");
				String columnName = atomParts[0];
				String columnValue = atomParts[1];
				int detailedSelectivity = Integer.parseInt(atomParts[2]);
				
				if(!valueCardinalityProfile.containsKey(columnName)) {
					HashMap<String,Integer> profile = new HashMap<String,Integer>();
					profile.put(columnValue, detailedSelectivity);
					valueCardinalityProfile.put(columnName, profile);
				}else {
					HashMap<String,Integer> profile = valueCardinalityProfile.get(columnName);
					if(!profile.containsKey(columnValue)) {
						profile.put(columnValue, detailedSelectivity);
						valueCardinalityProfile.put(columnName,profile);
					}else {
						int currentValue = profile.get(columnValue);
						profile.put(columnValue, currentValue + detailedSelectivity);
						valueCardinalityProfile.put(columnName,profile);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\nDONE WITH LOADING THE VALUE CARDINALITY PROFILE\n");
		return valueCardinalityProfile;
	}

	
	/**
	 * Builds the valueCardinalityProfile if the .ini file is missing from the dataset's input folder.
	 * It retrieves the tuples that correspond to the keys of the Reservoir Sample and fetches all
	 * the respective levels and values of each dimension that participates in the fact table tuple. 
	 * The result is written in a .ini file.
	 * @param cubeBase
	 * @param schemaName
	 * @param cubeName
	 * @param valueCardinalityProfilePercentage
	 * @param forceRebuild
	 * @throws IOException
	 * @throws SQLException
	 */
	public void buildSelectivitySampleWithBatches(CubeBase cubeBase, String schemaName, String cubeName, double valueCardinalityProfilePercentage, boolean forceRebuild, boolean sqrtSample) throws IOException, SQLException {
		File file;
		this.valueCardinalityProfilePercentage = valueCardinalityProfilePercentage;
		int numOfBatches = 0;
		int sampleLoopLimit; 
		HashMap<String,Integer> detailedLevelCardinalitiesInBatch = new HashMap<String,Integer>();
		
		if(sqrtSample) {
			file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_sqrt_valueCardinalityProfile.csv");
		}else {
			file = new File("InputFiles/" + inputFolder + "/" + cubeName + "_" + valueCardinalityProfilePercentage + "_valueCardinalityProfile.csv");
		}
		if (file.exists() && !forceRebuild) {
			calculateSelectivityFromFileWithBatches(sqrtSample);
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
					writer.println("factTableSize,," + storedFactTableSize);
					
					if(!sqrtSample) {
						this.sampleSize = (int) (valueCardinalityProfilePercentage * storedFactTableSize);
					}else {
						this.sampleSize = (int) (Math.sqrt(storedFactTableSize));
					}
					writer.println("sampleSize,," + sampleSize);
					
					for (int i = 0; i < dimensions.size(); i++) {
						String factFK = dimRefFields.get(i);
						if(dimensionPrimaryKeys.equals("")) {
							dimensionPrimaryKeys += factFK;
						}else {
							dimensionPrimaryKeys += "," + factFK;
						}
					}
					
					// BUILD SAMPLE
					String [] reservoirSampleIds = buildReservoir(storedFactTableSize,sampleSize,random);
					
					for(int i = 0;i<reservoirSampleIds.length;i += BATCH_SIZE) {
						// CREATE BATCH
						StringBuilder sqlWhereStatement = new StringBuilder(" WHERE SK_id IN (" + reservoirSampleIds[i]);
						numOfBatches++;
						sampleLoopLimit = numOfBatches*BATCH_SIZE;
						if(sampleLoopLimit> reservoirSampleIds.length) {
							sampleLoopLimit = reservoirSampleIds.length;
						}
						for(int j = i + 1; j<sampleLoopLimit;j++) {
							sqlWhereStatement.append("," + reservoirSampleIds[j]);
						}
						
						// FETCH BATCH 
						String sql = "SELECT " + dimensionPrimaryKeys + " FROM " + factTable + sqlWhereStatement + ");";
						Result result = new Result();
						cubeBase.executeQueryToProduceResult(sql, result);
						String[][] resultArray = result.getResultArray();
						
						// BUILD CARDINALITY FOR DETAILED TUPLES
						for(int k = 2;k<resultArray.length;k++) {
							for(int l = 0;l<dimensions.size();l++) {
								Dimension dimension = dimensions.get(l);
								String detailedTupleKey = dimension.getName() + "." + resultArray[k][l];
								if(!detailedLevelCardinalitiesInBatch.containsKey(detailedTupleKey)) {
									detailedLevelCardinalitiesInBatch.put(detailedTupleKey,1);
								}else {
									int currentEntryValue = detailedLevelCardinalitiesInBatch.get(detailedTupleKey);
									detailedLevelCardinalitiesInBatch.put(detailedTupleKey,currentEntryValue + 1);
								}
							}
						}
					}
					//FIND HIERARCHY LEVELS FOR ALL DETAILED TUPLES
					for(int i = 0;i<dimensions.size();i++) {
						Dimension dimension = dimensions.get(i);
						String tableName = dimension.getTableName();
						String dimensionName = dimension.getName();
						String dimPK = dimension.getHierarchy().get(0).getLevels().get(0).getAttributeName(0);
						int numOfLevels = 0;
						String sqlSelectClause = "SELECT ";
						
						for (Level level : dimension.getHierarchy().get(0).getLevels()) {
							if(level.getAttributeName(0).equals("All")) {
								sqlSelectClause += "`" + level.getAttributeName(0) + "`" + ",";
							}else {
								sqlSelectClause += level.getAttributeName(0) + ",";
							}
							numOfLevels++;
						}
						sqlSelectClause = sqlSelectClause.substring(0, sqlSelectClause.length() - 1);
						
						for(Entry<String,Integer> e: detailedLevelCardinalitiesInBatch.entrySet()) {
							String dimensionNameAsKey = e.getKey().split("\\.")[0];
							String dimensionID = e.getKey().split("\\.")[1];
							int selectivityInDetailedSample = e.getValue();
							
							if(dimensionNameAsKey.equals(dimensionName)) {
								String sqlQuery = sqlSelectClause + " FROM " + tableName + " WHERE " + dimPK + "='" + dimensionID + "'";
								Statement stmt = db.getConnection().createStatement();
								ResultSet results = stmt.executeQuery(sqlQuery);
								
								while(results.next()) {
									for(int k = 1;k<numOfLevels+1;k++) {
										String columnName = tableName + "." + dimension.getHierarchy().get(0).getLevels().get(k-1).getAttributeName(0);
										String columnValue = results.getString(k);
										
										if(!valueCardinalityProfile.containsKey(columnName)) {
											HashMap<String,Integer> profile = new HashMap<String,Integer>();
											profile.put(columnValue, selectivityInDetailedSample);
											valueCardinalityProfile.put(columnName, profile);
										}else {
											HashMap<String,Integer> profile = valueCardinalityProfile.get(columnName);
											if(!profile.containsKey(columnValue)) {
												profile.put(columnValue, selectivityInDetailedSample);
												valueCardinalityProfile.put(columnName,profile);
											}else {
												int currentValue = profile.get(columnValue);
												profile.put(columnValue, currentValue + selectivityInDetailedSample);
												valueCardinalityProfile.put(columnName,profile);
											}
										}
									}
								}
							}	
						}
					}
					for(Entry<String,HashMap<String,Integer>> e : valueCardinalityProfile.entrySet()) {
						String columnName = e.getKey();
						HashMap<String,Integer> profile = e.getValue();
						for(Entry <String,Integer> entry: profile.entrySet()) {
							String columnValue = entry.getKey();
							Integer detailedSelectivity = entry.getValue();
							writer.println(columnName + "," + columnValue + "," + detailedSelectivity);
						}
						
					}
				}
			}
		}
	}
}
