
package interestingnessengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.Level;
import result.Cell;
import result.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;
/**
 * 
 * @author eiriniMouselli
 *
 */
public class InputManager implements IHistoryInput, IExpectedValuesInput{
	
	private ArrayList<Cell> valuePredictions = new ArrayList<Cell>();
	private ArrayList<Cell> labelPredictions = new ArrayList<Cell>();
	private ArrayList<Result> historyResults = new ArrayList<Result>();
	private ArrayList<CubeQuery> historyQueries = new ArrayList<CubeQuery>();
	private FileInputStream fis;
	private Scanner sc;
	private CubeQuery currentQuery;
	private Result currentResult;
	private int kthNeighbor;
	private CubeManager cubeMng;
	private HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap;
	private HashMap<String, ArrayList<String>> levelsToDimensionsHashmap;
	
	private ArrayList<Cell> userGoalCells = new ArrayList<Cell>();

	
	/**
	 * Constructor to be used when there is no history stored and no expected values/labels.
	 * @param cubeMng The current {@link CubeManager} object
	 * @param k       The kth neighbor
	 */
	public InputManager(CubeManager cubeMng, int k){
		this.cubeMng = cubeMng;
		if(k != -1) {
			setKthNeighbor(k);
		}
	}

	/**
	 * Constructor to be used when there is history stored and/or expected values/labels.
	 * @param historyFolder   Path to the History folder
	 * @param expValuesFolder Path to the Expected Values folder
	 * @param expLabelsFolder Path to the Expected Labels folder
	 * @param cubeMng         The current {@link CubeManager} object
	 * @param k               The kth neighbor
	 * @throws RemoteException
	 */
	public InputManager(String historyFolder, String expValuesFolder, String expLabelsFolder, CubeManager cubeMng, int k) throws RemoteException{
		
		this.cubeMng = cubeMng;
		if(!expValuesFolder.equals("")) {
			parseExpectedValues(expValuesFolder);
			// Setting up query goals here for now. May have it's own folder/file/Variable in the future
			setQueryGoals(expValuesFolder);
		}
		if(!expLabelsFolder.equals("")) {
			parseExpectedLabels(expLabelsFolder);
		}
		if(!historyFolder.equals("")) {
			parseQueryHistoryResults(historyFolder + "/Results"); 
			parseQueryHistory(historyFolder + "/Queries");	
		}
		if(k != -1) {
			setKthNeighbor(k);
		}
	}
	/**
	 * Parses the user's expected values. 
	 * <p>
	 * Goes through the given file, creates the {@link Cell}s with the expected values it contains 
	 * and populates the appropriate list.
	 * @param filePath The path to the file containing the user's value predictions
	 */
	private int parseExpectedValues(String filePath) {
		
		try {
			fis = new FileInputStream(filePath);
			sc = new Scanner(fis);
			Cell nextCell = null;
			
			//skip header row
			sc.nextLine();
			
			while(sc.hasNextLine()){ 
				String[] values = sc.nextLine().split("\t");
				
				nextCell = new Cell(values);
				
				if(nextCell != null) {
					valuePredictions.add(nextCell);
				}
			} 
			
			sc.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}       
		
		if(valuePredictions.size() == 0) {
			System.out.println("No value predictions found.");
			return 0;
		}
		return valuePredictions.size();
	}

	public ArrayList<Cell> getExpectedValues() {
		return valuePredictions;
	}
	/**
	 * Parses the user's expected labels. 
	 * <p>
	 * Goes through the given file, creates the {@link Cell}s with the expected labels it contains 
	 * and populates the appropriate list.
	 * @param filePath The path to the file containing the user's label predictions
	 */
	private int parseExpectedLabels(String filePath) {
		try {
			fis = new FileInputStream(filePath);
			sc = new Scanner(fis);
			Cell nextCell = null;
			
			//skip header row
			sc.nextLine();
			
			while(sc.hasNextLine()){ 
				String[] values = sc.nextLine().split("\t");
				nextCell = new Cell(values);
				
				if(nextCell != null) {
					labelPredictions.add(nextCell);
				}
			} 
			
			sc.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}       
		
		if(labelPredictions.size() == 0) {
			System.out.println("No label predictions found.");
			return 0;
		}
		return labelPredictions.size();
	}
		
