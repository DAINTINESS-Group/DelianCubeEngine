package mainengine.managers;

/**
 * The Director class acts as the Dispatcher and Factory in the architecture
 * It implements the Command/Dispatcher pattern where it's main responsibility is to receive
 * a RequestCTO, determine which IBuilder (Manager) is responsible for handling it based on the ManagerType that came with the CTO,
 * create an instance of said manager and delegate the execution
 * 
 * @author Nik-Pt
 *
 */
public class Director {
	
	public ResponseDTO serve(RequestCTO cto) throws Exception {
		IBuilder manager = null;
		
		switch (cto.getManagerType()) {
			case CONNECTION:
				manager = new ConnectionManager();
				break;
				
			case EXECUTION:
				manager = new QueryExecutionManager();
				break;

			case INTENTIONAL:
				manager = new IntentionalManager();
				break;
				
			case OLAP:
				manager = new OLAPManager();
				break;
			
			case NL: 
				manager = new NLManager(); 
		        break;
			
			case VISUALIZATION:
				manager = new VisualizationManager();
				break;

			case OPTIMIZER:
				manager = new QueryOptimizationManager();
				break;

			case STATISTICS:
				manager = new StatisticsManager();
				break;

			default:
				throw new IllegalArgumentException("Unknown or Unimplemented Manager Type: " + cto.getManagerType());
		}
		
		return manager.execute(cto);
	}
}