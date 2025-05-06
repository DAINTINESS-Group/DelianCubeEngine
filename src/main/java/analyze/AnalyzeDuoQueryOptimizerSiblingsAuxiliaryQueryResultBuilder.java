package analyze;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import analyze.mqoaggregateadapt.AggregateAdapter;
import result.Cell;

public class AnalyzeDuoQueryOptimizerSiblingsAuxiliaryQueryResultBuilder {
	
	private class GrouperPairValues {
		private String grouperAValue;
		private String grouperBValue;
		
		private void setGrouperAValue(String grouperAValue) {
			this.grouperAValue=grouperAValue;
		}
		
		private void setGrouperBValue(String grouperBValue) {
			this.grouperBValue=grouperBValue;
		}
		
		
		private String getGrouperAValue() {
			return grouperAValue;
		}
		
		private String getGrouperBValue() {
			return grouperBValue;
		}
	}
	
	
	
	private HashMap<String, String> siblingAQueryMap;
	private HashMap<String, String> siblingBQueryMap;
	private GrouperPairValues siblingAQueryGroupers;
	private GrouperPairValues siblingBQueryGroupers;
	
	
	private void updateAuxiliaryQueryMap(String auxiliaryQueryType, 
			GrouperPairValues groupersPair,
			HashMap<String, String> groupersPairToMeasure, 
			String measure,
			AggregateAdapter aggrAdapter){
		String groupers = groupersPair.getGrouperAValue()+", "+groupersPair.getGrouperBValue();
		if(groupersPairToMeasure.containsKey(groupers)) {
			Double oldMeasureValue = Double.valueOf(groupersPairToMeasure.get(groupers));
			Double newMeasureValue = Double.valueOf(measure);
			Double adaptedMeasureValue = aggrAdapter.adapt(oldMeasureValue, newMeasureValue);
			
			adaptedMeasureValue = new BigDecimal(adaptedMeasureValue.toString()).setScale(4,RoundingMode.HALF_UP).doubleValue();
			
			//Double adaptedMeasureValue = Precision.round(aggrAdapter.adapt(oldMeasureValue, newMeasureValue), 4);
			
			String adaptedMeasureString = String.valueOf(adaptedMeasureValue);
			String[] roundit = adaptedMeasureString.split("\\.");
			if(roundit[1].length() == 0) {
				roundit[1]+="0000";
			} else if(roundit[1].length() == 1) {
				roundit[1]+="000";
			}else if(roundit[1].length() == 2) {
				roundit[1]+="00";
			}else if(roundit[1].length() == 3) {
				roundit[1]+="0";
			}
			adaptedMeasureString = roundit[0] + "." + roundit[1];
			groupersPairToMeasure.put(groupers, adaptedMeasureString);
			//System.out.println("New measure is: "+groupersPairToMeasure.get(groupers));
		}else {
			groupersPairToMeasure.put(groupers, measure);
			//System.out.println("New addition. New measure is: "+groupersPairToMeasure.get(groupers));
		}
	}
	
	
	public ArrayList<String> feedTheAuxiliaryQueriesfromMQO(ArrayList<Cell> resultCellsMQO, 
			ArrayList<String> sigmaExpressions, 
			HashMap<String, String> sigmaExpressionsToValues,
			AggregateAdapter aggrAdapter) {
		
		siblingAQueryMap = new HashMap<String, String>();
		siblingBQueryMap = new HashMap<String, String>();
		siblingAQueryGroupers = new GrouperPairValues();
		siblingBQueryGroupers = new GrouperPairValues();
		
		for(Cell c: resultCellsMQO) {			
			String phiA = "\'" + c.getDimensionMembers().get(2) + "\'";
			String phiB = "\'" + c.getDimensionMembers().get(3) + "\'";
			boolean sigmaPhiAFlag = false;
			boolean sigmaPhiBFlag = false;
			
			for(String sigmaExpression: sigmaExpressions) {
				String sigmaValue = sigmaExpressionsToValues.get(sigmaExpression);
				if(sigmaValue.equals(phiA)) {
					sigmaPhiAFlag = true;
				} else if(sigmaValue.equals(phiB)) {
					sigmaPhiBFlag = true;
				}
			}
			
			siblingAQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(2));
			siblingAQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(1));
			siblingBQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(0));
			siblingBQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(3));
			
			if(sigmaPhiBFlag) {
				updateAuxiliaryQueryMap("siblingA", siblingAQueryGroupers, siblingAQueryMap, c.getMeasure(), aggrAdapter);
			}
			if(sigmaPhiAFlag) {
				updateAuxiliaryQueryMap("siblingB", siblingBQueryGroupers, siblingBQueryMap, c.getMeasure(), aggrAdapter);
			}
		}
		
		ArrayList<String> mqoResult = new ArrayList<>();
		Set<String> siblingAGroupers = siblingAQueryMap.keySet();
		for(String str: siblingAGroupers) {
			//System.out.println(str+", "+siblingAQueryMap.get(str));
			mqoResult.add(str+", "+siblingAQueryMap.get(str));
		}
		Set<String> siblingBGroupers = siblingBQueryMap.keySet();
		for(String str: siblingBGroupers) {
			//System.out.println(str+", "+siblingBQueryMap.get(str));
			mqoResult.add(str+", "+siblingBQueryMap.get(str));
		}
		
		//System.out.println(mqoResult.size());
		return mqoResult;
	}

}
