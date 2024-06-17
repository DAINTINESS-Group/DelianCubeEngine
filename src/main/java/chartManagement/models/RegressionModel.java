package chartManagement.models;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import chartManagement.utils.ChartVisModel;
import chartManagement.utils.DataPoint;


public class RegressionModel extends ChartModel{
	
	enum RegressionType{
		LINEAR
	}
	

	private String result [][];
	private double score;

	

	@Override
	public int compute() {
	    result = readResultsFromFileAndSaveTo2DMatrix();
	    if(result!=null) {
	    	
	    	int counterOfSiblings = 1;
	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultRegression = findRegressionInArray(query);
	    		String typeQuery =  query[0][2].trim();
	    		if(typeQuery.equals("Sibling")) {
	    			
	    			reportedResult += getModelName() + "\t" + typeQuery + String.valueOf(counterOfSiblings) + "\t" + resultRegression + reportScore() + "\n";
	    			counterOfSiblings+=1;

	    		}else {
	    			reportedResult += getModelName() + "\t" + typeQuery + "\t" + resultRegression + reportScore() + "\n";

	    		}
	    	}
	    	
	    	return 0;
	    }
	    return -1;
	}

	public String findRegressionInArray(String[][] query) {
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query,1)) {
			return findRegressionForOneCategoryInSeries(query);
		}
	
		return "";//findRegressionForMultipleCategoriesInSeries(query);
	}

	private String findRegressionForMultipleCategoriesInSeries(String[][] query) {

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
			result += findRegressionForOneCategoryInSeries(arrayForCategory) + "\t";
			sum_scoreForAllSeries += this.score;
			
		}
		this.score = sum_scoreForAllSeries/categories.size();
		return result;
	}

	private String findRegressionForOneCategoryInSeries(String[][] query) {
		String category = query[2][1];
		SimpleRegression regression = new SimpleRegression();
		String regexYYYY = "\\d{4}-\\d{2}";
		String regexYYYYdd = "\\d{4}";
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
				regression.addData(x, y);
			}
			
		}
		//perform regression
		double intercept = regression.getIntercept();
		double slope = regression.getSlope();
		this.score = regression.getMeanSquareError();
		
		return "Linear regression for category (" + category + ") with intercept: " + intercept  + " and slope: " + slope;
    
	}
    
	

	@Override
	public String getModelName() {
		return "Regression";
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

		return "coefficients for regression from Apache common";
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
		
		List<DataPoint> points = model.getDataPoints();
		SimpleRegression regression = new SimpleRegression();
		String regexYYYY = "\\d{4}-\\d{2}";
		String regexYYYYdd = "\\d{4}";
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
				regression.addData(x,y);
			}
			
		}
		
		double intercept = regression.getIntercept();
		double slope = regression.getSlope();
		setScoreResult("Linear ' regression coefficients: intercept: " + intercept + " and slope:" + slope);
		return normalizeToRange(regression.getMeanSquareError());
	}
	
    private double normalizeToRange(double mse) {
        
        double normalized = Math.tanh(mse);
        return normalized;
    }



}
