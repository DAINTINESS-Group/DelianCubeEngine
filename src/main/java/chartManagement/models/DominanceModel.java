package chartManagement.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.abstracts.AbstractModel;
import result.Result;

public class DominanceModel extends AbstractModel implements IChartModel{
	
	private String fileName;
	private String fileLocation;
	private String result [][];
	
	private String reportedResult = "";
	
	public DominanceModel() {
		super();
	}

	public DominanceModel(Result aResult) {
		super(aResult);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compute() {
	    // TODO Auto-generated method stub
	    
	    result = readResultsFromFileAndSaveTo2DMatrix(this.fileLocation, this.fileName);
	    if(result!=null) {
	    	
//	    	System.out.println("From Dominance read:\t");
//	    	printAs2DStringArray();
	    	//TODO make a function that will split the array into smaller arrays, one to one query
	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultDominance = findDominanceInArray(query);
	    		System.out.println(resultDominance);
	    		reportedResult += getModelName() + "\t" + query[0][2] + "\t" + resultDominance + "\n";
	    	}
	    	//TODO if grouper 2 column second has different values then look for dominance ...otherwise no
	    	
	    }
	    return 0;
	}

	private String findDominanceInArray(String[][] query) {
		// TODO Auto-generated method stub
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query, 1)) {
			return "No Dominance, The query result has only one Category (" +  query[2][1] + ") for Series.";
		}
		return findIfDominanceExistsInSeries(query);
	}
	
	private String findIfDominanceExistsInSeries(String[][] query) {
		// TODO Auto-generated method stub
		Map<String, String> datesWithDominatorCategoriesValues = new HashMap<>();
		List <String> categories = new ArrayList<>(); 
		for(int i=2; i<query.length; i++) {
			String grouper1 = query[i][0];
			if(!datesWithDominatorCategoriesValues.containsKey(grouper1)) {
				datesWithDominatorCategoriesValues.put(grouper1, query[i][2]);
			}else {
				if(Double.parseDouble(datesWithDominatorCategoriesValues.get(grouper1))< 
						Double.parseDouble(query[i][2])) {
					datesWithDominatorCategoriesValues.put(grouper1, query[i][2]);
				}
			}
			if(!categories.contains(query[i][1])) {
				categories.add(query[i][1]);
			}
			
				
		}
		Map<String, String> sortedMap =  new TreeMap<>(datesWithDominatorCategoriesValues);
		String dominatorValue = null;
		for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            
			for(int i=2; i<query.length; i++) {
				
				if(query[i][0].equals(entry.getKey()) && query[i][2].equals(entry.getValue())){
					if(dominatorValue!=null && !dominatorValue.equals(query[i][1])) {
						
						
						return "There isn't a Dominator in Series with Categories: " + getCategories(categories);
					}
					dominatorValue = query[i][1];
				}
					
			}
            
        }
		return "Dominator Value for query : " + dominatorValue;
		
	}

	private String getCategories(List<String> collection) {
		// TODO Auto-generated method stub
		String categories = "";
		for(String category : collection) {
			categories += category + ", ";
		}
		return categories;
	}

	public Boolean findIfGrouper2ColumnContainsOnlyOneSeries(String[][] array, int columnIndex) {
        String value = "";
        Boolean hasOnlyOneSeries = true;
        for (int i = 2; i < array.length; i++) {
            if(value.equals("")) {
            	value = array[i][columnIndex];
            	continue;
            }
            if(!array[i][columnIndex].equals(value)) {
            	hasOnlyOneSeries = false;
            	break;
            }
            
        }
        return hasOnlyOneSeries;
    }

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "Dominance";
	}
	
	

	@Override
	public String[][] printAs2DStringArray() {
		// TODO Auto-generated method stub
		//System.out.println("im inside with size " + result.length + "x " + result[0].length);
		for (int i = 0; i < this.result.length; i++) {
			
            for(int j=0; j<this.result[i].length; j++) {
            	System.out.print(this.result[i][j] + "\t");
            }
            System.out.println();
        }
		return result;
	}

	@Override
	public String getInfoContent() {
		// TODO Auto-generated method stub
		return "A dominance model created by Aggeliki Dougia";
	}
	
	public void setFileName(String filename, String fileLocation) {
		 this.fileName = filename;
		 this.fileLocation = fileLocation;
	}
	
	public List<String[][]> extractArrayListWithSmallerArrays(String [][] bigArray){
		List<String[][]> smallerArrays = new ArrayList<>();
		int startIndex = -1;
        for (int i = 0; i < bigArray.length; i++) {
            if (bigArray[i][0].equals("Type") && bigArray[i][1].equals("Query")) {
                if (startIndex != -1) {
                    // If startIndex is not -1, add the current slice to smallerArrays
                    String[][] slice = new String[i - startIndex][];
                    System.arraycopy(bigArray, startIndex, slice, 0, slice.length);
                    smallerArrays.add(slice);
                }
                // Update startIndex for the next slice
                startIndex = i;
            }
        }

        // Add the last slice
        if (startIndex != -1) {
            String[][] slice = new String[bigArray.length - startIndex][];
            System.arraycopy(bigArray, startIndex, slice, 0, slice.length);
            smallerArrays.add(slice);
        }
        return smallerArrays;

	}

	@Override
	public String printAs2DStringArrayForChartReportModel() {
		// TODO Auto-generated method stub
		return this.reportedResult;
	}

	@Override
	public void setFolderAndFilename(String fileLocation, String filename) {
		this.fileLocation = fileLocation;
		this.fileName = filename;
		
	}


	

}
