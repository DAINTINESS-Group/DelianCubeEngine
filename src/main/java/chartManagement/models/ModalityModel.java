package chartManagement.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import chartManagement.utils.ChartVisModel;
import model.abstracts.AbstractModel;

public class ModalityModel extends ChartModel{


	private String result [][];
	private double score;

	@Override
	public int compute() {

	    result = readResultsFromFileAndSaveTo2DMatrix();
	    if(result!=null) {
	    	
	    	int counterOfSiblings = 1;
	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultModality = findModalityInArray(query);
	    		String typeQuery =  query[0][2].trim();
	    		if(typeQuery.equals("Sibling")) {
	    			
	    			reportedResult += getModelName() + "\t" + typeQuery + String.valueOf(counterOfSiblings) + "\t" + resultModality + "\n"; //+ reportScore() + "\n";
	    			counterOfSiblings+=1;

	    		}else {
	    			reportedResult += getModelName() + "\t" + typeQuery + "\t" + resultModality + "\n";  //reportScore() + "\n";

	    		}
	    	}
	    	
	    	return 0;
	    }
	    return -1;
	}

	public String findModalityInArray(String[][] query) {

		if(findIfGrouper2ColumnContainsOnlyOneSeries(query,1)) {
			return findModalityForOneCategoryInSeries(query);
		}
	
		return findModalityForMultipleCategoriesInSeries(query);
	}

	private String findModalityForMultipleCategoriesInSeries(String[][] query) {
	
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

		List<Double> differences = new ArrayList<>();
        for (int i = 0; i < measurements.size() - 1; i++) {
            differences.add(measurements.get(i+1)- measurements.get(i));
        }
        if (differences.size() < 2) {
        	this.score = 0;
        	return "There isn't a modality (points < 3).";
        }
        
        int signChanges = 0;
        this.score = 0;
        double sumOfScore = 0;
        int segments =0;
        for (int i = 1; i < differences.size(); i++) {
            if (differences.get(i) * differences.get(i-1) < 0) {
                signChanges++;
                if(areBothValuesPositive (measurements, i-1)) {
                	this.score = computeScoreForPositiveValues(measurements,i-1);
                } else {
                	
                	this.score = computeScoreForNegativeValues(measurements,i-1);
                }
                sumOfScore += this.score;
                segments+=1;
            }
        }
        this.score = sumOfScore/(segments);
        
        if(signChanges==1) {
        	return "has Unimodality.";
        }
        else if(signChanges ==3) {
        	return "has Bimodality.";
        }
		
		return "has no clear Modality.";
		
	}
	
	private double computeScoreForNegativeValues(List<Double> values, int index) {
		double number1 = values.get(index);
		double number2 = values.get(index+1);
		
		double offset = Math.abs(Math.min(number1, number2));
		
		double start = number1 + offset + 1;
		double end = number2 + offset +1;
		if(start>= end) {
			return (start - end)/start;
		} else {
			return (end - start)/end;
		}
		
	}
	
	

	private String findModalityForOneCategoryInSeries(String[][] query) {

		List<Double> values = new ArrayList<>();
		
		for(int i=2; i<query.length; i++) {
			
			values.add(Double.parseDouble(query[i][2]));
			
		}
		
		List<Double> differences = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            differences.add(values.get(i+1)- values.get(i));
        }
        
        int signChanges = 0;
        this.score = 0;
        double sumOfScore = 0;
        for (int i = 1; i < differences.size(); i++) {
            if (differences.get(i) * differences.get(i-1) < 0) {
                signChanges++;
                if(areBothValuesPositive (values, i-1)) {
                	this.score = computeScoreForPositiveValues(values,i-1);
                } else {
                	this.score = computeScoreForNegativeValues(values,i-1);
                }
                
                sumOfScore += this.score;
            }
        }
        this.score = sumOfScore/(differences.size()-1);
        
        if(signChanges==1) {
        	return query[2][1] + " has Unimodality.";
        }
        else if(signChanges ==3) {
        	return query[2][1] + " has Bimodality.";
        }
		
		return query[2][1] + " has no clear Modality.";
	}

	private double computeScoreForPositiveValues(List<Double> values, int index) {
		
		double number1 = Math.abs(values.get(index));
		double number2 = Math.abs(values.get(index+1));
		
		if(number1>= number2) {
			return (number1 - number2)/number1;
		} else {
			return (number2 - number1)/number2;
		}
		
	}

	private Boolean areBothValuesPositive (List<Double> values, int indexOfDifference) {
		
		double number1 = values.get(indexOfDifference);
		double number2 = values.get(indexOfDifference +1);
		
		if (number1 > 0 && number2 >0 ) {
			
			return true;
		}
		
		return false;
	}

	@Override
	public String getModelName() {

		return "modality";
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
		String result = findModality(measures);
		setScoreResult("Series " + result);
		return this.score;
	}
	


}
