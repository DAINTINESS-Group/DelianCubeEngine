package mainengine.managers;

import mainengine.managers.IBuilder;
import mainengine.managers.RequestCTO;
import mainengine.managers.ResponseDTO;

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

			default:
				throw new IllegalArgumentException("Unknown or Unimplemented Manager Type: " + cto.getManagerType());
		}
		
		return manager.execute(cto);
	}
}