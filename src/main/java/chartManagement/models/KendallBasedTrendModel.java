package chartManagement.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class KendallBasedTrendModel extends ChartModel{

	
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
		
		List<String> categories = new ArrayList<>();
		for(int i=2; i<query.length; i++) {
			if(!categories.contains(query[i][1])) {
				categories.add(query[i][1]);
			}
		}
		
		String result = "";
		for(String category: categories) {
			String [][] arrayForCategory = {{"Default_row_col_1","Default_row_col_1","Default_row_col_1"},
					{"Default_row_col_2","Default_row_col_2","Default_row_col_2"}};
			for(int i=2; i<query.length; i++) {
				if(query[i][1].equals(category)) {
					String [] insertedArray = {query[i][0],category,query[i][2]};
					arrayForCategory = addRow(arrayForCategory, insertedArray);
				}
			}
			result += findTrendForOneCategoryInSeries(arrayForCategory) + "\t";
			
		}
		return result;
	}

	private String findTrendForOneCategoryInSeries(String[][] query) {
		
		String category = query[2][1];
		KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();
		String regexYYYY = "\\d{4}-\\d{2}";
		String regexYYYYdd = "\\d{4}";
		double [] x_values = new double[query.length-2];
		double [] y_values = new double[query.length-2];
		int position_counter = 0;
		for(int i=2; i<query.length; i++) {
			
			double x=-1;
			double y;
			
			if (query[i][0].matches(regexYYYY)) {
				String month = query[i][0].split("-")[1];
				x = Double.parseDouble(month);
			} else if(query[i][0].matches(regexYYYYdd)) {
				x = Double.parseDouble(query[i][0]);
			}
			y = Double.parseDouble(query[i][2]);
			if(x!=-1) {
				x_values[position_counter] = x;
				y_values[position_counter] = y;
				position_counter +=1;
			}
			
		}
		double kendallCoefficient = kendallsCorrelation.correlation(x_values, y_values);
		String result = "";
		if (kendallCoefficient >= 0.5 && kendallCoefficient <1) {
			result = " has an uptrend.";
			
		} else if (kendallCoefficient > -0.5 && kendallCoefficient< 0.5) {
			result = " has no clear trend.";
		} else {
			result = " has a downtrend";
		}
		return query[2][0] + result;
	}

	private boolean originalIsInSpecifiedOrder(List<String> values, List<String> orderedValues) {
	
		
		return values.equals(orderedValues);
	}

	

	@Override
	public String getModelName() {
	
		return "Kendall's based Trend";
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
		return "Trend model (Uptrend/Downtrend) Kendall based.";
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
	
//	 // Method to check if there's an uptrend
//    private static boolean isUptrend(List<Double> measures) {
//        for (int i = 1; i < measures.size(); i++) {
//            if (measures.get(i) <= measures.get(i - 1)) {
//                return false; // Not an uptrend
//            }
//        }
//        return true; // All values are increasing
//    }
//
//    // Method to check if there's a downtrend
//    private static boolean isDowntrend(List<Double> measures) {
//        for (int i = 1; i < measures.size(); i++) {
//            if (measures.get(i) >= measures.get(i - 1)) {
//                return false; // Not a downtrend
//            }
//        }
//        return true; // All values are decreasing
//    }
}
