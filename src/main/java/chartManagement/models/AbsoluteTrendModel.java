package chartManagement.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import chartManagement.utils.ChartVisModel;


public class AbsoluteTrendModel extends ChartModel{


	private String result [][];
	private double score;
	
	@Override
	public int compute() {
		
	    result = readResultsFromFileAndSaveTo2DMatrix();
	    if(result!=null) {
	    	
	    	int counterOfSiblings = 1;
	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultTrend = findTrendInArray(query);
	    		String typeQuery =  query[0][2].trim();
	    		if(typeQuery.equals("Sibling")) {
	    			
	    			reportedResult += getModelName() + "\t" + typeQuery + String.valueOf(counterOfSiblings) + "\t" + resultTrend + reportScore() + "\n";
	    			counterOfSiblings+=1;

	    		}else {
	    			reportedResult += getModelName() + "\t" + typeQuery + "\t" + resultTrend + reportScore() + "\n";

	    		}
	    	}
	    	
	    	return 0;
	    }
	    return -1;
	}

	public String findTrendInArray(String[][] query) {
		
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query,1)) {
			return findTrendForOneCategoryInSeries(query);
		}
	
		return "";//findTrendForMultipleCategoriesInSeries(query);
	}

	private String findTrendForMultipleCategoriesInSeries(String[][] query) {
		
        String result = "";
        this.score = 0;
        double sum_scoreForAllSeries = 0;
        Map<String, List<String[]>> data = returnMapFromString2Darray(query);

        for (Map.Entry<String, List<String[]>> entry : data.entrySet()) {
            String grouper2 = entry.getKey();
            List<String[]> measures = entry.getValue();

            // Convert List<String[]> to String[][] for processing
            String[][] subset = new String[measures.size() + 1][];
            subset[0] = query[0]; // headers
            for (int i = 0; i < measures.size(); i++) {
                subset[i + 1] = measures.get(i);
            }

            // Call findTrendForOneCategoryInSeries for each category
            String trendResult = findTrendForOneCategoryInSeries(subset);
            sum_scoreForAllSeries += this.score;
            result += grouper2 + ": " + trendResult + "\t";
        }
        this.score = sum_scoreForAllSeries/data.size();
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
			this.score = 1;
			return query[2][1] + " has an uptrend.";
		}
		List<String> descendingOrderValues = new ArrayList<>(values);
		Collections.sort(descendingOrderValues, Collections.reverseOrder());
		if(originalIsInSpecifiedOrder(values, descendingOrderValues)) {
			this.score = Math.abs(-1);
			return query[2][1] + " has a downtrend.";
		}
		this.score = 0;
		return query[2][1] + " has no clear trend.";
	}

	private boolean originalIsInSpecifiedOrder(List<String> values, List<String> orderedValues) {
	
		
		return values.equals(orderedValues);
	}

	

	@Override
	public String getModelName() {
	
		return "Absolute Trend";
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
		return "Absolutetrend model (Uptrend/Downtrend)";
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

	
//	private Map<String, List<Double>> returnMapFromString2Darray(String[][] query){
//		Map<String, List<Double>> dataMap = new HashMap<>();
//		int index =0;
//        // Populate the map
//        for (String[] row : query) {
//        	if(index<2) {
//        		index+=1;
//        		continue;
//        	}
//            String grouper2 = row[1]; 
//            Double measure = Double.parseDouble(row[2]); // Assuming Measure is at index 2
//
//            // If the key doesn't exist, create a new list and put it into the map
//            dataMap.putIfAbsent(grouper2, new ArrayList<>());
//
//            // Add the measure to the list for the corresponding key
//            dataMap.get(grouper2).add(measure);
//        }
//        
//        return dataMap;
//	}
	
    private Map<String, List<String[]>> returnMapFromString2Darray(String[][] query) {
        Map<String, List<String[]>> data = new HashMap<>();

        for (int i = 1; i < query.length; i++) {
            String grouper = query[i][1];
            data.putIfAbsent(grouper, new ArrayList<>());
            data.get(grouper).add(query[i]);
        }

        return data;
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

	@Override
	public double getScoreOfModel() {
		// TODO Auto-generated method stub
		return this.score;
	}

	@Override
	public String reportScore() {
		// TODO Auto-generated method stub
		return "\t with score: " + getScoreOfModel();
	}

	@Override
	public double computeScore(ChartVisModel model) {
		
		List<Double> measures = model.getDataPoints().stream().map(m -> m.getMeasure()).collect(Collectors.toList());
		
		if(isUptrend(measures)) {
			setScoreResult("Series has an absolute uptrend.");
			return 1;
		} else if(isDowntrend(measures)) {
			setScoreResult("Series has an absolute downtrend.");
			return -1;
		}
		
		setScoreResult("Series hasn't a clear trend.");
		return 0;
	}




	

}
