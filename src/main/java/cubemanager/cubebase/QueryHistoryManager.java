package cubemanager.cubebase;

import java.util.ArrayList;
import java.util.List;

public class QueryHistoryManager {

	private String sessionId;
	private List<CubeQuery> queryHistory;
	
	public QueryHistoryManager(String sessionId ) {
		this.sessionId = sessionId;
		queryHistory = new ArrayList<CubeQuery>();
	}
	
	public void addQuery(CubeQuery cubeQuery) {
		queryHistory.add(cubeQuery);
	}
	
	public void deleteQuery(CubeQuery cubeQuery) {
		queryHistory.remove(cubeQuery);
	}
	
	public CubeQuery getQueryByName(String name) {
		for(CubeQuery cubeQuery : queryHistory) {
			if(cubeQuery.getName().equals(name)) {
				return cubeQuery;
			}
		}
		return null;
	}
	
	public List<CubeQuery> getHistoryOfQueries(){
		return queryHistory;
	}
	
	public CubeQuery getLastQuery() {
		if(queryHistory.isEmpty()) {
			return null;
		}
		return queryHistory.get( queryHistory.size() - 1 );
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<CubeQuery> getQueryHistory() {
		return queryHistory;
	}

	public void setQueryHistory(List<CubeQuery> queryHistory) {
		this.queryHistory = queryHistory;
	}
	
	
}
