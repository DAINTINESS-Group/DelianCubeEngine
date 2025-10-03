package analyze;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


import analyze.mqoaggregateadapt.AggregateAdapter;
import result.Cell;

public class AnalyzeMaxMQOAuxiliaryQueryResultBuilder {
	
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
	
	private HashMap<String, String> baseQueryMap;
	private HashMap<String, String> ddAQueryMap;
	private HashMap<String, String> ddBQueryMap;
	private HashMap<String, String> siblingAQueryMap;
	private HashMap<String, String> siblingBQueryMap;
	private GrouperPairValues baseQueryGroupers;
	private GrouperPairValues ddAQueryGroupers;
	private GrouperPairValues ddBQueryGroupers;
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
		
		baseQueryMap = new HashMap<String, String>();
		ddAQueryMap = new HashMap<String, String>();
		ddBQueryMap = new HashMap<String, String>();
		siblingAQueryMap = new HashMap<String, String>();
		siblingBQueryMap = new HashMap<String, String>();
		baseQueryGroupers = new GrouperPairValues();
		ddAQueryGroupers = new GrouperPairValues();
		ddBQueryGroupers = new GrouperPairValues();
		siblingAQueryGroupers = new GrouperPairValues();
		siblingBQueryGroupers = new GrouperPairValues();
		
		for(Cell c: resultCellsMQO) {			
			String phiA = "\'" + c.getDimensionMembers().get(4) + "\'";
			String phiB = "\'" + c.getDimensionMembers().get(5) + "\'";
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
			if(sigmaPhiAFlag && sigmaPhiBFlag) {
				baseQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(2));
				baseQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(3));
				ddAQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(0));
				ddAQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(3));
				ddBQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(2));
				ddBQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(1));
				//GrouperPairValues baseQueryGroupers = new GrouperPairValues(c.getDimensionMembers().get(2), c.getDimensionMembers().get(3));
				//GrouperPairValues ddAQueryGroupers = new GrouperPairValues(c.getDimensionMembers().get(0), c.getDimensionMembers().get(3));
				//GrouperPairValues ddBQueryGroupers = new GrouperPairValues(c.getDimensionMembers().get(2), c.getDimensionMembers().get(1));
				updateAuxiliaryQueryMap("base", baseQueryGroupers, baseQueryMap, c.getMeasure(), aggrAdapter);
				updateAuxiliaryQueryMap("ddA", ddAQueryGroupers, ddAQueryMap, c.getMeasure(), aggrAdapter);
				updateAuxiliaryQueryMap("ddB", ddBQueryGroupers, ddBQueryMap, c.getMeasure(), aggrAdapter);
			}
			siblingAQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(4));
			siblingAQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(3));
			siblingBQueryGroupers.setGrouperAValue(c.getDimensionMembers().get(2));
			siblingBQueryGroupers.setGrouperBValue(c.getDimensionMembers().get(5));
			//GrouperPairValues siblingAQueryGroupers = new GrouperPairValues(c.getDimensionMembers().get(4), c.getDimensionMembers().get(3));
			//GrouperPairValues siblingBQueryGroupers = new GrouperPairValues(c.getDimensionMembers().get(2), c.getDimensionMembers().get(5));
			//siblingAQueryMap.put(siblingAQueryGroupers, c.getMeasure());
			//siblingBQueryMap.put(siblingBQueryGroupers, c.getMeasure());
			
			if(sigmaPhiBFlag) {
				updateAuxiliaryQueryMap("siblingA", siblingAQueryGroupers, siblingAQueryMap, c.getMeasure(), aggrAdapter);
			}
			if(sigmaPhiAFlag) {
				updateAuxiliaryQueryMap("siblingB", siblingBQueryGroupers, siblingBQueryMap, c.getMeasure(), aggrAdapter);
			}
		}
		ArrayList<String> mqoResult = new ArrayList<>();
		Set<String> baseGroupers = baseQueryMap.keySet();
		//Collection<String> baseMeasures = baseQueryMap.values();
		for(String str: baseGroupers) {
			//System.out.println(str+", "+baseQueryMap.get(str));
			mqoResult.add(str+", "+baseQueryMap.get(str));
		}
		Set<String> ddAGroupers = ddAQueryMap.keySet();
		for(String str: ddAGroupers) {
			//System.out.println(str+", "+ddAQueryMap.get(str));
			mqoResult.add(str+", "+ddAQueryMap.get(str));
		}
		Set<String> ddBGroupers = ddBQueryMap.keySet();
		for(String str: ddBGroupers) {
			//System.out.println(str+", "+ddBQueryMap.get(str));
			mqoResult.add(str+", "+ddBQueryMap.get(str));
		}
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
