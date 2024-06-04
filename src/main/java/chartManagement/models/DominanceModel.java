package chartManagement.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class DominanceModel extends ChartModel{
	

	private String result [][];
	


	@Override
	public int compute() {
	    
	    result = readResultsFromFileAndSaveTo2DMatrix();
	    if(result!=null) {

	    	List<String[][]> smallerLists = extractArrayListWithSmallerArrays(result);
	    	for(String [][] query: smallerLists) {
	    		String resultDominance = findDominanceInArray(query);
	    		System.out.println(resultDominance);
	    		reportedResult += getModelName() + "\t" + query[0][2] + "\t"   + resultDominance + "\n";
	    	}
	    	
	    	return 0;
	    }
	    return -1;
	}

	public String findDominanceInArray(String[][] query) {

		if(findIfGrouper2ColumnContainsOnlyOneSeries(query, 1)) {
			return "No Dominance, The query result has only one Category (" +  query[2][1] + ") for Series.";
		}
		return findIfDominanceExistsInSeries(query);
	}
	
	private String findIfDominanceExistsInSeries(String[][] query) { //TODO na th valw mesa sthn findDominanceInArray

        Map<String, String> datesWithDominantCategory = new HashMap<>();
        Map<String, Double> maxMeasuresPerDate = new HashMap<>();

        // Populate maxMeasuresPerDate and datesWithDominantCategory
        for (int i = 2; i < query.length; i++) {
            String date = query[i][0];
            String category = query[i][1];
            double measure = Double.parseDouble(query[i][2]);

            if (!maxMeasuresPerDate.containsKey(date) || maxMeasuresPerDate.get(date) < measure) {
                maxMeasuresPerDate.put(date, measure);
                datesWithDominantCategory.put(date, category);
            }
        }

        // Check if there's consistency in dominant category across dates
        Set<String> uniqueCategories = new HashSet<>(datesWithDominantCategory.values());
        if (uniqueCategories.size() == 1) {
            return "Dominator Value for query: " + uniqueCategories.iterator().next();
        } else {
            return "There isn't a Dominator in Series with Categories: " + String.join(", ", uniqueCategories);
        }
    
		
	}

	private String getCategories(List<String> collection) {
	
		String categories = "";
		for(String category : collection) {
			categories += category + ", ";
		}
		return categories;
	}

	public Boolean findIfGrouper2ColumnContainsOnlyOneSeries (String[][] array, int columnIndex) {
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
	public String getModelName() {
	
		return "Dominance";
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

		return "A dominance model created by Aggeliki Dougia";
	}
	

	
//	public List<String[][]> extractArrayListWithSmallerArrays(String [][] bigArray) {
//		
//		List<String[][]> smallerArrays = new ArrayList<>();
//		int startIndex = -1;
//        for (int i = 0; i < bigArray.length; i++) {
//            if (bigArray[i][0].equals("Type") && bigArray[i][1].equals("Query")) {
//                if (startIndex != -1) {
//                    // If startIndex is not -1, add the current slice to smallerArrays
//                    String[][] slice = new String[i - startIndex][];
//                    System.arraycopy(bigArray, startIndex, slice, 0, slice.length);
//                    smallerArrays.add(slice);
//                }
//                // Update startIndex for the next slice
//                startIndex = i;
//            }
//        }
//
//        // Add the last slice
//        if (startIndex != -1) {
//            String[][] slice = new String[bigArray.length - startIndex][];
//            System.arraycopy(bigArray, startIndex, slice, 0, slice.length);
//            smallerArrays.add(slice);
//        }
//        return smallerArrays;
//
//	}




	

}
