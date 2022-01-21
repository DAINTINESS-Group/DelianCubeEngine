package interestingnessengine;

import java.util.ArrayList;
import cubemanager.cubebase.CubeQuery;

public class FamilyBasedRelevance {
	
	public double computeMeasure(String q1, String q2, InputManager inputManager) {
		
		int length = inputManager.getQueryHistory().size();
		CubeQuery query1 = inputManager.getCurrentQuery();
		CubeQuery query2 = inputManager.getQueryHistory().get(length-1);
		ArrayList<String[]> gamma1 = query1.getGammaExpressions();
		ArrayList<String[]> gamma2 = query2.getGammaExpressions();
		ArrayList<String[]> sigma1 = query1.getSigmaExpressions();
		ArrayList<String[]> sigma2 = query2.getSigmaExpressions();
		String aggr1 = query1.getAggregateFunction();
		String aggr2 = query2.getAggregateFunction();
		String cubeName1 = query1.getReferCube().getName();
		String cubeName2 = query2.getReferCube().getName();
		String measure1 = query1.getListMeasure().get(0).getName();
		String measure2 = query2.getListMeasure().get(0).getName();
		boolean similarityCheck = true;
		
		if(cubeName1.equals(cubeName2)) {			
			if(cubeName1.equals(cubeName2)) {
				if(aggr1.equals(aggr2)) {
					if(measure1.equals(measure2)) {
						if(gamma1.size()==gamma2.size()) {
							for(int i=0; i<gamma1.size();i++) {
								for(int j=0; j<gamma1.get(i).length; j++) {
									if(!gamma1.get(i)[j].equals(gamma2.get(i)[j])) {
										if(similarityCheck == false) { //An 1 gamma einai diaforetiko => mporei na einai siblings. An kai 2o gamma einai diaforetiko => den einai related
											return 1.0;	//dhladh an mpei edw shmainei oti vre8hkan parapanw apo 1 gamma diaforetika, ara return 1.0;
										}
										similarityCheck = false; //similarityCheck = false shmainei oti 1 gamma vre8hke diaforetiko
									}
								}
							}
							//check if siblings or mother/parent - child
							if(sigma1.size()==sigma2.size()) {
								for(int i=0; i<sigma1.size();i++) {
									for(int j=0; j<sigma1.get(i).length; j++) {
										if(!sigma1.get(i)[j].equals(sigma2.get(i)[j])) {
											if(similarityCheck == false) {
												return 1.0;	//An mpei edw shmainei oti vre8hkan parapanw apo 1 gamma + sigma diaforetika, ara return 1.0, non family related;
											}
											similarityCheck = false;
										}
									}
								}
							}else {
								// if sigma size doesn't match, not family related
								return 1.0;
							}
						}else {
							// if gamma size doesn't match, not family related
							return 1.0;
						}
					}else {
						// if it's not about the same measure, not family related
						return 1.0;
					}
				}else {
					// if it's not about the same cube, not family related
					return 1.0;
				}
			}
		}else {
			return 1.0;
		}
		// Queries are related, only 1 thing in gamma or sigma is different
		return 0.0;
	}
}