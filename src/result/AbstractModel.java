/*
*    DelianCubeEngine. A simple cube query engine.
*    Copyright (C) 2018  Panos Vassiliadis
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License as published
*    by the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*
*/
package result;

import java.util.HashMap;

/**
 * The abstract class representing <i>any</i> AbstractModel.
 * 
 * All concrete instantiations of model generation algorithms must implement the "contract" offered the abstract methods of AbstractModel.
 * THe class needs to have:
 * (1) a Result to which it refers to. Without a query result, creating a model is meaningless. This is also shown at the constructor.
 *     Remember that the Result has a <i>list</i> of cells. The model components also include a list of labels, in one-to-one correspondence with cells.
 * (2) a set (here: hashmap) of components. Each component aims to annotate each cell with a label / value
 * (3) an abstract method compute() which populates the components and their lists of labels.
 * 
 * @author pvassil
 *
 */
public abstract class AbstractModel {
	
	/**
	 * This is an abstract method to be overridden by concrete extensions of the class.
	 * Each model type (cluster, top-k, decision-tree, KPI, prediction, ...) must implement the compute() method.
	 * The method assumes that the hashmap has been created by the constructor.
	 * Then the method must:
	 * (1) create the appropriate components (this is dependent upon the type of the highlight)
	 * (2) compute the respective value of each component per cell of the result.
	 * 
	 * @return 0 if all is OK, a negative value if there are problems
	 */
	public abstract int compute();
	public abstract String[][] printAs2DStringArray();
	
	protected Result result;
	protected HashMap<String, AbstractModelComponent> components;
	
	public AbstractModel(Result aResult) {
		result = aResult;
		components = new HashMap<String, AbstractModelComponent>();
	}

	public HashMap<String, AbstractModelComponent> getComponents() {
		return components;
	}

	public void setComponents(HashMap<String, AbstractModelComponent> components) {
		this.components = components;
	}

	public AbstractModelComponent getComponentByName(String componentName) {
		return components.get(componentName);
	}
}
