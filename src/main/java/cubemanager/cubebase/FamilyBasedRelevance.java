package cubemanager.cubebase;

import java.util.List;

public class FamilyBasedRelevance {
	
	public boolean computeMeasure(CubeQuery q1, CubeQuery q2) {
		
		// NOT sure if this actually gets the list of the queries dimensions or I have to use another way to get them
		List<Dimension> dimension1 = q1.getListDimension();
		List<Dimension> dimension2 = q2.getListDimension();
		
		if (dimension1.size()==dimension2.size()) {
			//check if siblings
			if(dimension1.equals(dimension2)) {
				return true;
			}else {
				return false;
			}
		}else if (dimension1.size()>dimension2.size()) {
			//check if mother-child
			if(dimension1.size()-dimension2.size()==1) {
				dimension1.remove(dimension1.size()-1);
				if(dimension1.equals(dimension2)) {
					return true;
				}else {
					return false;
				}
			}else {
				//check if q1 is an ancestor of q2
				for (int i=0; i<dimension1.size()-dimension2.size(); i++) {
					//remove last elements to make the size of the lists matching
					dimension1.remove(dimension1.size()-i-1);
				}
				if(dimension1.equals(dimension2)) {
					return true;
				}else {
					return false;
				}
			}
		}else {
			//check if mother-child
			if(dimension2.size()-dimension1.size()==1) {
				dimension2.remove(dimension2.size()-1);
				if(dimension2.equals(dimension1)) {
					return true;
				}else {
					return false;
				}
			}else {
				//check if q2 is an ancestor of q1
				for (int i=0; i<dimension2.size()-dimension1.size(); i++) {
					//remove last elements to make the size of the lists matching
					dimension2.remove(dimension2.size()-i-1);
				}
				if(dimension2.equals(dimension1)) {
					return true;
				}else {
					return false;
				}
			}
		}
	}
	
}
