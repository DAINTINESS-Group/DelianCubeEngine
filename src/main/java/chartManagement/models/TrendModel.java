package chartManagement.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TrendModel extends ChartModel{


	private String result [][];
	
	@Override
	public int compute() {
		
		result = readResultsFromFileAndSaveTo2DMatrix();
		if(result!=null) {
	    	

	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultTrend = findTrendInArray(query);
	    		System.out.println(resultTrend);
	    		reportedResult += getModelName() + "\t" + query[0][2] + "\t" + resultTrend + "\n";
	    	}
	    	return 0;
	    }
		return -1;
	}

	public String findTrendInArray(String[][] query) {
		
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query,1)) {
			return findTrendForOneCategoryInSeries(query);
		}
	
		return findTrendForMultipleCategoriesInSeries(query);
	}

	private String findTrendForMultipleCategoriesInSeries(String[][] query) {
		
		String result = "";
		Map<String, List<Double>> data = returnMapFromString2Darray(query);
		
		for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
            String grouper2 = entry.getKey();
            List<Double> measures = entry.getValue();

            // Check for trend
            if (isUptrend(measures)) {
                result += grouper2 + " has an uptrend.";
            } else if (isDowntrend(measures)) {
                result += grouper2 + " has a downtrend.";
            } else {
                result += grouper2 + " has no clear trend.";
            }
            result+= "\t";
        };
      
        return result;
	}

	private String findTrendForOneCategoryInSeries(String[][] query) {
		
		List<String> values = new ArrayList<>();
		
		for(int i=2; i<query.length; i++) {
			
			values.add(query[i][2]);
			
		}
		
		List<String> ascendingOrderValues =  new ArrayList<>(values);
		Collections.sort(ascendingOrderValues);
		if(originalIsInSpecifiedOrder(values, ascendingOrderValues)) {
			return query[2][1] + " has an uptrend.";
		}
		List<String> descendingOrderValues = new ArrayList<>(values);
		Collections.sort(descendingOrderValues, Collections.reverseOrder());
		if(originalIsInSpecifiedOrder(values, descendingOrderValues)) {
			return query[2][1] + " has a downtrend.";
		}
		return query[2][1] + " has no clear trend.";
	}

	private boolean originalIsInSpecifiedOrder(List<String> values, List<String> orderedValues) {
	
		
		return values.equals(orderedValues);
	}

	

	@Override
	public String getModelName() {
	
		return "Trend";
	}

	@Override
	public String[][] printAs2DStringArray() {
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
		return "Trend model (Uptrend/Downtrend)";
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
            String grouper2 = row[1]; 
            Double measure = Double.parseDouble(row[2]); // Assuming Measure is at index 2

            // If the key doesn't exist, create a new list and put it into the map
            dataMap.putIfAbsent(grouper2, new ArrayList<>());

            // Add the measure to the list for the corresponding key
            dataMap.get(grouper2).add(measure);
        }
        
        return dataMap;
	}
	
	 // Method to check if there's an uptrend
    private static boolean isUptrend(List<Double> measures) {
        for (int i = 1; i < measures.size(); i++) {
            if (measures.get(i) <= measures.get(i - 1)) {
                return false; // Not an uptrend
            }
        }
        return true; // All values are increasing
    }

    // Method to check if there's a downtrend
    private static boolean isDowntrend(List<Double> measures) {
        for (int i = 1; i < measures.size(); i++) {
            if (measures.get(i) >= measures.get(i - 1)) {
                return false; // Not a downtrend
            }
        }
        return true; // All values are decreasing
    }


	

}
