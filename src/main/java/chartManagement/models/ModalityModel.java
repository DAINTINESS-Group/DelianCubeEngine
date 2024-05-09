package chartManagement.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.abstracts.AbstractModel;

public class ModalityModel extends AbstractModel implements IChartModel{

	private String fileName;
	private String fileLocation;
	private String result [][];
	
	private String reportedResult = "";
	
	@Override
	public String printAs2DStringArrayForChartReportModel() {
		// TODO Auto-generated method stub
		return this.reportedResult;
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
	    		String resultModality = findModalityInArray(query);
	    		System.out.println(resultModality);
	    		reportedResult += getModelName() + "\t" + query[0][2] + "\t" + resultModality + "\n";
	    	}
	    	//TODO if grouper 2 column second has different values then look for dominance ...otherwise no
	    	
	    }
		return 0;
	}

	private String findModalityInArray(String[][] query) {
		// TODO Auto-generated method stub
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query,1)) {
			return findModalityForOneCategoryInSeries(query);
		}
	
		return findModalityForMultipleCategoriesInSeries(query);
	}

	private String findModalityForMultipleCategoriesInSeries(String[][] query) {
		// TODO Auto-generated method stub
		String result = "";
		Map<String, List<Double>> data = returnMapFromString2Darray(query);
		
		for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
            String grouper2 = entry.getKey();
            List<Double> measures = entry.getValue();
            result += grouper2 + " " + findModality(measures) + "\t";
		}
		return result;
	}

	private String findModality(List<Double> measurements) {
		// TODO Auto-generated method stub
		List<Double> differences = new ArrayList<>();
        for (int i = 0; i < measurements.size() - 1; i++) {
            differences.add(measurements.get(i+1)- measurements.get(i));
        }
        
        int signChanges = 0;
        for (int i = 1; i < differences.size(); i++) {
            if (differences.get(i) * differences.get(i-1) < 0) {
                signChanges++;
            }
        }
        if(signChanges<=1) {
        	return "has Unimodality.";
        }
        else if(signChanges ==2) {
        	return "has Bimodality.";
        }
		
		return "has no clear Modality.";
		
	}

	private String findModalityForOneCategoryInSeries(String[][] query) {
		// TODO Auto-generated method stub
		List<Double> values = new ArrayList<>();
		
		for(int i=2; i<query.length; i++) {
			
			values.add(Double.parseDouble(query[i][2]));
			
		}
		
		List<Double> differences = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            differences.add(values.get(i+1)- values.get(i));
        }
        
        int signChanges = 0;
        for (int i = 1; i < differences.size(); i++) {
            if (differences.get(i) * differences.get(i-1) < 0) {
                signChanges++;
            }
        }
        
        if(signChanges<=1) {
        	return query[2][1] + " has Unimodality.";
        }
        else if(signChanges ==2) {
        	return query[2][1] + " has Bimodality.";
        }
		
		return query[2][1] + " has no clear Modality.";
	}

	@Override
	public void setFileName(String filename, String fileLocation) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "modality";
	}

	@Override
	public String[][] printAs2DStringArray() {
		// TODO Auto-generated method stub
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
		return "Unimodality or Bimodality finder model ";
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

	private Map<String, List<Double>> returnMapFromString2Darray(String[][] query){
		Map<String, List<Double>> dataMap = new HashMap<>();
		int index =0;
        // Populate the map
        for (String[] row : query) {
        	if(index<2) {
        		index+=1;
        		continue;
        	}
            String grouper2 = row[1]; // Assuming Grouper 2 is at index 1
            Double measure = Double.parseDouble(row[2]); // Assuming Measure is at index 2

            // If the key doesn't exist, create a new list and put it into the map
            dataMap.putIfAbsent(grouper2, new ArrayList<>());

            // Add the measure to the list for the corresponding key
            dataMap.get(grouper2).add(measure);
        }
        
        return dataMap;
	}

	@Override
	public void setFolderAndFilename(String fileLocation, String filename) {
		this.fileLocation = fileLocation;
		this.fileName = filename;
		
	}
}
