package mainengine.nlq;

import java.util.ArrayList;

public class NLTranslator implements ITranslator {
	private ArrayList<String> tokens;
	private String cubeName;
	private String queryName;
	private String aggrFunc;
	private String measure;
	private String gamma;
	private String sigma;
	
	
	public NLTranslator() {
		tokens = new ArrayList<String>();
		cubeName = "";
		queryName = "";
		aggrFunc = "";
		measure = "";
		gamma = "";
		sigma = "";
	
	}
	
	public QueryForm analyzeNLQuery(String NLQuery){

		clear();
		tokens = splitIntoTokens(NLQuery);
		
		cubeName = tokens.get(5);
		queryName = findQueryName(NLQuery);
		aggrFunc = tokens.get(3);
		measure = tokens.get(6);
		gamma = findGamma(NLQuery);
		sigma = findSigma(NLQuery);


		
		QueryForm query = new QueryForm("CubeName:" + cubeName, "Name:" + queryName, "AggrFunc:" + aggrFunc, "Measure:" + measure, "Gamma:" + gamma, "Sigma:" + sigma);

		
		//System.out.println(query.getCubeName()+query.getQueryName()+query.getAggregateFunction()+query.getMeasure()+query.getGamma()+query.getSigma());
		
		return query;
		
	}
	
	private void clear() {
		this.cubeName = "";
		this.queryName = "";
		this.aggrFunc = "";
		this.measure = "";
		this.gamma = "";
		this.sigma = "";
	}
	
	
	private ArrayList<String> splitIntoTokens(String NLQuery) {
		ArrayList<String> producedTokens = new ArrayList<String>();
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			producedTokens.add(splitIntoTokens[i]);
		}
		return producedTokens;
	}
	
	private String findSigma(String NLQuery) {
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			if (splitIntoTokens[i].equals("for")) {
				for(int j=i+1; j<splitIntoTokens.length; j++) {
					if(!(splitIntoTokens[j].equals("as"))) {
						sigma += splitIntoTokens[j] + " ";
					}
					else {
						break;
					}
				}
			}
		}
		return sigma;
	}//end findSigma()
	
	private String findGamma(String NLQuery) {
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			if (splitIntoTokens[i].equals("per")) {
				for(int j=i+1; j<splitIntoTokens.length; j++) {
					if(!(splitIntoTokens[j].equals("for"))) {
						gamma += splitIntoTokens[j] + " ";
					}
					else {
						break;
					}
				}
			}
		}
		return gamma;
	}//end findGamma()
	
	private String findQueryName(String NLQuery) {
		String[] splitIntoTokens = NLQuery.split("\\s+");
		for (int i=0; i<splitIntoTokens.length; i++) {
			if (splitIntoTokens[i].equals("as")) {
				queryName = splitIntoTokens[i+1];
			}
		}
		return queryName;
		
	}//end findQueryName()
	
}//end class
