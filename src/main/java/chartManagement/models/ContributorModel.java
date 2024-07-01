package chartManagement.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import chartManagement.utils.ChartVisModel;
import chartManagement.utils.DataPoint;


public class ContributorModel extends ChartModel{


	private String result [][];
	private double score;


	@Override
	public int compute() {
	    result = readResultsFromFileAndSaveTo2DMatrix();
	    if(result!=null) {
	    	
	    	int counterOfSiblings = 1;
	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultContribution = findContributionInArray(query);
	    		String typeQuery =  query[0][2].trim();
	    		if(typeQuery.equals("Sibling")) {
	    			
	    			reportedResult += getModelName() + "\t" + typeQuery + String.valueOf(counterOfSiblings) + "\t" + resultContribution + "\n"; //+ reportScore() + "\n";
	    			counterOfSiblings+=1;

	    		}else {
	    			reportedResult += getModelName() + "\t" + typeQuery + "\t" + resultContribution + "\n";// + reportScore() + "\n";

	    		}
	    	}
	    	
	    	return 0;
	    }
	    return -1;
		
	}

	public String findContributionInArray(String[][] query) {
		
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query, 1)) {
			this.score = 0;
			return "The query result has only one series (" + query[query.length-1][1] +  ". No contribution!";
		}
		
		Map<String, List<String []>> datesWithDominatorCategoriesValues = new HashMap<>();
		for(int i=2; i<query.length; i++) {
			String grouper1 = query[i][0];
			if(!datesWithDominatorCategoriesValues.containsKey(grouper1)) {
				datesWithDominatorCategoriesValues.put(grouper1, new ArrayList<>());
			}
			String [] insertedPair = {query[i][1], query[i][2]};
			datesWithDominatorCategoriesValues.get(grouper1).add(insertedPair);
			
				
		}
		
		Map<String, List<String []>> sortedMap =  new TreeMap<>(datesWithDominatorCategoriesValues);
		DecimalFormat df = new DecimalFormat("#.##");
		for (Map.Entry<String, List<String []>> entry : sortedMap.entrySet()) {
			
			List<String[]> categoriesWithMeasures = entry.getValue();
			
			double sumMeasures = categoriesWithMeasures.stream()
	                .mapToDouble(arr -> Double.parseDouble(arr[1])) // Convert the measure to double
	                .sum(); // Sum all the measures
			
			for (String[] $ : categoriesWithMeasures) {
				String percentage = df.format((Double.parseDouble($[1])/sumMeasures)*100);
				if ("sum".equals(getAggrFunc())) { //i have done then half of it
					if (this.score < Double.parseDouble(percentage)) {
						this.score = Double.parseDouble(percentage);
					}
				}
				
				$[1] = percentage + "%";
				
			}
		}
		String result = "Series that contributes the most for each date: ";
		for (Map.Entry<String, List<String []>> entry : sortedMap.entrySet()) {
			
			result += entry.getKey() + ": "; //ate
			List<String[]> categoriesWithMeasures = entry.getValue();
			String seriesWithGreatestPercentage ="";
			double greatestPercentage = 0.0; 
			for(String[] $ : categoriesWithMeasures) {
//				
//				result+= $[0] + " (" + $[1] + ") ";
				double percentage = Double.parseDouble($[1].replace("%",""));
				if(percentage >= greatestPercentage) {
					greatestPercentage = percentage;
					seriesWithGreatestPercentage = $[0];
				}
			}
			result += seriesWithGreatestPercentage + ", ";
//				if(!$.equals(categoriesWithMeasures.get(categoriesWithMeasures.size()-1))) {
//					result+= ",";
//				}
//			}
//			result += "\t";
			
		}
		this.score = computeScoreForEveryGrouper1Value(sortedMap);
		return result;
		
	}
	
	private double computeScoreForEveryGrouper1Value(Map<String, List<String []>> map) {
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		if("sum".equals(getAggrFunc())) {
			
			double sumForAllDatesAndCategories = 0;
			Map<String, Double> categoryWithSumMeasure = new HashMap<>();
			for (Map.Entry<String, List<String []>> entry : map.entrySet()) {
				
				List<String[]> categoriesWithMeasures = entry.getValue();
				
				for (String[] categoryWithMeasure : categoriesWithMeasures) {
					
					if(!categoryWithSumMeasure.containsKey(categoryWithMeasure[0])) {
						
						categoryWithSumMeasure.put(categoryWithMeasure[0], Double.valueOf(categoryWithMeasure[1]));
					} else {
						
						double tempSum = categoryWithSumMeasure.get(categoryWithMeasure[0]);
						categoryWithSumMeasure.replace(categoryWithMeasure[0], tempSum + Double.valueOf(categoryWithMeasure[1]));
					}
					
					sumForAllDatesAndCategories += Double.valueOf(categoryWithMeasure[1]);
					
				}
			
			}
			
			List<Double> lista = categoryWithSumMeasure.values().stream().collect(Collectors.toList());
			Double maxi = lista.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
			return maxi/sumForAllDatesAndCategories;
			
		} else {
			
			double countOfAllDates = map.size();
			Map<String, List<Double>> categoryWithMeasures = new HashMap<>();
			for (Map.Entry<String, List<String []>> entry : map.entrySet()) {
				
				List<String[]> categoriesWithMeasures = entry.getValue();
				
				for (String[] categoryWithMeasure : categoriesWithMeasures) {
					
					double measure = Double.valueOf(categoryWithMeasure[1].replace("%", ""));
					if(!categoryWithMeasures.containsKey(categoryWithMeasure[0])) {
						
						List<Double> measures = new ArrayList<>();
						measures.add(measure);
						categoryWithMeasures.put(categoryWithMeasure[0], measures);
					} else {
						
						List<Double> measures = categoryWithMeasures.get(categoryWithMeasure[0]);
						measures.add(measure);
						categoryWithMeasures.replace(categoryWithMeasure[0], measures);
					}
					
					
					
				}
				countOfAllDates += 1;
			
			}
			List<Integer> counts = categoryWithMeasures.values().stream().map(x -> x.size()).collect(Collectors.toList());
			int maxi = counts.stream().mapToInt(Integer:: intValue).max().getAsInt();
			return maxi/countOfAllDates;
		}
		
		
	}

	



	@Override
	public String getModelName() {
		return "Contributor";
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
		return "Model to print how much a category contributes to the sum";
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
		List<Double> measurements = model.getDataPoints().stream().map(m -> m.getMeasure()).collect(Collectors.toList());
		Double sumOfAllMeasurements = 0.0;
		for(Double measurement: measurements) {
			sumOfAllMeasurements+= measurement;
		}
		Double max_percentage = 0.0;
		String contributor ="";
	
		for(DataPoint dp: model.getDataPoints()) {
			Double measurement = dp.getMeasure();
			String grouper1 = dp.getGrouper1();
			if((measurement/sumOfAllMeasurements) >= max_percentage) {
				max_percentage = (measurement/sumOfAllMeasurements);
				contributor = grouper1;
			}
		}
		
		if(max_percentage >= 0.5) {
			setScoreResult("Series has a mega contributor for x = " + contributor);
		} else {
			setScoreResult("Series has not a mega contributor.");
		}
		return max_percentage;
	}
	
	
	

}
