package mainengine.managers;

import java.util.List;
import chartManagement.ChartManager;
import cubemanager.CubeManager;
import cubemanager.cubebase.BasicStoredCube;
import cubemanager.cubebase.Dimension;
import cubemanager.cubebase.QueryHistoryManager;
import cubemanager.queryoptimizer.IQueryOptimization;
import interestingnessengine.InterestingnessManager;
import mainengine.Session;

/**
 * A context object that holds the state and essential components of a session
 * This class serves as a State Container and it is initialized by the ConnectionManager upon successful connection
 * and then retained by the SessionQueryProcessorEngine
 * 
 * Its primary purpose is to aggregate all the specific Managers (e.g., CubeManager, ChartManager) 
 * and session metadata (e.g., Schema Name, Session ID) into a single object so that the SessionQueryProcessorEngine is debloated
 * 
 * @author Nik-Pt
 *
 */
public class SessionContext {
    private CubeManager cubeManager;
    private Session session;
    private String sessionId;
    private String schemaName;
    private String connectionType;
    private QueryHistoryManager queryHistoryMng;
    private InterestingnessManager interestMng;
    private ChartManager chartManager;
    private List<Dimension> registeredDimensions;
    private List<BasicStoredCube> registeredCubesList;

	private IQueryOptimization queryOptimizer;
	private String queryOptimizerMethod;
	private String inputFolder;
	private String cubeName;
	private String samplingAlgorithm;
	private double sampleFraction;
	//	private String currentQueryName;
	//	private ITranslatorFactory translatorFactory;
	//	private ArrayList<String> cubeNames;
	//	private ArrayList<String> aggrFunctions;
	//	private ArrayList<String> measuresFields;
	//	private ArrayList <String> dimensions;
	//	private HashMap<String, ArrayList<String>> dimensionsToLevelsHashmap;
	//	private HashMap<String, ArrayList<String>> levelsToDimensionsHashmap;
    
    //Getters and Setters
	public CubeManager getCubeManager() {
		return cubeManager;
	}
	
	public void setCubeManager(CubeManager cubeManager) {
		this.cubeManager = cubeManager;
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public String getSessionId() {
		return sessionId;
	}
	
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public String getSchemaName() {
		return schemaName;
	}
	
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	public String getConnectionType() {
		return connectionType;
	}
	
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	
	public QueryHistoryManager getQueryHistoryMng() {
		return queryHistoryMng;
	}
	
	public void setQueryHistoryMng(QueryHistoryManager queryHistoryMng) {
		this.queryHistoryMng = queryHistoryMng;
	}
	
	public InterestingnessManager getInterestMng() {
		return interestMng;
	}
	
	public void setInterestMng(InterestingnessManager interestMng) {
		this.interestMng = interestMng;
	}
	
	public ChartManager getChartManager() {
		return chartManager;
	}
	
	public void setChartManager(ChartManager chartManager) {
		this.chartManager = chartManager;
	}
	
	public List<Dimension> getRegisteredDimensions() {
		return registeredDimensions;
	}
	
	public void setRegisteredDimensions(List<Dimension> registeredDimensions) {
		this.registeredDimensions = registeredDimensions;
	}
	
	public List<BasicStoredCube> getRegisteredCubesList() {
		return registeredCubesList;
	}
	
	public void setRegisteredCubesList(List<BasicStoredCube> registeredCubesList) {
		this.registeredCubesList = registeredCubesList;
	}

	public IQueryOptimization getQueryOptimizer() { return queryOptimizer;}

	public void setQueryOptimizer(IQueryOptimization queryOptimizer) {
		this.queryOptimizer = queryOptimizer;
	}

	public String getQueryOptimizerMethod() { return queryOptimizerMethod;}

	public void setQueryOptimizerMethod(String queryOptimizerMethod) {
		this.queryOptimizerMethod = queryOptimizerMethod;
	}

	public String getInputFolder() {return inputFolder;}

	public String getCubeName() {return  cubeName;}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public void setCubeName(String cubeName) {
		this.cubeName = cubeName;
	}

	public String getSamplingAlgorithm() {return samplingAlgorithm;}

	public double getSampleFraction() {return sampleFraction;}

	public void setSamplingAlgorithm(String samplingAlgorithm) {this.samplingAlgorithm = samplingAlgorithm;}

	public void setSampleFraction(double sampleFraction) {this.sampleFraction = sampleFraction;}
}