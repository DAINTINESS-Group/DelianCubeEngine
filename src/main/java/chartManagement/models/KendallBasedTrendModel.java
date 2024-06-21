package chartManagement.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import chartManagement.utils.ChartVisModel;
import chartManagement.utils.DataPoint;

public class KendallBasedTrendModel extends ChartModel{

	
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
		
		List<String> categories = new ArrayList<>();
		for(int i=2; i<query.length; i++) {
			if(!categories.contains(query[i][1])) {
				categories.add(query[i][1]);
			}
		}
        this.score = 0;
        double sum_scoreForAllSeries = 0;
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
			sum_scoreForAllSeries += this.score;
			
		}
		this.score = sum_scoreForAllSeries/categories.size();
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
			result = " has a downtrend.";
		}
		this.score = Math.abs(kendallCoefficient);
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

	@Override
	public double getScoreOfModel() {
		// TODO Auto-generated method stub
		return 0;
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
	
	@Override
	public String reportScore() {
		// TODO Auto-generated method stub
		return "\t with score: " + getScoreOfModel();
	}

	@Override
	public double computeScore(ChartVisModel model) {
		List<DataPoint> points = model.getDataPoints();
		KendallsCorrelation kendallsCorrelation = new KendallsCorrelation();
		double kendallCoefficient =0;
		String regexYYYY = "\\d{4}-\\d{2}";
		String regexYYYYdd = "\\d{4}";
		double [] x_axis = new double[points.size()];
		double [] y_axis = new double[points.size()];
		int counter=0;
		for(DataPoint point: points) {
			Double x = -1.0;
			Double y;
			String x_value = point.getGrouper1();
			if (x_value.matches(regexYYYY)) {
				String month = x_value.split("-")[1];
				x = Double.parseDouble(month);
			} else if(x_value.matches(regexYYYYdd)) {
				x = Double.parseDouble(x_value);
			}
			y = point.getMeasure();
			if(x!=-1.0) {
				x_axis[counter] = x;
				y_axis[counter] = y;
				counter +=1;
			}
			
		}
		
		kendallCoefficient = kendallsCorrelation.correlation(x_axis, y_axis);
		if(kendallCoefficient<-0.5) {
			setScoreResult("Series has a Kendall based downtrend.");
		} else if(kendallCoefficient> 0.5) {
			setScoreResult("Series has a Kendall based uptrend.");
		} else {
			setScoreResult("Series hasn't a Kendall based trend.");
		}
		return kendallCoefficient;	
	}
	
	
}
