package mainengine.nlq;

import java.util.ArrayList;
import java.util.HashMap;

public class ITranslatorFactory {

	public ITranslator getITranslator(String type, HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap, HashMap<String, ArrayList<String>> levelsToDimensionsHashmap) {
		if(type == null) {
			return null;
		}
		if(type.equalsIgnoreCase("NLTranslator")) {
			return new NLTranslator(dimensionsToLevelsHashmap, levelsToDimensionsHashmap);
		}
		return null;
	}
}
