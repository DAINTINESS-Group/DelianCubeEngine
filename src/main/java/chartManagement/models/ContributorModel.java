package chartManagement.models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import model.abstracts.AbstractModel;

public class ContributorModel extends AbstractModel implements IChartModel{

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
	public int compute() { //TODO
	    result = readResultsFromFileAndSaveTo2DMatrix(this.fileLocation, this.fileName);
	    if(result!=null) {
	    	
//	    
	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultContribution = findContributionInArray(query);
	    		System.out.println(resultContribution);
	    		reportedResult += getModelName() + "\t" + query[0][2] + "\t" + resultContribution + "\n";
	    	}
	    	
	    	
	    }
	
		return 0;
	}

	private String findContributionInArray(String[][] query) {
		// TODO Auto-generated method stub
		if(findIfGrouper2ColumnContainsOnlyOneSeries(query, 1)) {
			return "The query result has only one Category (" + query[2][1] +  ") for Series (100% contribution).";
		}
		return findIfContributionExistsInSeries(query);
	}

	private String findIfContributionExistsInSeries(String[][] query) {
		// TODO Auto-generated method stub
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
			
			for(String[] $ : categoriesWithMeasures) {
				String percentage = df.format((Double.parseDouble($[1])/sumMeasures)*100);
				$[1] = percentage + "%";
				
			}
		}
		String result = "";
		for (Map.Entry<String, List<String []>> entry : sortedMap.entrySet()) {
			
			result += entry.getKey() + ": ";
			List<String[]> categoriesWithMeasures = entry.getValue();
			for(String[] $ : categoriesWithMeasures) {
				
				result+= $[0] + " (" + $[1] + ") ";
				if(!$.equals(categoriesWithMeasures.get(categoriesWithMeasures.size()-1))) {
					result+= ",";
				}
			}
			result += "\t";
		}
		
		
		
		
		return result;
	}

	@Override
	public void setFolderAndFilename(String fileLocation, String filename) {
		this.fileLocation = fileLocation;
		this.fileName = filename;
		
	}

	@Override
	public String getModelName() {
		// TODO Auto-generated method stub
		return "Contributor";
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
	public void setFileName(String filename, String fileLocation) {
		// TODO Auto-generated method stub
		
	}

}