	public ArrayList<Cell> getExpectedLabels() {
		return labelPredictions;
	}
	/** 
	 * Parses the user's query history results
	 * <p>
	 * Goes through every file in given folder and for each file creates a {@link Result} object 
	 * with the {@link Cell}s contained in the file and populates the appropriate list.
	 * @param filePath The path to the file containing previous queries' results
	 */
	private int parseQueryHistoryResults(String folderPath) {
		try {
			List<String> filesInFolder = Files.walk(Paths.get(folderPath))
			        .filter(Files::isRegularFile)
			        .map(Path::toString)
			        .collect(Collectors.toList());
			
			Result newResult;	
			ArrayList<String[]> valuesList;
			ArrayList<String> columnNames;
			ArrayList<String> columnLabels;
			ArrayList<Cell> cells;
			Cell nextCell;
			int rows, columns;
			for(String file: filesInFolder) {
				if(!file.equals("History/Results/.gitignore")) {
					try {
						fis = new FileInputStream(file);
						sc = new Scanner(fis);
						valuesList = new ArrayList<String[]>();
						columnNames = new ArrayList<String>();
						columnLabels = new ArrayList<String>();
						cells = new ArrayList<Cell>();
						nextCell = null;
						
						columnNames =  new ArrayList<String>(Arrays.asList(sc.nextLine().split("\t")));
						
						while(sc.hasNextLine()){ 
							String[] values = sc.nextLine().split("\t");
							valuesList.add(values);
							nextCell = new Cell(values);
							
							if(nextCell != null) {
								cells.add(nextCell);
							}
						} 
						rows = valuesList.size();
						columns = columnNames.size();
						String[][] resultArray = new String[rows + 2][columns];
						
						for(int k = 0; k < columns; k++) {
							resultArray[0][k] = columnNames.get(k);
						}
						
						for(int i = 2; i < rows; i++) {
							for(int j = 0; j < columns; j++) {
								resultArray[i][j] = valuesList.get(i-2)[j];
							}
						}
						newResult = new Result(resultArray, cells, columnNames, columnLabels);
						historyResults.add(newResult);
						sc.close();  
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				
			}	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return historyResults.size();
		
	}
	
	public int updateQueryHistoryResults(Result latestResult) {
		historyResults.add(latestResult);	
		return historyResults.size();
	}

	public ArrayList<Result> getQueryHistoryResults() {
		return historyResults;
	}
	/**
	 * Parses the user's query history 
	 * <p>
	 * Goes through every file in given folder and for each file creates a {@link CubeQuery} object from 
	 * the raw query string contained in the file.
	 * @param folderPath The path to the folder containing previous queries
	 * @throws RemoteException
	 */
	private int parseQueryHistory(String folderPath) throws RemoteException {
		HashMap<String, String> queryParams;
		CubeQuery query;
		try {
			List<String> filesInFolder = Files.walk(Paths.get(folderPath))
			        .filter(Files::isRegularFile)
			        .map(Path::toString)
			        .collect(Collectors.toList());
			
			for(String file: filesInFolder) {
				if(!file.equals("History/Results/.gitignore")) {
					String queryString = "";
					queryParams = new HashMap<String, String>();
					try {
						fis = new FileInputStream(file);
						sc = new Scanner(fis);
						
						while(sc.hasNextLine()){ 
							queryString += sc.nextLine() + "\n";
						}
						query = cubeMng.createCubeQueryFromString(queryString, queryParams);
						if(query != null) {
							this.historyQueries.add(query);
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return historyQueries.size();
	}

	public int updateQueryHistory(CubeQuery latestQuery) {
		historyQueries.add(latestQuery);		
		return historyQueries.size();
	}

	public ArrayList<CubeQuery> getQueryHistory() {
		return historyQueries;
	}
	
	public void setCurrentQuery(CubeQuery current) {
		currentQuery = current;		
	}

	public CubeQuery getCurrentQuery() {
		return currentQuery;
	}
	
	public void setCurrentQueryResult(Result result) {
		currentResult = result;
	}

	public Result getCurrentQueryResult() {
		return currentResult;
	}

	public ArrayList<Cell> computeDetailedAreaOfInterest() {
		
		TreeSet<Cell> detailedAreaOfInterest = new TreeSet<Cell>(new CellComp());
		ArrayList<Cell> res;
		for(int i = 0; i < historyQueries.size(); i++) {
			res = computeDetailedQueryCube(historyQueries.get(i));
			
			for(int j = 0; j < res.size(); j++) {
				detailedAreaOfInterest.add(res.get(j));
			}
		}
		
		return new ArrayList<Cell>(detailedAreaOfInterest);
	}
	
	public ArrayList<Cell> computeDetailedQueryCube(CubeQuery query) {
		retrieveLevels();
		
		CubeQuery detailedQuery = new CubeQuery(query);
		ArrayList<String[]> gammaExpr = detailedQuery.getGammaExpressions();
		
		for(int i=0; i < gammaExpr.size(); i++) {
			String levelZero = dimensionsToLevelsHashmap.get(gammaExpr.get(i)[0]).get(0);
			gammaExpr.get(i)[1] = levelZero;
		}
		
		detailedQuery.setGammaExpressions(gammaExpr);
		Result detailedResult = cubeMng.executeQuery(detailedQuery);
		
		return detailedResult.getCells();
	}
	
	private void retrieveLevels() {
		dimensionsToLevelsHashmap = new HashMap<String, ArrayList<String>>();
		levelsToDimensionsHashmap = new HashMap<String, ArrayList<String>>();
		List<Dimension> dimensionsList = this.cubeMng.getDimensions();
		ArrayList<String> tmpLvls = null; 
		ArrayList<String> tmpDim = null;
		for (int i=0;i < dimensionsList.size();i ++) {
			Dimension dim = dimensionsList.get(i);
			for(int j=0; j<dim.getHierarchy().size(); j++) {
				List<Level> l = dim.getHierarchy().get(j).getLevels();
				tmpLvls = new ArrayList<String>();
				for (int k=0; k<l.size(); k++) {
					tmpLvls.add(l.get(k).getName());
					String tmpLevelKey = l.get(k).getName();
					if(levelsToDimensionsHashmap.containsKey(tmpLevelKey)) {
						tmpDim = levelsToDimensionsHashmap.get(tmpLevelKey);
						tmpDim.add(dimensionsList.get(i).getName());
						levelsToDimensionsHashmap.put(tmpLevelKey, tmpDim);
					}else {
						tmpDim = new ArrayList<String>();
						tmpDim.add(dimensionsList.get(i).getName());
						levelsToDimensionsHashmap.put(tmpLevelKey, tmpDim);
					}
				}
				dimensionsToLevelsHashmap.put(dimensionsList.get(i).getName(), tmpLvls);
			}
		}
	}

	public int getKthNeighbor() {
		return kthNeighbor;
	}
	/**
	 * Sets the value of the kth neighbor to be k.
	 * @param k The desired value of the kth neighbor. >= 1
	 */
	private void setKthNeighbor(int k) {
		kthNeighbor = k;		
	}

	/**
	 *  My methods start here so that I don't mess up something in the old code 
	 *  ~ Spiros
	 * 
	 */
	
	public ArrayList<Cell> computeDetailedAreaOfInterestOfPreviousQueries() {
			
			TreeSet<Cell> detailedAreaOfInterest = new TreeSet<Cell>(new CellComp());
			ArrayList<Cell> res;
			for(int i = 0; i < historyQueries.size()-1; i++) {
				res = computeDetailedQueryCube(historyQueries.get(i));
				
				for(int j = 0; j < res.size(); j++) {
					detailedAreaOfInterest.add(res.get(j));
				}
			}
			
			return new ArrayList<Cell>(detailedAreaOfInterest);
	}

	public ArrayList<Cell> getAllCellsVisited() {
		ArrayList<Cell> cells = new ArrayList <Cell>();
		
		for (CubeQuery q: historyQueries) {
			for (Cell c: computeDetailedQueryCube(q)) {
				cells.add(c);
			}
		}
		
		return cells;
	}
	
	public void setQueryGoals(String filePath) throws RemoteException {
		try {
			fis = new FileInputStream(filePath);
			sc = new Scanner(fis);
			Cell nextCell = null;
			
			//skip header row
			sc.nextLine();
			
			while(sc.hasNextLine()){ 
				String[] values = sc.nextLine().split("\t");
				nextCell = new Cell(values);
				
				if(nextCell != null) {
					userGoalCells.add(nextCell);
				}
			} 
			
			sc.close();  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}       
		
		if(userGoalCells.size() == 0) {
			System.out.println("User's goals were not specified.");
		}
		
	}

	public ArrayList<Cell> getQueryGoals() {
		return userGoalCells;
	}
	
	/**
	 *  My methods end here
	 *  ~ Spiros
	 * 
	 */
}
/**
 * 
 * A Comparator implementation for the {@code TreeSet<Cell>} set needed in the 
 * computeDetailedAreaOfInterest() method
 *
 */
class CellComp implements Comparator<Cell>{

	/**
	 * Compares the dimension members of two cells.
	 * @param o1 First cell
	 * @param 02 Second cell
	 */
	public int compare(Cell o1, Cell o2) {
		return o1.getDimensionMembers().toString().compareTo(o2.getDimensionMembers().toString());	
	}	
	
}