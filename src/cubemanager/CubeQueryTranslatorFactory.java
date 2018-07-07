/**
 * 
 */
package cubemanager;


/**
 * @author pvassil
 *
 */
public class CubeQueryTranslatorFactory {

	public ICubeQueryTranslator createCubeQueryTranslator(String physicalDBType) {
		ICubeQueryTranslator result = null;
		switch (physicalDBType) {
			case "SQL": result = new CubeQueryTranslatorToSQL();
						break;
		}
		if(result == null) {
			System.out.println("Did not manage to create a translator for the given physical DB type. Exiting");
			System.exit(-1);
		}
		return result;
	}//end method
}//end class
