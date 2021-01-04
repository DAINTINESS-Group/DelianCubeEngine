package interestingnessengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cubemanager.CubeManager;
import cubemanager.cubebase.CubeQuery;
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

	public void parseExpectedValues(String filePath) {
		
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
		}
	}

	public ArrayList<Cell> getExpectedValues() {
		return valuePredictions;
	}

	public void parseExpectedLabels(String filePath) {
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
		}
	}
		
	public ArrayList<Cell> getExpectedLabels() {
		return labelPredictions;
	}

	public void parseQueryHistoryResults(String folderPath) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void updateQueryHistoryResults(Result latestResult) {
		historyResults.add(latestResult);		
	}

	public ArrayList<Result> getQueryHistoryResults() {
		return historyResults;
	}

	public void parseQueryHistory(String folderPath) throws RemoteException {
		HashMap<String, String> queryParams;
		CubeQuery query;
		try {
			List<String> filesInFolder = Files.walk(Paths.get(folderPath))
			        .filter(Files::isRegularFile)
			        .map(Path::toString)
			        .collect(Collectors.toList());
			
			for(String file: filesInFolder) {
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
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void updateQueryHistory(CubeQuery latestQuery) {
		historyQueries.add(latestQuery);		
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

		CubeQuery detailedQuery = new CubeQuery(query);
		ArrayList<String[]> gammaExpr = detailedQuery.getGammaExpressions();
		
		for(int i=0; i < gammaExpr.size(); i++) {
			gammaExpr.get(i)[1] = "lvl0";
		}
		detailedQuery.setGammaExpressions(gammaExpr);
		Result detailedResult = cubeMng.executeQuery(detailedQuery);
		
		return detailedResult.getCells();
	}

	public int getKthNeighbor() {
		return kthNeighbor;
	}

	public void setKthNeighbor(int k) {
		kthNeighbor = k;		
	}
	
	
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