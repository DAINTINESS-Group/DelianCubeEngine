package chartManagement.models;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.regression.SimpleRegression;


public class RegressionModel extends ChartModel{
	
	enum RegressionType{
		LINEAR,
		POLYNOMIAL,
		NON_LINEAR
	}
	

	private String result [][];
	

	

	@Override
	public int compute() {
		result = readResultsFromFileAndSaveTo2DMatrix();
		if(result!=null) {
	    	

	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultRegression = findRegressionInArray(query);
	    		System.out.println(resultRegression);
	    		reportedResult += getModelName() + "\t" + query[0][2] + "\t" + resultRegression + "\n";
	    	}
	    	
	    }
		return 0;
	}

	private String findRegressionInArray(String[][] query) {
		// TODO Auto-generated method stub
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query,1)) {
			return findRegressionForOneCategoryInSeries(query);
		}
	
		return findRegressionForMultipleCategoriesInSeries(query);
	}

	private String findRegressionForMultipleCategoriesInSeries(String[][] query) {
		// TODO Auto-generated method stub
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
			result += findRegressionForOneCategoryInSeries(arrayForCategory) + "\t";
			
		}
		return result;
	}

	private String findRegressionForOneCategoryInSeries(String[][] query) {
		String category = query[2][1];
		SimpleRegression regression = new SimpleRegression();
		String regexYYYY = "\\d{4}-\\d{2}";
		String regexYYYYdd = "\\d{4}";
		for(int i=2; i<query.length; i++) {
			
			double x = -200;
			double y;
			
			if(query[i][0].matches(regexYYYY)) {
				String month = query[i][0].split("-")[1];
				x = Double.parseDouble(month);
			}else if(query[i][0].matches(regexYYYYdd)) {
				x = Double.parseDouble(query[i][0]);
			}
			y = Double.parseDouble(query[i][2]);
			if(x!=-200) {
				regression.addData(x, y);
			}
			
		}
		//perform regression
		double intercept = regression.getIntercept();
		double slope = regression.getSlope();
		return "Linear regression for category (" + category + ") with intercept: " + intercept  + " and slope: " + slope;
    
	}
    
	

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "Regression";
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
		return "coefficients for regression from Apache common";
	}

	@Override
	public void setFolderAndFilename(String fileLocation, String filename) {
		this.fileLocation = fileLocation;
		this.fileName = filename;
		
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


}
